package com.example.doanandroid.ClassModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doanandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class VideoYouTubeAdapter extends ArrayAdapter<VideoYoutube> {

    public VideoYouTubeAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<VideoYoutube> objects) {
        super(context, 0, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);
        VideoYoutube videoYoutube=getItem(position);
        ImageView imageView=convertView.findViewById(R.id.thumnails);
        TextView textView=convertView.findViewById(R.id.titles);

        textView.setText(videoYoutube.getTitle());
        Picasso.with(getContext()).load(videoYoutube.getThumnails()).into(imageView);
        return convertView;
    }

    public void filter(String text)
    {
        text=text.toLowerCase(Locale.getDefault());

    }
}
