package com.example.sgondala.bubblenote;

import android.app.ActionBar;
import android.content.Context;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgondala on 14/7/15.
 */
public class ChatAdapter extends ArrayAdapter<DataProvider> {

    private List<DataProvider> chatList = new ArrayList<DataProvider>();
    private TextView CHAT_TXT;
    Context ctx;

    public ChatAdapter(Context context, int resource) {
        super(context, resource);
        ctx = context;
    }


    @Override
    public void add(DataProvider object){
        chatList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public DataProvider getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.singlemessagelayout,
                    parent, false);
        }

        CHAT_TXT = (TextView) convertView.findViewById(R.id.singleMessage);
        String Message;
        boolean POSITION;
        DataProvider provider = getItem(position);
        Message = provider.message;
        POSITION = provider.position;
        CHAT_TXT.setText(Message);
        CHAT_TXT.setBackgroundResource(R.drawable.temp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if(!POSITION) {params.gravity = Gravity.RIGHT;}
        else{ params.gravity = Gravity.LEFT;}
        CHAT_TXT.setLayoutParams(params);
        return convertView;
    }

    public void removeItem(int position){
        chatList.remove(position);
        notifyDataSetChanged();
    }
}
