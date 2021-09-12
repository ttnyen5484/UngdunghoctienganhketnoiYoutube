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

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<PlayListVideo> {
    public PlaylistAdapter(@NonNull Context context, @NonNull List<PlayListVideo> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_playlist, parent, false);
        PlayListVideo playList = getItem(position);
        TextView tvTitle = convertView.findViewById(R.id.txttenthe);
        ImageView imageView = convertView.findViewById(R.id.imgIPL);


        tvTitle.setText(playList.getTitle());
        imageView.setImageResource(R.drawable.ytplaylist);
        return convertView;
    }
}
