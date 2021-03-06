package com.example.sgondala.bubblenote;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    EditText chat_text;
    Button sendButton;
    boolean position = false;
    ChatAdapter adapter;
    Context ctx = this;
    DBHelper ourHelper;
    ActionMode.Callback mActionModeCallback;
    List<Integer> listOfItemsToBeDeleted = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.chat_list_view);
        chat_text = (EditText) findViewById(R.id.chatText);
        sendButton = (Button) findViewById(R.id.sendButton);
        adapter = new ChatAdapter(ctx, R.layout.singlemessagelayout);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatTextMessage = chat_text.getText().toString();
                if (!chatTextMessage.isEmpty()) {
                    adapter.add(new DataProvider(position, chat_text.getText().toString()));
                    position = !position;
                    DatabaseOperations DB = new DatabaseOperations(ctx);
                    DB.putInformation(DB, chat_text.getText().toString());
                    chat_text.setText("");
                }

            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                listOfItemsToBeDeleted.add(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.action_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_delete:
                        deleteAllMessages();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        generatePrevious();
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

    public void generatePrevious(){
        DatabaseOperations dop = new DatabaseOperations(ctx);
        Cursor cr = dop.getInformation(dop);
        int count = cr.getCount();
        if(count!=0){
            cr.moveToFirst();
            do{
                String message = cr.getString(0);
                adapter.add(new DataProvider(position, message));
                position = !position;
                System.out.println(message);
            }
            while(cr.moveToNext());
        }
    }

    public void deleteMessage(int position){
        DataProvider dp = adapter.getItem(position);
        adapter.removeItem(position);
        String messageSelected = dp.message;
        DatabaseOperations dop = new DatabaseOperations(ctx);
        dop.deleteMessage(dop, messageSelected);
    }

    public void deleteAllMessages(){
        Collections.sort(listOfItemsToBeDeleted, Collections.reverseOrder());
        for(Integer i : listOfItemsToBeDeleted){
            deleteMessage(i);
        }
        listOfItemsToBeDeleted = new ArrayList<Integer>();
    }
}
