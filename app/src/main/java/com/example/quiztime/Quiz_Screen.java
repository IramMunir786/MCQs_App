package com.example.quiztime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Quiz_Screen extends AppCompatActivity {

    ArrayList<Integer> randGenerated=new ArrayList<Integer>();
    int noOfQues;
    String time;
    ArrayList<Integer> questionIndex=new ArrayList<Integer>();
    ArrayList<Integer> skipIndex=new ArrayList<Integer>();
    ArrayList<String> answers=new ArrayList<String>();
    int questionAnswered=0;
    int quesIndex=0;
    boolean submitted=false;
    CountDownTimer countDownTimer;
    long timeLeftInMilliSecond;
    TextView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__screen);

        //set heading in center
        TextView textView1 = new TextView(this);
        textView1.setText("General Knowledge Quiz");
        textView1.setTextSize(20);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(getResources().getColor(R.color.color_btn));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView1);




        Intent intent = getIntent();
        noOfQues = Integer.parseInt(intent.getStringExtra("NoOfQues"));
        time = intent.getStringExtra("Time");

        timerView=findViewById(R.id.timer);
        timerView.setText(time+":00");


        if (time.equals("10")){
            timeLeftInMilliSecond=600000;
        }else if (time.equals("20")){
            timeLeftInMilliSecond=1200000;
        }else{
            timeLeftInMilliSecond=1800000;
        }

        countDownTimer=new CountDownTimer(timeLeftInMilliSecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSecond=millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

                int total=questionIndex.size()+skipIndex.size();
                int totall=noOfQues-total;
                for (int i=0;i<totall;i++){
                    skipIndex.add(0);
                }

                Toast toast = Toast.makeText(getApplicationContext(), "Time's Up!", Toast.LENGTH_SHORT);
                toast.show();
                showIntent();

            }
        }.start();


        final String []array=getResources().getStringArray(R.array.MCQs);
        final String []array2=getResources().getStringArray(R.array.MCQs_Options);


    quesIndex = displayQuestion(array, array2);


    Button btn_sumbit = findViewById(R.id.submit);

    btn_sumbit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            int checked1 = radioGroup.getCheckedRadioButtonId();

            if (checked1 == -1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please Select one option to submit", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                RadioButton radioButton = findViewById(checked1);
                String text = radioButton.getText().toString();

                answers.add(text);
                questionIndex.add(quesIndex);

                radioGroup.clearCheck();

                if (noOfQues!=questionAnswered) {
                    quesIndex = displayQuestion(array, array2);
                }
                else {
                   showIntent();
                }
            }

        }
    });

    Button btn_skip=findViewById(R.id.skip);
    btn_skip.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            skipIndex.add(quesIndex);
            if (noOfQues!=questionAnswered) {
                quesIndex = displayQuestion(array, array2);
            }
            else {
                //show result
               showIntent();
            }

        }
    });
}



    public int displayQuestion(String[] array, String[] array2) {
        int i1;

        do {
            Random r = new Random();
            i1  =r.nextInt(40 - 0) + 0;
        }while(randGenerated.contains(i1));

        questionAnswered=questionAnswered+1;
        TextView txt=findViewById(R.id.QuestionCount);
        String count=questionAnswered+"/"+noOfQues;
        txt.setText(count);
        randGenerated.add(i1);



        TextView textView=findViewById(R.id.Question);
        textView.setText(array[i1]);

        RadioButton rb1=findViewById(R.id.rb1);
        int i2=i1*4;
        rb1.setText(array2[i2]);
        rb1.setChecked(false);

        RadioButton rb2=findViewById(R.id.rb2);
        i2=i2+1;
        rb2.setText(array2[i2]);
        rb2.setChecked(false);

        RadioButton rb3=findViewById(R.id.rb3);
        i2=i2+1;
        rb3.setText(array2[i2]);
        rb3.setChecked(false);

        RadioButton rb4=findViewById(R.id.rb4);
        i2=i2+1;
        rb4.setText(array2[i2]);
        rb4.setChecked(false);

        return i1;
    }

    public void updateTimer(){
        int min=(int)timeLeftInMilliSecond/60000;
        int second=(int)timeLeftInMilliSecond%60000/1000;

        String timee=min+":";
        if (second<10){
            timee+="0";
        }
        timee+=second;
        timerView.setText(timee);

    }

    public void showIntent(){
        Intent intent=new Intent(getApplicationContext(),resultCard.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST1",(Serializable)questionIndex);
        intent.putExtra("Question", args);


        Bundle args1 = new Bundle();
        args1.putSerializable("ARRAYLIST2",(Serializable)skipIndex);

        intent.putExtra("Skip",args1);

        Bundle args2 = new Bundle();
        args2.putSerializable("ARRAYLIST3",(Serializable)answers);
        intent.putExtra("answers",args2);
        startActivity(intent);

        finish();
    }

}