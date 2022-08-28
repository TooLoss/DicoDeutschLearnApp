package com.example.projectdicodeutsch.model;

public class WordModel {
    private int id, status;
    private String upWord, downWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpWord() {
        return upWord;
    }

    public void setUpWord(String upWord) {
        this.upWord = upWord;
    }

    public String getDownWord() {
        return downWord;
    }

    public void setDownWord(String downWord) {
        this.downWord = downWord;
    }
}
