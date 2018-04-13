package com.example.avinash.postmancollection.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avinash.postmancollection.Pojo.QuestionsDo;
import com.example.avinash.postmancollection.R;
import com.example.avinash.postmancollection.Utility.Utility;


/**
 * Created by avinash on 13/4/18.
 */


public class Learn extends Fragment {

    static TextToSpeech tts;
    ImageView practiceImage, audioImage;
    TextView questionText, tvEndofSession;
    Button quizeButton;
    String questionTextString;
    View rootView;
    String img;
    OnArticleSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_quize_fragment, container, false);
        practiceImage = (ImageView) rootView.findViewById(R.id.practice_image_view);
        audioImage = (ImageView) rootView.findViewById(R.id.audio_icon);
        questionText = (TextView) rootView.findViewById(R.id.question_id);
        quizeButton = (Button) rootView.findViewById(R.id.quiz_button);

        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        audioImage.startAnimation(animShake);
        final QuestionsDo questionsDo = (QuestionsDo) getArguments()
                .getSerializable("learnType");


        if (questionsDo.getConceptName().trim().equalsIgnoreCase("what")) {
            img = "what";
        } else if (questionsDo.getConceptName().trim().equalsIgnoreCase("how")) {
            img = "how";
        } else {
            img = "where";
        }

        questionTextString = questionsDo.getConceptName() + " " + "(" + questionsDo.getTargetScript() + ")";
        loadImage(img, questionTextString);

        audioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.stopPlaying();

                Utility.playAudioFiles(getActivity(), questionsDo.getAudio_url());

            }
        });


        quizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.stopPlaying();

                Uri myUri = Uri.parse("http://www.google.com");

                mListener.onArticleSelected(myUri);

            }
        });


        return rootView;
    }

    public void onStop() {
        Utility.stopPlaying();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    /** Set lesson learn type data and learn image**/
    void loadImage(String learningImage, String questionTextString) {
        String PACKAGE_NAME = getActivity().getPackageName();
        int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + learningImage, null, null);
        System.out.println("IMG ID :: " + imgId);
        System.out.println("PACKAGE_NAME :: " + PACKAGE_NAME);
        questionText.setText(questionTextString);
        practiceImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));


    }


    public interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }


}
