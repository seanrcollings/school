package com.example.tree.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;


public class TreeView extends View {
    private Paint paint = new Paint();
    private Tree tree;

    public TreeView(Context context, Intent intent) {
        super(context);
        paint.setColor(Color.BLACK);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0xFF40b6ff,0xFFFFFFDD});
        setBackground(gd);

        Bundle extras = intent.getExtras();
        tree = new Tree(
                Integer.parseInt((String) extras.get("max_length")),
                Integer.parseInt((String) extras.get("min_length")),
                Float.parseFloat((String) extras.get("max_angle")),
                Float.parseFloat((String) extras.get("min_angle")),
                Integer.parseInt((String) extras.get("max_trunk_width")),
                Integer.parseInt((String) extras.get("min_trunk_width"))
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tree.createTree(getHeight(), getWidth());
        tree.drawTree(canvas, paint);
    }
}

// This thing is overly long and complex, but I don't feel like changing it
class Tree {
    private TreeComponent root;
    private final int MAX_CHILDREN = 3;
    private final int MAX_DEPTH = 12;
    private int maxLength;
    private int minLength;
    private float maxAngle;
    private float minAngle;
    private int minTrunkWidth;
    private int maxTrunkWidth;

    public Tree(int maxLength, int minLength, float maxAngle, float minAngle, int maxTrunkWidth, int minTrunkWidth) {
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.maxAngle = maxAngle;
        this.minAngle = minAngle;
        this.maxTrunkWidth = maxTrunkWidth;
        this.minTrunkWidth = minTrunkWidth;
    }

    public void createTree(int height, int width) {
        root = createComponents(width / 2, height,  minTrunkWidth, maxTrunkWidth, 0, 0);
    }

    private TreeComponent createComponents(float startX, float startY, int minWidth,
                                           int width, float angle, int depth) {

        int length = getRandomNumberBetween(minLength, maxLength);
        float endX = (float)(startX + Math.cos(Math.toRadians(angle)) + length);
        float endY = (float)(startY - Math.sin(Math.toRadians(angle)) - length);
        TreeComponent newComp = new TreeComponent(startX, startY, endX, endY, width, angle);

        if (width <= 5 || depth >= MAX_DEPTH) return newComp;

        int numberOfKids = getRandomNumberBetween(0, MAX_CHILDREN);
        for (int i = 0; i <= numberOfKids; i++) {
            newComp.children.add(createComponents(
                    endX,
                    endY,
                    5,
                    getRandomNumberBetween(minWidth, width),
                    getRandomNumberBetween(minAngle, maxAngle),
                    depth + 1));
        }

        return newComp;
    }

    public void drawTree(Canvas canvas, Paint paint) {
        drawTree(canvas, root, paint);
    }

    private void drawTree(Canvas canvas, TreeComponent component, Paint paint) {
        if (component == null) return;
        component.draw(canvas, paint);
        component.children.forEach(child -> {
            drawTree(canvas, child, paint);
        });
    }

    private int getRandomNumberBetween(int first, int second) {
        return (int)(Math.random() * (second - first) + first);
    }

    private float getRandomNumberBetween(float first, float second) {
        return (float)(Math.random() * (second - first) + first);
    }


    private class TreeComponent {
        private float startX;
        private float startY;
        private float angle;
        private float width;
        private float endY;
        private float endX;
        public ArrayList<TreeComponent> children;

        public TreeComponent(float startX, float startY, float endX, float endY,  float width, float angle) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.angle = angle;
            this.width = width;
            children = new ArrayList<>();
        }

        public void draw(Canvas canvas, Paint paint) {
            canvas.save();
            paint.setStrokeWidth(width);
            canvas.translate(startX, startY);
//            canvas.rotate(angle);
            canvas.drawLine(0, 0, endX, -atmaendY, paint);
            canvas.restore();
        }
    }
}
