package com.example.doanandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.doanandroid.ClassModel.AvatarVideo;
import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.ClassModel.VideoYouTubeAdapter;
import com.example.doanandroid.ClassModel.VideoYoutube;
import com.example.doanandroid.PlayvideoActivity;
import com.example.doanandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<VideoYoutube> arrayList;
    VideoYouTubeAdapter adapter;
    ListView listView;
    VideoUntils vu;
    EditText editsearch;
    ImageButton btnsearch;
    String TenVD="";
    DBHelper dbHelper;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.result_list);
        editsearch=view.findViewById(R.id.search_field);
        btnsearch=view.findViewById(R.id.search_btn);
        arrayList = new ArrayList<>();
        adapter = new VideoYouTubeAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        vu = new VideoUntils(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlayvideoActivity.class);
                //Toast.makeText(MainActivity.this, ""+arrayList.get(position).getVideoid(), Toast.LENGTH_SHORT).show();
                intent.putExtra("url","");
                intent.putExtra("idvideoyoutube", arrayList.get(position).getVideoid());
                intent.putExtra("namevideo", arrayList.get(position).getTitle());
                intent.putExtra("thumnails", arrayList.get(position).getThumnails());
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
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenVD=editsearch.getText().toString();
                arrayList.clear();
                adapter.notifyDataSetChanged();
                for(AvatarVideo av:dbHelper.LoadChuDe()) {
                    getJSONYoutube(av.getLinkUrl(), TenVD);
                }

            }
        });

    }
    public void getJSONYoutube(String URL,String txtsearch)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
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
                                if(jsonSnip.getString("title").contains(txtsearch)) {
                                    title = jsonSnip.getString("title");
                                    ChannelTitle = jsonSnip.getString("videoOwnerChannelTitle");
                                    JSONObject jsonThum = jsonSnip.getJSONObject("thumbnails");
                                    JSONObject jsonMedi = jsonThum.getJSONObject("medium");
                                    url = jsonMedi.getString("url");
                                    JSONObject JsonID = jsonSnip.getJSONObject("resourceId");
                                    ID = JsonID.getString("videoId");
                                    arrayList.add(new VideoYoutube(title, url, ID));
                                }
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
                        Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}