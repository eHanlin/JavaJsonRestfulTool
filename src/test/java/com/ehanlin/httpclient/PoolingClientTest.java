package com.ehanlin.httpclient;

import org.junit.Test;

import org.junit.Assert;

public class PoolingClientTest {
    
    @Test
    public void testSettingByProperties(){
        PoolingClient poolingClient = new PoolingClient();
        Assert.assertNotNull(poolingClient.getHttpClient());
        Assert.assertTrue(poolingClient.getMaximumPoolSize() == 10);
    }

}
