package com.example.doanandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.doanandroid.ClassModel.AvartarAdapter;
import com.example.doanandroid.ClassModel.AvatarVideo;
import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.Photo;
import com.example.doanandroid.ClassModel.PhotoAdapter;
import com.example.doanandroid.MainActivity;
import com.example.doanandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer timer;
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GridView gridView;
    List<AvatarVideo> arrayList;
    AvartarAdapter adapter;
    DBHelper dbHelper;
    public static String url1="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnutshXwQoDJatT-pDEF2gLoC&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url2="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuv-5WzbhNYgh0nEoBS4-2ZN&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url3="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusR4HdZfSt-qURh9M5z9P2K&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url4="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuuei2_pEKC9R6wxxj2MpTqB&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url5="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnutcQ_KvekmAQqMc-oYcTRfB&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url6="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuvzEWVSNm0VD_UZ1w18uwyk&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url7="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusFMUYw1WZJnI6QgIFMm-CF&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public static String url8="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuupsjNCeHWVH3wzqf-seSHm&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private long count;
    private Toast toast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(count + 2000 >  System.currentTimeMillis())
                {
                    this.handleOnBackPressed();
                }
                else
                {
                    toast = Toast.makeText(getContext(),"Ấn 1 lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT);
                    toast.show();
                }
                count = System.currentTimeMillis();
            }
        } ;
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbHelper=new DBHelper(getContext());
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.gridview);
        arrayList = dbHelper.LoadChuDe().subList(0,8);
        adapter=new AvartarAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        gridView.setAdapter(adapter);

        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(this.getContext(), mListPhoto);
        photoAdapter  = new PhotoAdapter(this.getContext(), getListPhoto());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlide();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), MainActivity.class);
                intent.putExtra("url",arrayList.get(position).getLinkUrl());
                intent.putExtra("key", arrayList.get(position).getName());
                startActivity(intent);
            }
        });
    }
    public String display(int pos)
    {
        String url="";
        switch (pos)
        {
            case 0:
                url=url1;
                break;
            case 1:
                url=url2;
                break;
            case 2:
                url=url3;
                break;
            case 3:
                url=url4;
                break;
            case 4:
                url=url5;
                break;
            case 5:
                url=url6;
                break;
            case 6:
                url=url7;
                break;
            case 7:
                url=url8;
                break;
        }
        return url;
    }

//    public ArrayList<AvatarVideo> getData()
//    {
//        ArrayList<AvatarVideo> tmp=new ArrayList<>();
//        tmp.add(new AvatarVideo(getString(R.string.nametopic1),R.drawable.avtfood));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic2),R.drawable.avtsports));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic3),R.drawable.avttravel));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic4),R.drawable.avtfamily));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic5),R.drawable.avtcolor));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic6),R.drawable.avtdaily));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic7),R.drawable.avtclothes));
//        tmp.add(new AvatarVideo(getString(R.string.nametopic8),R.drawable.avttrans));
//        return  tmp;
//    }

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

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
    }
}