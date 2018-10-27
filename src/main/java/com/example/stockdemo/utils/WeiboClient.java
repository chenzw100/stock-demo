package com.example.stockdemo.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微博免登陆请求客户端
 *
 * @author fullstackyang
 */

public class WeiboClient {

    private static CookieFetcher cookieFetcher = new CookieFetcher();

    private volatile String cookie;

    public WeiboClient() {
        this.cookie = cookieFetcher.getCookie();
    }

    private static Lock lock = new ReentrantLock();

    public void cookieReset() {
        if (lock.tryLock()) {
            try {
                this.cookie = cookieFetcher.getCookie();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * get方法，获取微博平台的其他页面
     * @param url
     * @return
     */
    public String get(String url) {
        if (StringUtils.isEmpty(url))
            return "";

        HttpGet httpGet = new HttpGet(url);
        Map<String, String> headers = new HashMap();
        headers.put(HttpHeaders.COOKIE, cookie);
        headers.put(HttpHeaders.HOST, "weibo.com");
        headers.put("Upgrade-Insecure-Requests", "1");

        httpGet.setConfig(RequestConfig.custom().setSocketTimeout(3000)
                .setConnectTimeout(3000).setConnectionRequestTimeout(3000).build());

        String html = HttpTookit.sendGet(url,null,headers);
        System.out.printf("===>"+html);
        return  html;
    }

    /**
     * 获取访问微博时必需的Cookie
     */
    static class CookieFetcher {

        //static final String PASSPORT_URL = "https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=http://weibo.com/?category=2&domain=.weibo.com&ua=php-sso_sdk_client-0.6.23";
        static final String PASSPORT_URL ="https://passport.weibo.cn/sso/login";
        static final String GEN_VISITOR_URL = "https://passport.weibo.com/visitor/genvisitor";

        static final String VISITOR_URL = "https://passport.weibo.com/visitor/visitor?a=incarnate";

        private String getCookie() {
            Map<String, String> map;
            map = getCookieParam();
            /*while (true) {
                map = getCookieParam();
                if (map.containsKey("SUB") && map.containsKey("SUBP") &&
                        StringUtils.isNoneEmpty(map.get("SUB"), map.get("SUBP")))
                    break;
                //HttpClientInstance.instance().changeProxy();
            }*/
            return " YF-Page-G0=" + "; _s_tentry=-; SUB=" + map.get("SUB") + "; SUBP=" + map.get("SUBP");
        }

        private Map<String, String> getCookieParam() {
            String time = System.currentTimeMillis() + "";
            time = time.substring(0, 9) + "." + time.substring(9, 13);
            String passporturl = PASSPORT_URL + "&_rand=" + time;

            String tid = "";
            String c = "";
            String w = "";
            {
                String str = postGenvisitor(passporturl);
                str=str.substring(0,str.length()-2);
                if (str.contains("\"retcode\":20000000")) {
                    JSONObject jsonObject = JSONObject.parseObject(str).getJSONObject("data");
                    tid = jsonObject.getString("tid");
                    try {
                        tid = URLEncoder.encode(tid, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    c = jsonObject.containsKey("confidence") ? "000" + jsonObject.getInteger("confidence") : "100";
                    w = jsonObject.getBoolean("new_tid") ? "3" : "2";
                }
            }
            String s = "";
            String sp = "";
            {
                if (StringUtils.isNoneEmpty(tid, w, c)) {
                    String str = getVisitor(tid, w, c, passporturl);
                    str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                    if (str.contains("\"retcode\":20000000")) {
                        //System.out.println(JSONObject.parseObject(str).toString(2));
                        JSONObject jsonObject = JSONObject.parseObject(str).getJSONObject("data");
                        s = jsonObject.getString("sub");
                        sp = jsonObject.getString("subp");
                    }

                }
            }
            Map<String, String> map = new HashMap();
            map.put("SUB", s);
            map.put("SUBP", sp);
            return map;
        }

        private String postGenvisitor(String passporturl)  {

            Map<String, String> headers = new HashMap();
            headers.put(HttpHeaders.ACCEPT, "*/*");
            headers.put(HttpHeaders.ORIGIN, "https://passport.weibo.com");
            headers.put(HttpHeaders.REFERER, passporturl);

            Map<String, String> params = new HashMap();
            params.put("cb", "gen_callback");
            params.put("fp", fp());


            String str =  HttpTookit.sendHttpsPost(GEN_VISITOR_URL,params,headers);

            return str.substring(str.indexOf("(") + 1, str.lastIndexOf(""));
        }

        private String getVisitor(String tid, String w, String c, String passporturl) {
            String url = VISITOR_URL + "&t=" + tid + "&w=" + "&c=" + c.substring(c.length() - 3)
                    + "&gc=&cb=cross_domain&from=weibo&_rand=0." + rand();

            Map<String, String> headers = new HashMap();
            headers.put(HttpHeaders.ACCEPT, "*/*");
            headers.put(HttpHeaders.HOST, "passport.weibo.com");
            headers.put(HttpHeaders.COOKIE, "tid=" + tid + "__0" + c);
            headers.put(HttpHeaders.REFERER, passporturl);

            return HttpTookit.sendHttpsPost(passporturl,null,headers);
        }

        private static String rand() {
            return new BigDecimal(Math.floor(Math.random() * 10000000000000000L)).toString();
        }

        private static String fp() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("os", "1");
            jsonObject.put("browser", "Chrome59,0,3071,115");
            jsonObject.put("fonts", "undefined");
            jsonObject.put("screenInfo", "1680*1050*24");
            jsonObject.put("plugins",
                    "Enables Widevine licenses for playback of HTML audio/video content. (version: 1.4.8.984)::widevinecdmadapter.dll::Widevine Content Decryption Module|Shockwave Flash 26.0 r0::pepflashplayer.dll::Shockwave Flash|::mhjfbmdgcfjbbpaeojofohoefgiehjai::Chrome PDF Viewer|::internal-nacl-plugin::Native Client|Portable Document Format::internal-pdf-viewer::Chrome PDF Viewer");
            return jsonObject.toString();
        }
    }
}
