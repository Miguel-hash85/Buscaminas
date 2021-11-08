package com.example.buscaminas;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonXY extends androidx.appcompat.widget.AppCompatButton{

    private int xCoord;
    private int yCoord;


    public ButtonXY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getxCoord() {
        return xCoord;
    }


    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
