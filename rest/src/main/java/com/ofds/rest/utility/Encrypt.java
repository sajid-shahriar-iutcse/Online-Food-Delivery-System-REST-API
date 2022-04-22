package com.ofds.rest.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    public static String encryptPassword(String passwordTobeEncrypted){
        String encryptedPassword = null;

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(passwordTobeEncrypted.getBytes());
            byte[] bytes = messageDigest.digest();

            StringBuilder stringBuilder = new StringBuilder();

            for(int i=0;i<bytes.length;i++){
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100,16).substring(1));
            }
            encryptedPassword = stringBuilder.toString();

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
       return encryptedPassword;

    }
}
