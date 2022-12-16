package com.example.tree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.tree.views.LabelInput;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private InputData[] inputData = {
            new InputData("Max Length", "200"),
            new InputData("Min Length", "50"),
            new InputData("Max Angle", "20"),
            new InputData("Min Angle", "-20"),
            new InputData("Max Trunk Width", "300"),
            new InputData("Min Trunk Width", "100")
    };
    private ArrayList<LabelInput> labelInputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        for (InputData data : inputData) {
            LabelInput input = new LabelInput(this, data);
            labelInputs.add(input);
            mainLayout.addView(input);
        }

        AppCompatButton button = new AppCompatButton(this);
        button.setText(R.string.generate_button);
        button.setOnClickListener((view) -> {
            Intent intent = new Intent(this, TreeActivity.class);
            for(LabelInput input : labelInputs) {
                intent.putExtra(input.getLabel(), input.getText().toString());
            }
            startActivity(intent);
        });
        mainLayout.addView(button);

        setContentView(mainLayout);
    }
}