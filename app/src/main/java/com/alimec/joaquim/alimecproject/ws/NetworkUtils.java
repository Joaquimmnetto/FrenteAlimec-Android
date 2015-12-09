package com.alimec.joaquim.alimecproject.ws;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by KithLenovo on 15/07/2015.
 */
public class NetworkUtils {

    public static InetAddress getEnderecoBroadcast() {
        try {
            Enumeration<NetworkInterface> interfaces = null;

            interfaces = NetworkInterface.getNetworkInterfaces();


            while (interfaces.hasMoreElements()) {
                NetworkInterface intf = interfaces.nextElement();
                if (!intf.isLoopback()) {
                    List<InterfaceAddress> intfaddrs = intf.getInterfaceAddresses();
                    if(intfaddrs.size() > 0){
                        return intfaddrs.get(0).getBroadcast(); //return first IP address
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


}
