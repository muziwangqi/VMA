package com.soling.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.soling.model.PhoneCallLog;
import com.soling.model.PhoneDto;
import com.soling.model.PhoneInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhoneUtil {
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    public static final String SORT_KEY_PARMARY = "sort_key";
    private Context context;
    private Uri uri;

    public PhoneUtil(Context context) {
        this.context = context;
    }

    public PhoneUtil() {
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
                    phoneDto.setId(cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
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
    public List<PhoneInformation> getInformationList() {
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date desc");
        List<PhoneInformation> phoneInformations = new ArrayList<PhoneInformation>();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    PhoneInformation phoneInformation = new PhoneInformation();
                    phoneInformation.setStrAddress(cursor.getString(cursor.getColumnIndex("address")));
                    phoneInformation.setStrBody(cursor.getString(cursor.getColumnIndex("body")));
                    phoneInformation.setDate(cursor.getLong(cursor.getColumnIndex("date")));
                    phoneInformation.setType(cursor.getInt(cursor.getColumnIndex("type")));
                    phoneInformation.setPerson(getPerson(cursor.getString(cursor.getColumnIndex("address"))));
                    phoneInformations.add(phoneInformation);
                }
                return phoneInformations;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private String getPerson(String address) {
        List<PhoneDto> phoneList = new ArrayList<PhoneDto>();
        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);

            cursor = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToNext();
                    String name = cursor.getString(0);
                    return name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

      /*
    根据电话号码合并通话记录和短信记录
     */

    public List<PhoneInformation> handleListInformation() {
        List<PhoneInformation> list = new ArrayList<PhoneInformation>();
        list = getInformationList();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                String number1 = list.get(j).getStrAddress();
                String number2 = list.get(i).getStrAddress();
                if (number1.equals(number2)) {
                    list.remove(j);
                }
            }
        }
        return list;
    }


    /*
    获取设备通话记录
     */
    public List<PhoneCallLog> getPhoneCallLog() {
        List<PhoneCallLog> phoneCallLogs = new ArrayList<PhoneCallLog>();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,new String[]{
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
        }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    PhoneCallLog phoneCallLog = new PhoneCallLog();
                    phoneCallLog.setType(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    phoneCallLog.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                    phoneCallLog.setDuration(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                    phoneCallLog.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    phoneCallLog.setName(getPerson(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))));
                    phoneCallLogs.add(phoneCallLog);
                }
                return phoneCallLogs;
            } catch (Exception E) {
                E.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public List<PhoneCallLog> handleListPhoneCallLog() {
        List<PhoneCallLog> list = new ArrayList<PhoneCallLog>();
        list = getPhoneCallLog();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                String number1 = list.get(j).getNumber();
                String number2 = list.get(i).getNumber();
                if (number1.equals(number2)) {
                    list.remove(j);
                }
            }
        }
        return list;
    }


    /*
    更新联系人
     */
    public void updateContact(long rawContactId, String name, String number, String email, String company, String position, String im) {
        uri = Uri.parse("\"content://com.android.contacts/data");
        ContentValues values = new ContentValues();
        if (number != null && number != "") {
            //更新电话号码
            values.put("data1", number);
            context.getContentResolver().update(uri, values, "mimetype_id=? and raw_contact_id=?", new String[]{"5", rawContactId + ""});
        }

        if (name != null && name != "") {
            //更新联系人姓名
            values.clear();
            values.put("data1", name);
            context.getContentResolver().update(uri, values, "mimetype_id=? and raw_contact_id=?", new String[]{"7", rawContactId + ""});
        }

        if (email != null && email != "") {
            //更新联系人邮箱
            values.clear();
            values.put("data1", email);
            context.getContentResolver().update(uri, values, "mimetype_id=? and raw_contact_id=?", new String[]{"1", rawContactId + ""});
        }

        if (im != null && im != "") {
            //更新联系人im
            values.clear();
            values.put("data1", im);
            context.getContentResolver().update(uri, values, "mimetype_id=? and raw_contact_id=?", new String[]{"2", rawContactId + ""});
        }

        if (company != null && company != "") {
            //更新联系人company
            values.clear();
            values.put("data1", company);
            values.put("data3", name);
            values.put("data4", position);
            context.getContentResolver().update(uri, values, "mimetype_id=? and raw_contact_id=?", new String[]{"4", rawContactId + ""});
        }
    }

    /*
    删除联系人
     */
    public void deleteContact(long rawContactId) {
        uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.RawContacts._ID}, "contact_id=?", new String[]{String.valueOf(rawContactId)}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            uri = Uri.parse("content://com.android.contacts/data");
            context.getContentResolver().delete(uri, "raw_contact_id=?", new String[]{id + ""});
            cursor.close();
        }
    }

    /*
    增加联系人
     */
    public void addContact(String name, String phoneNumber, String email, String company, String position, String im) {
        uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentValues values = new ContentValues();
        long rawContactId = ContentUris.parseId(context.getContentResolver().insert(uri, values));
        uri = Uri.parse("content://com.android.contacts/raw_contacts");
        // 向data表插入数据
        if (name != "") {
            values.put("raw_contact_id", rawContactId);
            values.put("mimetype", "vnd.android.cursor.item/name");
            values.put("data2", name);
            context.getContentResolver().insert(uri, values);
        }
        // 向data表插入电话号码
        if (phoneNumber != "") {
            values.clear();
            values.put("raw_contact_id", rawContactId);
            values.put("mimetype", "vnd.android.cursor.item/phone_v2");
            values.put("data2", "2");
            values.put("data1", phoneNumber);
            context.getContentResolver().insert(uri, values);
        }
        //向data表中插入邮箱
        if (email != "") {
            // 添加Email
            values.clear();
            values.put("raw_contact_id", rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Email.DATA, email);
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
            context.getContentResolver().insert(uri, values);
        }
        //向data表中插入联系人的组织
        if (company != "" && position != "") {
            //organization
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.CommonDataKinds.Organization.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Organization.LABEL, name);
            values.put(ContactsContract.CommonDataKinds.Organization.TITLE, position);
            values.put(ContactsContract.CommonDataKinds.Organization.COMPANY, company);
            values.put(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK);
            context.getContentResolver().insert(uri, values);
        }
        //向data表中插入联系人的QQ
        if (im != "") {
            //im
            values.clear();
            values.put("raw_contact_id", rawContactId);
            values.put("mimetype", "vnd.android.cursor.item/im");
            values.put(ContactsContract.CommonDataKinds.Im.DATA, im);
            values.put(ContactsContract.CommonDataKinds.Im.TYPE, ContactsContract.CommonDataKinds.Im.TYPE_WORK);
            context.getContentResolver().insert(uri, values);
        }
    }

    /*
    读取联系人详细信息
     */
    public void readContactsDetails(String id) {
        ContentResolver resolver = context.getContentResolver();
        uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor rawContactsIdCur = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{id}, null);
        if (rawContactsIdCur.getCount() > 0) {
            while (rawContactsIdCur.moveToNext()) {
                String mimetype = rawContactsIdCur.getString(rawContactsIdCur.getColumnIndex(ContactsContract.Data.MIMETYPE));

            }
        }
    }

    /*
    根据电话号码查询与该电话号码的通话记录
     */

    public  List<PhoneCallLog> selectPhoneLog(String number){
        List<PhoneCallLog> list = new ArrayList<PhoneCallLog>();
        List<PhoneCallLog> phoneCallLogs = new ArrayList<PhoneCallLog>();
        list = getPhoneCallLog();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getNumber().equals(number)){
                phoneCallLogs.add(list.get(i));
            }
        }
        return phoneCallLogs;
    }

    /*
    根据电话号码查询与该电话号码的短信记录
     */
    public List<PhoneInformation> selectInformationLog(String number){
        List<PhoneInformation> list = new ArrayList<PhoneInformation>();
        List<PhoneInformation> phoneInformations = new ArrayList<PhoneInformation>();
        list = getInformationList();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getStrAddress().equals(number)){
                phoneInformations.add(list.get(i));
            }
        }
        return phoneInformations;
    }
}
