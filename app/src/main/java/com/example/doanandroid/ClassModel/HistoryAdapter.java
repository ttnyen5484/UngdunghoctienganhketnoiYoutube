package com.example.doanandroid.ClassModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.doanandroid.PlayvideoActivity;
import com.example.doanandroid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VideoHolder>{

    Context context;
    int resourceId;
    private List<VideoYoutube> mListVideo;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    ImageView imageView;
    TextView textView;
    DBHelper dbHelper;
    String linkUrl="";

    public HistoryAdapter(Context context, int resourceId, List<VideoYoutube> mListVideo) {
        this.context = context;
        this.resourceId = resourceId;
        this.mListVideo = mListVideo;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube, parent, false);
        imageView = view.findViewById(R.id.thumnails);
        textView = view.findViewById(R.id.titles);
        dbHelper=new DBHelper(context);
        return new HistoryAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.VideoHolder holder, int position) {
        VideoYoutube videoYoutube = mListVideo.get(position);
        textView.setText(videoYoutube.getTitle());
        Picasso.with(context).load(videoYoutube.getThumnails()).into(imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PlayvideoActivity.class);
                intent.putExtra("url","");
                intent.putExtra("idvideoyoutube", mListVideo.get(position).getVideoid());
                intent.putExtra("namevideo",mListVideo.get(position).getTitle());
                intent.putExtra("thumnails",mListVideo.get(position).getThumnails());
                intent.putExtra("idHis",mListVideo.get(position).getIdhis());
                intent.putExtra("idFavo",mListVideo.get(position).getIdfa());
                view.getContext().startActivity(intent);
            }
        });
        if(videoYoutube == null)
            return;
        viewBinderHelper.bind(holder.swipeRevealLayout, videoYoutube.getVideoid());
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc chứ?")
                        .setMessage("Bạn muốn xóa mục này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.UpdateVideoHis(mListVideo.get(position).getVideoid(),0);
                                mListVideo.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
    }
    @Override
    public int getItemCount() {
        if(mListVideo != null){
            return mListVideo.size();
        }
        return 0;
    }
    public class VideoHolder extends RecyclerView.ViewHolder{
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDelete;
        private ImageView imageView;
        private TextView textView;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipeReveralLayout);
            layoutDelete = itemView.findViewById(R.id.layout_delete);
            imageView = itemView.findViewById(R.id.thumnails);
            textView = itemView.findViewById(R.id.titles);
        }
    }
    public int getJSONYoutube(String URL,String txtsearch)
    {
        ArrayList<VideoYoutube> arrayList=new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
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
                                if(jsonSnip.getString("title").equals(txtsearch)) {
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
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
        return arrayList.size();
    }
}
