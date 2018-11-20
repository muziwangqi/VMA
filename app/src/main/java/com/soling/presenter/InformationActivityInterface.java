package com.soling.presenter;

import android.view.LayoutInflater;

public interface InformationActivityInterface {
    public void refreshInformation();
}
interface InformationPresenterInterface{
    public void startActivity(LayoutInflater inflater);
}