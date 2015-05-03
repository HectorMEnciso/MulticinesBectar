package com.example.hector.multicinesbectar;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hector on 03/05/2015.
 */
public class Hash {
    private static int NO_OPTIONS=0;
    private String SHAHash;

    public Hash() {
    }

    public String computeSHAHash(String userName,String password) {
        MessageDigest mdShaPass = null;
        String HashUserPass=userName+password;
        try {
            mdShaPass = MessageDigest.getInstance("SHA-1");//define tipo de hasheo
        } catch (NoSuchAlgorithmException e1) {
        }
        try {
            mdShaPass.update(HashUserPass.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
        }
        byte[] dataPass = mdShaPass.digest();
        try {
            SHAHash = convertToHex(dataPass);
        } catch (IOException e) {
        }
        return SHAHash;
    }

    private static String convertToHex(byte[] data) throws java.io.IOException
    {


        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }

}
