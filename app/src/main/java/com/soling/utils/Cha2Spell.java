package com.soling.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Cha2Spell {
    private static StringBuffer stringBuffer = new StringBuffer();
    /*
    获取汉字字符串的汉语拼音
     */
    public static String getPinYin(String s){
        stringBuffer.setLength(0);
        char[] chars = s.toCharArray();
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i=0;i<chars.length;i++){
            if(chars[i]>128){
                try {
                    stringBuffer.append(PinyinHelper.toHanyuPinyinStringArray(chars[i],hanyuPinyinOutputFormat)[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                stringBuffer.append(chars[i]);
            }
        }
        return stringBuffer.toString();
    }
    /*
    汉字转拼音工具类
    获取汉字字符串首字母，英文字符不变
    */
    public static String getPinYinHeadChar(String s){
        stringBuffer.setLength(0);
        char c = s.charAt(0);
        String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if(pinYinArray!=null){
            stringBuffer.append(pinYinArray[0].charAt(0));
        }else{
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

}
