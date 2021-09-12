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

public class AvartarAdapter extends ArrayAdapter<AvatarVideo> {


    public AvartarAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<AvatarVideo> objects) {
        super(context, 0, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_themes, parent, false);
        AvatarVideo avatarVideo=getItem(position);
        ImageView imageView=convertView.findViewById(R.id.imageView);
        TextView textView=convertView.findViewById(R.id.txttheme);
        //TextView textView1=convertView.findViewById(R.id.ttxlink);
        textView.setText(avatarVideo.getName());
        imageView.setImageBitmap(AvatarVideo.convertStringToBitmapFromAccess(getContext(),avatarVideo.getImage()));
        //textView1.setText(avatarVideo.getUrl());
        return  convertView;
    }
}
