package com.classichabbo.goldfish.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class NetworkUtil {
    public static String getUniqueIdentifier() {
        String id = null;

        try {
            id = StringUtil.createHash(new String(getAllAvailableInterfaces().get(0).getHardwareAddress()));
        } catch (SocketException e) {
            id = StringUtil.createHash(UUID.randomUUID().toString());
        }
        
        id = id.replace("-", "");
        id = id.toUpperCase();

        return id;
    }

    public static List<NetworkInterface> getAllAvailableInterfaces() throws SocketException {
        var objList = new ArrayList<NetworkInterface>();
        NetworkInterface obj;

        for(Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            try {
                obj = (NetworkInterface) en.nextElement();

                if (obj.getHardwareAddress() != null)
                    objList.add(obj);
            } catch (Exception e) {

            }
        }
        return objList;
    }
}
