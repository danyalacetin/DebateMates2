package com.example.aleczhong.myapplication.applogic;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<ChatMessage> items;
    private AppCompatActivity activity;

    public MessageAdapter(AppCompatActivity activity, List items) {
        this.activity = activity;
        this.items = items;
    }

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
        ChatMessage newMessage = items.get(i);
        int layout;
        int textId;

        switch (newMessage.getType()) {
            case SELF:
                layout = R.layout.sent_message_layout;
                textId = R.id.sentMessage;
                break;
            case OPPONENT:
                layout = R.layout.recieved_message_layout;
                textId = R.id.receiveMessage;
                break;
            default:
                layout = R.layout.server_message_layout;
                textId = R.id.serverMessage;
                break;
        }

        view = activity.getLayoutInflater().inflate(layout, null);


        TextView messageView = view.findViewById(textId);
        messageView.setText(newMessage.getContent());

        return view;
    }
}
