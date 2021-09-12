package com.example.doanandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.PlaylistAdapter;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.ClassModel.VideoYouTubeAdapter;
import com.example.doanandroid.ClassModel.VideoYoutube;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Locale;

public class PlayVideoytActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    YouTubePlayerView youTubePlayerView;
    private TextToSpeech textToSpeech;
    String idvy="";
    int RequestVideo=12;
    TextView textView;
    ImageButton imageButton,imageButtonplay;
    ArrayList<VideoYoutube> arrayList;
    String name="";
    String thumnails="";
    int idhis=0,idfavo=0, idPL=0;;
    VideoUntils vu;
    Button btnluu;
    ListView listVideo,listPlay;
    EditText txtnhap,txtTitle;
    VideoYouTubeAdapter adapter;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    Button btnTao,btnCancel;
    PlaylistAdapter adapterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_videoyt);
        youTubePlayerView=findViewById(R.id.myyoutubetb);
        textView=findViewById(R.id.txtnamevy);
        imageButton=findViewById(R.id.imageButtonyt);
        imageButtonplay=findViewById(R.id.imgPlayyt);
        listVideo=findViewById(R.id.listvideoyt);
        dbHelper=new DBHelper(getApplicationContext());
        txtnhap=findViewById(R.id.txtnhapyt);
        btnluu=findViewById(R.id.btnluuyt);
        Intent intent=getIntent();
        idPL=intent.getIntExtra("IDlistPL",0);
        idvy=intent.getStringExtra("idvideoyoutube");
        name=intent.getStringExtra("namevideo");
        textView.setText(name);
        thumnails=intent.getStringExtra("thumnails");
        idhis=intent.getIntExtra("idHis",0);
        idfavo=intent.getIntExtra("idFavo",0);
        if (getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
        }
        youTubePlayerView.initialize(MainActivity.Key_API,this);
        arrayList=dbHelper.LoadVideoOfPlayList(idPL);
        adapter=new VideoYouTubeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        listVideo.setAdapter(adapter);
        if(idfavo == 0){
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }else{
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayList.get(position).getVideoid().equals(idvy))
                {
                    Toast.makeText(PlayVideoytActivity.this, "Video đang được phát", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), PlayVideoytActivity.class);
                    intent.putExtra("IDlistPL", idPL);
                    intent.putExtra("idvideoyoutube", arrayList.get(position).getVideoid());
                    intent.putExtra("namevideo", arrayList.get(position).getTitle());
                    intent.putExtra("thumnails", arrayList.get(position).getThumnails());
                    intent.putExtra("idHis", arrayList.get(position).getIdhis());
                    if (dbHelper.CheckFavorite(arrayList.get(position).getVideoid()) >= 0) {
                        intent.putExtra("idFavo", 1);
                    } else {
                        intent.putExtra("idFavo", 0);
                    }
                    if (dbHelper.KiemTraKhoaChinh(arrayList.get(position).getVideoid()) > 0) {
                        dbHelper.UpdateVideoHis(arrayList.get(position).getVideoid(), 1);
                    } else {
                        dbHelper.insertVideo(new VideoYoutube(arrayList.get(position).getTitle(), arrayList.get(position).getThumnails(), arrayList.get(position).getVideoid(), 1, 0));
                    }
                    startActivity(intent);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.KiemTraKhoaChinh(idvy)>0) {
                    if (idfavo == 0) {
                        idfavo = 1;
                        imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {
                        idfavo = 0;
                        imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }

                    dbHelper.UpdateVideoFavo(idvy, idfavo);
                }else
                {
                    dbHelper.insertVideo(new VideoYoutube(name,thumnails,idvy,1,1));
                }

            }
        });
        txtnhap.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i ==KeyEvent.KEYCODE_ENTER)) {
                    String note = txtnhap.getText().toString();
                    dbHelper.insertNote(note);
                    txtnhap.getText().clear();
                    Toast.makeText(PlayVideoytActivity.this, "Đã Thêm", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = txtnhap.getText().toString();
                dbHelper.insertNote(note);
                txtnhap.getText().clear();
            }
        });
        imageButtonplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PlayVideoytActivity.this);
                dialog.setContentView(R.layout.dialog_playlist);
                btnTao = dialog.findViewById(R.id.btnTaoPL);
                btnCancel = dialog.findViewById(R.id.btnCancel);
                txtTitle=dialog.findViewById(R.id.etPlayList);
                listPlay=dialog.findViewById(R.id.listPlaytitle);
                adapterList=new PlaylistAdapter(getApplicationContext(),dbHelper.getALLPlayList());
                listPlay.setAdapter(adapterList);
                listPlay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(dbHelper.KiemTraKCPlayList(dbHelper.getALLPlayList().get(position).getIDPList(), intent.getStringExtra("idvideoyoutube"))>0)
                        {
                            Toast.makeText(PlayVideoytActivity.this, "Đã có trong danh sách phát", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dbHelper.insertVideoPlayList(dbHelper.getALLPlayList().get(position).getIDPList(), intent.getStringExtra("idvideoyoutube"));
                            Toast.makeText(PlayVideoytActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnTao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = txtTitle.getText().toString();
                        if (title.length() == 0 || title.equals("") || title == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Bạn chưa nhập tên cho PlayList..!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dbHelper.insertPlayList(title);
                        adapterList=new PlaylistAdapter(getApplicationContext(),dbHelper.getALLPlayList());
                        listPlay.setAdapter(adapterList);
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(idvy);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError())
        {
            youTubeInitializationResult.getErrorDialog(PlayVideoytActivity.this,RequestVideo);
        }
        else
        {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RequestVideo)
        {
            youTubePlayerView.initialize(MainActivity.Key_API,PlayVideoytActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
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