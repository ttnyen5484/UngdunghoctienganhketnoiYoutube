package com.example.doanandroid.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.PlayListVideo;
import com.example.doanandroid.ClassModel.PlaylistAdapter;
import com.example.doanandroid.R;
import com.example.doanandroid.VideoOfPlayListActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    ArrayList<PlayListVideo> arrayList;
    PlaylistAdapter adapter;
    ImageButton imgXoa,imgaddpl;
    DBHelper dbHelper;
    EditText txtNamePL;
    Button btnTao,btnCancle;
    TextView textView;
    private String namePlayList = "";

    public PlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
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
        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView=view.findViewById(R.id.listPL);
        arrayList=dbHelper.getALLPlayList();
        adapter=new PlaylistAdapter(getContext(),arrayList);
        listView.setAdapter(adapter);
        textView = view.findViewById(R.id.txttenthe);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), VideoOfPlayListActivity.class);
                intent.putExtra("IDlist",dbHelper.getALLPlayList().get(position).getIDPList());
                intent.putExtra("key", dbHelper.getALLPlayList().get(position).getTitle());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(PlayListFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa mục này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deletePlayList(dbHelper.getALLPlayList().get(position).getIDPList());
                                arrayList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.xoa: {
                new AlertDialog.Builder(PlayListFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Bạn chắc không?")
                        .setMessage("Bạn muốn xóa tất cả?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(PlayListVideo pl:arrayList)
                                {
                                    dbHelper.deletePlayList(pl.getIDPList());
                                }
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
                break;
            }

            case R.id.adddsp:{
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_titleplaylist);
                btnTao = dialog.findViewById(R.id.btnTaoPl);
                btnCancle = dialog.findViewById(R.id.btnCCPL);
                txtNamePL=dialog.findViewById(R.id.txttitle);
                btnTao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = txtNamePL.getText().toString();
                        if (title.length() == 0 || title.equals("") || title == null) {
                            Toast.makeText(getContext(),
                                    "Bạn chưa nhập tên cho PlayList..!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dbHelper.insertPlayList(title);
                        arrayList=dbHelper.getALLPlayList();
                        adapter=new PlaylistAdapter(getContext(),arrayList);
                        listView.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}