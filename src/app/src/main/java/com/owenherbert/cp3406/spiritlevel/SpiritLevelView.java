package com.owenherbert.cp3406.spiritlevel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SpiritLevelView extends View {

    private static final int COLOUR_LIME = Color.rgb(50, 205, 128);
    private static final int COLOUR_TOMATO = Color.rgb(100, 31, 18);
    private static final int COLOUR_GREY = Color.rgb(59, 59,59);

    private static final int LEVEL_BUFFER = 8;
    private static final int[] levelAngles = { 0, 90, 180 };

    Paint paint;
    private float[] orientation;

    public SpiritLevelView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        paint = new Paint();
        orientation = new float[3];
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float spiritOrientation = getZDegrees() + 90;

        // draw background
        paint.setColor(COLOUR_GREY);

        @SuppressLint("DrawAllocation")
        Rect backgroundRectangle = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawRect(backgroundRectangle, paint);

        // draw the spirit level
        canvas.rotate(spiritOrientation, getWidth() >> 1, getHeight() >> 1);

        if (isAlmostLevel(spiritOrientation)) {
            paint.setColor(COLOUR_LIME);
        } else {
            paint.setColor(COLOUR_TOMATO);
        }

        @SuppressLint("DrawAllocation")
        Rect spiritLevelRectangle = new Rect(-getWidth(), -getHeight(), getWidth() / 2,
                getHeight() * 2);

        canvas.drawRect(spiritLevelRectangle, paint);

        invalidate();
    }

    /**
     * Returns if the spirit orientation is almost level to the phones four sides.
     *
     * @param spiritOrientation the spirit orientation angle
     * @return if the spirit level is almost flat
     */
    private static boolean isAlmostLevel(float spiritOrientation) {

        for (int levelAngle : levelAngles) {
            if (spiritOrientation > levelAngle - LEVEL_BUFFER
                    && spiritOrientation < levelAngle + LEVEL_BUFFER) return true;
        }

        return false;
    }

    /**
     * Returns Z degrees.
     *
     * @return z degrees
     */
    public float getZDegrees() {

        return (orientation[2]) % 360;
    }

    /**
     * Sets the orientation.
     * 
     * @param orientation the orientation
     */
    public void setOrientation(float[] orientation) {
        this.orientation = orientation;
    }
}
