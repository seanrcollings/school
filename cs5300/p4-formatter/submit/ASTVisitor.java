package submit;

import org.antlr.v4.runtime.tree.TerminalNode;
import parser.CminusBaseVisitor;
import parser.CminusParser;
import submit.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTVisitor extends CminusBaseVisitor<Node> {
    private final Logger LOGGER;
    private SymbolTable symbolTable;

    public ASTVisitor(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }



    @Override
    public Node visitProgram(CminusParser.ProgramContext ctx) {
        symbolTable = new SymbolTable();
        List<Declaration> decls = new ArrayList<>();
        for (CminusParser.DeclarationContext d : ctx.declaration()) {
            decls.add((Declaration) visitDeclaration(d));
        }
        return new Program(decls);
    }

    @Override
    public VarDeclaration visitVarDeclaration(CminusParser.VarDeclarationContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        List<String> ids = new ArrayList<>();
        List<Integer> arraySizes = new ArrayList<>();
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            LOGGER.fine("Var ID: " + id);
            ids.add(id);
            symbolTable.addSymbol(id, new SymbolInfo(id, type, false));
            if (v.NUMCONST() != null) {
                arraySizes.add(Integer.parseInt(v.NUMCONST().getText()));
            } else {
                arraySizes.add(-1);
            }
        }
        final boolean isStatic = false;
        return new VarDeclaration(type, ids, arraySizes, isStatic);
    }

    @Override
    public FunctionDeclaration visitFunDeclaration(CminusParser.FunDeclarationContext ctx) {
        VarType returnType = getVarType(ctx.typeSpecifier());
        String id = ctx.ID().getText();
        LOGGER.fine("Fun ID: " + id);

        List<String> paramIds = new ArrayList<>();
        List<String> paramTypes = new ArrayList<>();
        symbolTable.addSymbol(id, new SymbolInfo(id, returnType, true));
        SymbolTable child = symbolTable.createChild();

        for (CminusParser.ParamContext p: ctx.param()) {
            VarType paramType = getVarType(p.typeSpecifier());
            paramTypes.add(paramType.toString());
            CminusParser.ParamIdContext paramId = p.paramId();
            paramIds.add(paramId.getText());
            child.addSymbol(paramId.ID().getText(), new SymbolInfo(paramId.ID().getText(), paramType, false));
        }

        symbolTable = child;
        Statement body = visitStatement(ctx.statement());
        symbolTable  = symbolTable.getParent();

        return new FunctionDeclaration(id, returnType, paramIds, paramTypes, body);
    }

    @Override
    public Node visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
        if (ctx.expression() != null) {
            return new Return(visitExpression(ctx.expression()));
        }
        return new Return(null);
    }

    @Override
    public Node visitConstant(CminusParser.ConstantContext ctx) {
        final Node node;
        if (ctx.NUMCONST() != null) {
            node = new NumConstant(Integer.parseInt(ctx.NUMCONST().getText()));
        } else if (ctx.CHARCONST() != null) {
            node = new CharConstant(ctx.CHARCONST().getText().charAt(0));
        } else if (ctx.STRINGCONST() != null) {
            node = new StringConstant(ctx.STRINGCONST().getText());
        } else {
            node = new BoolConstant(ctx.getText().equals("true"));
        }
        return node;
    }

    @Override
    public CompoundStatement visitCompoundStmt(CminusParser.CompoundStmtContext ctx) {
        List<VarDeclaration> decls =  ctx
                .varDeclaration()
                .stream()
                .map(this::visitVarDeclaration)
                .collect(Collectors.toList());

        List<Statement> stmts =  ctx
                .statement()
                .stream()
                .map(this::visitStatement)
                .collect(Collectors.toList());;

        return new CompoundStatement(decls, stmts);
    }

    @Override
    public Statement visitStatement(CminusParser.StatementContext ctx) {
        return (Statement) visitChildren(ctx);
    }

    @Override
    public Node visitExpressionStmt(CminusParser.ExpressionStmtContext ctx) {
        Expression expr =  visitExpression(ctx.expression());
        return new ExpressionStatement(expr);
    }

    @Override
    public Statement visitIfStmt(CminusParser.IfStmtContext ctx) {
        return new IfStatement(
                visitSimpleExpression(ctx.simpleExpression()),
                visitStatement(ctx.statement(0)),
                ctx.statement(1) != null ? (Statement) visitStatement(ctx.statement(1)): null
        );
    }

    @Override
    public Statement visitWhileStmt(CminusParser.WhileStmtContext ctx) {
        return new WhileStatement(
                visitSimpleExpression(ctx.simpleExpression()),
                visitStatement(ctx.statement())
        );
    }

    @Override
    public Statement visitBreakStmt(CminusParser.BreakStmtContext ctx) {
        return new BreakStatement();
    }

    @Override
    public Expression visitExpression(CminusParser.ExpressionContext ctx) {
        // TODO: need to handle simple expression correctly
        return switch (ctx.getChildCount()) {
            case 2 ->  new UnaryOperator(
                        visitMutable(ctx.mutable()),
                        UnaryOperatorType.fromString(ctx.getChild(1).getText()),
                        "right");
            case 3 -> new BinaryOperator(
                        visitMutable(ctx.mutable()),
                        ctx.getChild(1).getText(),
                        visitExpression(ctx.expression()));
            default -> visitSimpleExpression(ctx.simpleExpression());
        };
    }

    @Override
    public Expression visitSimpleExpression(CminusParser.SimpleExpressionContext ctx) {
        return visitOrExpression(ctx.orExpression());
    }

    @Override
    public Expression visitOrExpression(CminusParser.OrExpressionContext ctx) {
        return getRightAssociativeOperator(
                ctx.andExpression().stream().map(this::visitAndExpression).collect(Collectors.toList()),
                BinaryOperatorType.OR
        );
    }

    @Override
    public Expression visitAndExpression(CminusParser.AndExpressionContext ctx) {
        return getRightAssociativeOperator(
                ctx.unaryRelExpression().stream().map(this::visitUnaryRelExpression).collect(Collectors.toList()),
                BinaryOperatorType.AND
        );
    };

    @Override
    public Expression visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) {
        if (ctx.BANG().size() > 0) {
            Expression curr = visitRelExpression(ctx.relExpression());

            for (TerminalNode op: ctx.BANG()) {
                curr = new UnaryOperator(curr, UnaryOperatorType.BANG, "left");
            }

            return curr;
        }

        return  visitRelExpression(ctx.relExpression());
    }

    @Override
    public Expression visitRelExpression(CminusParser.RelExpressionContext ctx) {
        return getRightAssociativeOperator(
                ctx.sumExpression().stream().map(this::visitSumExpression).collect(Collectors.toList()),
                ctx.relop().stream().map((op) -> BinaryOperatorType.fromString(op.getText())).collect(Collectors.toList())
        );
    }

    @Override
    public Expression visitSumExpression(CminusParser.SumExpressionContext ctx) {
        return getRightAssociativeOperator(
                ctx.termExpression().stream().map(this::visitTermExpression).collect(Collectors.toList()),
                ctx.sumop().stream().map((op) -> BinaryOperatorType.fromString(op.getText())).collect(Collectors.toList())
        );

    }

    @Override
    public Expression visitTermExpression(CminusParser.TermExpressionContext ctx) {
        return getRightAssociativeOperator(
                ctx.unaryExpression().stream().map(this::visitUnaryExpression).collect(Collectors.toList()),
                ctx.mulop().stream().map((op) -> BinaryOperatorType.fromString(op.getText())).collect(Collectors.toList())
        );
    }

    public Expression visitUnaryExpression(CminusParser.UnaryExpressionContext ctx) {
        if (ctx.unaryop().size() == 0) {
            return visitFactor(ctx.factor());
        }

        Expression curr = visitFactor(ctx.factor());

        for (int i  = ctx.unaryop().size() - 1; i >= 0; i--) {
            CminusParser.UnaryopContext op = ctx.unaryop().get(i);
            curr = new UnaryOperator(curr, UnaryOperatorType.fromString(op.getText()), "left");
        }

        return curr;
    }

    @Override
    public Expression visitFactor(CminusParser.FactorContext ctx) {
        return (Expression) visitChildren(ctx);
    }

    @Override public Mutable visitMutable(CminusParser.MutableContext ctx) {
        String id = ctx.ID().getText();
        if (symbolTable.find(id) == null)
            LOGGER.warning(String.format("Undefined symbol on line %d: %s", ctx.getStart().getLine(), id));

        Expression expr = ctx.expression() != null ? visitExpression(ctx.expression()) : null;
        return new Mutable(ctx.ID().getText(), expr);
    }

    @Override
    public Immutable visitImmutable(CminusParser.ImmutableContext ctx) {
        Node child;
        if (ctx.expression() != null) {
            child = visitExpression(ctx.expression());
        } else {
            child = visitChildren(ctx);
        }

        return new Immutable(child, ctx.expression() != null);
    }

    @Override
    public Call visitCall(CminusParser.CallContext ctx) {
        String id = ctx.ID().getText();
        if (symbolTable.find(id) == null)
            LOGGER.warning(String.format("Undefined symbol on line %d: %s", ctx.getStart().getLine(), id));

        List<Expression> expressions = ctx
                .expression()
                .stream()
                .map(this::visitExpression)
                .collect(Collectors.toList());

        return new Call(id, expressions);
    }

    private VarType getVarType(CminusParser.TypeSpecifierContext ctx) {
        if (ctx == null) return null;
        final String t = ctx.getText();
        return (t.equals("int")) ? VarType.INT : (t.equals("bool")) ? VarType.BOOL : VarType.CHAR;
    }

    private Expression getRightAssociativeOperator(List<Expression> ops, BinaryOperatorType operator) {
        return getRightAssociativeOperator(
                ops,
                ops.stream().map((o) -> operator).collect(Collectors.toList())
        );
    };

    private Expression getRightAssociativeOperator(List<Expression> ops, List<BinaryOperatorType> operators) {
        Expression first = ops.remove(0);

        if (ops.size() == 0) return first;

        AtomicInteger i = new AtomicInteger();

        return ops
                .stream()
                .reduce(
                        first,
                        (left, right) -> {
                            BinaryOperatorType operator = operators.get(i.getAndIncrement());
                            return new BinaryOperator(left, operator, right);
                        }
                );
    };
}
