package com.cwy.xxs.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResultData {

    public static final int RESULT_ERROR = 1004;

    public static final int RESULT_SUCCESS = 2000;

    private int code;

    private String message;

    private Object object;

    public ResultData(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

    public ResultData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultData returnResultData(int result,Object object){
        switch (result){
            case -1 : return new ResultData(1004,"数据有误或为空",object);
            case 0 : return new ResultData(1003,"未处理数据",object);
            default : return new ResultData(2000,"成功处理了"+result+"条数据",object);
        }
    }

    public static ResultData returnResultData(int result,String message,Object object){
        switch (result){
            case -1 : return new ResultData(1004,message,object);
            case 0 : return new ResultData(1003,message,object);
            default : return new ResultData(2000,message,object);
        }
    }

    public static ResultData returnResultData(int result,String message){
        switch (result) {
            case -1:
                return new ResultData(1004, message, null);
            case 0:
                return new ResultData(1003, message, null);
            default:
                return new ResultData(2000, message, null);
        }
    }

    public static ResultData returnResultData(int result){
        return returnResultData(result,null);
    }

    public ResultData() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
