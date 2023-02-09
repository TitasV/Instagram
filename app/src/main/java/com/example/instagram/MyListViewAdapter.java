package com.example.instagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> images;
    ArrayList<String> names;
    LayoutInflater layoutInflater;

    public MyListViewAdapter(Context context, ArrayList<Bitmap> images, ArrayList<String> names) {
        this.context = context;
        this.images = images;
        this.names = names;
    }

    @Override
    public int getCount() {
        return images.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = layoutInflater.inflate(R.layout.custom_list_view_item, null);
        }
        ImageView imageView = view.findViewById(R.id.listViewImage);
        TextView textView = view.findViewById(R.id.listViewTextView);
        textView.setText(names.get(position));
        imageView.setImageBitmap(images.get(position));
        return view;
    }
}
