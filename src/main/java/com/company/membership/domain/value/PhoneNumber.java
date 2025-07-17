package com.company.membership.domain.value;

import lombok.Value;
import java.util.regex.Pattern;

@Value
public class PhoneNumber {
    private final String value;
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^010-9-?[0-9]{4}-?[0-9]{4}");
    
    public PhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
        this.value = normalizePhoneNumber(phoneNumber);
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber.replaceAll("-", "")).matches();
    }
    
    private String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("-", "");
    }
    
    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }
    
    public String getFormatted() {
        return value.substring(0, 3) + "-" + value.substring(3, 7) + "-" + value.substring(7);
    }
    
    @Override
    public String toString() {
        return value;
    }
} 