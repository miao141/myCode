 
package com.webapp.utils.http;

import com.webapp.utils.string.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class)HttpUtils.class);
    private static CloseableHttpClient client;
    private static PoolingHttpClientConnectionManager cm;
    private static Properties prop;

    private static CloseableHttpClient getClient() {
        if (client == null) {
            return HttpClients.createDefault();
        }
        cm.closeExpiredConnections();
        cm.closeIdleConnections(30, TimeUnit.MINUTES);
        return client;
    }

    public static BuilderGet get(String url) {
        return new BuilderGet(url);
    }

    public static BuilderPost post(String url) {
        return new BuilderPost(url);
    }

    private static List<NameValuePair> getParams(Map<String, String> param) {
        if (param == null) {
            return null;
        }
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : param.keySet()) {
            String value = param.get(key);
            params.add((NameValuePair)new BasicNameValuePair(key, value));
        }
        return params;
    }

    static {
        prop = new Properties();
        InputStream in = HttpUtils.class.getResourceAsStream("/http.properties");
        try {
            if (in != null) {
                prop.load(in);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (Boolean.valueOf(prop.getProperty("httpPool")).booleanValue()) {
            logger.info("\u542f\u7528Http\u8fde\u63a5\u6c60");
            Integer maxTotal = Integer.valueOf(prop.getProperty("maxTotal", "500"));
            Integer reqTimeout = Integer.valueOf(prop.getProperty("requestTimeout", "30000"));
            Integer conTimeout = Integer.valueOf(prop.getProperty("connectTimeout", "30000"));
            Integer socketTimeout = Integer.valueOf(prop.getProperty("socketTimeout", "30000"));
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(maxTotal.intValue());
            cm.setDefaultMaxPerRoute(cm.getMaxTotal());
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(reqTimeout.intValue()).setConnectTimeout(conTimeout.intValue()).setSocketTimeout(socketTimeout.intValue()).build();
            client = HttpClients.custom().setConnectionManager((HttpClientConnectionManager)cm).setDefaultRequestConfig(config).build();
        } else {
            logger.info("\u672a\u542f\u7528Http\u8fde\u63a5\u6c60");
        }
    }

    public static class BuilderGet
    extends Builder {
        public BuilderGet(String url) {
            super(url);
        }

        @Override
        public Builder send() {
            try {
                URIBuilder uri = new URIBuilder(this.url);
                uri.addParameters(HttpUtils.getParams(this.param));
                HttpGet get = new HttpGet(uri.build());
                this.setHeader((HttpUriRequest)get);
                this.resp = HttpUtils.getClient().execute((HttpUriRequest)get, this.getContext());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), (Throwable)e);
            }
            return this;
        }
    }

    public static class BuilderPost
    extends Builder {
        public BuilderPost(String url) {
            super(url);
        }

        @Override
        public Builder send() {
            HttpPost post = new HttpPost(this.url);
            this.setHeader((HttpUriRequest)post);
            try {
                post.setEntity((HttpEntity)new UrlEncodedFormEntity(HttpUtils.getParams(this.param), Utils.Charsets.uft8));
                this.resp = HttpUtils.getClient().execute((HttpUriRequest)post, this.getContext());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), (Throwable)e);
            }
            return this;
        }
    }

    public static abstract class Builder {
        CloseableHttpResponse resp;
        CookieStore cookieStore;
        String url;
        Map<String, String> param = new HashMap<String, String>();
        Map<String, String> header = new HashMap<String, String>();

        public Builder(String url) {
            this.url = url;
        }

        public Builder addParam(String key, String val) {
            this.param.put(key, val);
            return this;
        }

        public Builder addParam(Map<String, String> param) {
            this.param.putAll(param);
            return this;
        }

        public Builder addHeader(String key, String val) {
            this.header.put(key, val);
            return this;
        }

        public Builder addHeader(Map<String, String> param) {
            this.header.putAll(param);
            return this;
        }

        public abstract Builder send();

        public CloseableHttpResponse response() {
            return this.resp;
        }

        public List<Cookie> getCookies() {
            return this.cookieStore.getCookies();
        }

        public Cookie getCookie(String name) {
            List<Cookie> cookies = this.getCookies();
            for (Cookie cookie : cookies) {
                if (!cookie.getName().equals(name)) continue;
                return cookie;
            }
            return null;
        }

        public Header[] getHeaders(String name) {
            return this.resp.getHeaders(name);
        }

        public Header getHeader(String name) {
            Header[] headers;
            for (Header header : headers = this.resp.getHeaders(name)) {
                String key = header.getName();
                if (!key.equals(name)) continue;
                return header;
            }
            return null;
        }

        public String getBody() {
            if (this.resp == null) {
                return null;
            }
            HttpEntity entity = this.resp.getEntity();
            try {
                return EntityUtils.toString((HttpEntity)entity, (String)Utils.Charsets.uft8);
            }
            catch (IOException e) {
                logger.error(e.getMessage(), (Throwable)e);
                return null;
            }
        }

        public void close() {
            if (this.resp != null && client == null) {
                try {
                    this.resp.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void setHeader(HttpUriRequest request) {
            for (String key : this.header.keySet()) {
                request.addHeader(key, this.header.get(key));
            }
        }

        protected HttpContext getContext() {
            this.cookieStore = new BasicCookieStore();
            BasicHttpContext context = new BasicHttpContext();
            context.setAttribute("http.cookie-store", (Object)this.cookieStore);
            return context;
        }
    }

}
