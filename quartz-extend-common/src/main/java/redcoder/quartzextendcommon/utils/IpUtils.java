package redcoder.quartzextendcommon.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author redcoder54
 * @since 1.0.0
 */
@Slf4j
public class IpUtils {

    private static String localIP;

    /**
     * 获取当前机器的ip地址
     */
    public static String getLocalIp() {
        if (localIP != null && !localIP.isEmpty()) {
            return localIP;
        }
        localIP = privateGetLocalIp2();
        return localIP;
    }

    private static String privateGetLocalIp2() {
        String sIP = "";
        try {
            sIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            sIP = "127.0.0.1";
        }
        if ("127.0.0.1".equals(sIP)) {
            InetAddress ip = null;
            try {
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ipTmp = addresses.nextElement();
                        if (ipTmp instanceof Inet4Address
                                && ipTmp.isSiteLocalAddress()
                                && !ipTmp.isLoopbackAddress()
                                && !ipTmp.getHostAddress().contains(":")) {
                            ip = ipTmp;
                        }
                    }
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }

            if (ip != null) {
                sIP = ip.getHostAddress();
            }
        }
        return sIP;
    }
}
