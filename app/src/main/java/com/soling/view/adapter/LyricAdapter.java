package com.soling.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class LyricAdapter extends ArrayAdapter<String> {

    private List<String> lyric;

    public LyricAdapter(Context context, int resource, List<String> lyric) {
        super(context, resource, lyric);
        this.lyric = lyric;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        return view;
    }

}
