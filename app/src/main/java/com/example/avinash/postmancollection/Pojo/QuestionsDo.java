package com.example.avinash.postmancollection.Pojo;

import java.io.Serializable;

/**
 * Created by avinash on 13/4/18.
 */

public class QuestionsDo implements Serializable {

    private String  type;
    private String  conceptName;
    private String  pronunciation;
    private String  targetScript;
    private String  audio_url;

    public QuestionsDo()
    {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getTargetScript() {
        return targetScript;
    }

    public void setTargetScript(String targetScript) {
        this.targetScript = targetScript;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }
}
