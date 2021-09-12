package com.example.doanandroid.ClassModel;

public class Answer {
    int id;
    private String content;
    private Boolean isCorrect;
    private int IDques;

    public Answer(String content, Boolean isCorrect, int IDques) {
        this.content = content;
        this.isCorrect = isCorrect;
        this.IDques = IDques;
    }

    public Answer(String content, Boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIDques() {
        return IDques;
    }

    public void setIDques(int IDques) {
        this.IDques = IDques;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
