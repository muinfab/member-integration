# 🚀 멤버십 통합 API 구현 가이드

## 📋 프로젝트 개요
- **프로젝트명**: H2O 멤버십 통합 시스템
- **개발 기간**: 3일
- **개발 인원**: 1년차 주니어 개발자 2명
- **기술 스택**: Spring Boot, JPA, MySQL, REST API

## 🎯 목표
- 15개 API 완전 구현
- 외부 시스템 연동 (CMS, CRS)
- 회원 통합 및 관리 기능

## 📁 프로젝트 구조

```
src/main/java/com/company/membership/
├── api/
│   ├── dto/                    # Request/Response DTO
│   ├── exception/              # 예외 처리
│   └── MembershipController.java
├── application/
│   └── MembershipService.java  # 비즈니스 로직
├── domain/                     # 도메인 모델
│   ├── enums/                  # 열거형
│   ├── repository/             # 도메인 레포지토리 인터페이스
│   └── value/                  # 값 객체
└── infrastructure/             # 인프라스트럭처
    ├── repository/             # 실제 레포지토리 구현
    └── client/                 # 외부 시스템 클라이언트
```

## 🔧 개발 환경 설정

### 1. 의존성 추가 (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- BCrypt -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-crypto</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### 2. application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/h2o_membership?useSSL=false&serverTimezone=UTC
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

# 외부 시스템 설정
external:
  cms:
    url: http://cms-server:8080
    timeout: 5000
  crs:
    url: http://crs-server:8080
    timeout: 5000
  email:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

## 📊 데이터베이스 스키마

### 1. 회원 테이블 (member)
```sql
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_type VARCHAR(1) NOT NULL COMMENT 'U:통합회원, R:Rewards, W:Web',
    integration_type VARCHAR(1) NOT NULL COMMENT 'U:통합대상, T:전환대상, N:미대상',
    login_id VARCHAR(50) UNIQUE NOT NULL COMMENT '통합 로그인 ID',
    membership_id VARCHAR(50) COMMENT '멤버십 ID',
    web_member_id VARCHAR(100) COMMENT '웹회원 ID',
    web_info_id BIGINT COMMENT '웹회원 전용 시퀀스',
    cms_profile_id BIGINT COMMENT 'CMS 프로필 ID',
    member_name VARCHAR(100) NOT NULL COMMENT '한글 이름',
    member_first_name VARCHAR(50) COMMENT '영문 이름',
    member_middle_name VARCHAR(50) COMMENT '영문 중간 이름',
    member_last_name VARCHAR(50) COMMENT '영문 성',
    member_mobile VARCHAR(20) NOT NULL COMMENT '휴대전화',
    member_email VARCHAR(100) NOT NULL COMMENT '이메일',
    member_gender INT COMMENT '0:남성, 1:여성, 2:기타',
    member_birth VARCHAR(8) COMMENT '생년월일 YYYYMMDD',
    member_zip_code VARCHAR(10) COMMENT '우편번호',
    member_address1 VARCHAR(200) COMMENT '주소1',
    member_address2 VARCHAR(100) COMMENT '주소2',
    member_join_date VARCHAR(8) COMMENT '가입일 YYYYMMDD',
    employee_status VARCHAR(1) DEFAULT 'N' COMMENT '임직원여부',
    password_hash VARCHAR(255) COMMENT '암호화된 비밀번호',
    member_status VARCHAR(1) DEFAULT '00' COMMENT '00:유효, 1:계정잠금, 2:탈퇴',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_login_id (login_id),
    INDEX idx_membership_id (membership_id),
    INDEX idx_web_member_id (web_member_id),
    INDEX idx_member_email (member_email),
    INDEX idx_member_mobile (member_mobile)
);
```

### 2. 이메일 인증 테이블 (email_certification)
```sql
CREATE TABLE email_certification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    certification_key VARCHAR(6) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, VERIFIED, EXPIRED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP NULL,
    
    INDEX idx_email_key (email, certification_key),
    INDEX idx_status (status)
);
```

### 3. 멤버십 결제 테이블 (membership_payment)
```sql
CREATE TABLE membership_payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    membership_type VARCHAR(1) NOT NULL COMMENT 'G:골드, P:플래티넘, L:스마트',
    payment_bill_no VARCHAR(50) NOT NULL,
    payment_amount INT NOT NULL,
    payment_date VARCHAR(14) NOT NULL COMMENT 'YYYYMMDDhhmmss',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES member(id),
    INDEX idx_member_id (member_id),
    INDEX idx_payment_bill_no (payment_bill_no)
);
```

### 4. 약관 동의 테이블 (terms_agreement)
```sql
CREATE TABLE terms_agreement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    term_info_id VARCHAR(20) NOT NULL,
    agree_yn VARCHAR(1) NOT NULL COMMENT 'Y:동의, N:거부',
    version INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    agree_date VARCHAR(14) NOT NULL COMMENT 'YYYYMMDDhhmmss',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES member(id),
    INDEX idx_member_id (member_id),
    INDEX idx_term_info_id (term_info_id)
);
```

### 5. 공통코드 테이블 (common_code)
```sql
CREATE TABLE common_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_code VARCHAR(20) NOT NULL COMMENT '분류 코드',
    code VARCHAR(20) NOT NULL COMMENT '코드',
    code_name VARCHAR(100) NOT NULL COMMENT '코드명',
    sort_order INT DEFAULT 0,
    use_yn VARCHAR(1) DEFAULT 'Y' COMMENT '사용여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_class_code (class_code, code),
    INDEX idx_class_code (class_code)
);
```

## 🔑 핵심 비즈니스 로직

### 1. 회원 유형 결정 로직
```java
// 회원 유형 결정
private MemberType determineMemberType(Member member) {
    if (member.getMembershipId() != null && member.getWebMemberId() != null) {
        return MemberType.U; // 통합회원
    } else if (member.getMembershipId() != null) {
        return MemberType.R; // Rewards
    } else {
        return MemberType.W; // Web
    }
}

// 통합 유형 결정
private IntegrationType determineIntegrationType(Member member) {
    if (member.getIntegrationType() != null) {
        return member.getIntegrationType();
    }
    
    // 통합 가능 여부 판단
    if (member.getMembershipId() != null && member.getWebMemberId() != null) {
        return IntegrationType.N; // 이미 통합됨
    } else if (member.getMembershipId() != null || member.getWebMemberId() != null) {
        return IntegrationType.U; // 통합 대상
    } else {
        return IntegrationType.N; // 미대상
    }
}
```

### 2. 비밀번호 암호화/검증
```java
@Autowired
private PasswordEncoder passwordEncoder;

// 비밀번호 암호화
public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
}

// 비밀번호 검증
public boolean matchesPassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
}
```

### 3. 이메일 인증번호 생성
```java
// 6자리 랜덤 인증번호 생성
public String generateCertificationKey() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 6; i++) {
        sb.append(random.nextInt(10));
    }
    return sb.toString();
}
```

## 🚨 에러 코드 정의

```java
public enum ErrorCode {
    // 성공
    SUCCESS("1000", "Success"),
    
    // 일반 에러
    INVALID_REQUEST("ERR001", "잘못된 요청입니다"),
    MEMBER_NOT_FOUND("ERR002", "회원 정보를 찾을 수 없습니다"),
    DUPLICATE_ID("ERR003", "이미 사용 중인 아이디입니다"),
    DUPLICATE_EMAIL("ERR004", "이미 사용 중인 이메일입니다"),
    INVALID_PASSWORD("ERR005", "비밀번호가 일치하지 않습니다"),
    
    // 인증 관련
    EMAIL_CERTIFICATION_FAILED("ERR101", "이메일 인증에 실패했습니다"),
    EMAIL_CERTIFICATION_EXPIRED("ERR102", "이메일 인증번호가 만료되었습니다"),
    
    // 멤버십 관련
    MEMBERSHIP_NOT_FOUND("ERR201", "멤버십 정보를 찾을 수 없습니다"),
    INVALID_MEMBERSHIP_TYPE("ERR202", "유효하지 않은 멤버십 유형입니다"),
    
    // 외부 시스템 연동
    CMS_SYNC_FAILED("ERR301", "CMS 동기화에 실패했습니다"),
    CRS_SYNC_FAILED("ERR302", "CRS 동기화에 실패했습니다"),
    
    // 시스템 에러
    INTERNAL_SERVER_ERROR("ERR999", "내부 서버 오류가 발생했습니다");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
```

## 📝 구현 우선순위

### Day 1 (기본 API)
1. **IDMI-REWARDS-004**: 아이디 중복 체크
2. **IDMI-REWARDS-005**: 이메일 중복 체크
3. **IDMI-REWARDS-003**: 가입 약관 조회
4. **IDMI-REWARDS-006**: 회원 가입
5. **IDMI-REWARDS-001**: 로그인

### Day 2 (회원 관리 API)
1. **IDMI-REWARDS-007**: 비밀번호 확인
2. **IDMI-REWARDS-008**: 회원 정보 수정
3. **IDMI-REWARDS-009**: 비밀번호 변경
4. **IDMI-REWARDS-011**: 탈퇴 요청
5. **IDMI-REWARDS-022**: 회원 정보 조회

### Day 3 (고급 기능 API)
1. **IDMI-REWARDS-002**: 회원 통합
2. **IDMI-REWARDS-012**: 회원 아이디 찾기
3. **IDMI-REWARDS-013**: 비밀번호 찾기
4. **IDMI-REWARDS-020**: 이메일 인증 번호 확인
5. **IDMI-REWARDS-021**: 이메일 인증 번호 발송
6. **IDMI-REWARDS-023**: 멤버십 가입
7. **IDMI-REWARDS-024**: 회원관리 목록 조회
8. **IDMI-REWARDS-010**: 공통코드 조회

## 🔗 외부 시스템 연동 (추후 구현)
- **IDMI-REWARDS-014**: 신규 회원 정보 연동
- **IDMI-REWARDS-015**: 회원 정보 동기화 (H2O > CMS)
- **IDMI-REWARDS-016**: 회원 정보 동기화 (CMS > H2O)
- **IDMI-REWARDS-017**: The Parnas 추가 가입
- **IDMI-REWARDS-018**: 회원 정보 동기화 (H2O > CRS)

## 🧪 테스트 전략

### 1. 단위 테스트
- Service 계층 비즈니스 로직 테스트
- Repository 계층 데이터 접근 테스트
- DTO 검증 로직 테스트

### 2. 통합 테스트
- Controller 계층 API 엔드포인트 테스트
- 데이터베이스 연동 테스트
- 외부 시스템 연동 테스트

### 3. API 테스트
- Postman 컬렉션 작성
- 각 API별 요청/응답 테스트
- 에러 케이스 테스트

## 📚 참고 자료
- Spring Boot 공식 문서
- JPA/Hibernate 가이드
- MySQL 8.0 레퍼런스
- REST API 설계 가이드 