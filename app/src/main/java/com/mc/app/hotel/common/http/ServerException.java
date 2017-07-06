package com.mc.app.hotel.common.http;

/**
 * Created by Administrator on 2017/7/3.
 */
public class ServerException extends Exception {

    public static final String USER_NOT_EXIST = "100";
    public static final String WRONG_PASSWORD = "101";
    private static String message;

    public ServerException(String resultCode) {
        super(getApiExceptionMessage(resultCode));
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(String code){
        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
