package com.soling.model;

import com.soling.utils.Cha2Spell;

public class PhoneDto {
    private String name;
    private String telPhone;
    private String namePinYin;
    private String firstLetter;
    public String getFirstLetter() {
        return firstLetter;
    }
    public String getNamePinYin() {
        return namePinYin;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        namePinYin=Cha2Spell.getPinYin(name);
        firstLetter = namePinYin.substring(0,1).toUpperCase();
        if(!firstLetter.matches("[A-Z]")){
            firstLetter = "#";
        }
    }

    public String getTelPhone() {
        return telPhone;
    }
    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }
    public PhoneDto(String name,String telPhone ){
        this.name=name;
        this.telPhone=telPhone;
    }
   public PhoneDto(){
       super();
   }
   /*
   根据name拼音对联系人进行排序
    */
   public int compareTo(PhoneDto phoneDto){
       if(firstLetter.equals("#")&&phoneDto.getFirstLetter().equals("#")){
            return 1;
       }else if(!firstLetter.equals("#")&&phoneDto.getFirstLetter().equals("#")){
           return -1;
       }else{
           return namePinYin.compareToIgnoreCase(phoneDto.getNamePinYin());
       }
   }
}
