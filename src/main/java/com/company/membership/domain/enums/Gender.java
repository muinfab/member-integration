package com.company.membership.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE(0, "남성"),
    FEMALE(1, "여성"),
    OTHER(2, "기타");
    
    private final Integer code;
    private final String description;
    
    public static Gender fromCode(Integer code) {
        for (Gender gender : values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender code: " + code);
    }
    
    public boolean isMale() {
        return this == MALE;
    }
    
    public boolean isFemale() {
        return this == FEMALE;
    }
    
    public boolean isOther() {
        return this == OTHER;
    }
} 