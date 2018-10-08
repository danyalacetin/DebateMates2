package com.example.aleczhong.myapplication.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetQuestionsActivity extends ListActivity {

    private final ArrayList<Question> items;
    private final BaseAdapter adapter;

    public SetQuestionsActivity() {
        items = new ArrayList<>();
        adapter = new BaseAdapter() {



            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = getLayoutInflater().inflate(R.layout.question_layout, null);

                TextView text = view.findViewById(R.id.tempItem);
                SeekBar bar = view.findViewById(R.id.seekBar);

                final Question q = items.get(i);

                text.setText(q.getContent());
                bar.setProgress(q.getScore());
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();
                        q.setScore(progress);
                    }
                });


                return view;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_questions);
        setListAdapter(adapter);
        populateQuestions();
    }

    public void onCancelPress(View view) {
        finish();
    }

    public void onConfirmPress(View view) {
        ClientApp.getClientApp().updateQuestions(items);
        finish();
    }

    public void populateQuestions() {
        //ClientApp.getClientApp().sendData("getQuestions");
        List<Question> questions = ClientApp.getClientApp().getPreferenceQuestions();
        items.clear();

        for (Question question : questions) {
            String content = question.getContent();
            int score = question.getScore();
            Question newQuestion = new Question(content);
            newQuestion.setScore(score);
            items.add(newQuestion);
        }
        adapter.notifyDataSetChanged();
    }
}
