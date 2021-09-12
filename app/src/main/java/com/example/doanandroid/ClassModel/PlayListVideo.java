package com.example.doanandroid.ClassModel;

public class PlayListVideo {
    int IDPList;
    String Title;
    String IdVY;

    public PlayListVideo(int IDPList, String title) {
        this.IDPList = IDPList;
        Title = title;
    }

    public PlayListVideo(int IDPList, String title, String idVY) {
        this.IDPList = IDPList;
        Title = title;
        IdVY = idVY;
    }

    public int getIDPList() {
        return IDPList;
    }

    public void setIDPList(int IDPList) {
        this.IDPList = IDPList;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIdVY() {
        return IdVY;
    }

    public void setIdVY(String idVY) {
        IdVY = idVY;
    }
}
