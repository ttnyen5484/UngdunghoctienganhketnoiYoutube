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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.doanandroid.PlayvideoActivity;
import com.example.doanandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.VideoHolder>{
    Context context;
    int resourceId;
    private List<VideoYoutube> mListVideo;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    VideoUntils videoUntils;
    ImageView imageView;
    TextView textView;
    DBHelper dbHelper;
    public static int flag = 0;

    public FavoriteAdapter(Context context, int resourceId, List<VideoYoutube> mListVideo) {
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
        return new FavoriteAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
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
                if(dbHelper.KiemTraKhoaChinh(mListVideo.get(position).getVideoid())>0)
                {
                    dbHelper.UpdateVideoHis(mListVideo.get(position).getVideoid(),1);
                }
                else {
                    dbHelper.insertVideo(new VideoYoutube(mListVideo.get(position).getTitle(), mListVideo.get(position).getThumnails(), mListVideo.get(position).getVideoid(), 1, 0));
                }
                view.getContext().startActivity(intent);
            }
        });
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
                                dbHelper.UpdateVideoFavo(mListVideo.get(position).getVideoid(),0);
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
    public class VideoHolder extends RecyclerView.ViewHolder {
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

}
