package com.newland.property.utils.httpUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpsUtils {

    private static final Log logger = LogFactory.getLog(HttpsUtils.class);

    private final static String CHAT_ENCODE = "UTF-8";


    //    public static void main(String[] args) throws IOException {
//        System.out.println(URLDecoder.decode(HttpsUtils.bocAasrequest("https://aas.bocmacau.com/w/rsa/mercapi_ol", "POST", "%7B%22admissionNoticeUrl%22%3A%22%22%2C%22amount%22%3A%22400%22%2C%22businessType%22%3A%224%22%2C%22cashierLanguage%22%3A%22zh_TW%22%2C%22checkInTime%22%3A%22%22%2C%22checkOutTime%22%3A%22%22%2C%22departureTime%22%3A%22%22%2C%22flightNumber%22%3A%22%22%2C%22goodsInfo%22%3A%22%E3%80%90%E8%BD%9F%E7%82%B8%E9%9B%9E+Bomber+Chicken%E3%80%91%E7%82%B8%E9%9B%9E%E4%B8%AD%E7%BF%BC%5E1%7C%22%2C%22groupUserId%22%3A%22%22%2C%22hotelName%22%3A%22%22%2C%22ipAddress%22%3A%22117.136.32.79%22%2C%22mercOrderNo%22%3A%22SN169529323111789846%22%2C%22merchantId%22%3A%22138626080028300%22%2C%22merchantPreferentialCnName%22%3A%22%22%2C%22merchantPreferentialEnName%22%3A%22%22%2C%22merchantSign%22%3A%22TUJ%2Bv4Oo1XAwoyH1EWl%2F%2BX55PDN6xaKCrKmgmG01v8onbrZvyJNcj%2BOlKRBKqQH0V%2BEgPlGEtq3PMxpYGkSYf6OYulKSLocwk967zYDAc3xXYHDTHZ2Oo1KtJZjE9M%2FlZPx%2BlI75oSok6hWJ6U5NcDA7rzFl6ocEPRDZu%2Bz5zqpHKpAqaNRTqZcYDvlqvxqI3BGaHFw8KDCR0mjkpiL2tmvmd0iPQBV2SXHNW17z%2BJcKJ%2Frzoq7C0rJUrTCvmaqt03x4TJalrbzI3Zh6%2BxpbPVQwowZ7EKlPzCQT4n%2FYhuYWboRiviye%2B%2Bp0fhXGDyHu5AILBJFJQoHHZH7iM0NwHg%3D%3D%22%2C%22notifyUrl%22%3A%22%2Fapi%2Fpay%2F666666%2Fboc%2Fcallback%22%2C%22orderDate%22%3A%2220230921%22%2C%22orderTime%22%3A%22184711%22%2C%22originalAmount%22%3A%22400%22%2C%22otherBusinessType%22%3A%22%22%2C%22pageUrl%22%3A%22pages%2Fuser%2Fmoney%2Fsuccess%3ForderNum%3DSN169529323111789846%26token%3DE39AB7903B4947448997BAE729F72034%26channelId%3D666666%26orderId%3D84414%22%2C%22payChannel%22%3A%22MPGS%22%2C%22payParameters%22%3A%22%22%2C%22productCode%22%3A%22MOBILEWEB%22%2C%22productDesc%22%3A%22%22%2C%22referUrl%22%3A%22https%3A%2F%2Fwww.newlandgo.com%22%2C%22requestId%22%3A%22RP169529345773365460%22%2C%22reserved1%22%3A%22666666%22%2C%22reserved2%22%3A%22%22%2C%22reserved3%22%3A%22%22%2C%22service%22%3A%22CreateBocCashier%22%2C%22signType%22%3A%22RSA2%22%2C%22subject%22%3A%22%E8%90%AC%E5%8D%9A%E8%96%88-SN169529323111789846%22%2C%22supplier%22%3A%22%22%2C%22terminalNo%22%3A%2298034499%22%2C%22totalQuantity%22%3A1%2C%22validNumber%22%3A%227500%22%2C%22version%22%3A%222.0%22%2C%22DefaultUI%22%3A%22AUTO%22%7D", "application/json; charset=UTF-8")));
//        System.out.println(URLDecoder.decode(HttpsUtils.request("https://aas.bocmacau.com/w/rsa/mercapi_ol", "POST", "%7B%22admissionNoticeUrl%22%3A%22%22%2C%22amount%22%3A%22400%22%2C%22businessType%22%3A%224%22%2C%22cashierLanguage%22%3A%22zh_TW%22%2C%22checkInTime%22%3A%22%22%2C%22checkOutTime%22%3A%22%22%2C%22departureTime%22%3A%22%22%2C%22flightNumber%22%3A%22%22%2C%22goodsInfo%22%3A%22%E3%80%90%E8%BD%9F%E7%82%B8%E9%9B%9E+Bomber+Chicken%E3%80%91%E7%82%B8%E9%9B%9E%E4%B8%AD%E7%BF%BC%5E1%7C%22%2C%22groupUserId%22%3A%22%22%2C%22hotelName%22%3A%22%22%2C%22ipAddress%22%3A%22117.136.32.79%22%2C%22mercOrderNo%22%3A%22SN169529323111789846%22%2C%22merchantId%22%3A%22138626080028300%22%2C%22merchantPreferentialCnName%22%3A%22%22%2C%22merchantPreferentialEnName%22%3A%22%22%2C%22merchantSign%22%3A%22TUJ%2Bv4Oo1XAwoyH1EWl%2F%2BX55PDN6xaKCrKmgmG01v8onbrZvyJNcj%2BOlKRBKqQH0V%2BEgPlGEtq3PMxpYGkSYf6OYulKSLocwk967zYDAc3xXYHDTHZ2Oo1KtJZjE9M%2FlZPx%2BlI75oSok6hWJ6U5NcDA7rzFl6ocEPRDZu%2Bz5zqpHKpAqaNRTqZcYDvlqvxqI3BGaHFw8KDCR0mjkpiL2tmvmd0iPQBV2SXHNW17z%2BJcKJ%2Frzoq7C0rJUrTCvmaqt03x4TJalrbzI3Zh6%2BxpbPVQwowZ7EKlPzCQT4n%2FYhuYWboRiviye%2B%2Bp0fhXGDyHu5AILBJFJQoHHZH7iM0NwHg%3D%3D%22%2C%22notifyUrl%22%3A%22%2Fapi%2Fpay%2F666666%2Fboc%2Fcallback%22%2C%22orderDate%22%3A%2220230921%22%2C%22orderTime%22%3A%22184711%22%2C%22originalAmount%22%3A%22400%22%2C%22otherBusinessType%22%3A%22%22%2C%22pageUrl%22%3A%22pages%2Fuser%2Fmoney%2Fsuccess%3ForderNum%3DSN169529323111789846%26token%3DE39AB7903B4947448997BAE729F72034%26channelId%3D666666%26orderId%3D84414%22%2C%22payChannel%22%3A%22MPGS%22%2C%22payParameters%22%3A%22%22%2C%22productCode%22%3A%22MOBILEWEB%22%2C%22productDesc%22%3A%22%22%2C%22referUrl%22%3A%22https%3A%2F%2Fwww.newlandgo.com%22%2C%22requestId%22%3A%22RP169529345773365460%22%2C%22reserved1%22%3A%22666666%22%2C%22reserved2%22%3A%22%22%2C%22reserved3%22%3A%22%22%2C%22service%22%3A%22CreateBocCashier%22%2C%22signType%22%3A%22RSA2%22%2C%22subject%22%3A%22%E8%90%AC%E5%8D%9A%E8%96%88-SN169529323111789846%22%2C%22supplier%22%3A%22%22%2C%22terminalNo%22%3A%2298034499%22%2C%22totalQuantity%22%3A1%2C%22validNumber%22%3A%227500%22%2C%22version%22%3A%222.0%22%2C%22DefaultUI%22%3A%22AUTO%22%7D", "application/json; charset=UTF-8")));
//    }
    public static String bocAasrequest(String urlPath, String requestMethod, String data, String contentType) throws IOException {
        return send(urlPath, data, "text/plain; charset=UTF-8");
    }
    public static String bocOldRrequest(String urlPath, String requestMethod, String data, String contentType) throws IOException {
        return send(urlPath, data, contentType);
    }

    public static String send(String url, String data, String header) {
        logger.info("send url=" + url);
        logger.info("send data=" + data);
        StringBuffer sb = new StringBuffer();
        URL myurl = null;
        try {
            myurl = new URL(url);
        } catch (MalformedURLException e1) {
            logger.error("myurl error:" + e1);
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) myurl.openConnection();
            con.setSSLSocketFactory(new TLSSocketConnectionFactory());
            /*用于解决host name wrong问题，重写主机验证方法，如果请求正常可以去掉*/
//            con.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    // TODO Auto-generated method stub
//                    return true;
//                }
//            });

            System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
            System.setProperty("sun.net.client.defaultReadTimeout", "300000");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", header);
//            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            logger.info("start connect");
            con.connect();
            logger.info("end connect");
        } catch (IOException e1) {
            logger.error("HttpsURLConnection error:" + e1);
        }
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            out = new OutputStreamWriter(con.getOutputStream(), CHAT_ENCODE);
            if (StringUtils.isNotEmpty(data)) {
                out.append(data);
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            logger.error("IOException:" + e1);
        } catch (Exception ex) {
            logger.error("HTTPS Exception:" + ex);
            logger.error(ex.getStackTrace());
        }
        try {
            logger.info("responsecode={}" + con.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            sb.append("");
            String s = reader.readLine();
            while (s != null) {
                sb.append(s);
                s = reader.readLine();
            }
            reader.close();
            con.disconnect();
        } catch (IOException ioex) {
            logger.error("https write file error:" + ioex);
        }
        return sb.toString();
    }

    public static String postJson(String url, String data, String contentType, Map<String, String> headers) {
        logger.info("send url=" + url);
        logger.info("send data=" + data);
        StringBuffer sb = new StringBuffer();
        URL myurl = null;
        try {
            myurl = new URL(url);
        } catch (MalformedURLException e1) {
            logger.error("myurl error:" + e1);
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) myurl.openConnection();
            con.setSSLSocketFactory(new TLSSocketConnectionFactory());
            /*用于解决host name wrong问题，重写主机验证方法，如果请求正常可以去掉*/
//            con.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    // TODO Auto-generated method stub
//                    return true;
//                }
//            });

            System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
            System.setProperty("sun.net.client.defaultReadTimeout", "300000");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", contentType);
//            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            logger.info("start connect");

            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            con.connect();
            logger.info("end connect");
        } catch (IOException e1) {
            logger.error("HttpsURLConnection error:" + e1);
        }
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            out = new OutputStreamWriter(con.getOutputStream(), CHAT_ENCODE);
            if (StringUtils.isNotEmpty(data)) {
                out.append(data);
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            logger.error("IOException:" + e1);
        } catch (Exception ex) {
            logger.error("HTTPS Exception:" + ex);
            logger.error(ex.getStackTrace());
        }
        try {
            logger.info("responsecode={}" + con.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            sb.append("");
            String s = reader.readLine();
            while (s != null) {
                sb.append(s);
                s = reader.readLine();
            }
            reader.close();
            con.disconnect();
        } catch (IOException ioex) {
            logger.error("https write file error:" + ioex);
        }
        return sb.toString();
    }

    public static String sendGet(String url, String data, String contentType, Map<String, String> headers) {
        logger.info("send url=" + url);
        logger.info("send data=" + data);
        StringBuffer sb = new StringBuffer();
        URL myurl = null;
        try {
            myurl = new URL(url + "?" + data);
        } catch (MalformedURLException e1) {
            logger.error("myurl error:" + e1);
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) myurl.openConnection();
            con.setSSLSocketFactory(new TLSSocketConnectionFactory());
            /*用于解决host name wrong问题，重写主机验证方法，如果请求正常可以去掉*/
//            con.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    // TODO Auto-generated method stub
//                    return true;
//                }
//            });

            System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
            System.setProperty("sun.net.client.defaultReadTimeout", "300000");
            con.setRequestMethod("GET");
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setUseCaches(false);
//            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", contentType);
//            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            logger.info("start connect");

            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            con.connect();
            logger.info("end connect");
        } catch (IOException e1) {
            logger.error("HttpsURLConnection error:" + e1);
        }
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            out = new OutputStreamWriter(con.getOutputStream(), CHAT_ENCODE);
            if (StringUtils.isNotEmpty(data)) {
                out.append(data);
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            logger.error("IOException:" + e1);
        } catch (Exception ex) {
            logger.error("HTTPS Exception:" + ex);
            logger.error(ex.getStackTrace());
        }
        try {
            logger.info("responsecode={}" + con.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            sb.append("");
            String s = reader.readLine();
            while (s != null) {
                sb.append(s);
                s = reader.readLine();
            }
            reader.close();
            con.disconnect();
        } catch (IOException ioex) {
            logger.error("https write file error:" + ioex);
        }
        return sb.toString();
    }

    public static String request(String urlPath, String requestMethod, String data, String contentType) throws IOException {
        return bocOldRrequest(urlPath,requestMethod,data,contentType);
//        String result = null;
//        URL url = null;
//        HttpsURLConnection httpurlconnection = null;
//        OutputStreamWriter out = null;
//        BufferedReader reader = null;
//        try {
//            url = new URL(urlPath);
//            httpurlconnection = (HttpsURLConnection) url.openConnection();
//            httpurlconnection.setSSLSocketFactory(new TLSSocketFactory());
//            httpurlconnection.setDoInput(true);
//            httpurlconnection.setDoOutput(true);
//
//            httpurlconnection.setRequestMethod(requestMethod);
//            if (StringUtils.isNotEmpty(contentType)) {
//                httpurlconnection.setRequestProperty("Content-Type", contentType);
//            }
//
//            httpurlconnection.setConnectTimeout(60 * 1000);
//
//            httpurlconnection.connect();
//            out = new OutputStreamWriter(httpurlconnection.getOutputStream(), CHAT_ENCODE);
//            if (StringUtils.isNotEmpty(data)) {
//                out.append(data);
//            }
//            out.flush();
//            out.close();
//
//            int code = httpurlconnection.getResponseCode();
//            logger.info("请求返回http code:" + code);
//
//            if (200 == code) {
//                InputStream is = httpurlconnection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(is));
//                String line = "";
//                StringBuilder builder = new StringBuilder();
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//                result = builder.toString();
//                logger.info("请求返回http response:" + result);
//            }
//        } finally {
//            url = null;
//            if (httpurlconnection != null) {
//                httpurlconnection.disconnect();
//            }
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException e) {
//                logger.error("关闭请求流异常:", e);
//            }
//        }
//        return result;
    }

    public static String post(String urlPath, String data, Map<String, String> header) throws IOException {
        String result = null;
        URL url = null;
        HttpsURLConnection httpurlconnection = null;
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            url = new URL(urlPath);
            httpurlconnection = (HttpsURLConnection) url.openConnection();
            httpurlconnection.setSSLSocketFactory(new TLSSocketFactory());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);

            httpurlconnection.setRequestMethod("POST");

            if (null != header && header.size() > 0) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpurlconnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpurlconnection.setConnectTimeout(60 * 1000);

            httpurlconnection.connect();
            out = new OutputStreamWriter(httpurlconnection.getOutputStream(), CHAT_ENCODE);
            out.append(data);
            out.flush();
            out.close();

            int code = httpurlconnection.getResponseCode();
            logger.info("请求返回http code:" + code);

            if (200 == code) {
                InputStream is = httpurlconnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
                logger.info("请求返回http response:" + result);
            }
        } finally {
            url = null;
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("关闭请求银行流异常:", e);
            }
        }
        return result;
    }

    public static String get(String urlPath, Map<String, String> header) throws IOException {
        String result = null;
        URL url = null;
        HttpsURLConnection httpurlconnection = null;
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            url = new URL(urlPath);
            httpurlconnection = (HttpsURLConnection) url.openConnection();
            httpurlconnection.setSSLSocketFactory(new TLSSocketFactory());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);

            httpurlconnection.setRequestMethod("GET");

            if (null != header && header.size() > 0) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpurlconnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpurlconnection.setConnectTimeout(60 * 1000);
            httpurlconnection.connect();
            out = new OutputStreamWriter(httpurlconnection.getOutputStream(), CHAT_ENCODE);
            out.flush();
            out.close();

            int code = httpurlconnection.getResponseCode();
            logger.info("请求返回http code:" + code);

            if (200 == code) {
                InputStream is = httpurlconnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
                logger.info("请求返回http response:" + result);
            }
        } finally {
            url = null;
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("关闭请求银行流异常:", e);
            }
        }
        return result;
    }
    public static String doPost(String url,List<BasicNameValuePair> formParams){
        for (BasicNameValuePair basicNameValuePair : formParams) {
            logger.info(basicNameValuePair.getName()+"="+basicNameValuePair.getValue());
        }
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);

            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(uefEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"UTF-8");
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String send(String url, String data) {
        logger.info("send url=" + url);
        logger.info("send data=" + data);
        StringBuffer sb = new StringBuffer();
        URL myurl = null;
        try {
            myurl = new URL(url);
        } catch (MalformedURLException e1) {
            logger.error("myurl error:" + e1);
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) myurl.openConnection();
            con.setSSLSocketFactory(new TLSSocketConnectionFactory());
            /*用于解决host name wrong问题，重写主机验证方法，如果请求正常可以去掉*/
//            con.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    // TODO Auto-generated method stub
//                    return true;
//                }
//            });

            System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
            System.setProperty("sun.net.client.defaultReadTimeout", "300000");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            logger.info("start connect");
            con.connect();
            logger.info("end connect");
        } catch (IOException e1) {
            logger.error("HttpsURLConnection error:" + e1);
        }
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(con.getOutputStream(), CHAT_ENCODE);
            if (StringUtils.isNotEmpty(data)) {
                out.append(data);
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            logger.error("IOException:" + e1);
        } catch (Exception ex) {
            logger.error("HTTPS Exception:" + ex);
            logger.error(ex.getStackTrace());
        }
        //接收响应
        BufferedReader reader = null;
        try {
            logger.info("responsecode={}" + con.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            sb.append("");
            String s = reader.readLine();
            while (s != null) {
                sb.append(s);
                s = reader.readLine();
            }
            reader.close();
            con.disconnect();
        } catch (IOException ioex) {
            logger.error("https write file error:" + ioex);
        }
        return sb.toString();
    }
}
