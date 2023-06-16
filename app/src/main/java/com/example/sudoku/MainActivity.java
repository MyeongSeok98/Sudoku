package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    CustomButton[][] buttons = new CustomButton[9][9];
    BoardGenerator board = new BoardGenerator();

    //null 꼭 필요한가
    CustomButton clickedCustomButton;


    //입력 테이블 레이아웃
    TableLayout tableLayout;

    //메모 테이블 레이아웃
    TableLayout memoLayout;
    /*
    LayoutInflater dialogMemoLayoutInflator;
    FrameLayout MemoFrameLayout;
    Button cancel;
    Button delete;w
    Button memoOK;
    boolean[] checkMemoNum = new boolean[9];
    ToggleButton[] memoNumToggle = new ToggleButton[9];
    */

    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table;
        table = findViewById(R.id.t1);
        tableLayout = findViewById(R.id.NumPad);
        tableLayout.setVisibility(View.INVISIBLE);

        //메모 레이아웃
        memoLayout = findViewById(R.id.MemoPad);
        memoLayout.setVisibility(View.INVISIBLE);
        /*
        MemoFrameLayout = findViewById(R.id.frameLayout);

        dialogMemoLayoutInflator = (LayoutInflater)  this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogMemoLayoutInflator.inflate(R.layout.dialog_memo, MemoFrameLayout,true);
         */

        for(int i = 0; i < 9; i++){
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);

            //[9][9]개의 버튼 생성
            for(int j = 0; j < 9; j++){
                buttons[i][j] = new CustomButton(this,i, j);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);
                layoutParams.setMargins(2,2,2,2);

                //짧게 눌러서 숫자 입력
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedCustomButton = (CustomButton) view;
                        tableLayout.setVisibility(View.VISIBLE);
                        memoLayout.setVisibility(View.INVISIBLE);
                    }
                });

                //길게 눌러서 숫자 메모

                buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        clickedCustomButton = (CustomButton) view;
                        memoLayout.setVisibility(View.VISIBLE);
                        tableLayout.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });


                //보드에서 i행 j열의 숫자를 가져옴
                int number = board.get(i,j);
                buttons[i][j].setLayoutParams(layoutParams);

                // 버튼 i j 삽입
                tableRow.addView(buttons[i][j]);
                double random = (int) (Math.random() * 100);
                if(random > 50) {

                    //버튼 i j의 텍스트 수정
                    buttons[i][j].set(number);
                }

            }
        }
        TableLayout scoreTable;
        scoreTable = findViewById(R.id.t2);
        TableRow scoreRow = new TableRow(this);
        scoreTable.addView(scoreRow);
        TextView scoreBoard = new TextView(this);
        TextView scoreBoardName = new TextView(this);
        scoreRow.setWeightSum(6f);
        scoreBoardName.setText("Score : ");
        scoreBoardName.setTextSize(40);
        scoreBoardName.setGravity(Gravity.CENTER);
        scoreBoardName.setBackgroundColor(Color.GREEN);
        scoreBoard.setTextSize(40);
        scoreBoard.setGravity(Gravity.CENTER);
        scoreBoard.setBackgroundColor(Color.GREEN);
        scoreBoard.setText(String.valueOf(score));
        scoreRow.addView(scoreBoardName);
        scoreRow.addView(scoreBoard);


    }
    //비충돌 판정
    public void unsetConflict(){
        int col = clickedCustomButton.col;
        int row = clickedCustomButton.row;
        int value = clickedCustomButton.value;
        TableLayout tableLayout = findViewById(R.id.t1);

        int flagRow = 0;
        int flagCol = 0;
        int flagBox = 0;

        //행
        for(int i = 0; i < 9; i++) {
            TableRow tR = (TableRow) tableLayout.getChildAt(row);
            CustomButton cb = (CustomButton) tR.getChildAt(i);

            if (value != cb.value){
                cb.setBackgroundColor(Color.WHITE);
                flagCol++;
                if(flagCol == 8){
                    score +=2;
                }
            }
        }
        //열
        for(int i = 0; i < 9; i++) {
            TableRow tR = (TableRow) tableLayout.getChildAt(i);
            CustomButton cb = (CustomButton) tR.getChildAt(col);
            if (value != cb.value){
                cb.setBackgroundColor(Color.WHITE);
                flagRow++;
                if(flagRow == 8){
                    score +=2;
                }
            }
        }
        //박스
        //// 각 좌표를 3으로 나눠 준 후 *3을 해주면 해당 좌표가 속한 박스의 시작점이 나온다.

        col /= 3;
        row /= 3;
        col *= 3;
        row *= 3;

        for(int i = 0; i < 3; i++){
            TableRow tR = (TableRow) tableLayout.getChildAt(row+i);
            for(int j = 0; j < 3; j++)
            {
                CustomButton cb = (CustomButton) tR.getChildAt(col+j);
                if (value != cb.value){
                    cb.setBackgroundColor(Color.WHITE);
                    flagBox++;
                    if(flagBox == 8){
                        score +=2;
                    }
                }
            }
        }


    }
    //점수 적용
    public void setScore(){
        TableLayout scoreTableLayout = findViewById(R.id.t2);
        TableRow tR = (TableRow) scoreTableLayout.getChildAt(0);
        TextView score1 = (TextView) tR.getChildAt(1);
        score1.setText(String.valueOf(score));
    }

    //setConflict는 버튼마다 적용
    //충돌판정

    public void setConflict(){
        int col = clickedCustomButton.col;
        int row = clickedCustomButton.row;
        int value = clickedCustomButton.value;

        int flagMinus = 0;


        TableLayout tableLayout = findViewById(R.id.t1);

        //행
        for(int i = 0; i < 9; i++) {
            TableRow tR = (TableRow) tableLayout.getChildAt(row);
            CustomButton cb = (CustomButton) tR.getChildAt(i);


            //본인을 제외하는 변수
            TableRow trCJJ = (TableRow) tableLayout.getChildAt(row);
            CustomButton cbCollect = (CustomButton) trCJJ.getChildAt(col);

            if (value == cb.value){
                cb.setBackgroundColor(Color.RED);
                flagMinus++;

            }
            if(cbCollect.value == value){
                cbCollect.setBackgroundColor(Color.WHITE);
            }
        }
        //열
        for(int i = 0; i < 9; i++) {

            TableRow tR = (TableRow) tableLayout.getChildAt(i);
            CustomButton cb = (CustomButton) tR.getChildAt(col);

            //본인을 제외하는 변수
            TableRow trCJJ = (TableRow) tableLayout.getChildAt(row);
            CustomButton cbCollect = (CustomButton) trCJJ.getChildAt(col);


            if (value == cb.value){
                cb.setBackgroundColor(Color.RED);
                flagMinus++;

            }

            if(cbCollect.value == value){
                cbCollect.setBackgroundColor(Color.WHITE);
            }
        }
        //박스
        //// 각 좌표를 3으로 나눠 준 후 *3을 해주면 해당 좌표가 속한 박스의 시작점이 나온다.

        //자기 자신의 좌표 먼저 저장
        int colMy = col;
        int rowMy = row;

        //이후 좌표 지정 위해 저장
        col /= 3;
        row /= 3;
        col *= 3;
        row *= 3;

        for(int i = 0; i < 3; i++){

            TableRow tR = (TableRow) tableLayout.getChildAt(row+i);

            //본인제외 변수
            TableRow tRCollect = (TableRow) tableLayout.getChildAt(rowMy);
            CustomButton cbCollect = (CustomButton) tRCollect.getChildAt(colMy);

            for(int j = 0; j < 3; j++)
            {
                CustomButton cb = (CustomButton) tR.getChildAt(col+j);
                if (value == cb.value){
                    cb.setBackgroundColor(Color.RED);
                    flagMinus++;

                }
                if(cbCollect.value == value){
                    cbCollect.setBackgroundColor(Color.WHITE);
                }
            }

        }
/*
        for (int i = 0; i < 9; i++) {
            if (buttons[row][i].value == value && clickedCustomButton != buttons[row][i]) {
                buttons[row][i].textView.setBackgroundColor(Color.RED);
                clickedCustomButton.textView.setBackgroundColor(Color.RED);
            }

            if (buttons[i][col].value == value && clickedCustomButton != buttons[i][col]) {
                buttons[i][col].textView.setBackgroundColor(Color.RED);
                clickedCustomButton.textView.setBackgroundColor(Color.RED);
            }
        }

        for (int i = clickedCustomButton.cbRow; i < clickedCustomButton.cbRow + 3; i++) {
            for (int j = clickedCustomButton.cbCol; j < clickedCustomButton.cbCol + 3; j++) {
                if (buttons[i][j].value == value && clickedCustomButton != buttons[i][j]) {
                    buttons[i][j].textView.setBackgroundColor(Color.RED);
                    clickedCustomButton.textView.setBackgroundColor(Color.RED);
                }
            }
        }
*/
        if(flagMinus >= 1) {
            score-=1;
        }
    }


    //숫자 입력 1~취소까지
    public void onClickNum1(View v) {
        clickedCustomButton.set(1);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum2(View v) {
        clickedCustomButton.set(2);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum3(View v) {
        clickedCustomButton.set(3);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum4(View v) {
        clickedCustomButton.set(4);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum5(View v) {
        clickedCustomButton.set(5);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum6(View v) {
        clickedCustomButton.set(6);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum7(View v) {
        clickedCustomButton.set(7);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum8(View v) {
        clickedCustomButton.set(8);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum9(View v) {
        clickedCustomButton.set(9);
        tableLayout.setVisibility(View.INVISIBLE);
        setConflict();
        unsetConflict();
        onClickButtonAllMemoDelete();
        setScore();
        clickedButtonBackground();
    }

    public void onClickNum0(View v) {
        clickedCustomButton.set(0);
        tableLayout.setVisibility(View.INVISIBLE);
        unsetConflict();
    }

    public void onClickCancel(View v) {
        tableLayout.setVisibility(View.INVISIBLE);
    }


    //숫자 메모 1~취소까지
    public void m1(View view) {
        if (clickedCustomButton.mT[0][0].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[0][0].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[0][0].setVisibility(View.VISIBLE);
        }
    }
    public void m2(View view) {
        if (clickedCustomButton.mT[0][1].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[0][1].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[0][1].setVisibility(View.VISIBLE);
        }
    }
    public void m3(View view) {
        if (clickedCustomButton.mT[0][2].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[0][2].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[0][2].setVisibility(View.VISIBLE);
        }
    }
    public void m4(View view) {
        if (clickedCustomButton.mT[1][0].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[1][0].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[1][0].setVisibility(View.VISIBLE);
        }
    }
    public void m5(View view) {
        if (clickedCustomButton.mT[1][1].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[1][1].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[1][1].setVisibility(View.VISIBLE);
        }
    }
    public void m6(View view) {
        if (clickedCustomButton.mT[1][2].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[1][2].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[1][2].setVisibility(View.VISIBLE);
        }
    }
    public void m7(View view) {
        if (clickedCustomButton.mT[2][0].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[2][0].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[2][0].setVisibility(View.VISIBLE);
        }
    }
    public void m8(View view) {
        if (clickedCustomButton.mT[2][1].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[2][1].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[2][1].setVisibility(View.VISIBLE);
        }
    }
    public void m9(View view) {
        if (clickedCustomButton.mT[2][2].getVisibility() == View.VISIBLE) {
            clickedCustomButton.mT[2][2].setVisibility(View.INVISIBLE);
        } else {
            clickedCustomButton.mT[2][2].setVisibility(View.VISIBLE);
        }
    }

    public void m0(View view) {
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                clickedCustomButton.mT[a][b].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void memoOK(View view) {

        memoLayout.setVisibility(View.INVISIBLE);
    }

    public void onClickButtonAllMemoDelete() {
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                clickedCustomButton.mT[a][b].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void clickedButtonBackground() {
        clickedCustomButton.setBackgroundColor(Color.rgb(252,242,206));
    }

}