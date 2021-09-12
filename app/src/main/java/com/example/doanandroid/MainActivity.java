package com.example.doanandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.Photo;
import com.example.doanandroid.ClassModel.PhotoAdapter;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.ClassModel.VideoYouTubeAdapter;
import com.example.doanandroid.ClassModel.VideoYoutube;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer timer;
    TextView txtchude;
    String linkUrl="";
////    String key_API="AIzaSyCg4VrxcY-oQqe8Y4MnxzdKTRL329CvvCQ";
    String IDplayList="PL3BXo6gVOnutshXwQoDJatT-pDEF2gLoC";
   // String url1="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnutshXwQoDJatT-pDEF2gLoC&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
//    String url2="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuv-5WzbhNYgh0nEoBS4-2ZN&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";

    //    Button btn;
//    YouTubePlayerView youTubePlayerView;
//    YouTubePlayer.OnInitializedListener onInitializedListener;
    public  static String Key_API="AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM";
    ListView listView;
    ArrayList<VideoYoutube> arrayList;
    VideoYouTubeAdapter adapter;
    BottomNavigationView navView;
    VideoUntils vu;
    public static int flag=0;
    Context context;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        txtchude = findViewById(R.id.txtnamechude);
        listView = findViewById(R.id.listview);
        dbHelper=new DBHelper(getApplicationContext());
        arrayList=new ArrayList<>();
        adapter=new VideoYouTubeAdapter(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        vu=new VideoUntils(getApplicationContext());
        Intent intent=getIntent();
        String s = intent.getStringExtra("key");
        txtchude.setText(s);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        //getJSONYoutube(url1);
        //getJSONYoutube(intent.getStringExtra("url"));
        linkUrl=intent.getStringExtra("url");
        vu.getJSONYoutube(linkUrl,adapter,arrayList,MainActivity.this);

        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(this, mListPhoto);
        photoAdapter  = new PhotoAdapter(this, getListPhoto());

        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlide();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PlayvideoActivity.class);
                intent.putExtra("url",linkUrl);
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

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(MainActivity.this, Theme1Activity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }
        public void getJSONYoutube(String URL)
        {
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try
                            {
                                JSONArray jsonArray=response.getJSONArray("items");
                                String title="";
                                String url="";
                                String ID="";
                                String ChannelTitle="";
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonItem=jsonArray.getJSONObject(i);
                                    JSONObject jsonSnip=jsonItem.getJSONObject("snippet");
                                    title=jsonSnip.getString("title");
                                    ChannelTitle=jsonSnip.getString("videoOwnerChannelTitle");
                                    JSONObject jsonThum=jsonSnip.getJSONObject("thumbnails");
                                    JSONObject jsonMedi=jsonThum.getJSONObject("medium");
                                    url=jsonMedi.getString("url");
                                    JSONObject JsonID=jsonSnip.getJSONObject("resourceId");
                                    ID=JsonID.getString("videoId");
                                    arrayList.add(new VideoYoutube(title,url,ID));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                           // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }

    private List<Photo> getListPhoto()
    {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.image1));
        list.add(new Photo(R.drawable.image2));
        list.add(new Photo(R.drawable.image3));
        list.add(new Photo(R.drawable.image4));
        list.add(new Photo(R.drawable.image5));

        return list;
    }
    private void autoSlide()
    {
        if(mListPhoto == null || mListPhoto.isEmpty() || viewPager == null ){
            return;
        }
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if(currentItem < totalItem)
                        {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }
                        else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
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