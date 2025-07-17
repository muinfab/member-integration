package com.company.membership.domain.value;

import lombok.Value;
import java.util.regex.Pattern;

@Value
public class Email {
    private final String value;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
       ^a-zA-Z0-9._%+-]+@a-zA-Z0-9.-]+\\.[a-zA-Z]{2$"
    );
    
    public Email(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.value = email.trim().toLowerCase();
    }
    
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static Email of(String email) {
        return new Email(email);
    }
    
    public String getDomain() {
        return value.substring(value.indexOf("@") + 1;
    }
    
    @Override
    public String toString() {
        return value;
    }
} 