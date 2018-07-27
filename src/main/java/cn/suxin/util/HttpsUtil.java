package cn.suxin.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import javax.net.ssl.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * <p>
 * Title: HttpUtil工具类，增加自动重试功能，并且将上层堆栈信息打印
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * <p>
 * Company: www.netease.com
 * </p>
 * 
 * @author Barney Woo
 * @date 2012-08-16
 * @version 1.0
 */

public class HttpsUtil {
    private static final Log logger = LogFactory.getLog(HttpsUtil.class);
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String sendRequest(String url, int connectTimeout, int readTimeout, String charset,
            boolean returnSingle) throws RemoteInvocationFailureException {
        return (String) invoke("sendRequest", new Object[] { url, connectTimeout, readTimeout, charset, returnSingle });
    }

    public static String sendRequest(String url, boolean returnSingle) throws RemoteInvocationFailureException {
        return (String) invoke("sendRequest", new Object[] { url, returnSingle });
    }

    public static byte[] sendRequest(String url, int connectTimeout, int readTimeout)
            throws RemoteInvocationFailureException {
        return (byte[]) invoke("sendRequest", new Object[] { url, connectTimeout, readTimeout });
    }

    public static byte[] sendRequest(String url) throws RemoteInvocationFailureException {
        return (byte[]) invoke("sendRequest", new Object[] { url });
    }

    public static String sendPostRequest(String url, String content, String charset) {
        return (String) invoke("sendPostRequest", new Object[] { url, content, charset });
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout,
            int readTimeout) {
        return (String) invoke("sendPostRequest", new Object[] { url, content, charset, connectTimeout, readTimeout });
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout,
            int readTimeout, boolean needCompress) throws RemoteInvocationFailureException {
        return (String) invoke("sendPostRequest",
                new Object[] { url, content, charset, connectTimeout, readTimeout, needCompress });
    }

    public static String sendPostRequest(String url, String content, String inCharset, String outCharset,
            int connectTimeout, int readTimeout, boolean needCompress) throws RemoteInvocationFailureException {
        return (String) invoke("sendPostRequest",
                new Object[] { url, content, inCharset, outCharset, connectTimeout, readTimeout, needCompress });
    }

    public static String sendPostRequest(String url, String content, String charset, boolean needCompress) {
        return (String) invoke("sendPostRequest", new Object[] { url, content, charset, needCompress });
    }

    private static Object invoke(String methodName, Object[] parameters) {
        Object result = null;

        int retryTimes = 5;
        boolean success = false;
        while (!success && retryTimes >= 0) {
            try {
                result = locateMethod(methodName, parameters).invoke(HttpsUtilOrigin.class, parameters);
                if (null == result) {
                    logger.debug("Null http response received:");
                    logger.debug(parameters[0]);
                    return null;
                }

                success = true;

                // retryTimes = 0;
                // throw new RuntimeException("i am an error");

            } catch (Exception e) {
                retryTimes--;

                if (retryTimes < 0) {
                    // errorStack = getErrorStack(e);
                    // errorMsg = e.getMessage();
                }
            }
        }
        if (retryTimes < 0) {
            // LoggerUtil.inOutInfo("Maximum retry times is over while request url : " + parameters[0]);

            // LoggerUtil.alarmInfoStrategy10m10("[通用模块][紧急][网络IO异常 : " + errorMsg + ", 调用堆栈 : " +
            // locateInvokerStack(errorStack));

            // throw new RuntimeException("Maximum retry times is over while request url : " + parameters[0]);
        }

        return result;
    }

    static Pattern pattern = Pattern.compile("at [^\\)]+\\)");
    // private static String locateInvokerStack(String errorStack)
    // {
    // String result = null;
    // try {
    // errorStack = errorStack.substring(errorStack.lastIndexOf(".HttpUtil."));
    // Matcher matcher = pattern.matcher(errorStack);
    // if (matcher.find())
    // {
    // result = matcher.group();
    // }
    // return result;
    // } catch (Exception e) {
    // return errorStack;
    // }
    // }
    //
    // private static String getErrorStack(Exception e)
    // {
    // String errorStack = null;
    //
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // PrintStream ps = new PrintStream(out);
    // e.printStackTrace(ps);
    // errorStack = new String(out.toByteArray());
    //
    // return errorStack;
    // }

    protected static Method locateMethod(String methodName, Object[] parameters) {
        debug("locate method : " + methodName + ", and parameters : ");
        for (Object obj : parameters) {
            debug(String.valueOf(obj));
        }

        Method result = null;

        Method[] methods = HttpsUtilOrigin.class.getMethods();

        debug("method size : " + methods.length);

        for (Method method : methods) {
            debug("try method : " + method.toGenericString());

            if (!methodName.equals(method.getName())) {
                continue;
            }

            Class<?>[] parameterClassList = method.getParameterTypes();
            boolean isMatch = true;
            if (parameterClassList.length == parameters.length) {
                debug("parameter size match");

                for (int i = 0, n = parameters.length; i < n; i++) {
                    Object parameter = parameters[i];
                    Class<?> clazz = parameterClassList[i];

                    if ("int".equals(clazz.getCanonicalName())) {
                        clazz = Integer.class;
                    } else if ("boolean".equals(clazz.getCanonicalName())) {
                        clazz = Boolean.class;
                    }

                    debug("parameter : " + parameter + " with class type : " + parameter.getClass() + ", class : "
                            + clazz.getCanonicalName());
                    debug(String.valueOf(clazz.isInstance(parameter)));

                    if (parameter != null && !clazz.isInstance(parameter)) {
                        debug("parameter class type do not match");

                        isMatch = false;
                        break;
                    }
                }
            } else {
                debug("parameter size do not match");

                isMatch = false;
            }
            if (isMatch) {
                debug("congratulations");

                result = method;
                break;
            }
        }

        return result;
    }

    private static void debug(String msg) {
        // LoggerUtil.debug(msg);
    }

}

class HttpsUtilOrigin {
    // public Log logger = LogFactory.getLog(getClass());

    public static final int DEFAULT_CONNECT_TIME_OUT = 30000;

    public static final int DEFAULT_READ_TIME_OUT = 30000;

    public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String THREADLOCAL_SPANID= "spanid";     //spanId

    public static void trustAll() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendRequest(String url, int connectTimeout, int readTimeout, String charset,
            boolean returnSingle) throws RemoteInvocationFailureException {

        trustAll();
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            if (StringUtils.isEmpty(charset)) {
                charset = DEFAULT_CHARSET;
            }
            conn = getURLConnection(url, connectTimeout, readTimeout);
            
            in = new BufferedReader(new InputStreamReader(connect(conn), charset));
            String result = getReturnResult(in, returnSingle);
            if (StringUtils.isEmpty(result)) {
                throw new RemoteInvocationFailureException("sendRequestError " + url);
            }
            return result;
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                // logger.error("", e);
            } 
        }
    }

    public static String sendRequest(String url, boolean returnSingle) throws RemoteInvocationFailureException {
        return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT, DEFAULT_CHARSET, returnSingle);
    }

    public static byte[] sendRequest(String url, int connectTimeout, int readTimeout)
            throws RemoteInvocationFailureException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            trustAll();
            conn = getURLConnection(url, connectTimeout, readTimeout);
            is = connect(conn);

            byte[] result = null;

            // you can choose implementation from solution 1 and solution 2 in the following

            // solution 1: use read
            // List<Byte> byteList = new ArrayList<Byte>();
            // byte[] byteBuffer = new byte[512];
            // int readLength = -1;
            // while ((readLength = is.read(byteBuffer)) != -1)
            // {
            // for (int i = 0; i < readLength; i++)
            // {
            // byteList.add(byteBuffer[i]);
            // }
            // }
            // int len = byteList.size();
            // result = new byte[len];
            // for (int i = 0; i < len; i++)
            // {
            // result[i] = byteList.get(i);
            // }

            // solution 2: use Reader.ready()
            // String defaultCharSet = "ISO-8859-1";
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, DEFAULT_CHARSET));
            while (!reader.ready()) {
            }
            StringBuffer buf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append("\r\n").append(line);
            }
            buf.delete(0, 2);
            result = buf.toString().getBytes(DEFAULT_CHARSET);

            // int size = is.available();
            // byte[] result = new byte[size];
            // //logger.debug("result length:" + size);
            // is.read(result, 0, size);

            return result;
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + url, e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                // logger.error("", e);
            } 
        }
    }

    public static byte[] sendRequest(String url) throws RemoteInvocationFailureException {
        return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT);
    }

    public static String sendPostRequest(String url, String content, String charset) {
        return sendPostRequest(url, content, charset, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT);
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout,
            int readTimeout) throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset, connectTimeout, readTimeout, false);
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout,
            int readTimeout, boolean needCompress) throws RemoteInvocationFailureException {
        trustAll();
        BufferedReader in = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = getURLConnection(url, connectTimeout, readTimeout);
            
            if (StringUtils.isEmpty(charset)) {
                charset = DEFAULT_CHARSET;
            }
            // logger.debug("请求发送地址:" + url);
            // logger.debug("参数:" + content);
            InputStream stream = postConnect(httpConn, content, charset, needCompress);

            in = new BufferedReader(new InputStreamReader(stream, charset));
            String result = getReturnResult(in, false);
            // logger.debug("请求返回结果:" + result);
            if (StringUtils.isEmpty(result)) {
                throw new RemoteInvocationFailureException("sendRequestError " + url);
            }
            return result;
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                // logger.error("", e);
            } 
        }
    }

    public static String sendPostRequest(String url, String content, String inCharset, String outCharset,
            int connectTimeout, int readTimeout, boolean needCompress) throws RemoteInvocationFailureException {
        BufferedReader in = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = getURLConnection(url, connectTimeout, readTimeout);
            if (StringUtils.isEmpty(inCharset)) {
                inCharset = DEFAULT_CHARSET;
            }
            // logger.debug("请求发送地址:" + url);
            // logger.debug("参数:" + content);
            InputStream stream = postConnect(httpConn, content, inCharset, needCompress);

            in = new BufferedReader(new InputStreamReader(stream, outCharset));
            String result = getReturnResult(in, false);
            // logger.debug("请求返回结果:" + result);
            if (StringUtils.isEmpty(result)) {
                throw new RemoteInvocationFailureException("sendRequestError " + url);
            }
            return result;
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                // logger.error("", e);
            } 
        }
    }

    public static String sendPostRequest(String url, String content, String charset, boolean needCompress)
            throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT, needCompress);
    }

    private static InputStream postConnect(HttpURLConnection httpConn, String content, String charset,
            boolean needCompress) {
        String urlStr = httpConn.getURL().toString();
        try {
            if (StringUtils.isEmpty(charset)) {
                charset = DEFAULT_CHARSET;
            }
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,
            // 默认情况下是false;
            httpConn.setDoOutput(true);
            // Post 请求不能使用缓存
            httpConn.setUseCaches(false);
            // 设定请求的方法为"POST"，默认是GET
            httpConn.setRequestMethod("POST");
            if (needCompress) {
                sendCompressRequest(content, charset, httpConn);
            } else {
                sendNoCompressRequest(content, charset, httpConn);
            }
            // 接收数据
            if (needCompress) {
                return new GZIPInputStream(httpConn.getInputStream());
            }
            return httpConn.getInputStream();
        } catch (MalformedURLException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + urlStr, e);
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + urlStr, e);
        }
    }

    private static void sendCompressRequest(String content, String charset, HttpURLConnection httpConn) {
        GZIPOutputStream out = null;
        try {
            httpConn.setRequestProperty("Content-Type", "application/x-gzip");
            httpConn.setRequestProperty("Accept", "application/x-gzip");
            out = new GZIPOutputStream(httpConn.getOutputStream());
            out.write(content.getBytes("GBK"));
            out.flush();
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + httpConn.getURL().toString(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 发送原始消息
     * 
     * @param content
     * @param charset
     * @param httpConn
     */
    private static void sendNoCompressRequest(String content, String charset, HttpURLConnection httpConn) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), charset));
            out.write(content);
            out.flush();
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + httpConn.getURL().toString(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 建立远程连接
     * 
     * @param urlStr
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    private static InputStream connect(HttpURLConnection httpConn) {
        String urlStr = httpConn.getURL().toString();
        try {
            if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // logger.error(urlStr + "|ResponseCode=" + httpConn.getResponseCode());
                throw new RemoteInvocationFailureException(
                        "sendRequestError " + urlStr + ", result:" + httpConn.getResponseCode());
            }
            return httpConn.getInputStream();
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + urlStr, e);
        }
    }

    /**
     * 构造URLConnnection
     * 
     * @param urlStr
     * @param connectTimeout
     * @param readTimeout
     * @return
     * @throws RemoteInvocationFailureException
     */
    private static HttpURLConnection getURLConnection(String urlStr, int connectTimeout, int readTimeout)
            throws RemoteInvocationFailureException {
        // logger.debug("请求URL:" + urlStr);
        try {
            URL remoteUrl = new URL(urlStr);
            javax.net.ssl.HttpsURLConnection httpConn = (javax.net.ssl.HttpsURLConnection) remoteUrl.openConnection();

            httpConn.setHostnameVerifier(new TrustAnyHostnameVerifier());

            httpConn.setConnectTimeout(connectTimeout);
            httpConn.setReadTimeout(readTimeout);
            return httpConn;
        } catch (MalformedURLException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + urlStr, e);
        } catch (IOException e) {
            // logger.error("", e);
            throw new RemoteInvocationFailureException("sendRequestError " + urlStr, e);
        }
    }

    private static String getReturnResult(BufferedReader in, boolean returnSingleLine) throws IOException {
        if (returnSingleLine) {
            return in.readLine();
        } else {
            StringBuffer sb = new StringBuffer();
            String result = "";
            while ((result = in.readLine()) != null) {
                sb.append(StringUtils.trimWhitespace(result));
            }
            return sb.toString();
        }
    }

}

class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        // 直接返回true
        // LoggerUtil.inOutInfo("Warning: URL Host: "+hostname+" vs. ssl "+session.getPeerHost());
        return true;
    }
}
