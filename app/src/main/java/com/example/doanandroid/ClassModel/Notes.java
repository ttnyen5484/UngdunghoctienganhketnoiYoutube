package com.example.doanandroid.ClassModel;

public class Notes {
    int id;
    String NoiDung;

    public Notes(int id, String noiDung) {
        this.id = id;
        NoiDung = noiDung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }
}
