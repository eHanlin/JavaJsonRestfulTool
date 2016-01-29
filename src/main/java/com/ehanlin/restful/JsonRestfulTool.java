package com.ehanlin.restful;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import com.ehanlin.httpclient.PoolingClient;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>方便用來處理基於 json 格式的 restful api</p>
 * <p>傳入的 json 格式皆是 { "value" : object}</p>
 */
public class JsonRestfulTool {
    
    private PoolingClient poolingClient = null;
    private ObjectMapper mapper = null;
    
    public JsonRestfulTool(){
        this(null, null);
    }
    
    public JsonRestfulTool(PoolingClient poolingClient){
        this(poolingClient, null);
    }
    
    public JsonRestfulTool(ObjectMapper mapper){
        this(null, mapper);
    }
    
    public JsonRestfulTool(PoolingClient poolingClient, ObjectMapper mapper){
        if(poolingClient == null){
            this.poolingClient = new PoolingClient();
        }else{
            this.poolingClient = poolingClient;
        }
        if(mapper == null){
            this.mapper = new ObjectMapper();
        }else{
            this.mapper = mapper;
        }  
    }

    
    /**
     * HTTP GET 方法，用來取得值
     * @return
     */
    public JsonRestfulToolResult get(URI uri){
        try{
            return executeRequest(new HttpGet(uri));
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }
    
    /**
     * HTTP POST 方法，用來新增值
     * @return
     */
    public JsonRestfulToolResult post(URI uri, Map<String, Object> map){
        try{
            return executeStringBodyRequest(new HttpPost(uri), map);
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }
    
    /**
     * HTTP PUT 方法，用來取代或新增值
     * @return
     */
    public JsonRestfulToolResult put(URI uri, Map<String, Object> map){
        try{
            return executeStringBodyRequest(new HttpPut(uri), map);
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }
    
    /**
     * HTTP DELETE 方法，用來刪除值
     * @return
     */
    public JsonRestfulToolResult delete(URI uri){
        try{
            return executeRequest(new HttpDelete(uri));
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }
    
    /**
     * HTTP PATCH 方用，用來更新部份的值
     * @return
     */
    public JsonRestfulToolResult patch(URI uri, Map<String, Object> map){
        try{
            return executeStringBodyRequest(new HttpPatch(uri), map);
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }
    
    
    /**
     * 用來執行不帶 json body 參數的 request。若錯誤，會傳回 status = -1。
     * @param request
     * @return
     */
    private JsonRestfulToolResult executeRequest(HttpRequestBase request){
        try{
            request.addHeader("Content-Type", "application/json");
            HttpResponse httpResponse = getPoolingClient().getHttpClient().execute(request);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String dataString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                JsonRestfulToolResult result = mapper.readValue(dataString, JsonRestfulToolResult.class);
                return result;
            }else{
                return JsonRestfulToolResult.errorResult;
            }
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }finally{
            request.abort();
            request.releaseConnection();
        }
    }
    
    /**
     * 用來執行帶 json body 參數的 request。若錯誤，會傳回 status = -1。
     * @param request
     * @param map
     * @return
     */
    private JsonRestfulToolResult executeStringBodyRequest(HttpEntityEnclosingRequestBase request, Map<String, Object> map){
        try{
            Map<String, Object> sendData = new HashMap<String, Object>();
            sendData.put("value", map);
            request.setEntity(new StringEntity(mapper.writeValueAsString(sendData), "UTF-8"));
            return executeRequest(request);
        }catch(Exception e){
            return JsonRestfulToolResult.errorResult;
        }
    }

    public PoolingClient getPoolingClient() {
        return poolingClient;
    }

    public void setPoolingClient(PoolingClient poolingClient) {
        this.poolingClient = poolingClient;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
