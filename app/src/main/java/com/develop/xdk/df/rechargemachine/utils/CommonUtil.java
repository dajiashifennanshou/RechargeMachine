package com.develop.xdk.df.rechargemachine.utils;

/**
 * Created by df on 2017/9/15.
 */
public class CommonUtil {
    //ByteToString
    public static  String  ByteToStr(int byteSize, byte[] in) {
        String ret = new String("");
        if(in.length < byteSize)
            return ret;

        for(int i = 0; i < byteSize; i++){
            ret = ret.concat(String.format("%1$02X ", in[i]));
        }
        return ret;
    }
    //StringToByte
    public  static int  strToByte(String in,int byteSize,byte[] out){
        String str=in.replace(" ", "");
        if(str.length()!=byteSize*2 || out==null)
        {
            return -1;
        }
        char[] hexChars = str.toCharArray();
        if(hexChars==null)
        {
            return -1;
        }
        for (int i = 0; i < byteSize; i++) {
            int pos = i * 2;
            out[i] = (byte) ((charToByte(hexChars[pos]) << 4 )| (charToByte(hexChars[pos + 1])));
        }
        return 0;
    }
    private static int charToByte(char c) {
        return  "0123456789ABCDEF".indexOf(c);
    }
}
