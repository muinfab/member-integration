package com.company.membership.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE(00,활성),
    LOCKED(01, 계정잠금"),
    WITHDRAWN(02, ;
    
    private final String code;
    private final String description;
    
    public static MemberStatus fromCode(String code) {
        for (MemberStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown member status code: +code);
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean isLocked() {
        return this == LOCKED;
    }
    
    public boolean isWithdrawn() {
        return this == WITHDRAWN;
    }
    
    public boolean canLogin() {
        return this == ACTIVE;
    }
} 