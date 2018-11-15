package com.soling.custom.view.tabflow;

import android.content.Context;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class TagView extends FrameLayout implements Checkable {

    private boolean checked = false;

    public TagView(Context context) {
        super(context);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked  = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

}
