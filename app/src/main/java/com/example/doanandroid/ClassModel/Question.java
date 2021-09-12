package com.example.doanandroid.ClassModel;

import java.util.List;

public class Question {
    private int number;
    private String content;
    private List<Answer> answerList;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public Question(int number, String content) {
        this.number = number;
        this.content = content;
    }

    public Question(int number, String content, List<Answer> answerList) {
        this.number = number;
        this.content = content;
        this.answerList = answerList;
    }
}
