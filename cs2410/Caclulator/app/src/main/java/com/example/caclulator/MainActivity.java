package com.example.caclulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<CalculatorButtonData> calculatorButtonData = new ArrayList<CalculatorButtonData>() {
        {
            add(new CalculatorButtonData("1", 0, 1, 1));
            add(new CalculatorButtonData("2", 1, 1, 1));
            add(new CalculatorButtonData("3", 2, 1, 1));
            add(new CalculatorButtonData("4", 0, 2, 1));
            add(new CalculatorButtonData("5", 1, 2, 1));
            add(new CalculatorButtonData("6", 2, 2, 1));
            add(new CalculatorButtonData("7", 0, 3, 1));
            add(new CalculatorButtonData("8", 1, 3, 1));
            add(new CalculatorButtonData("9", 2, 3, 1));
            add(new CalculatorButtonData("0", 0, 4, 2));
            add(new CalculatorButtonData(".", 2, 4, 1));
            add(new CalculatorButtonData("C", 0, 5, 1, CalculatorButtonData.ButtonType.CLEAR));
            add(new CalculatorButtonData("DEL", 1, 5, 1, CalculatorButtonData.ButtonType.DELETE));
            add(new CalculatorButtonData("PREV", 2, 5, 1, CalculatorButtonData.ButtonType.PREVIOUS));
            add(new CalculatorButtonData("=", 3, 5, 1, CalculatorButtonData.ButtonType.EQUALS));
            add(new CalculatorButtonData("+", 3, 1, 1, CalculatorButtonData.ButtonType.OPERATOR));
            add(new CalculatorButtonData("-", 3, 2, 1, CalculatorButtonData.ButtonType.OPERATOR));
            add(new CalculatorButtonData("*", 3, 3, 1, CalculatorButtonData.ButtonType.OPERATOR));
            add(new CalculatorButtonData("/", 3, 4, 1, CalculatorButtonData.ButtonType.OPERATOR));
        }
    };

    ArrayList<String> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLayout();
        createCalculatorNumberDisplay();
        createButtons();
    }

    private void createLayout() {
        GridLayout mainLayout = new GridLayout(this);
        mainLayout.setColumnCount(4);
        mainLayout.setId(R.id.mainLayout);
        setContentView(mainLayout);
    }

    private void createCalculatorNumberDisplay() {
        CalculatorDisplay calculatorDisplay = new CalculatorDisplay(this);
        GridLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.addView(calculatorDisplay);
    }

    private void createButtons() {
        GridLayout mainLayout = findViewById(R.id.mainLayout);
        CalculatorDisplay calculatorDisplay = findViewById(R.id.calculatorDisplay);
        for (CalculatorButtonData data : calculatorButtonData) {
            CalculatorButton button = new CalculatorButton(
                this,
                data,
                (view)-> {
                    String content = calculatorDisplay.getText().toString();
                    switch (data.getType()) {
                        case EQUALS:
                            if (content.length() == 0) break;
                            history.add(content);
                            calculatorDisplay.setText(Parser.evaluate(content).toString());
                            break;
                        case NUMBER:
                            calculatorDisplay.setText(content + data.getText());
                            break;
                        case OPERATOR:
                            calculatorDisplay.setText(content + " "  + data.getText() + " ");
                            break;
                        case CLEAR:
                            calculatorDisplay.setText("");
                            break;
                        case PREVIOUS:
                            String previous = history.size() > 0 ? history.remove(history.size() - 1) : content;
                            calculatorDisplay.setText(previous);
                            break;
                        case DELETE:
                            if (content.length() == 0) break;
                            int remove = 2;
                            if (content.length() == 1) remove = 1;
                            String deleted = content.substring(0, content.length() - remove);
                            calculatorDisplay.setText(deleted);
                            break;

                    }
                }
            );
            mainLayout.addView(button);
        }
    }
}