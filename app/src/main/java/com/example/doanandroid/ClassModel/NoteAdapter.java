package com.example.doanandroid.ClassModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doanandroid.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Notes> {
    public NoteAdapter (@NonNull  Context context, @NonNull  List<Notes> objects){
            super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        Notes notes=getItem(position);
        TextView textView=convertView.findViewById(R.id.textItem);
        textView.setText(notes.getNoiDung());
        return convertView;
    }
}
