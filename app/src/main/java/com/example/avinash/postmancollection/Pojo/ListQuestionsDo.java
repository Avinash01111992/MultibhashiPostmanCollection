package com.example.avinash.postmancollection.Pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by avinash on 13/4/18.
 */

public class ListQuestionsDo implements Serializable {

     private List<QuestionsDo>  lesson_data;

    public List<QuestionsDo> getLesson_data() {
        return lesson_data;
    }

    public void setLesson_data(List<QuestionsDo> lesson_data) {
        this.lesson_data = lesson_data;
    }

    public ListQuestionsDo()
    {

    }
}
