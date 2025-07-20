# 통합 멤버십 API 에러 코드 명세서

## 📋 개요
통합 멤버십 시스템에서 사용되는 표준화된 에러 코드 목록입니다. 모든 API에서 일관된 에러 응답을 제공하기 위해 사용됩니다.

## 🔗 에러 응답 형식
```json
{
  "code": "에러코드",
  "message": "에러 메시지",
  "data": null
}
```

## 📊 에러 코드 목록

### 🔍 입력값 검증 에러 (1xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR001 | 필수 데이터가 없습니다. | 필수로 표시된 항목에 대한 정보가 없을 시 발생 | 400 |

### 👤 인증/인가 에러 (1xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR101 | 가입정보가 없습니다. | 해당 가입자에 대한 정보가 없을 시 발생 | 404 |
| ERR102 | 아이디 또는 비밀번호가 틀렸습니다. | 아이디 혹은 비밀번호가 틀렸을 시 발생 | 401 |
| ERR103 | 계정 잠금 상태입니다. | 해당 계정이 현재 잠금 상태일 때 발생 | 403 |
| ERR104 | 블락 된 회원입니다. | 해당 회원의 정보가 블락처리 되었을 때 발생 | 403 |

### 📝 회원 관리 에러 (2xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR201 | 이미 가입 된 회원입니다. | 해당 정보의 회원이 이미 가입되어 있을 시 발생 | 409 |
| ERR202 | 회원 정보 생성에 실패하였습니다. | 회원 정보 생성에 실패하였을 때 발생 | 500 |
| ERR203 | 전환 회원 대상이 아닙니다. | API호출 시 코드 잘못 보냈을 시 발생 | 400 |
| ERR204 | 해당 아이디는 멤버십에서 사용 중 입니다. | 아이디 중복 시 발생 | 409 |

### 🔐 비밀번호 관리 에러 (3xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR301 | 현재 비밀번호와 일치하지 않습니다. | 비밀번호 변경 시 현재 비밀번호가 다를 시 발생 | 400 |

### 🔄 동기화 에러 (4xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR401 | 동기화에 실패하였습니다. | 정보 동기화 실패 시 발생 | 500 |

### ✅ 유효성 검증 에러 (5xx)
| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR501 | 사용 할 수 없는 아이디입니다. | 아이디 유효성 검증 실패 | 400 |
| ERR502 | 사용 할 수 없는 이메일입니다. | 이메일 유효성 검증 실패 | 400 |
| ERR503 | 약관이 존재하지 않습니다. | 언어코드로 약관 조회시 언어코드에 맞는 약관이 없을때 발생 | 404 |

## 🎯 API별 에러 코드 매핑

### 로그인 API (IDMI-REWARDS-001)
- **ERR001**: 필수 파라미터 누락 (loginId, loginPw, loginType 등)
- **ERR101**: 존재하지 않는 회원
- **ERR102**: 비밀번호 불일치
- **ERR103**: 계정 잠금 상태
- **ERR104**: 블락된 회원

### 회원가입 API
- **ERR001**: 필수 파라미터 누락
- **ERR201**: 이미 가입된 회원
- **ERR202**: 회원 정보 생성 실패
- **ERR204**: 아이디 중복
- **ERR501**: 사용할 수 없는 아이디
- **ERR502**: 사용할 수 없는 이메일

### 비밀번호 변경 API
- **ERR001**: 필수 파라미터 누락
- **ERR101**: 존재하지 않는 회원
- **ERR301**: 현재 비밀번호 불일치

### 회원 정보 동기화 API
- **ERR401**: 동기화 실패

### 약관 조회 API
- **ERR503**: 약관이 존재하지 않음

## 🔧 구현 가이드

### 에러 응답 생성 예시
```java
// Controller에서 에러 응답 생성
@ExceptionHandler(MissingParameterException.class)
public ResponseEntity<ErrorResponse> handleMissingParameter(MissingParameterException e) {
    ErrorResponse error = ErrorResponse.builder()
        .code("ERR001")
        .message("필수 데이터가 없습니다.")
        .data(null)
        .build();
    
    return ResponseEntity.badRequest().body(error);
}
```

### 에러 코드 상수 정의
```java
public class ErrorCode {
    // 입력값 검증 에러
    public static final String REQUIRED_DATA_MISSING = "ERR001";
    
    // 인증/인가 에러
    public static final String MEMBER_NOT_FOUND = "ERR101";
    public static final String INVALID_CREDENTIALS = "ERR102";
    public static final String ACCOUNT_LOCKED = "ERR103";
    public static final String ACCOUNT_BLOCKED = "ERR104";
    
    // 회원 관리 에러
    public static final String MEMBER_ALREADY_EXISTS = "ERR201";
    public static final String MEMBER_CREATION_FAILED = "ERR202";
    public static final String INVALID_TRANSITION_TARGET = "ERR203";
    public static final String ID_ALREADY_IN_USE = "ERR204";
    
    // 비밀번호 관리 에러
    public static final String CURRENT_PASSWORD_MISMATCH = "ERR301";
    
    // 동기화 에러
    public static final String SYNC_FAILED = "ERR401";
    
    // 유효성 검증 에러
    public static final String INVALID_ID = "ERR501";
    public static final String INVALID_EMAIL = "ERR502";
    public static final String TERMS_NOT_FOUND = "ERR503";
}
```

### 에러 메시지 관리
```java
public class ErrorMessage {
    private static final Map<String, String> ERROR_MESSAGES = Map.of(
        "ERR001", "필수 데이터가 없습니다.",
        "ERR101", "가입정보가 없습니다.",
        "ERR102", "아이디 또는 비밀번호가 틀렸습니다.",
        "ERR103", "계정 잠금 상태입니다.",
        "ERR104", "블락 된 회원입니다.",
        "ERR201", "이미 가입 된 회원입니다.",
        "ERR202", "회원 정보 생성에 실패하였습니다.",
        "ERR203", "전환 회원 대상이 아닙니다.",
        "ERR204", "해당 아이디는 멤버십에서 사용 중 입니다.",
        "ERR301", "현재 비밀번호와 일치하지 않습니다.",
        "ERR401", "동기화에 실패하였습니다.",
        "ERR501", "사용 할 수 없는 아이디입니다.",
        "ERR502", "사용 할 수 없는 이메일입니다.",
        "ERR503", "약관이 존재하지 않습니다."
    );
    
    public static String getMessage(String errorCode) {
        return ERROR_MESSAGES.getOrDefault(errorCode, "알 수 없는 오류가 발생했습니다.");
    }
}
```

## 📝 개발자 노트

### 에러 코드 사용 원칙
1. **일관성**: 모든 API에서 동일한 에러 코드 사용
2. **명확성**: 에러 코드와 메시지가 명확하게 의미 전달
3. **확장성**: 새로운 에러 코드 추가 시 기존 체계 유지
4. **국제화**: 향후 다국어 지원을 고려한 구조

### 로깅 전략
- **에러 로그**: 모든 에러 발생 시 상세 로그 기록
- **사용자 정보**: 민감하지 않은 사용자 정보 포함
- **스택 트레이스**: 개발 환경에서만 상세 스택 트레이스 제공
- **모니터링**: 에러 발생 빈도 및 패턴 모니터링

### 보안 고려사항
- **정보 노출**: 민감한 정보가 에러 메시지에 노출되지 않도록 주의
- **에러 추적**: 사용자가 에러를 추적할 수 있는 고유 식별자 제공
- **로그 보안**: 에러 로그에 민감한 정보 기록 금지 