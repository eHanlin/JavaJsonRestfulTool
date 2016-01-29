package com.ehanlin.restful;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRestfulToolTest {
    
    private static Server server = null;
    private static Integer port = 18080;
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    private static JsonRestfulTool jsonRestfulTool = new JsonRestfulTool();
    
    @BeforeClass
    public static void startServer() throws Exception{
        server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        
        handler.addServletWithMapping(RestfulServlet.class,"/test");
        
        new Thread(new Runnable(){
            public void run() {
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        Thread.sleep(1000L);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGet() throws URISyntaxException{
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("127.0.0.1").setPort(port).setPath("/test");
        URI uri = builder.build();
        JsonRestfulToolResult result = jsonRestfulTool.get(uri);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getSuccess());
        Assert.assertNotNull(result.getResult());
        Map<String, Object> resultData = (Map<String, Object>) result.getResult();
        Assert.assertEquals(resultData.get("name"), "熱狗");
        Assert.assertTrue((int)resultData.get("no") == 123);
    }
    
    @Test
    public void testDelete() throws URISyntaxException{
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("127.0.0.1").setPort(port).setPath("/test");
        URI uri = builder.build();
        JsonRestfulToolResult result = jsonRestfulTool.delete(uri);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getSuccess());
        Assert.assertNull(result.getResult());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testPost() throws URISyntaxException{
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("127.0.0.1").setPort(port).setPath("/test");
        URI uri = builder.build();
        Map<String, Object> sendData = new HashMap<String, Object>();
        sendData.put("name", "熱狗");
        sendData.put("no", 123);
        JsonRestfulToolResult result = jsonRestfulTool.post(uri, sendData);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getSuccess());
        Assert.assertNotNull(result.getResult());
        Map<String, Object> resultData = (Map<String, Object>) result.getResult();
        Assert.assertEquals(resultData.get("name"), "熱狗");
        Assert.assertTrue((int)resultData.get("no") == 123);
        Assert.assertTrue((boolean)resultData.get("post"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testPut() throws URISyntaxException{
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("127.0.0.1").setPort(port).setPath("/test");
        URI uri = builder.build();
        Map<String, Object> sendData = new HashMap<String, Object>();
        sendData.put("name", "熱狗");
        sendData.put("no", 123);
        JsonRestfulToolResult result = jsonRestfulTool.put(uri, sendData);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getSuccess());
        Assert.assertNotNull(result.getResult());
        Map<String, Object> resultData = (Map<String, Object>) result.getResult();
        Assert.assertEquals(resultData.get("name"), "熱狗");
        Assert.assertTrue((int)resultData.get("no") == 123);
        Assert.assertTrue((boolean)resultData.get("put"));
    }
    
    @AfterClass
    public static void stopServer() throws Exception{
        server.stop();
    }
    
    public static class RestfulServlet extends HttpServlet{

        private static final long serialVersionUID = 557381238259270669L;

        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", true);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(mapper.writeValueAsString(result).getBytes("UTF-8"));
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", true);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", "熱狗");
            data.put("no", 123);
            result.put("result", data);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(mapper.writeValueAsString(result).getBytes("UTF-8"));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String sendData = IOUtils.toString(req.getInputStream(), "UTF-8");
            Map<String, Object> data = (Map<String, Object>) mapper.readValue(sendData, Map.class).get("value");
            data.put("post", true);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", data);
            result.put("success", true);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(mapper.writeValueAsString(result).getBytes("UTF-8"));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String sendData = IOUtils.toString(req.getInputStream(), "UTF-8");
            Map<String, Object> data = (Map<String, Object>) mapper.readValue(sendData, Map.class).get("value");
            data.put("put", true);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", data);
            result.put("success", true);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(mapper.writeValueAsString(result).getBytes("UTF-8"));
        }
        
    }
}
