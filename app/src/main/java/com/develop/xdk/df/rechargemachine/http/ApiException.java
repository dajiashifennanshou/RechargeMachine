package com.develop.xdk.df.rechargemachine.http;

/**
 * 异常处理
 */
public class ApiException extends RuntimeException {

    public static final String NONET = "no internet";


    public ApiException(String msg) {
        super(getApiExceptionMessage(msg));
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param msg
     * @return
     */
    private static String getApiExceptionMessage(String msg){
        String message = msg;
        switch (msg) {
            case NONET:
                message = "本地断网，无法充值";
                break;
        }
        return message;
    }
}

