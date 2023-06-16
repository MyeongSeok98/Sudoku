package com.example.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomButton extends FrameLayout {

    int row;
    int col;
    int value;

    int cbRow;
    int cbCol;



    TextView textView;
    TableLayout memo;
    TextView[] memos = new TextView[9];
    TextView mT[][]=new TextView[3][3];
    boolean customButton;



    public CustomButton(Context context, int row, int col) {
        super(context);
        this.row = row;
        this.col = col;

        cbCol = (this.col / 3) * 3;
        cbRow = (this.row / 3) * 3;

        this.textView = new TextView(context);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        addView(textView);
        setClickable(true);
        setBackgroundResource(R.drawable.button_selector);


        //메모기능
/*

        LayoutInflater layoutInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memo = (TableLayout) layoutInflator.inflate(R.layout.layout_memo, null);
        addView(memo);

        int k = 0;

        for(int i = 0; i < 3; i++){
            TableRow tableRow = (TableRow) memo.getChildAt(i);

            for(int j = 0; j < 3; j++, k++){
                TextView tv = (TextView) tableRow.getChildAt(j);
                tv.setVisibility(INVISIBLE);
                memos[k] = tv;
            }
        }


 */TableLayout tL = new TableLayout(context);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.7f);

        int a=1;
        for (int c = 0; c < 3; c++) {

            TableRow tR = new TableRow(context);
            tL.setLayoutParams(layoutParams);
            for (int d = 0; d < 3; d++) {
                mT[c][d] = new TextView(context);
                mT[c][d].setVisibility(INVISIBLE);
                mT[c][d].setLayoutParams(layoutParams);
                mT[c][d].setText(String.valueOf(a++));
                mT[c][d].setTextColor(Color.rgb(51,255,51));
                tR.addView(mT[c][d]);
            }
            tL.addView(tR);
        }
        TableLayout.LayoutParams lP1 = new TableLayout.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f);
        tL.setLayoutParams(lP1);
        addView(tL);
        


    }

    void setText(String a){
        int numInt = Integer.parseInt(a);
        if(numInt == 0){
            textView.setText(null);
        }else {
            textView.setText(a);
        }
    }


    public void set(int i) {
        if(i == 0){
            this.value = 0;
            textView.setText(null);
        }else{
            this.value = i;
            textView.setText(String.valueOf(i));
        }
    }
    void returnValue(int row, int col){
        this.row = row;
        this.col = col;

    }


}



