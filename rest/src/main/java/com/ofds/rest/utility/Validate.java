package com.ofds.rest.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static boolean isValidEmail(String email){
        String regex = "^(.+)@(.+)$";
        //Compile Pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
