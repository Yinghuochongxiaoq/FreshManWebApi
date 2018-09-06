package com.freshman.webapi.filters;

import com.freshman.webapi.model.BaseResponse;
import com.freshman.webapi.model.ResponseCode;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 异常增强，以JSON的形式返回给客服端
 * 异常增强类型：NullPointerException,RunTimeException,ClassCastException,
 * 　　　　　　　　 NoSuchMethodException,IOException,IndexOutOfBoundsException
 * 　　　　　　　　 以及springmvc自定义异常等，如下：
 * SpringMVC自定义异常对应的status code
 * Exception                       HTTP Status Code
 * ConversionNotSupportedException         500 (Internal Server Error)
 * HttpMessageNotWritableException         500 (Internal Server Error)
 * HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
 * HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
 * HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
 * NoSuchRequestHandlingMethodException    404 (Not Found)
 * TypeMismatchException                   400 (Bad Request)
 * HttpMessageNotReadableException         400 (Bad Request)
 * MissingServletRequestParameterException 400 (Bad Request)
 */
@ControllerAdvice
public class RestExceptionHandler {
    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse runtimeExceptionHandler(RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return new BaseResponse(ResponseCode.RUNTIMEEXCEPTION, runtimeException.getMessage());
    }

    /**
     * 空指针异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public BaseResponse nullPointerExceptionHandler(NullPointerException ex) {
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.NULLPOINTEREXCEPTION, ex.getMessage());
    }

    /**
     * 类型转换异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public BaseResponse classCastExceptionHandler(ClassCastException ex) {
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.CLASSCASTEXCEPTION, ex.getMessage());
    }

    /**
     * IO异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public BaseResponse iOExceptionHandler(IOException ex) {
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.IOEXCEPTION, ex.getMessage());
    }

    /**
     * 未知方法异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public BaseResponse noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.NOSUCHMETHODEXCEPTION, ex.getMessage());
    }

    /**
     * 数组越界异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public BaseResponse indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.INDEXOUTOFBOUNDSEXCEPTION, ex.getMessage());
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public BaseResponse requestNotReadable(HttpMessageNotReadableException ex) {
        System.out.println("400..requestNotReadable");
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.HTTPMESSAGENOTREADABLEEXCEPTION, ex.getMessage());
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public BaseResponse requestTypeMismatch(TypeMismatchException ex) {
        System.out.println("400..TypeMismatchException");
        ex.printStackTrace();
        return new BaseResponse(ResponseCode.TYPEMISMATCHEXCEPTION, ex.getMessage());
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public BaseResponse requestMissingServletRequest(MissingServletRequestParameterException ex) {
        System.out.println("400..MissingServletRequest");
        ex.printStackTrace();
        String parmstr = "parm不能为空！";
        return new BaseResponse(ResponseCode.MISSINGSERVLETREQUESTPARAMETEREXCEPTION, parmstr.replaceAll("parm", ex.getParameterName()));//ReturnFormat.retParam(400, null);
    }

    /**
     * 406错误
     *
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public BaseResponse request406() {
        return new BaseResponse(ResponseCode.HTTPMEDIATYPENOTACCEPTABLEEXCEPTION, null);
    }

    /**
     * 500错误
     *
     * @param runtimeException
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public BaseResponse server500(RuntimeException runtimeException) {
        return new BaseResponse(ResponseCode.CONVERSIONNOTSUPPORTEDEXCEPTION, runtimeException.getMessage());
    }
}
