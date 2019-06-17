package com.example.android.smartlabapplication.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DELL on 3/16/2019.
 */

public class SecurityModule {

   public  String encrypt(String plainText)
    {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update(plainText.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder encryptedText = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                encryptedText.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            return encryptedText.toString().toLowerCase();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public  boolean validateString(String string)
    {   string = string.toLowerCase();
        String[] blackList =  {"select","insert","update","delete",";","'","remove","table"};
        for(String x:blackList) {
            if(string.contains(x))
                return false;

        }
        return true;
    }


}
