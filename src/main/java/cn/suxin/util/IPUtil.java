package cn.suxin.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


public class IPUtil {

    public static List<String> getServerIps(boolean incluedOuter) throws Exception {
        List<String> list = new ArrayList<String>();
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        for (; netInterfaces.hasMoreElements(); ) {
            NetworkInterface netInterface = netInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
            for (; inetAddresses.hasMoreElements(); ) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    String localIp = inetAddress.getHostAddress();
                    if (localIp.equals("127.0.0.1")) {
                        continue;
                    }
                    if (incluedOuter || isInnerIp(localIp)) {
                        list.add(localIp);
                    }
                }
            }
        }
        return list;
    }

    private static Pattern p = Pattern.compile("^(10\\.|172\\.(1[6-9]|2[0-9]|3[01])\\.|192\\.168\\.)");

    private static boolean isInnerIp(String ip) {
        return p.matcher(ip).find();
    }

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获取本服务器ip
     */
    public static String loadServerIp() {

        try {
            List<String> ips = IPUtil.getServerIps(false);
            if (ips.size() > 0) {
                return ips.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(getServerIps(true));
        System.out.println("==============================");
        System.out.println(getServerIps(false));
    }
}
