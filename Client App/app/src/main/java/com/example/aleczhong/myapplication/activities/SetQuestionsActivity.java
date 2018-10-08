package com.example.aleczhong.myapplication.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.aleczhong.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SetQuestionsActivity extends ListActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_questions);

        items = new ArrayList<>(Arrays.asList("Hello", "World!"));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    public void test(View view) {
        items.add("Added");
        adapter.notifyDataSetChanged();
    }
}
