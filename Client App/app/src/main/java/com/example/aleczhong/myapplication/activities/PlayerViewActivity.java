package com.example.aleczhong.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ChatMessage;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;

import java.util.List;

public class PlayerViewActivity extends AppCompatActivity {
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        userInput = findViewById(R.id.inputArea);
        ListView listView = findViewById(R.id.messageArea);

        final List<ChatMessage> messages = ClientApp.getClientApp().getMessages();

        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return messages.size();
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
                ChatMessage newMessage = messages.get(i);
                int layout;
                int textId;

                switch (newMessage.getType()) {
                    case SELF:
                        layout = R.layout.sent_message_layout;
                        textId = R.id.sentMessage;
                        break;
                    case SERVER:
                        layout = R.layout.recieved_message_layout;
                        textId = R.id.receiveMessage;
                        break;
                    default:
                        layout = R.layout.server_message_layout;
                        textId = R.id.serverMessage;
                        break;
                }

                view = getLayoutInflater().inflate(layout, null);


                TextView messageView = view.findViewById(textId);
                ClientApp.log(String.valueOf(messageView));
                messageView.setText(newMessage.getContent());

                return view;
            }
        };

        listView.setAdapter(adapter);

       ClientApp.getClientApp().addMessageChangeListener(new Runnable() {
           @Override
           public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       adapter.notifyDataSetChanged();
                   }
               });
           }
       });
    }

    public void sendText(View view) {
        String text = userInput.getText().toString();
        userInput.getText().clear();
        if (!text.equals("")) ClientApp.getClientApp().sendChatMessage(text);
    }
}
