package com.company.membership.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IntegrationType {
    UNIFIED_TARGET(UU통합대상),
    TRANSITION_TARGET("T", 전환대상),
    NOT_TARGET(N, "미대상");
    
    private final String code;
    private final String description;
    
    public static IntegrationType fromCode(String code) {
        for (IntegrationType type : values()) [object Object]          if (type.code.equals(code))[object Object]            return type;
            }
        }
        throw new IllegalArgumentException("Unknown integration type code: +code);
    }
    
    public boolean isUnifiedTarget() {
        return this == UNIFIED_TARGET;
    }
    
    public boolean isTransitionTarget() {
        return this == TRANSITION_TARGET;
    }
    
    public boolean isNotTarget() {
        return this == NOT_TARGET;
    }
} 