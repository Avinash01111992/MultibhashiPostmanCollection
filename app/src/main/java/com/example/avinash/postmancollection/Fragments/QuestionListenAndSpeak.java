package com.example.avinash.postmancollection.Fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avinash.postmancollection.Pojo.QuestionsDo;
import com.example.avinash.postmancollection.Activity.QuestionsBoard;
import com.example.avinash.postmancollection.R;
import com.example.avinash.postmancollection.Utility.Connectivity;
import com.example.avinash.postmancollection.Utility.Utility;

import java.util.ArrayList;
import java.util.Locale;



import static android.app.Activity.RESULT_OK;

/**
 * Created by avinash on 13/4/18.
 */
public class QuestionListenAndSpeak extends Fragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextView tvPhraseToDisplay, tvUserInput;
    Button btnSpeak,  btnNext;
    ImageView imageViewSignal,imageSpeak;
    View rootView;
    TextView textViewResult;
    LinearLayout linearLayoutResult;
    ImageView imageViewresult;
    int buttonCount = 0;
    QuestionsDo questionsDo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_speak_and_learn, container, false);

        questionsDo = (QuestionsDo) getArguments().getSerializable("questionType");

        textViewResult = (TextView) rootView.findViewById(R.id.textView_result);
        linearLayoutResult = (LinearLayout) rootView.findViewById(R.id.linearLayout_result);
        imageViewresult = (ImageView) rootView.findViewById(R.id.imageView_result);

        btnSpeak = (Button) rootView.findViewById(R.id.button_speak);
        btnNext = (Button) rootView.findViewById(R.id.button_next);

        tvPhraseToDisplay = (TextView) rootView.findViewById(R.id.tv_phrase_to_speak);
        tvPhraseToDisplay.setText(questionsDo.getTargetScript());

        tvUserInput = (TextView) rootView.findViewById(R.id.tv_phrase_user_input);

        imageViewSignal = (ImageView) rootView.findViewById(R.id.imageView_signal);
        imageSpeak = (ImageView) rootView.findViewById(R.id.iv_speak_question_lc);


        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        imageSpeak.startAnimation(animShake);


        imageSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.stopPlaying();
                speakOut();
            }
        });


        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Connectivity.isNetworkAvailable(getActivity())) {


                    if (buttonCount == 0) {
                        loadSpeechToText();
                    }
                } else {
                    Toast.makeText(getActivity(), "There is no internet Connection, please connect internet and try again", Toast.LENGTH_LONG).show();

                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utility.stopPlaying();
                ((QuestionsBoard) getActivity()).loadingQuiz();
            }
        });

        return  rootView;

    }



    /** Speach to text method **/
    private void loadSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(), "speech not supported",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Play the audio**/
    private void speakOut() {
        Utility.playAudioFiles(getActivity(),questionsDo.getAudio_url());
    }

    /** Check the user answer and the system answer**/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvUserInput.setText(result.get(0));

                    String str = questionsDo.getPronunciation();

                    str = str.replaceAll("[|?*<\":>+\\[\\]/',.]", "");

                    Log.e("str",str);

                    if (str.trim().equalsIgnoreCase(tvUserInput.getText().toString().trim())) {
                        imageViewSignal.setImageResource(R.drawable.green_light);

                        btnSpeak.playSoundEffect(SoundEffectConstants.CLICK);

                        textViewResult.setBackgroundColor(Color.parseColor("#F0F8E7"));
                        linearLayoutResult.setBackgroundColor(Color.parseColor("#A4C639"));

                        String resultText = "Answer" + ": " + "\"" + questionsDo.getTargetScript() + "\"";
                        textViewResult.setText(resultText);
                        linearLayoutResult.setVisibility(View.VISIBLE);
                        ((QuestionsBoard) getActivity()).progressIndicator();
                        imageViewresult.setImageResource(R.drawable.right);
                        imageViewresult.setBackgroundColor(Color.parseColor("#F0F8E7"));
                        Utility.playAudio(getActivity(), true);
                        buttonCount++;
                        btnSpeak.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                    } else {
                        imageViewSignal.setImageResource(R.drawable.red_light);

                        linearLayoutResult.setVisibility(View.VISIBLE);

                        btnSpeak.playSoundEffect(SoundEffectConstants.CLICK);

                        textViewResult.setBackgroundColor(Color.parseColor("#F0F8E7"));
                        linearLayoutResult.setBackgroundColor(Color.parseColor("#A4C639"));

                        String resultText = "Answer" + ": " + "\"" + questionsDo.getTargetScript() + "\"";
                        textViewResult.setText(resultText);
//                        ((QuestionsBoard) getActivity()).progressIndicator();
                        imageViewresult.setImageResource(R.drawable.wrong);
                        imageViewresult.setBackgroundColor(Color.parseColor("#F0F8E7"));
                        Utility.playAudio(getActivity(), true);
                        btnNext.setVisibility(View.VISIBLE);
                        btnSpeak.setVisibility(View.GONE);
                        buttonCount++;

                    }



                }
                break;
            }

        }
    }

}

