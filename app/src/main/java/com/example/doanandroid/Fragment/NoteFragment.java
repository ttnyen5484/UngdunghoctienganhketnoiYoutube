package com.example.doanandroid.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.NoteAdapter;
import com.example.doanandroid.ClassModel.Notes;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    private TextToSpeech textToSpeech;
    VideoUntils videoUntils;
    ImageButton imageButton;
    ListView listView;
    public static int flag=0;
    NoteAdapter adapter;
    ArrayList<Notes> arrayList;
    EditText editText;
    DBHelper dbHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_note);
        editText = view.findViewById(R.id.txtnhap);
        arrayList = dbHelper.LoadNotes();
        adapter=new NoteAdapter(getContext(),arrayList);
        listView.setAdapter(adapter);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i ==KeyEvent.KEYCODE_ENTER)) {
                    String note = editText.getText().toString();
                    dbHelper.insertNote(note);
                    arrayList=dbHelper.LoadNotes();
                    adapter=new NoteAdapter(getContext(),arrayList);
                    listView.setAdapter(adapter);
                    editText.getText().clear();
                    Toast.makeText(getContext(), editText.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        textToSpeech = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if(result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("Thông báo", "Ngôn ngữ không được hỗ trợ");
                    }
                    else{
                        listView.setEnabled(true);
                    }
                }
                else {
                    Log.e("Thông báo", "Initialization failed");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = arrayList.get(i).getNoiDung();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int which_item = i;

                new AlertDialog.Builder(NoteFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa mục này?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.DeleteNote(arrayList.get(which_item).getId());
                                arrayList.remove(which_item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu,menu);
        menu.findItem(R.id.adddsp).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.xoa: {
                new AlertDialog.Builder(NoteFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa tất cả?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(Notes notes:arrayList)
                                {
                                    dbHelper.DeleteNote(notes.getId());
                                }
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}