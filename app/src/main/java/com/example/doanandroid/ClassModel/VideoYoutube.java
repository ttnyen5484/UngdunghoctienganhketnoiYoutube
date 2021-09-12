package com.example.doanandroid.ClassModel;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class VideoYoutube {
    String title;
    String thumnails;
    String videoid;


    public VideoYoutube(String title, String thumnails, String videoid, int idhis, int idfa) {
        this.title = title;
        this.thumnails = thumnails;
        this.videoid = videoid;
        this.idhis = idhis;
        this.idfa = idfa;
    }

    public int getIdhis() {
        return idhis;
    }

    public void setIdhis(int idhis) {
        this.idhis = idhis;
    }

    public int getIdfa() {
        return idfa;
    }

    public void setIdfa(int idfa) {
        this.idfa = idfa;
    }

    int idhis;
    int idfa;

    public String getChannelTitle() {
        return ChannelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        ChannelTitle = channelTitle;
    }

    String ChannelTitle;

    public VideoYoutube() {
    }

    public VideoYoutube(String title, String thumnails, String videoid, String channelTitle) {
        this.title = title;
        this.thumnails = thumnails;
        this.videoid = videoid;
        ChannelTitle = channelTitle;
    }

    public VideoYoutube(String title, String thumnails, String videoid) {
        this.title = title;
        this.thumnails = thumnails;
        this.videoid = videoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumnails() {
        return thumnails;
    }

    public void setThumnails(String thumnails) {
        this.thumnails = thumnails;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }


    public static Bitmap convertStringToBitmapFromAccess(Context context, String
            filename){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
