package com.freshman.webapi.model;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    /**
     * 错误码与错误描述
     */
    protected static Map<String, String> error = new HashMap<String, String>();


    public static String getErrorInfo(String defindCode) {
        return error.get(defindCode);
    }

    /**
     * 构造方法私有化
     */
    private ResponseCode() {
    }

    /**
     * 成功
     */
    public static final String SUCCESS = "0";

    /**
     * 运行时异常
     */
    public static final String RUNTIMEEXCEPTION = "1000";

    /**
     * 空指针异常
     */
    public static final String NULLPOINTEREXCEPTION = "1001";

    /**
     * 类型转换异常
     */
    public static final String CLASSCASTEXCEPTION = "1002";

    /**
     * IO异常
     */
    public static final String IOEXCEPTION = "1003";

    /**
     * 未知方法异常
     */
    public static final String NOSUCHMETHODEXCEPTION = "1004";

    /**
     * 数组越界异常
     */
    public static final String INDEXOUTOFBOUNDSEXCEPTION = "1005";

    /**
     * 登录信息有误
     */
    public static final String LOGINFOERROR = "1006";

    /**
     * 错误
     */
    public static final String FAIL = "1007";

    /**
     * 400错误
     */
    public static final String HTTPMESSAGENOTREADABLEEXCEPTION = "400";

    /**
     * 400错误
     */
    public static final String TYPEMISMATCHEXCEPTION = "400";

    /**
     * 406错误
     */
    public static final String HTTPMEDIATYPENOTACCEPTABLEEXCEPTION = "406";

    /**
     * 参数为空
     **/
    public static final String MISSINGSERVLETREQUESTPARAMETEREXCEPTION = "400";

    /**
     * 请求参数转换响应参数异常
     **/
    public static final String CONVERSIONNOTSUPPORTEDEXCEPTION = "500";

    /**
     * 请求参数转换响应参数异常
     **/
    public static final String HTTPMESSAGENOTWRITABLEEXCEPTION = "500";


    static {
        error.put(SUCCESS, "成功");
        error.put(RUNTIMEEXCEPTION, "运行时异常");
        error.put(NULLPOINTEREXCEPTION, "空指针异常");
        error.put(CLASSCASTEXCEPTION, "类型转换异常");
        error.put(IOEXCEPTION, "IO异常");
        error.put(NOSUCHMETHODEXCEPTION, "未知方法异常");
        error.put(INDEXOUTOFBOUNDSEXCEPTION, "数组越界异常");
        error.put(HTTPMESSAGENOTREADABLEEXCEPTION, "400");
        error.put(TYPEMISMATCHEXCEPTION, "400");
        error.put(HTTPMEDIATYPENOTACCEPTABLEEXCEPTION, "406");
        error.put(MISSINGSERVLETREQUESTPARAMETEREXCEPTION, "400");
        error.put(CONVERSIONNOTSUPPORTEDEXCEPTION, "类型转换异常");
        error.put(HTTPMESSAGENOTWRITABLEEXCEPTION, "系统未知异常");
        error.put(LOGINFOERROR, "登录信息有误");
        error.put(FAIL, "系统错误");
    }
}
