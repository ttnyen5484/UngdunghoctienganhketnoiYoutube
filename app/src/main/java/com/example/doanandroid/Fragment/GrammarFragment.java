package com.example.doanandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doanandroid.ClassModel.AvatarVideo;
import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.ClassModel.VideoYouTubeAdapter;
import com.example.doanandroid.ClassModel.VideoYoutube;
import com.example.doanandroid.PlayvideoActivity;
import com.example.doanandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrammarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrammarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String url9="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusXtDXIcWFNrZ5R2BebHCwK&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50";
    ListView listView;
    ArrayList<VideoYoutube> arrayList;
    VideoYouTubeAdapter adapter;
    BottomNavigationView navView;
    DBHelper dbHelper;
    List<AvatarVideo> list;
    VideoUntils vu;
    public GrammarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrammarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrammarFragment newInstance(String param1, String param2) {
        GrammarFragment fragment = new GrammarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return inflater.inflate(R.layout.fragment_grammar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listgram);
        arrayList=new ArrayList<>();
        adapter=new VideoYouTubeAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        vu=new VideoUntils(getContext());
        vu.getJSONYoutube(dbHelper.LoadGrammar(),adapter,arrayList,getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), PlayvideoActivity.class);
                intent.putExtra("url",dbHelper.LoadGrammar());
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
}