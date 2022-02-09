import argparse
import os
import logging


logger = logging.getLogger()


def runProgram(fileName="code.lsp"):
    # reads the code in file name, parses it, executes each statement
    program = readInFile(fileName)
    # parse the string into a list of lists
    codeList = parseProgram(program)
    evalProgram(codeList)


def readInFile(fileName):
    # reads in a lisp program, returns as one big string
    logger.info("Reading %s", fileName)
    with open(fileName, "r") as file:  # ignore comment lines
        lines = [l for l in file.readlines() if not l[0] == "#"]
    whole = ""
    for line in lines:
        whole = whole + line
    return whole


########################################################################################################
### SYNTAX
########################################################################################################
def parseProgram(programString):
    # takes a string that represents a program, multiple statements
    # returns a list of list of tokens, each a statement
    tokens = spaceOut(programString).split()
    codeList = []
    while not tokens == []:
        code = createCode(tokens)
        codeList.append(code)
    return codeList


def spaceOut(string):
    return string.replace("(", " ( ").replace(")", " ) ")


def createCode(tokenList):
    # takes a token list and returns a parse tree
    token = tokenList.pop(0)  # pop off ( or atom
    if token.isdigit() or token[0].isdigit():  # integer
        return int(token)
    if isinstance(token, str) and not token == "(":  # atom
        return token
    args = []
    while not tokenList[0] == ")":
        args.append(createCode(tokenList))
    tokenList.pop(0)  # pop the ')'
    return args


#######################################################################################################################
###### semantics
#######################################################################################################################
def evalProgram(codeList):
    # top call, evaluates a list of parsed statements
    evalLispBlock(codeList, {}, [])


def evalLisp(code, bindings=[], depth=0):
    # evaluates the code with the bindings so far
    # bindings is a list of dictionaries, each with a mapping from a symbol
    # to its value. These are generated from def statements, and when
    # a user-defined function is called
    if depth == 0:
        output = logger.info
    else:
        output = logger.debug

    output("%sEval %s" % ("|  " * depth, str(code)))
    answer = evalLispHelp(code, bindings, depth)
    output("%sAns  %s " % ("|  " * depth, str(answer)))
    return answer


def evalLispHelp(code, bindings, depth):
    # do all atoms
    if isinstance(code, int):
        return code
    if "True" == code:
        return True
    if "False" == code:
        return False
    if "nil" == code:  # nil is the empty list
        return []
    if isinstance(code, str):  # variable
        return findValue(code, bindings, depth)
    # do all composite code
    fun = code[0]
    # numerical functions

    if fun == "+":
        return evalLisp(code[1], bindings, depth + 1) + evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "-":
        return evalLisp(code[1], bindings, depth + 1) - evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "*":
        return evalLisp(code[1], bindings, depth + 1) * evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "/":
        return evalLisp(code[1], bindings, depth + 1) / evalLisp(
            code[2], bindings, depth + 1
        )
    # control functions
    if fun == "if":
        if evalLisp(code[1], bindings, depth + 1):
            return evalLisp(code[2], bindings, depth + 1)
        else:
            return evalLisp(code[3], bindings, depth + 1)
    # boolean functions
    if fun == "not":
        return not evalLisp(code[1], bindings, depth + 1)
    if fun == "and":
        return evalLisp(code[1], bindings, depth + 1) and evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "or":
        return evalLisp(code[1], bindings, depth + 1) or evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "eq":  # only works between atoms
        return simpleEqual(
            evalLisp(code[1], bindings, depth + 1),
            evalLisp(code[2], bindings, depth + 1),
        )
    if fun == "<":
        return evalLisp(code[1], bindings, depth + 1) < evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "atom":
        ans = evalLisp(code[1], bindings, depth + 1)
        return (
            isinstance(ans, int)
            or isinstance(ans, str)
            or ans == True
            or ans == False
            or ans == []
        )
    # data structures
    if fun == "cons":  # add the first to a list
        return [evalLisp(code[1], bindings, depth + 1)] + evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "first":  # eval then return the first
        return evalLisp(code[1], bindings, depth + 1)[0]
    if fun == "rest":  # eval then return the rest
        return evalLisp(code[1], bindings, depth + 1)[1:]
    # evaluation
    if fun == "quote":  # stop evaluation
        return code[1]
    if fun == "eval":  # evaluate to get the code, then evaluate the code
        return evalLisp(evalLisp(code[1], bindings, depth + 1))
        # must be a user function call
    return evalLispFun(code[0], code[1:], bindings, depth + 1)


def simpleEqual(a, b):
    # only works on primitives
    if isinstance(a, int) and isinstance(b, int):
        return a == b
    if (a is True and b is True) or (a is False and b is False):
        return True
    if (a == []) and (b == []):
        return True
    return False


def evalLispFun(funName, args, bindings, depth):
    # Execute a user defined function
    logger.debug("|  " * depth + "Calling function %s" % (funName,))
    # lookup the function definition in bindings
    (params, block) = findValue(funName, bindings, depth)
    # create a new binding to hold local variables from parmeters, sets and defs
    oneBinding = {}
    # add the mapping from parameter names to values
    for (oneParam, oneExpression) in zip(params, args):
        value = evalLisp(oneExpression, bindings, depth + 1)
        oneBinding[oneParam] = value
        logger.debug(
            "|  " * depth
            + "Create parameter-value binding: %s = %s" % (oneParam, value)
        )
    # work through each statement in block
    return evalLispBlock(block, oneBinding, bindings, depth=depth + 1)


def evalLispBlock(block, oneBinding, bindings, depth=0):
    # walks down the block (a list of expressions) evaluating each statement and accumulating bindings
    # into this oneBinding for this block
    if len(block) == 0:  # empty block, return None
        return
    else:
        code = block[0]
        if not isinstance(code, list):
            ans = evalLisp(block[0], [oneBinding] + bindings, depth)
            # if last expression then return the answer
            if block[1:] == []:
                return ans
            else:  # keep going through the block
                return evalLispBlock(block[1:], oneBinding, bindings, depth=depth)

        if code[0] == "set":
            # map the name of the variable to the eval of value
            logger.debug(
                "|  " * depth + "Evaluating set %s = %s", code[1], str(code[2])
            )
            value = evalLisp(code[2], [oneBinding] + bindings, depth + 1)
            oneBinding[code[1]] = value
            logger.debug(
                "|  " * depth + "Create set variable binding: %s = %s", code[1], value
            )
            # keep going through the block
            return evalLispBlock(block[1:], oneBinding, bindings, depth=depth)

        elif code[0] == "defun":  # put mapping from name to arguments, block
            oneBinding[code[1]] = (code[2], code[3:])
            logger.debug(
                "|  " * depth + "Create function binding: %s = [%s, %s]",
                code[1],
                str(code[2]),
                str(code[3:]),
            )
            # keep working through block
            return evalLispBlock(block[1:], oneBinding, bindings, depth=depth)

        else:  # found an expression, evaluate it with current bindings
            ans = evalLisp(block[0], [oneBinding] + bindings, depth)
            # if last expression then return the answer
            if block[1:] == []:
                return ans
            else:  # keep going through the block
                return evalLispBlock(block[1:], oneBinding, bindings, depth=depth)


def findValue(name, bindings, depth):
    # name is a variable, bindings is a list of dictionaries ordered most
    # recent first, search up the bindings seeing if this variable is in
    # one of the dictionaries
    if bindings == []:  # have not found the value
        logger.info("NO Value %s" % name)
    if name in bindings[0]:  # found its value
        logger.debug("|  " * depth + "Found Value of %s as %s", name, bindings[0][name])
        return bindings[0][name]

    logger.debug("|  " * depth + "Looking for %s", name)
    return findValue(name, bindings[1:], depth)


def toString(code):
    # prints the parse tree in a more readable form
    if isinstance(code, int) or isinstance(code, str) or len(code) == 0:
        return str(code) + " "
    string = "("
    for item in code:
        string = string + toString(item)
    return string + ")"


def repl():
    # SUPER Simple single-line repl
    while True:
        try:
            line = input(">>> ")
            if line == "exit":
                break
            codeList = parseProgram(line)
            evalProgram(codeList)
        except Exception as e:
            print(e)


def main(file, trace):
    logging.basicConfig(
        level=[logging.INFO, logging.DEBUG][trace],
        format="%(message)s",
    )

    if not file:
        repl()
        return

    if not os.path.isfile(file):
        raise ValueError(f"{file} is not a valid filepath")

    runProgram(file)


parser = argparse.ArgumentParser(description="Lisp interpreter written in Python.")
parser.add_argument(
    "file",
    nargs="?",
    help="the source file to execute. If no file is given, will open in repl mode",
)
parser.add_argument("-t", "--trace", action="store_true", help="enable traceback")
args = parser.parse_args()

main(args.file, args.trace)
