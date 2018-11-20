package com.soling.utils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.widget.Toast;

import com.soling.model.PhoneCallLog;
import com.soling.model.PhoneDto;
import com.soling.model.PhoneInformation;
import com.soling.view.fragment.PhoneFragment;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhoneUtil  {
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    public static final String SORT_KEY_PARMARY = "sort_key";
    private Context context;
    private Uri uri;
    public PhoneUtil(Context context){
        this.context = context;
    }
    public PhoneUtil(){
        super();
    }

    /*
     获取手机联系人信息
      */
    public List<PhoneDto> getPhoneList() {
        ArrayList<PhoneDto> phoneDtos = new ArrayList<PhoneDto>();
        Cursor cursor = null;
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        cursor = context.getContentResolver().query(uri, new String[]{
                "display_name",
                "sort_key",
                "contact_id",
                "data1",
        }, null, null, "sort_key");
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    PhoneDto phoneDto = new PhoneDto();
                    phoneDto.setName(cursor.getString(0));
                    //phoneDto.setFirstLetter(phoneDto.setFirstLetter(cursor.getString(1)));
                    phoneDto.setId(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    phoneDto.setTelPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    phoneDtos.add(phoneDto);
                }
                return phoneDtos;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }



    /*
    获取手机短信信息
     */
    public List<PhoneInformation> getInformationList(){
        final String SMS_URI_ALL = "content://sms/";
        List<PhoneInformation>  phoneInformations = new ArrayList<PhoneInformation>();
            StringBuilder smsBuilder = new StringBuilder();
            uri = Uri.parse(SMS_URI_ALL);
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,new String[] {
                    Telephony.Sms.ADDRESS,
                    Telephony.Sms.BODY,
                    Telephony.Sms.DATE,
                    Telephony.Sms.READ,
                    Telephony.Sms.STATUS,
                    Telephony.Sms.TYPE,
            },null,null,"date DESC limit 2");
            if(cursor!=null){
                try {
                    while(cursor.moveToNext()){
                        PhoneInformation phoneInformation = new PhoneInformation();
                        phoneInformation.setStrAddress(cursor.getString(0));
                        phoneInformation.setStrBody(cursor.getString(1));
                        phoneInformation.setDate(cursor.getLong(2));
                        phoneInformation.setRead(cursor.getInt(3));
                        phoneInformation.setStatus(cursor.getInt(4));
                        phoneInformation.setType(cursor.getInt(5));
                        phoneInformation.setPerson(getPerson(phoneInformation.getStrAddress()));
                        phoneInformations.add(phoneInformation);
                    }
                    return phoneInformations;
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    cursor.close();
                }

            }
        return null;
    }
    private String getPerson(String address){
        List<PhoneDto> phoneList = new ArrayList<PhoneDto>();
        Cursor cursor =  null;
       try{
           ContentResolver resolver = context.getContentResolver();
           uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,address);

           cursor = resolver.query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null,null,null);
           if(cursor!=null){
               if(cursor.getCount()!=0){
                   cursor.moveToNext();
                   String name = cursor.getString(0);
                   return name;
               }
           }
       }catch(Exception e){
           e.printStackTrace();
       }finally{
           cursor.close();
       }
        return null;
    }
/*
获取设备通话记录
 */
    public List<PhoneCallLog> getPhoneCallLog(){
       List<PhoneCallLog> phoneCallLogs = new ArrayList<PhoneCallLog>();
      Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{
               CallLog.Calls.CACHED_FORMATTED_NUMBER,
               CallLog.Calls.CACHED_MATCHED_NUMBER,
               CallLog.Calls.CACHED_NAME,
               CallLog.Calls.TYPE,
               CallLog.Calls.DATE,
               CallLog.Calls.DURATION,
               CallLog.Calls.GEOCODED_LOCATION,
       }, null, null, "date DESC limit 2");
      if(cursor!=null){
          try{
              while (cursor.moveToNext()){
                  PhoneCallLog phoneCallLog = new PhoneCallLog();
                  phoneCallLog.setFormatted_number(cursor.getString(0));
                  phoneCallLog.setMatched_number(cursor.getString(1));
                  phoneCallLog.setName(cursor.getString(2));
                  phoneCallLog.setType(cursor.getInt(3));
                  phoneCallLog.setDate(cursor.getLong(4));
                  phoneCallLog.setDuration(cursor.getLong(5));
                  phoneCallLog.setLocation(cursor.getString(6));
                  phoneCallLogs.add(phoneCallLog);
              }
              return phoneCallLogs;
          }catch(Exception E){
              E.printStackTrace();
          }finally {
              cursor.close();
          }
      }
        return null;
    }
    /*
    调用设备拨号页面
     */


}
