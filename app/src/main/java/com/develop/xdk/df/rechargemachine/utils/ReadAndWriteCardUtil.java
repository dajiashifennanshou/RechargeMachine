package com.develop.xdk.df.rechargemachine.utils;

import android.app.Activity;
import android.os.Handler;
import android.reader.ReaderAndroid;
import android.text.TextUtils;

import com.develop.xdk.df.rechargemachine.Application.App;

import java.io.IOException;

/**
 * 读写卡的再封装
 */
public class ReadAndWriteCardUtil {
    //返回sn号码结果
    protected static byte[] card_flag = new byte[2];
    //读写卡so类
    protected static ReaderAndroid mSerialPort = null;
    protected static int handle;
    //数据容器
    protected static byte[] ver = new byte[20];
    static {
        if(mSerialPort == null){
            try {
                mSerialPort = App.getInstance().getSerialPort();
                handle = mSerialPort.getHandle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String readSnNumber(){
        int ret = mSerialPort.getCardSN(handle, C.CARD_ADDRESS, C.CARD_MODE,C.CARD_HALT, card_flag, ver);
        String snnumber = CommonUtil.ByteToStr(card_flag[1], ver);
        if(snnumber != null){
            snnumber = snnumber.replace(" ","");
        }
        if(0 == ret) {
            return snnumber;
        }else{
            LogUtils.e("错误码："+ret);
            return null;
        }
    }

    public  static String readCardBlock(Handler handler, final Activity ac,String snnumber){
        String key = getCardPass(handler,ac,snnumber);
        String block = (String) SharedPreferencesUtils.getParam(ac,C.BLOCK_NAME,C.BLOCK);
        byte mblock = (byte)(Integer.valueOf(block)&0xff);
        byte[] password = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(password == null||CommonUtil.strToByte(key.toUpperCase(),6,password)!=0){
            return null;
        }
        int result = mSerialPort.MFRead(handle, C.CARD_ADDRESS, C.CARD_MODE,mblock,(byte)(0x01&0xff), password, ver);
        if(result == 0){
           return CommonUtil.ByteToStr(16, ver);
        }else{
            final String reslut = "key"+key+"读卡错误，错误码："+result+"状态码："+CommonUtil.ByteToStr(1, ver);
            LogUtils.e(reslut);
        }
        return  null;
    }
    public static String getCardPass(Handler handler,Activity activity,String snNumber){
        if(TextUtils.isEmpty(snNumber)||snNumber.length() != 8){
            return null;
        }
        //pass6位数122334FEEDDC
        //卡号四位数
        String key0 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(0,2),16)+0x12);
        key0 = key0.substring(key0.length()-2);
        String key1 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(2,4),16)+0x23);
        key1 = key1.substring(key1.length()-2);
        String key2 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(4,6),16)|0x34);
        key2 = key2.substring(key2.length()-2);
        String key3 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(6),16)^0xFE);
        key3 = key3.substring(key3.length()-2);
        String key4 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(0,2),16)^0xED);
        key4 = key4.substring(key4.length()-2);
        String key5 = "00"+Integer.toHexString(Integer.valueOf(snNumber.substring(6),16)&0xDC);
        key5 = key5.substring(key5.length()-2);
        String key =  key0+" ";
        key += key1+" ";
        key += key2+" ";
        key += key3+" ";
        key += key4+" ";
        key += key5;
        return key.toUpperCase();
    }
    public static boolean writeCardBlock(String content, Handler handler, final Activity activity, byte which, String snnumber){
        String key = getCardPass(handler,activity,snnumber);
        byte[] password = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(key == null||password == null ||CommonUtil.strToByte(key,6,password)!=0){
            return false;
        }
        byte[] value=new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(!TextUtils.isEmpty(content)){
            if(CommonUtil.strToByte(content,16,value) != 0) {
                LogUtils.e("写入内容有误,必须为16字节32字符");
                return false;
            }
        }else{
            LogUtils.e("请输入写入内容");
            return false;
        }
        String data = CommonUtil.ByteToStr(16,value);
        int write =   mSerialPort.MFWrite(handle, C.CARD_ADDRESS, C.CARD_MODE,which,(byte)(0x01 & 0xff), password, value);
        if(write == 0){
            LogUtils.e("写入卡"+CommonUtil.ByteToStr(password[0], value)+"成功");
            return true;
        }else{
            LogUtils.e("写卡错误，错误码："+write+"状态码："+CommonUtil.ByteToStr(1, value));
            return false;
        }
    }
    public static boolean writeCardBlockWitholdpass(String content, Handler handler, final Activity activity, byte which, String snnumber){
        String key = "FFFFFFFFFFFF";
        byte[] password = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(key == null||password == null ||CommonUtil.strToByte(key,6,password)!=0){
            return false;
        }
        byte[] value=new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(!TextUtils.isEmpty(content)){
            if(CommonUtil.strToByte(content,16,value) != 0) {
                LogUtils.e("写入内容有误,必须为16字节32字符");
                return false;
            }
        }else{
            LogUtils.e("请输入写入内容");
            return false;
        }
        String data = CommonUtil.ByteToStr(16,value);
        int write =   mSerialPort.MFWrite(handle, C.CARD_ADDRESS, C.CARD_MODE,which,(byte)(0x01 & 0xff), password, value);
        if(write == 0){
            LogUtils.e("写入卡"+CommonUtil.ByteToStr(password[0], value)+"成功");
            return true;
        }else{
            LogUtils.e("写卡错误，错误码："+write+"状态码："+CommonUtil.ByteToStr(1, value));
            return false;
        }
    }
    public static boolean writeCardBlockWithpass(String content, Handler handler, final Activity activity, byte which, String snnumber){
        String key = "551202999999";
        byte[] password = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(key == null||password == null ||CommonUtil.strToByte(key,6,password)!=0){
            return false;
        }
        byte[] value=new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        if(!TextUtils.isEmpty(content)){
            if(CommonUtil.strToByte(content,16,value) != 0) {
                LogUtils.e("写入内容有误,必须为16字节32字符");
                return false;
            }
        }else{
            LogUtils.e("请输入写入内容");
            return false;
        }
        String data = CommonUtil.ByteToStr(16,value);
        int write =   mSerialPort.MFWrite(handle, C.CARD_ADDRESS, C.CARD_MODE,which,(byte)(0x01 & 0xff), password, value);
        if(write == 0){
            LogUtils.e("写入卡"+CommonUtil.ByteToStr(password[0], value)+"成功");
            return true;
        }else{
            LogUtils.e("写卡错误，错误码："+write+"状态码："+CommonUtil.ByteToStr(1, value));
            return false;
        }
    }
}
