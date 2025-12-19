/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Nitro V 16
 */
public class OTPService {
     private static final String DIGITS = "0123456789";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALID_MINUTES = 10;
    
    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        
        return otp.toString();
    }
    
    public static Date calculateExpiryTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, OTP_VALID_MINUTES);
        return calendar.getTime();
    }
    
    public static boolean isOTPExpired(Date expiryTime) {
        if (expiryTime == null) {
            return true;
        }
        return new Date().after(expiryTime);
    }
    
    public static boolean validateOTP(String enteredOTP, String storedOTP, Date expiryTime) {
        if (enteredOTP == null || storedOTP == null || expiryTime == null) {
            return false;
        }
        
        return enteredOTP.equals(storedOTP) && !isOTPExpired(expiryTime);
    }
}
