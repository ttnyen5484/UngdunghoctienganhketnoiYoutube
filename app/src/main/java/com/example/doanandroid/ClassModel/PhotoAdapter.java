package com.example.doanandroid.ClassModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.doanandroid.R;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {
    private Context mContext;
    private List<Photo> mlistPhoto;

    public PhotoAdapter(Context mContext, List<Photo> mlistPhoto) {
        this.mContext = mContext;
        this.mlistPhoto = mlistPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        Photo photo = mlistPhoto.get(position);
        if(photo != null)
        {
            Glide.with(mContext).load(photo.getResourceId()).into(imgPhoto);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mlistPhoto != null)
            return mlistPhoto.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
