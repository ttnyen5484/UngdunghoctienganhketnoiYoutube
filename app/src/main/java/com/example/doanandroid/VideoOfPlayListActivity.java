package com.example.doanandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.VideoYouTubeAdapter;
import com.example.doanandroid.ClassModel.VideoYoutube;

import java.util.ArrayList;
import java.util.Locale;

public class VideoOfPlayListActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ListView listView;
    ArrayList<VideoYoutube> arrayList;
    VideoYouTubeAdapter adapter;
    Button btnXoa;
    TextView textView;
    int idPL=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_of_play_list);
        dbHelper=new DBHelper(getApplicationContext());
        Intent intent=getIntent();
        idPL=intent.getIntExtra("IDlist",0);
        String s = intent.getStringExtra("key");;
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(s);
        }
        listView=findViewById(R.id.listVideoPL);
        arrayList=dbHelper.LoadVideoOfPlayList(idPL);
        adapter=new VideoYouTubeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(VideoOfPlayListActivity.this,PlayVideoytActivity.class);
                intent.putExtra("IDlistPL",idPL);
                intent.putExtra("idvideoyoutube",arrayList.get(position).getVideoid());
                intent.putExtra("namevideo",arrayList.get(position).getTitle());
                intent.putExtra("thumnails",arrayList.get(position).getThumnails());
                intent.putExtra("idHis", arrayList.get(position).getIdhis());
                if(dbHelper.CheckFavorite(arrayList.get(position).getVideoid())>0) {
                    intent.putExtra("idFavo", 1);
                }
                else
                {
                    intent.putExtra("idFavo", 0);
                }
                if(dbHelper.KiemTraKhoaChinh(arrayList.get(position).getVideoid())>0)
                {
                    dbHelper.UpdateVideoHis(arrayList.get(position).getVideoid(),1);
                }
                else {
                    dbHelper.insertVideo(new VideoYoutube(arrayList.get(position).getTitle(), arrayList.get(position).getThumnails(), arrayList.get(position).getVideoid(), 1, 0));
                }
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;

                new AlertDialog.Builder(VideoOfPlayListActivity.this).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa mục này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteSongInPlayList(arrayList.get(position).getVideoid(),idPL);
                                arrayList.remove(which_item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.adddsp).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.xoa: {
                new AlertDialog.Builder(VideoOfPlayListActivity.this).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa mục này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(VideoYoutube vy:arrayList)
                                {
                                    dbHelper.deleteSongInPlayList(vy.getVideoid(),idPL);
                                }
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
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