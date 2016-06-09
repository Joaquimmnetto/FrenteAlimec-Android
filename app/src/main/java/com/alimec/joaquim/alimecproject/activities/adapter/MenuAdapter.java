package com.alimec.joaquim.alimecproject.activities.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by KithLenovo on 24/08/2015.
 */
public class MenuAdapter extends BaseAdapter {

    private Context context;
    //Linked hash porque eu preciso que fique na ordem de insercao
    private LinkedHashMap<String, Drawable> menuIcon = new LinkedHashMap<>();

    public MenuAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return menuIcon.size();
    }

    @Override
    public Object getItem(int position) {
        return new ArrayList<>(menuIcon.keySet()).get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        String title = (String) getItem(position);
        Drawable icon = menuIcon.get(title);

        if(convertView == null){
            v = View.inflate(context, R.layout.item_menu,null);
        }

        ((TextView)v.findViewById(R.id.menu_text)).setText(title);

        if(icon != null){
            ((ImageView)v.findViewById(R.id.menu_icon)).setImageDrawable(icon);
        }
        else{
            ((ImageView)v.findViewById(R.id.menu_icon)).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
        }


        return v;
    }


    public void add(String text, Drawable icon){
        menuIcon.put(text,icon);
    }

    public void remove(String text) {
        menuIcon.remove(text);
    }
}
