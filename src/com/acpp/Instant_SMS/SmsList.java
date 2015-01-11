package com.acpp.Instant_SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SmsList extends BaseAdapter implements ListAdapter {

    private ArrayList<String> to;
    private ArrayList<String> what;
    private ArrayList<String> saveAs;
    private Context context;

    public SmsList(ArrayList<String> to, ArrayList<String> what, ArrayList<String> saveAs, Context context) {
        this.to = to;
        this.what = what;
        this.saveAs = saveAs;
        this.context = context;
    }


    @Override
    public int getCount() {
        return to.size();
    }

    @Override
    public Object getItem(int position) {
        return to.get(position);
     }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view, null);
        }

        TextView toIn = (TextView)view.findViewById(R.id.invisible);
        toIn.setText(to.get(position));

        TextView textViewTo = (TextView)view.findViewById(R.id.view_to);
        textViewTo.setText(saveAs.get(position));

        if (textViewTo.getText().toString().equals("")) {
            textViewTo.setText(to.get(position));
        }

        TextView textViewWhat = (TextView)view.findViewById(R.id.view_what);
        textViewWhat.setText(what.get(position));

        return view;
    }
}
