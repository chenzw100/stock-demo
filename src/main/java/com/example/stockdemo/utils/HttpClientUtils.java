package com.example.stockdemo.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.CookieStore;
import java.util.List;

public class HttpClientUtils {
    public static CloseableHttpClient generateClient(HttpHost httpHost, CookieStore cookieStore) {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        // http连接池管理，服务于多个执行进程的连接请求
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig);
        return httpClientBuilder.build();
    }

    private JSONObject getTidAndC() throws IOException
    {
        String url = "https://passport.weibo.com/visitor/genvisitor";

        //按指定编码转换结果实体为String类型
        String body = HttpTookit.sendPost(url,null,null);
        body = body.replaceAll("window.gen_callback && gen_callback\\(", "");
        body = body.replaceAll("\\);", "");
        JSONObject json = JSONObject.parseObject(body).getJSONObject("data");
        System.out.println(body);

        return json;

    }
    public String getCookie() throws IOException {
        JSONObject json = getTidAndC();
        String t = "";
        String w = "";

        String c = json.containsKey("confidence") ? json.getString("confidence") : "100";
        if (json.containsKey("new_tid")) {
            w = json.getBoolean("new_tid") ? "3" : "2";
        }
        if (json.containsKey("tid")) {
            t = json.getString("tid");
        }
        System.out.println(c);
        String url = "https://passport.weibo.com/visitor/visitor?a=incarnate&t=" + t + "&w=" + w + "&c=0" + c + "&gc=&cb=cross_domain&from=weibo&_rand=" + Math.random();
        url=url+ "tid=" + t + "__" + c;

        String body = HttpTookit.sendGet(url,null,null);
        System.out.println(body);
        body = body.replaceAll("window.cross_domain && cross_domain\\(", "");
        body = body.replaceAll("\\);", "");

        JSONObject obj = JSONObject.parseObject(body).getJSONObject("data");
        System.out.println(obj.toString());
        String cookie = "YF-Page-G0=" + getYF() + "; SUB=" + obj.getString("sub") + "; SUBP=" + obj.getString("subp");
        System.out.println("cookie:  " + cookie);
        return cookie;

    }
    public String getYF() throws IOException {
        String domain = "1087030002_2975_5012_0";
        String url = "https://d.weibo.com/" + domain;
        HttpTookit.sendGet(url,null,null);
        String str = getCookie();

        return str;

    }
}
