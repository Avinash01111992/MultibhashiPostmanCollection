package com.example.avinash.postmancollection.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.avinash.postmancollection.Fragments.Learn;
import com.example.avinash.postmancollection.Fragments.QuestionListenAndSpeak;
import com.example.avinash.postmancollection.Pojo.ListQuestionsDo;
import com.example.avinash.postmancollection.R;
import com.example.avinash.postmancollection.Utility.Connectivity;
import com.example.avinash.postmancollection.Utility.Utility;
import com.example.avinash.postmancollection.volley.GsonRequest;
import com.example.avinash.postmancollection.volley.VolleyService;

import static com.example.avinash.postmancollection.Utility.constants.POSTMAN_API;


/**
 * Created by avinash on 13/4/18.
 */

public class QuestionsBoard extends AppCompatActivity implements
        Learn.OnArticleSelectedListener {
    ProgressBar myprogress;
    ImageView imageViewCloseQuestionsBoard;
    int questionsCount = -1;
    int totalScore;
    ListQuestionsDo listQuestionsDo;
    int INCREMENT_PROGRESS_BAR;

    TextView tvNumOfQuest;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_board);
        VolleyService.init(this);
        dialog = new ProgressDialog(QuestionsBoard.this);
        dialog.setMessage("Loading lesson data");


        tvNumOfQuest = (TextView) findViewById(R.id.tv_num_of_quest);

        myprogress = (ProgressBar) findViewById(R.id.myprogressbar);
        imageViewCloseQuestionsBoard = (ImageView) findViewById(R.id
                .imageView_close_questions_board);
        INCREMENT_PROGRESS_BAR = getResources().getInteger(R.integer.progress_bar_incrementation);


        imageViewCloseQuestionsBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        float[] roundedCorners = new float[]
                {5, 5, 5, 5, 5, 5, 5, 5};
        myprogress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
        shape.getPaint().setColor(ContextCompat.getColor(getApplicationContext(),
                R.color.enable_submit_answer));
        ClipDrawable progress = new ClipDrawable(shape, Gravity.START, ClipDrawable.HORIZONTAL);
        myprogress.setProgressDrawable(progress);
        myprogress.setProgress(totalScore * 5);
        imageViewCloseQuestionsBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Connectivity.isNetworkAvailable(QuestionsBoard.this)) {

            dialog.show();
            callGetRestAPIandGetLessionData();

        } else {
            dialog.dismiss();
            showNoConnection();
        }

    }

    /** Call Postman GET  API and get the lesson data **/
    public void callGetRestAPIandGetLessionData() {
        GsonRequest<ListQuestionsDo> myReq = new GsonRequest<>(POSTMAN_API,
                ListQuestionsDo.class, null,
                createMyReqSuccessListener(),
                createMyReqErrorListener());
        VolleyService.getRequestQueue().add(myReq);
    }

    private Response.Listener<ListQuestionsDo> createMyReqSuccessListener() {
        return new Response.Listener<ListQuestionsDo>() {

            /** On success put the response into Pojo **/
            @Override
            public void onResponse(ListQuestionsDo response) {
                listQuestionsDo = response;
                loadingQuiz();
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                // Do whatever you want to do with error.getMessage();
            }
        };
    }


    /** Load lesson data and call respective fragment based on lesson type (Learn or Question**/
    public void loadingQuiz() {
        dialog.dismiss();
        Utility.stopPlaying();
        questionsCount++;

        String questionCount = "Question No: " + (questionsCount) + " of "
                + listQuestionsDo.getLesson_data().size();

        tvNumOfQuest.setText(questionCount);

        if (questionsCount < listQuestionsDo.getLesson_data().size()) {
            if (listQuestionsDo.getLesson_data().get(questionsCount).getType().trim().equalsIgnoreCase("learn")) {
                Learn fragmentMCF = new Learn();
                Bundle bundleQuizInfo = new Bundle();
                bundleQuizInfo.putSerializable("learnType", listQuestionsDo.getLesson_data().get(questionsCount));
                fragmentMCF.setArguments(bundleQuizInfo);
                getFragmentManager().beginTransaction().replace(R.id.fl_questions_container,
                        fragmentMCF).commit();
            } else {
                QuestionListenAndSpeak fragmentLsp = new QuestionListenAndSpeak();
                Bundle bundleLsp = new Bundle();
                bundleLsp.putSerializable("questionType", listQuestionsDo.getLesson_data().get(questionsCount));
                fragmentLsp.setArguments(bundleLsp);
                getFragmentManager().beginTransaction().replace(R.id.fl_questions_container,
                        fragmentLsp).commit();
            }
        } else {
            endOfQuestions();
        }
    }


    @Override
    public void onBackPressed() {
        showAlert();

    }


    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Html.fromHtml("<font color='#000000'>Are you sure about that</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>Do you want to quit the test?</font>"));

        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(QuestionsBoard.this, SplashScreen.class));
                finish();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    /** All questions are completed **/
    public void endOfQuestions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Html.fromHtml("<font color='#000000'>Completed</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>Your test score is</font>") + " " + Integer.toString(totalScore));

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(QuestionsBoard.this, SplashScreen.class));
                finish();
            }
        });


        builder.create().show();

    }


    private void showNoConnection() {
        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    /** Increment the progress bar if the answer is correct **/
    public int progressIndicator() {
        totalScore++;
        myprogress.setMax(listQuestionsDo.getLesson_data().size() * INCREMENT_PROGRESS_BAR);
        myprogress.incrementProgressBy(INCREMENT_PROGRESS_BAR);

        return myprogress.getProgress();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /** Fragment interface listener  for loading next fragment**/
    @Override
    public void onArticleSelected(Uri articleUri) {
        loadingQuiz();
    }


}
