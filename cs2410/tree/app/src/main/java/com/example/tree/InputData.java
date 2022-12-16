package com.example.tree;

public class InputData {
    private String label;
    private String defaultData;
    private String snakeLabel;

    public InputData(String label, String defaultData) {
        this.label = label;
        this.defaultData = defaultData;
        this.snakeLabel = label.replace(" ", "_").toLowerCase();
    }

    public String getDefaultData() {
        return defaultData;
    }

    public String getLabel() {
        return label;
    }

    public String getSnakeLabel() {
        return snakeLabel;
    }
}
