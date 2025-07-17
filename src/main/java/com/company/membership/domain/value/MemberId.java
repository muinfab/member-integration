package com.company.membership.domain.value;

import lombok.Value;

@Value
public class MemberId {
    private final Long value;
    
    public MemberId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        this.value = value;
    }
    
    public static MemberId of(Long value) {
        return new MemberId(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
} 