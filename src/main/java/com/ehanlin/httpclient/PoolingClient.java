package com.ehanlin.httpclient;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 * <p>打包多執行緒用的 pooling httpclient。</p>
 * <p>會自動去尋找 /poolingClient.properties 檔，若有找到，會以該設定檔中的設定為主。</p>
 * <p>poolingClient.properties 中可用的選項
 *  <ul>
 *      <li>maximumPoolSize 指定 http connection 最大可用的數量。預設是 100</li>
 *  <ul>
 * </p>
 */
public class PoolingClient {
    
    private PoolingClientConnectionManager clientConnectionManager = null;
    private HttpClient httpClient = null;
    
    private Integer maximumPoolSize = 100;
    
    /**
     * 以 /poolingClient.properties 檔為主的建構式。
     */
    public PoolingClient(){
        this(Thread.currentThread().getContextClassLoader().getResource("poolingClient.properties"));
    }
    
    public PoolingClient(String configPath){
        this(Thread.currentThread().getContextClassLoader().getResource(configPath));
    }
    
    /**
     * 以傳入的設定檔 URL 建構，若設定檔不存在，或是有缺少選項，則缺少的部份以預設值帶入。
     * @param configFilePath 設定檔 URL
     */
    public PoolingClient(URL configFilePath){
        init(configFilePath);
    }
    
    /**
     * 直接傳入參數建構。
     * @param maximumPoolSize 指定 http connection 最大可用的數量。
     */
    public PoolingClient(Integer maximumPoolSize){
        init(maximumPoolSize);
    }
    
    private void init(URL configFilePath){
        try{
            Properties props = new Properties();
            props.load(new FileInputStream(configFilePath.getPath()));
            maximumPoolSize = Integer.parseInt(props.getProperty("maximumPoolSize", "100"));
            init(maximumPoolSize);
        }catch(Exception e){
            init(maximumPoolSize);
        }
    }
    
    private void init(Integer maximumPoolSize){
        
        clientConnectionManager = new PoolingClientConnectionManager();
        clientConnectionManager.setMaxTotal(maximumPoolSize);
        
        httpClient = new DefaultHttpClient(clientConnectionManager);
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8"); 
    }
    
    /**
     * 傳回建構好的 httpclient
     * @return httpclient
     */
    public HttpClient getHttpClient(){
        return httpClient;
    }

    /**
     * @return http connection 最大可用的數量。
     */
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

}
