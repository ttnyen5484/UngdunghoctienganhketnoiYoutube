package com.example.doanandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.ClassModel.DBHelper;

import java.util.Locale;

public class WellcomeActivity extends AppCompatActivity {
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wellcome);
        dbHelper = new DBHelper(WellcomeActivity.this);
        //Khởi tạo bảng
        dbHelper.createTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dbHelper.LoadChuDe().size() > 0 && dbHelper.LoadQuestion().size()>0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(WellcomeActivity.this, Theme1Activity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }else {
            dbHelper.insertChuDe();
            dbHelper.insertAnwer();
            dbHelper.insertQuestion();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(WellcomeActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 5000);

        }
    }
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Cài đặt", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Cài đặt", Activity.MODE_PRIVATE);
        String lang = preferences.getString("My_Lang", "");
        setLocale(lang);
    }
}