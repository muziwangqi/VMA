package com.soling.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.soling.model.PhoneDto;
import com.soling.view.fragment.PhoneFragment;

import java.util.ArrayList;
import java.util.List;
public class PhoneUtil {
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    private Context context;
    private Uri phoneUri;
    public PhoneUtil(Context context){
        this.context = context;
    }
    public PhoneUtil(){
        super();
    }
    /*
    获取手机上联系人
     */
    public List<PhoneDto> getPhone(){
        phoneUri =  ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        List<PhoneDto> photoList = new ArrayList<PhoneDto>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while(cursor.moveToNext()){
            PhoneDto photoDao = new PhoneDto(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)));
            photoList.add(photoDao);
        }
        return photoList;
    }
    /*
    获取SIM卡联系人
     */
    public List<PhoneDto> getPhoneSIM(){
        phoneUri = Uri.parse("content://icc/adn");
        List<PhoneDto> photoList = new ArrayList<PhoneDto>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while(cursor.moveToFirst()){
            PhoneDto photoDao = new PhoneDto(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)));
            photoList.add(photoDao);
        }
        return photoList;
    }
}
