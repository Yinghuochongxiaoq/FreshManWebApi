package com.freshman.webapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

@ApiModel(value = "com.freshman.webapi.model.BaseResponse", description = "返回参数")
public class BaseResponse {
    public BaseResponse() {
        this.data = "";
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = "";
    }

    public BaseResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(Object data) {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseCode.getErrorInfo(ResponseCode.SUCCESS);
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        if (StringUtils.isBlank(message)) {
            message = ResponseCode.getErrorInfo(this.code);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @ApiModelProperty(value = "响应编码")
    private String code;
    @ApiModelProperty(value = "响应消息")
    private String message;
    @ApiModelProperty(value = "响应数据")
    private Object data;
}
