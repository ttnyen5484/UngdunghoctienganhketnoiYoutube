package com.example.doanandroid.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.doanandroid.ClassModel.Answer;
import com.example.doanandroid.ClassModel.DBHelper;
import com.example.doanandroid.ClassModel.Question;
import com.example.doanandroid.ClassModel.VideoUntils;
import com.example.doanandroid.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment implements View.OnClickListener {
    TextView tvQuestion;
    TextView tvContentQues;
    TextView tvAnwser1,tvAnwser2,tvAnwser3,tvAnwser4;
    TextView txtTotal;
    int Scores = 0;
    VideoUntils videoUntils;
    private List<Question> questionList;
    private Question mquestion;
    private int currentQues=0;
    DBHelper dbHelper;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
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
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);
        return v;
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitUI(view);
        questionList = dbHelper.LoadQuestion();
        if (questionList.isEmpty())
            return;
        SetDataQuestion(questionList.get(currentQues));
    }

    private void SetDataQuestion(Question question) {
        if(question == null)
            return;
        mquestion = question;
        tvAnwser1.setBackgroundResource(R.drawable.bg_blue_conner_30);
        tvAnwser2.setBackgroundResource(R.drawable.bg_blue_conner_30);
        tvAnwser3.setBackgroundResource(R.drawable.bg_blue_conner_30);
        tvAnwser4.setBackgroundResource(R.drawable.bg_blue_conner_30);
        String title="Question "+ question.getNumber();
        String diem="Score: "+Scores;
        txtTotal.setText(diem);
        tvQuestion.setText(title);
        tvContentQues.setText(question.getContent());
        tvAnwser1.setText(question.getAnswerList().get(0).getContent());
        tvAnwser2.setText(question.getAnswerList().get(1).getContent());
        tvAnwser3.setText(question.getAnswerList().get(2).getContent());
        tvAnwser4.setText(question.getAnswerList().get(3).getContent());

        tvAnwser1.setOnClickListener(this::onClick);
        tvAnwser2.setOnClickListener(this::onClick);
        tvAnwser3.setOnClickListener(this::onClick);
        tvAnwser4.setOnClickListener(this::onClick);
    }

    public void InitUI(View view)
    {
        tvQuestion= view.findViewById(R.id.tv_question1);
        tvContentQues= view.findViewById(R.id.tv_content_question);
        tvAnwser1= view.findViewById(R.id.tv_anaswer1);
        tvAnwser2= view.findViewById(R.id.tv_anaswer2);
        tvAnwser3= view.findViewById(R.id.tv_anaswer3);
        tvAnwser4= view.findViewById(R.id.tv_anaswer4);
        txtTotal = view.findViewById(R.id.txtTotal);
    }

   private void CheckAnswer(TextView textView, Question question, Answer answer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(answer.getCorrect())
                {
                    Scores+=10;
                    textView.setBackgroundResource(R.drawable.bg_green_conner_30);
                    nextQuestion();
                }
                else
                {
                    textView.setBackgroundResource(R.drawable.bg_red_conner_30);
                    showAnswerCororrect(question);
                    nextQuestion();
                    //gameOver();
                }
            }
        }, 1000);
   }
    private void gameOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog("Game Over");
            }
        }, 1000);
    }

    private void showAnswerCororrect(Question question) {
        if (question == null || question.getAnswerList() == null || question.getAnswerList().isEmpty())
            return;
        if (question.getAnswerList().get(0).getCorrect()) {
            tvAnwser1.setBackgroundResource(R.drawable.bg_green_conner_30);
        } else if (question.getAnswerList().get(1).getCorrect()) {
            tvAnwser2.setBackgroundResource(R.drawable.bg_green_conner_30);
        } else if (question.getAnswerList().get(2).getCorrect()) {
            tvAnwser3.setBackgroundResource(R.drawable.bg_green_conner_30);
        } else if (question.getAnswerList().get(3).getCorrect()) {
            tvAnwser4.setBackgroundResource(R.drawable.bg_green_conner_30);
        }
    }

    private void nextQuestion() {
        if (currentQues==questionList.size()-1)
        {
            Scores+=10;
            showDialog("Bạn đã hoàn thành");

        }
        else
        {
            currentQues++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetDataQuestion(questionList.get(currentQues));
                }
            }, 1000);

        }
    }
    public void showDialog(String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_anaswer1:
                tvAnwser1.setBackgroundResource(R.drawable.bg_orange_conner_30);
                CheckAnswer(tvAnwser1, mquestion, mquestion.getAnswerList().get(0));
                break;
            case R.id.tv_anaswer2:
                tvAnwser2.setBackgroundResource(R.drawable.bg_orange_conner_30);
                CheckAnswer(tvAnwser2,mquestion,mquestion.getAnswerList().get(1));
                break;
            case R.id.tv_anaswer3:
                tvAnwser3.setBackgroundResource(R.drawable.bg_orange_conner_30);
                CheckAnswer(tvAnwser3,mquestion,mquestion.getAnswerList().get(2));
                break;
            case R.id.tv_anaswer4:
                tvAnwser4.setBackgroundResource(R.drawable.bg_orange_conner_30);
                CheckAnswer(tvAnwser4,mquestion,mquestion.getAnswerList().get(3));
                break;
        }
    }
}