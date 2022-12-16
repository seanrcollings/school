package com.example.contact.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

// This isn't a good solution, but it's the best one I could come up with :(
// Based on: https://stackoverflow.com/a/8831182/7829170
public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private final int backgroundColor;

    public TextDrawable(String text, int backgroundColor) {

        this.text = text;
        this.backgroundColor = backgroundColor;
        this.paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setTextSize(150f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        canvas.drawText(text, 75, 110, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        paint.setColorFilter(filter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
