package com.ehanlin.restful;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用來裝 JsonRestfulTool 執行後的回傳結果。
 */
public class JsonRestfulToolResult {
    public final static JsonRestfulToolResult errorResult = new JsonRestfulToolResult(false, "network error", null);
    
    private Boolean success = null;
    private String errMsg = null;
    private Object result = null;
    
    @JsonCreator
    public JsonRestfulToolResult(
            @JsonProperty("success") Boolean success, 
            @JsonProperty("errMsg") String errMsg, 
            @JsonProperty("result") Object result)
    {
        this.success = success;
        this.errMsg = errMsg;
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Object getResult() {
        return result;
    }
}
