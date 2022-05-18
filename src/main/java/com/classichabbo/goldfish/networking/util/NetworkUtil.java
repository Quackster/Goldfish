package com.classichabbo.goldfish.networking.util;

import org.apache.commons.codec.binary.Hex;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
