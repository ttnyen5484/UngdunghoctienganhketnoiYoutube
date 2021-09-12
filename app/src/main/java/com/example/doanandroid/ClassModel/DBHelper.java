package com.example.doanandroid.ClassModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

public class DBHelper {
//
    Context context;
    VideoUntils utils;

    String dbName = "YoutubeAPIDB.db";

    public DBHelper(Context context) {
        this.context = context;
        utils = new VideoUntils(context);
    }

    private SQLiteDatabase openDB() {
        return context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }

    private void closeDB(SQLiteDatabase db) {
        db.close();
    }

    public void createTable() {
        SQLiteDatabase db = openDB();
        String sqlChuDe = "CREATE TABLE IF NOT EXISTS ChuDe (" +
                " ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " Name TEXT," +
                " Image TEXT," +
                " LinkUrl TEXT );";
        String SqlVideoYoutube = "CREATE TABLE IF NOT EXISTS VideoYoutube (" +
                " IDVY TEXT NOT NULL PRIMARY KEY," +
                " Ten TEXT," +
                " Thumnails TEXT," +
                " IDHis INTEGER," +
                " IDFavor INTEGER );";
        String SqlNote="CREATE TABLE IF NOT EXISTS GhiChu (" +
                " IDNotes INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " NoiDung TEXT );";
        String SqlQues="CREATE TABLE IF NOT EXISTS Question (" +
                " IDQues INTEGER NOT NULL PRIMARY KEY ," +
                " CauHoi TEXT );";
        String SqlAnswer="CREATE TABLE IF NOT EXISTS Answer (" +
                " IDAn INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " NoiDung TEXT," +
                " KetQua INTEGER," +
                " IDQues INTEGER );";
        String SqlPlayList="CREATE TABLE IF NOT EXISTS PlayList (" +
                " IDList INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " Title TEXT );";
        String SqlPlayListVideo="CREATE TABLE IF NOT EXISTS PlayListVideo (" +
                " IDList INTEGER ," +
                " IDVY TEXT );";
        db.execSQL(sqlChuDe);
        db.execSQL(SqlVideoYoutube);
        db.execSQL(SqlNote);
        db.execSQL(SqlQues);
        db.execSQL(SqlAnswer);
        db.execSQL(SqlPlayList);
        db.execSQL(SqlPlayListVideo);
        closeDB(db);
    }
    public void insertQuestion(){
        ArrayList<Question> arrAvt = utils.listQuestion();
        SQLiteDatabase db = openDB();
        for(Question fu : arrAvt) {
            ContentValues cv = new ContentValues();
            cv.put("IDQues", fu.getNumber());
            cv.put("CauHoi", fu.getContent());
            db.insert("Question", null, cv);
        }
        closeDB(db);
    }
    public void insertAnwer(){
        ArrayList<Answer> arrAvt = utils.ListAnswer();
        SQLiteDatabase db = openDB();
        int kq=0;
        Random rd=new Random();
        for(Answer fu : arrAvt) {
            if(fu.getCorrect())
            {
                kq=1;
            }
            else
            {
                kq=0;
            }
            ContentValues cv = new ContentValues();
            cv.put("NoiDung", fu.getContent());
            cv.put("KetQua", kq);
            cv.put("IDQues", fu.getIDques());
            db.insert("Answer", null, cv);
        }
        closeDB(db);
    }
    public ArrayList<Question> LoadQuestion()
    {
        SQLiteDatabase db = openDB();
        ArrayList<Question> arr = new ArrayList<>();
        String sql = "select * from Question";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    ArrayList<Answer> answers=LoadAnswer(id);
                    arr.add(new Question(id,name,answers));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public ArrayList<Answer> LoadAnswer(int idq)
    {
        SQLiteDatabase db = openDB();
        ArrayList<Answer> arr = new ArrayList<>();
        Cursor csr = db.rawQuery("select * from Answer where IDQues = ?", new String[]{String.valueOf(idq)});
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    boolean flag=false;
                    int id = csr.getInt(0);
                    String noidung = csr.getString(1);
                    if(csr.getInt(2)==1)
                    {
                        flag=true;
                    }
                    else
                    {
                        flag=false;
                    }
                    arr.add(new Answer(noidung,flag));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public void insertVideo(VideoYoutube vy){
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        cv.put("IDVY", vy.getVideoid());
        cv.put("Ten", vy.getTitle());
        cv.put("Thumnails", vy.getThumnails());
        cv.put("IDHis", vy.getIdhis());
        cv.put("IDFavor", vy.getIdfa());
        db.insert("VideoYoutube", null, cv);
        closeDB(db);
    }
    public void insertNote(String Text){
        SQLiteDatabase db = openDB();
        Random rd=new Random();
        ContentValues cv = new ContentValues();
        cv.put("IDNotes",rd.nextInt());
        cv.put("NoiDung", Text);
        db.insert("GhiChu", null, cv);
        closeDB(db);
    }
    public void UpdateVideoHis(String idvy,int idhis)
    {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        cv.put("IDHis", idhis);
        db.update("VideoYoutube",cv,"IDVY=?",new String[]{String.valueOf(idvy)});
        closeDB(db);
    }
    public void UpdateVideoFavo(String idvy,int idfo)
    {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        cv.put("IDFavor", idfo);
        db.update("VideoYoutube",cv,"IDVY=?",new String[]{String.valueOf(idvy)});
        closeDB(db);
    }
    public void insertChuDe(){
        ArrayList<AvatarVideo> arrAvt = utils.getDataChuDe();
        SQLiteDatabase db = openDB();
        Random random = new Random();
        for(AvatarVideo fu : arrAvt) {
            ContentValues cv = new ContentValues();
            cv.put("Name", fu.getName());
            cv.put("Image", fu.getImage());
            cv.put("LinkUrl", fu.getLinkUrl());
            db.insert("ChuDe", null, cv);
        }
        closeDB(db);
    }
    public ArrayList<VideoYoutube> LoadHistory()
    {
        SQLiteDatabase db = openDB();
        ArrayList<VideoYoutube> arr = new ArrayList<>();
        String sql = "select * from VideoYoutube where IDHis=1";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    String id = csr.getString(0);
                    String name = csr.getString(1);
                    String image = csr.getString(2);
                    int idhis= Integer.parseInt(csr.getString(3));
                    int idfo= Integer.parseInt(csr.getString(4));
                    arr.add(new VideoYoutube(name,image,id,idhis,idfo));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public ArrayList<VideoYoutube> LoadVideo(String idvy)
    {
        SQLiteDatabase db = openDB();
        ArrayList<VideoYoutube> arr = new ArrayList<>();
        String sql = "select * from VideoYoutube WHERE IDVY IS NOT '"+idvy+"'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    String id = csr.getString(0);
                    String name = csr.getString(1);
                    String image = csr.getString(2);
                    int idhis= Integer.parseInt(csr.getString(3));
                    int idfo= Integer.parseInt(csr.getString(4));
                    arr.add(new VideoYoutube(name,image,id,idhis,idfo));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public ArrayList<Notes> LoadNotes()
    {
        SQLiteDatabase db = openDB();
        ArrayList<Notes> arr = new ArrayList<>();
        String sql = "select * from GhiChu";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    arr.add(new Notes(id,name));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public void DeleteNote(int id)
    {
        SQLiteDatabase db = openDB();
        db.delete("GhiChu","IDNotes = ?",new String[]{String.valueOf(id)});
        closeDB(db);
    }
    public int KiemTraKhoaChinh(String id)
    {
        int kq=0;
        SQLiteDatabase db = openDB();
        Cursor csr = db.rawQuery("select * from VideoYoutube where IDVY = ?",new String[]{String.valueOf(id)});
        if(csr!=null)
        {
            kq=csr.getCount();
        }
        else
        {
            kq=0;
        }
        closeDB(db);
        return kq;
    }
    public int CheckFavorite(String id)
    {
        int kq=0;
        SQLiteDatabase db = openDB();
        Cursor csr = db.rawQuery("select * from VideoYoutube where IDVY = ? and IDFavor = 1",new String[]{String.valueOf(id)});
        if(csr!=null)
        {
            kq=csr.getCount();
        }
        else
        {
            kq=0;
        }
        closeDB(db);
        return kq;
    }
    public ArrayList<VideoYoutube> LoadFavorite()
    {
        SQLiteDatabase db = openDB();
        ArrayList<VideoYoutube> arr = new ArrayList<>();
        String sql = "select * from VideoYoutube where IDFavor=1";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    String id = csr.getString(0);
                    String name = csr.getString(1);
                    String image = csr.getString(2);
                    int idhis= Integer.parseInt(csr.getString(3));
                    int idfo= Integer.parseInt(csr.getString(4));
                    arr.add(new VideoYoutube(name,image,id,idhis,idfo));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public ArrayList<AvatarVideo> LoadChuDe()
    {
        SQLiteDatabase db = openDB();
        ArrayList<AvatarVideo> arr = new ArrayList<>();
        String sql = "select * from ChuDe";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String image = csr.getString(2);
                    String link= csr.getString(3);
                    arr.add(new AvatarVideo(id,name,image,link));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public String LoadGrammar()
    {
        SQLiteDatabase db = openDB();
        String url="";
        String sql = "select * from ChuDe where ID=9 ";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    url= csr.getString(3);
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return url;
    }
    public void insertPlayList(String title) {
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        db.insert("PlayList", null, values);
        db.close();
    }

    public void updatePlayList(int id,String title){
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        db.update("PlayList", values, "IDList="+id,null );
        db.close();
    }

    public void deletePlayList(int id){
        SQLiteDatabase db = openDB();
        db.delete("PlayList", "IDList = "+id, null);
        db.delete("PlayListVideo", "IDList = "+id, null);
        closeDB(db);
    }
    public ArrayList<PlayListVideo> getALLPlayList() {
        SQLiteDatabase db = openDB();
        ArrayList<PlayListVideo> arr = new ArrayList<>();
        String sql = "select * from PlayList";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String title = csr.getString(1);
                    arr.add(new PlayListVideo(id,title));

                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public ArrayList<VideoYoutube> LoadVideoOfPlayList(int IDList)
    {
        SQLiteDatabase db = openDB();
        ArrayList<VideoYoutube> arr = new ArrayList<>();
        String sql = "select  VideoYoutube.IDVY,Ten,Thumnails,IDHis,IDFavor" +
                " from VideoYoutube, PlayListVideo where PlayListVideo.IDList = '" + IDList+"'" +
                " AND VideoYoutube.IDVY = PlayListVideo.IDVY";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    String id = csr.getString(0);
                    String name = csr.getString(1);
                    String image = csr.getString(2);
                    int idhis= Integer.parseInt(csr.getString(3));
                    int idfo= Integer.parseInt(csr.getString(4));
                    arr.add(new VideoYoutube(name,image,id,idhis,idfo));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
    public void insertVideoPlayList(int idPlayList, String idVideo) {
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("IDList", idPlayList);
        values.put("IDVY", idVideo);
        db.insert("PlayListVideo", null, values);
        db.close();
    }

    public void deleteSongInPlayList(String idVideo,int idPlayList) {
        SQLiteDatabase db = openDB();
        db.delete("PlayListVideo", "IDList = ? AND IDVY  = ?",
                new String[] {String.valueOf(idPlayList),String.valueOf(idVideo)});
        db.close();
    }
    public int KiemTraKCPlayList(int idplay,String idvideo)
    {
        int kq=0;
        SQLiteDatabase db = openDB();
        Cursor csr = db.rawQuery("select * from PlayListVideo where IDList = ? and IDVY  = ?",new String[]{String.valueOf(idplay),idvideo});
        if(csr!=null)
        {
            kq=csr.getCount();
        }
        else
        {
            kq=0;
        }
        closeDB(db);
        return kq;
    }
}
