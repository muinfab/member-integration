package com.company.membership.domain.value;

import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.regex.Pattern;

@Value
public class Password {
    private final String hashedPassword;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    
    private Password(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    
    public static Password of(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!isValidPassword(plainPassword)) {
            throw new IllegalArgumentException("Password must be at least8haracters long and contain letters, numbers, and special characters");
        }
        return new Password(encoder.encode(plainPassword));
    }
    
    public static Password fromHashed(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedPassword);
    }
    
    private static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    public boolean matches(String plainPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
    
    public String getHashedValue() {
        return hashedPassword;
    }
    
    @Override
    public String toString() {
        return "***"; // 보안상 해시값을 직접 노출하지 않음
    }
} 