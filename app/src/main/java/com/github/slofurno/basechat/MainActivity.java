package com.github.slofurno.basechat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ArrayAdapter<String> messageAdapter;
    LinkedList<String> messageStack;
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bus = OttoBus.getInstance().getBus();


        messageStack = new LinkedList<String>();

        List<String> messages = new ArrayList<String>();
        messages.add("test message");
        messages.add("hi");

        messageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageStack);

        ListView listView = (ListView) findViewById(R.id.messagelist);
        listView.setAdapter(messageAdapter);

        //StartConnect();
        //bus.register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onResume() {


        StartConnect();
        // Register ourselves so that we can provide the initial value.
        OttoBus.getInstance().getBus().register(this);
        super.onResume();
    }

    @Override protected void onPause() {

        // Always unregister when an object no longer should be on the bus.
        OttoBus.getInstance().getBus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void putUserAccountResult(ReceiveMessageEvent event){
        for (ChatMessage message : event.getMessages()){
            messageStack.addFirst(message.Message);
            messageAdapter.notifyDataSetChanged();
           // messageAdapter.add(message.Message);
        }
    }

    public void StartConnect(){

        Intent intent=new Intent(this, ChatService.class);

        startService(intent);

    }

    public void sendMessage(View view) {

        EditText message = (EditText)findViewById(R.id.nextmessage);
        bus.post(new SendMessageEvent(new ChatMessage("name", message.getText().toString())));

    }
}
