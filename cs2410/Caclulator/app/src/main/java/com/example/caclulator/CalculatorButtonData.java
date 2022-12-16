package com.example.caclulator;

public class CalculatorButtonData {
    private String text;
    private ButtonType type;
    private int row;
    private int column;
    private int size;

    enum ButtonType {
        NUMBER,
        OPERATOR,
        EQUALS,
        CLEAR,
        PREVIOUS,
        DELETE
    }

    public CalculatorButtonData(String text, int column, int row, int size) {
        this(text, column, row, size, ButtonType.NUMBER);
    }

    public CalculatorButtonData(String text, int column, int row, int size, ButtonType type) {
        this.text = text;
        this.row = row;
        this.column  = column;
        this.size = size;
        this.type = type;
    }

    public String getText() { return this.text; }
    public ButtonType getType() { return this.type; }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
    public int getSize() { return this.size; }
}
