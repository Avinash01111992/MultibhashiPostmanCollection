package com.example.avinash.postmancollection.Utility;


import android.content.Context;

import android.media.AudioManager;
import android.media.MediaPlayer;

import android.speech.tts.TextToSpeech;

import android.widget.Toast;

import java.util.Locale;

import static com.example.avinash.postmancollection.Utility.constants.RAW;
import static com.example.avinash.postmancollection.Utility.constants.SOUND_CORRECT;
import static com.example.avinash.postmancollection.Utility.constants.SOUND_WRONG;


/**
 * created by: "Avinash"
 * on: "13 - Apr - 2018", at: "15:17hrs"
 * for: "Lifeboard Edu Solutions Pvt. Ltd.", Bengaluru
 * email: avinash@lifeboard.in
 **/

public class Utility {
    static TextToSpeech tts;
    static MediaPlayer mediaPlayer = null;

    /** Play audio for write or wrong answer from the user **/
    public static void playAudio(Context context, boolean isCorrect) {
        int rawId;

        if (isCorrect) {
            rawId = context.getResources().getIdentifier(SOUND_CORRECT, RAW, context.getPackageName());
        } else {
            rawId = context.getResources().getIdentifier(SOUND_WRONG, RAW, context.getPackageName());
        }
        try {
            mediaPlayer = MediaPlayer.create(context, rawId);
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    /** Play audio for learn data and question data**/
    public static void playAudioFiles(Context context, String mediaUrl) {
        stopPlaying();
        if (mediaUrl != null) {
            try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(mediaUrl);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();

            }
    }

    /** Stop the player if user presses speak button twice or more and stop if next fragent loaded **/
    public static void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
