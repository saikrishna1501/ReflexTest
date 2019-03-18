package com.example.reflextest;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView headingTextView;// = (TextView) findViewById(R.id.headingTextView);
    TextView lastScoreTextView;// = (TextView) findViewById(R.id.lastScoreTextView);
    Button reflexButton ;//= (Button) findViewById(R.id.reflexButton);
    Button startButton ;//= (Button) findViewById(R.id.startButton);
    int lastScore = 0;
    int reflexTime = 0;
    int delay = 0;
    int timeElapsed = 0;
    Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {

        }
    };
    int delayElapsed;

    int stage;
    // stage 1 is the stage that appears at the start of the app
    // stage 2 is the stage that appears during the get ready stage
    // stage 3 is the stage that apppears at the start of green color
    // stage 4 is the stage that appears after tapping the button
    // stage 5 is the stage that appears after tapping the button too soon

    /*
    transitions of stages :
    intially
    stage 1 on tap -> stage 2 after waiting for random delay -> stage 3 after tapping -> stage 4

    on restart
    stage 2 after waiting for random delay -> stage 3 after tapping -> stage 4

    stage 2 -> not waiting for random delay ->stage 5

    any stage on restart
    transition back to stage 2
     */

    /*
    colors :
    yellow : #FFF4F142
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stage = 1;
        headingTextView = (TextView) findViewById(R.id.headingTextView);
        lastScoreTextView = (TextView) findViewById(R.id.lastScoreTextView);
        reflexButton = (Button) findViewById(R.id.reflexButton);
        startButton = (Button) findViewById(R.id.startButton);
        reflexButton.setBackgroundColor(Color.parseColor("#f4f142"));
    }

    public void reflexButtonPressed(View view) {
        if(stage == 2) {
            handler.removeCallbacks(run);
            stage = 5;
            viewElementsInStage();
        }
        if(stage == 3) {
            stage++;
            handler.removeCallbacks(run);
            reflexTime = timeElapsed - delay;
            viewElementsInStage();
        }
    }
    public void viewElementsInStage() {

        if(stage == 1) {
            startButton.setText("START");
            lastScoreTextView.setText("Last Score : " + lastScore);
            reflexButton.setText("Tap when the color turns green");
        }
        else if(stage == 2) {
            startButton.setText("Restart");
            lastScoreTextView.setText("Last Score : " + lastScore + " ms");
            reflexButton.setText("Get Ready");
            reflexButton.setBackgroundColor(Color.parseColor("#f4f142")); // yellow
        }
        else if(stage == 3) {
            reflexButton.setText("Click here");
            reflexButton.setBackgroundColor(Color.parseColor("#62f441")); //green
        }
        else if(stage == 4) {
            reflexButton.setText("relex time : " + reflexTime + " ms");
            reflexButton.setBackgroundColor(Color.parseColor("#62f441")); //green
            lastScore = reflexTime;
        }
        else if(stage == 5) {
            reflexButton.setText("Too Soon");
            reflexButton.setBackgroundColor(Color.parseColor("#f9182b")); //red
        }

    }

    public int generateRandomDelay() {
        int delay;
        Random random = new Random();
        int low = 1000;
        int high = 4000;
        delay = random.nextInt(high-low) + low; // generate a random number between 1000(inclusive) and 4000(exclusive)
        return delay;
    }


    public void startButtonPressed(View view) {
        handler.removeCallbacks(run);
        resetDefaults();
        stage = 2;
        viewElementsInStage();
        delay = generateRandomDelay();
        run = new Runnable() {
            @Override
            public void run() {
                timeElapsed++;
                if(timeElapsed == delay) {
                    stage = 3;
                    viewElementsInStage();
                }
                handler.postDelayed(this,1);
            }
        };
        handler.post(run);
    }
    public void resetDefaults() {
        reflexTime = 0;
        delay = 0;
        timeElapsed = 0;
    }
}
