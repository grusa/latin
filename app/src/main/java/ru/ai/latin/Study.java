package ru.ai.latin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class Study extends AppCompatActivity {
    ImageView okNo;
    TextView ask,w1,w2,w3,w4;
    int id_book,id_chapter;
    VocabularyController.Word word;
    private VocabularyController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        okNo = findViewById(R.id.okNo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        controller = new VocabularyController(this);

        ask = findViewById(R.id.textViewAsk);
        w1 = findViewById(R.id.textView1);
        w2 = findViewById(R.id.textView2);
        w3 = findViewById(R.id.textView3);
        w4 = findViewById(R.id.textView4);

        Intent intent = getIntent();
        id_book = intent.getIntExtra("id_book",0);
        id_chapter = intent.getIntExtra("id_chapter",0);

        try {callWord();} catch (Exception e ) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    private void callWord() {
        word = controller.getWord(id_book,id_chapter);
        ask.setText(word.latin);
        Random random = new Random();
        int i = random.nextInt(4);
        switch ( i) {
            case 0:{
                w1.setText(word.rus);
                w2.setText(word.wrong1);
                w3.setText(word.wrong2);
                w4.setText(word.wrong3);
                break;
            }
            case 1:{
                w1.setText(word.wrong1);
                w2.setText(word.rus);
                w3.setText(word.wrong2);
                w4.setText(word.wrong3);
                break;
            }
            case 2:{
                w1.setText(word.wrong1);
                w2.setText(word.wrong2);
                w3.setText(word.rus);
                w4.setText(word.wrong3);
                break;
            }
            case 3:{
                w1.setText(word.wrong1);
                w2.setText(word.wrong2);
                w3.setText(word.wrong3);
                w4.setText(word.rus);
            }
        }

    }
    public void bt1ChoseClicked(View view) {
        if (w1.getText() == word.rus) {
            //okNo.setImageResource(R.drawable.ok);
            //okNo.setVisibility(ImageView.VISIBLE);
            w1.setBackground(getDrawable(R.drawable.backgroundcorrect));
            w2.setVisibility(View.INVISIBLE);
            w3.setVisibility(View.INVISIBLE);
            w4.setVisibility(View.INVISIBLE);
        } else {
            //okNo.setImageResource(R.drawable.no);
            //okNo.setVisibility(ImageView.VISIBLE);
            w1.setBackground(getDrawable(R.drawable.backgroundwrong));
            if (w2.getText()==word.rus) {
                w3.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w2.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w3.getText()==word.rus) {
                w2.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w3.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w4.getText()==word.rus) {
                w2.setVisibility(View.INVISIBLE);
                w3.setVisibility(View.INVISIBLE);
                w4.setBackground(getDrawable(R.drawable.backgroundcorrect));
            }
        }
        btChoseClicked(view);
    }
    public void bt2ChoseClicked(View view) {
        if (w2.getText() == word.rus) {
            //okNo.setImageResource(R.drawable.ok);
            //okNo.setVisibility(ImageView.VISIBLE);
            w2.setBackground(getDrawable(R.drawable.backgroundcorrect));
            w1.setVisibility(View.INVISIBLE);
            w3.setVisibility(View.INVISIBLE);
            w4.setVisibility(View.INVISIBLE);
        } else {
            //okNo.setImageResource(R.drawable.no);
            //okNo.setVisibility(ImageView.VISIBLE);
            w2.setBackground(getDrawable(R.drawable.backgroundwrong));
            if (w1.getText()==word.rus) {
                w3.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w1.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w3.getText()==word.rus) {
                w1.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w3.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w4.getText()==word.rus) {
                w1.setVisibility(View.INVISIBLE);
                w3.setVisibility(View.INVISIBLE);
                w4.setBackground(getDrawable(R.drawable.backgroundcorrect));
            }
        }
        btChoseClicked(view);
    }
    public void bt3ChoseClicked(View view) {
        if (w3.getText() == word.rus) {
            //okNo.setImageResource(R.drawable.ok);
            //okNo.setVisibility(ImageView.VISIBLE);
            w3.setBackground(getDrawable(R.drawable.backgroundcorrect));
            w2.setVisibility(View.INVISIBLE);
            w1.setVisibility(View.INVISIBLE);
            w4.setVisibility(View.INVISIBLE);
        } else {
            //okNo.setImageResource(R.drawable.no);
            //okNo.setVisibility(ImageView.VISIBLE);
            w3.setBackground(getDrawable(R.drawable.backgroundwrong));
            if (w1.getText()==word.rus) {
                w2.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w1.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w2.getText()==word.rus) {
                w1.setVisibility(View.INVISIBLE);
                w4.setVisibility(View.INVISIBLE);
                w2.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w4.getText()==word.rus) {
                w2.setVisibility(View.INVISIBLE);
                w1.setVisibility(View.INVISIBLE);
                w4.setBackground(getDrawable(R.drawable.backgroundcorrect));
            }
        }
        btChoseClicked(view);
    }
    public void bt4ChoseClicked(View view) {
        if (w4.getText() == word.rus) {
            //okNo.setImageResource(R.drawable.ok);
            //okNo.setVisibility(ImageView.VISIBLE);
            w4.setBackground(getDrawable(R.drawable.backgroundcorrect));
            w2.setVisibility(View.INVISIBLE);
            w3.setVisibility(View.INVISIBLE);
            w1.setVisibility(View.INVISIBLE);
        } else {
            //okNo.setImageResource(R.drawable.no);
            //okNo.setVisibility(ImageView.VISIBLE);
            w4.setBackground(getDrawable(R.drawable.backgroundwrong));
            if (w1.getText()==word.rus) {
                w2.setVisibility(View.INVISIBLE);
                w3.setVisibility(View.INVISIBLE);
                w1.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w2.getText()==word.rus) {
                w1.setVisibility(View.INVISIBLE);
                w3.setVisibility(View.INVISIBLE);
                w2.setBackground(getDrawable(R.drawable.backgroundcorrect));
            } else if (w3.getText()==word.rus) {
                w1.setVisibility(View.INVISIBLE);
                w2.setVisibility(View.INVISIBLE);
                w3.setBackground(getDrawable(R.drawable.backgroundcorrect));
            }
        }
        btChoseClicked(view);
    }
    public void btChoseClicked(View view) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                w1.setBackground(getDrawable(R.drawable.background));
                w2.setBackground(getDrawable(R.drawable.background));
                w3.setBackground(getDrawable(R.drawable.background));
                w4.setBackground(getDrawable(R.drawable.background));
                w1.setVisibility(View.VISIBLE);
                w2.setVisibility(View.VISIBLE);
                w3.setVisibility(View.VISIBLE);
                w4.setVisibility(View.VISIBLE);

                //okNo.setVisibility(ImageView.INVISIBLE);
                try {callWord();} catch (Exception e ) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, 3000);
    }

}
