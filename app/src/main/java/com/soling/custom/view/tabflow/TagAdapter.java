package com.soling.custom.view.tabflow;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class TagAdapter<T> {

    private List<T> items;

    public TagAdapter(List<T> items) {
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

}
