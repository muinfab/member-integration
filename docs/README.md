# 🏢 H2O 멤버십 통합 시스템

## 📋 프로젝트 개요

H2O 멤버십 통합 시스템은 기존 Rewards 회원과 Web 회원을 통합 관리하는 시스템입니다. 
1년차 주니어 개발자 2명이 3일 내에 구현해야 하는 프로젝트입니다.

### 🎯 주요 기능
- **회원 통합**: Rewards 회원과 Web 회원의 통합 관리
- **멤버십 관리**: 골드, 플래티넘, 스마트 등급 관리
- **외부 시스템 연동**: CMS, CRS 시스템과의 동기화
- **이메일 인증**: 외국인 전용 이메일 인증 시스템
- **회원 관리**: 회원 정보 조회, 수정, 탈퇴 등

### 🛠️ 기술 스택
- **Backend**: Spring Boot 2.7.x
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Security**: Spring Security (BCrypt)
- **API**: REST API
- **Build Tool**: Maven

## 📁 프로젝트 구조

```
member-integration/
├── docs/                           # 문서
│   ├── sequence-diagrams/          # 시퀀스 다이어그램
│   ├── implementation-guide.md     # 구현 가이드
│   ├── api-pseudo-codes.md         # API Pseudo 코드
│   ├── database-schema.sql         # 데이터베이스 스키마
│   ├── postman-collection.json     # Postman 테스트 컬렉션
│   └── README.md                   # 이 파일
├── src/
│   └── main/
│       ├── java/
│       │   └── com/company/membership/
│       │       ├── api/            # API 계층
│       │       ├── application/    # 서비스 계층
│       │       ├── domain/         # 도메인 계층
│       │       └── infrastructure/ # 인프라 계층
│       └── resources/
│           └── application.yml     # 설정 파일
├── apispec.txt                     # API 명세서
└── pom.xml                         # Maven 설정
```

## 🚀 빠른 시작

### 1. 환경 설정

#### 필수 요구사항
- Java 11 이상
- MySQL 8.0
- Maven 3.6 이상

#### 데이터베이스 설정
```bash
# MySQL 접속
mysql -u root -p

# 데이터베이스 생성 및 스키마 적용
source docs/database-schema.sql
```

#### 애플리케이션 설정
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/h2o_membership
    username: your_username
    password: your_password
```

### 2. 애플리케이션 실행
```bash
# 프로젝트 빌드
mvn clean install

# 애플리케이션 실행
mvn spring-boot:run
```

### 3. API 테스트
```bash
# Postman 컬렉션 import
# docs/postman-collection.json 파일을 Postman에 import

# 또는 curl로 테스트
curl -X GET "http://localhost:8080/v1/membership/integration/check/id?checkInfo=testuser"
```

## 📊 API 목록

### ✅ 구현 완료 (15개)
| 번호 | API 코드 | API명 | 메서드 | URI |
|------|----------|-------|--------|-----|
| 1 | IDMI-REWARDS-004 | 아이디 중복 체크 | GET | `/check/id` |
| 2 | IDMI-REWARDS-005 | 이메일 중복 체크 | GET | `/check/email` |
| 3 | IDMI-REWARDS-003 | 가입 약관 조회 | GET | `/terms` |
| 4 | IDMI-REWARDS-006 | 회원 가입 | POST | `/join` |
| 5 | IDMI-REWARDS-001 | 로그인 | POST | `/login` |
| 6 | IDMI-REWARDS-007 | 비밀번호 확인 | POST | `/update` |
| 7 | IDMI-REWARDS-008 | 회원 정보 수정 | PUT | `/update` |
| 8 | IDMI-REWARDS-009 | 비밀번호 변경 | PUT | `/update/pw` |
| 9 | IDMI-REWARDS-011 | 탈퇴 요청 | POST | `/hub/member/withdrawal/request` |
| 10 | IDMI-REWARDS-022 | 회원 정보 조회 | GET | `/user` |
| 11 | IDMI-REWARDS-002 | 회원 통합 | POST | `/register` |
| 12 | IDMI-REWARDS-012 | 회원 아이디 찾기 | POST | `/find/id` |
| 13 | IDMI-REWARDS-013 | 비밀번호 찾기 | POST | `/find/pw` |
| 14 | IDMI-REWARDS-020 | 이메일 인증 번호 확인 | POST | `/email/certification` |
| 15 | IDMI-REWARDS-021 | 이메일 인증 번호 발송 | GET | `/email/certification` |
| 16 | IDMI-REWARDS-023 | 멤버십 가입 | POST | `/payment` |
| 17 | IDMI-REWARDS-024 | 회원관리 목록 조회 | GET | `/user/list` |
| 18 | IDMI-REWARDS-010 | 공통코드 조회 | GET | `/reason` |

### 🔗 외부 시스템 연동 (추후 구현)
| 번호 | API 코드 | API명 | 설명 |
|------|----------|-------|------|
| 19 | IDMI-REWARDS-014 | 신규 회원 정보 연동 | The Parnas 회원 CMS 연동 |
| 20 | IDMI-REWARDS-015 | 회원 정보 동기화 (H2O > CMS) | H2O에서 CMS로 동기화 |
| 21 | IDMI-REWARDS-016 | 회원 정보 동기화 (CMS > H2O) | CMS에서 H2O로 동기화 |
| 22 | IDMI-REWARDS-017 | The Parnas 추가 가입 | The Parnas 멤버십 추가 가입 |
| 23 | IDMI-REWARDS-018 | 회원 정보 동기화 (H2O > CRS) | H2O에서 CRS로 동기화 |

## 📝 개발 가이드

### 1. 구현 우선순위

#### Day 1: 기본 API (5개)
1. **IDMI-REWARDS-004**: 아이디 중복 체크
2. **IDMI-REWARDS-005**: 이메일 중복 체크
3. **IDMI-REWARDS-003**: 가입 약관 조회
4. **IDMI-REWARDS-006**: 회원 가입
5. **IDMI-REWARDS-001**: 로그인

#### Day 2: 회원 관리 API (5개)
1. **IDMI-REWARDS-007**: 비밀번호 확인
2. **IDMI-REWARDS-008**: 회원 정보 수정
3. **IDMI-REWARDS-009**: 비밀번호 변경
4. **IDMI-REWARDS-011**: 탈퇴 요청
5. **IDMI-REWARDS-022**: 회원 정보 조회

#### Day 3: 고급 기능 API (8개)
1. **IDMI-REWARDS-002**: 회원 통합
2. **IDMI-REWARDS-012**: 회원 아이디 찾기
3. **IDMI-REWARDS-013**: 비밀번호 찾기
4. **IDMI-REWARDS-020**: 이메일 인증 번호 확인
5. **IDMI-REWARDS-021**: 이메일 인증 번호 발송
6. **IDMI-REWARDS-023**: 멤버십 가입
7. **IDMI-REWARDS-024**: 회원관리 목록 조회
8. **IDMI-REWARDS-010**: 공통코드 조회

### 2. 핵심 비즈니스 로직

#### 회원 유형 결정
```java
// 회원 유형 결정 로직
private MemberType determineMemberType(Member member) {
    if (member.getMembershipId() != null && member.getWebMemberId() != null) {
        return MemberType.U; // 통합회원
    } else if (member.getMembershipId() != null) {
        return MemberType.R; // Rewards
    } else {
        return MemberType.W; // Web
    }
}
```

#### 비밀번호 암호화
```java
// BCrypt를 사용한 비밀번호 암호화
@Autowired
private PasswordEncoder passwordEncoder;

public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
}
```

### 3. 에러 처리

#### 표준 에러 코드
```java
public enum ErrorCode {
    SUCCESS("1000", "Success"),
    INVALID_REQUEST("ERR001", "잘못된 요청입니다"),
    MEMBER_NOT_FOUND("ERR002", "회원 정보를 찾을 수 없습니다"),
    DUPLICATE_ID("ERR003", "이미 사용 중인 아이디입니다"),
    DUPLICATE_EMAIL("ERR004", "이미 사용 중인 이메일입니다"),
    INVALID_PASSWORD("ERR005", "비밀번호가 일치하지 않습니다"),
    // ... 기타 에러 코드
}
```

## 🧪 테스트

### 1. 단위 테스트
```bash
# 단위 테스트 실행
mvn test
```

### 2. API 테스트
```bash
# Postman 컬렉션 사용
# docs/postman-collection.json 파일을 Postman에 import하여 테스트
```

### 3. 통합 테스트
```bash
# 통합 테스트 실행
mvn verify
```

## 📊 데이터베이스

### 주요 테이블
- **member**: 회원 정보
- **email_certification**: 이메일 인증
- **membership_payment**: 멤버십 결제
- **terms_agreement**: 약관 동의
- **common_code**: 공통코드
- **login_history**: 로그인 히스토리
- **withdrawal_history**: 탈퇴 히스토리
- **terms_info**: 약관 정보

### 샘플 데이터
데이터베이스 스키마에 기본 샘플 데이터가 포함되어 있습니다:
- 공통코드 (성별, 회원상태, 멤버십유형, 탈퇴사유)
- 약관 정보 (이용약관, 개인정보처리방침, 마케팅동의)
- 테스트 회원 데이터

## 🔧 설정

### 1. 외부 시스템 설정
```yaml
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

### 2. 로깅 설정
```yaml
logging:
  level:
    com.company.membership: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## 🚨 주의사항

### 1. 보안
- 비밀번호는 반드시 BCrypt로 암호화
- 민감한 정보는 로그에 출력하지 않음
- SQL Injection 방지를 위한 PreparedStatement 사용

### 2. 성능
- 데이터베이스 인덱스 최적화
- N+1 문제 방지를 위한 Fetch Join 사용
- 페이지네이션 적용

### 3. 에러 처리
- 모든 API는 표준 에러 코드 사용
- 예외 발생 시 적절한 HTTP 상태 코드 반환
- 클라이언트에게 명확한 에러 메시지 제공

## 📚 참고 자료

### 문서
- [구현 가이드](docs/implementation-guide.md)
- [API Pseudo 코드](docs/api-pseudo-codes.md)
- [데이터베이스 스키마](docs/database-schema.sql)
- [Postman 컬렉션](docs/postman-collection.json)

### 시퀀스 다이어그램
- [회원 가입](docs/sequence-diagrams/006-join-sequence.puml)
- [로그인](docs/sequence-diagrams/001-login-sequence.puml)
- [회원 통합](docs/sequence-diagrams/002-register-sequence.puml)
- [이메일 인증](docs/sequence-diagrams/020-email-certification-verify-sequence.puml)
- [멤버십 가입](docs/sequence-diagrams/023-membership-payment-sequence.puml)

### 외부 링크
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [JPA/Hibernate 가이드](https://hibernate.org/orm/documentation/)
- [MySQL 8.0 레퍼런스](https://dev.mysql.com/doc/refman/8.0/en/)
- [REST API 설계 가이드](https://restfulapi.net/)

## 🤝 기여

### 개발 규칙
1. **코딩 컨벤션**: Google Java Style Guide 준수
2. **커밋 메시지**: 한글로 작성, 명확한 설명
3. **테스트**: 모든 API에 대한 단위 테스트 작성
4. **문서화**: 코드 변경 시 관련 문서 업데이트

### 브랜치 전략
- **main**: 프로덕션 배포용
- **develop**: 개발 통합용
- **feature/**: 기능 개발용
- **hotfix/**: 긴급 수정용

## 📞 문의

프로젝트 관련 문의사항이 있으시면 다음으로 연락주세요:
- **이메일**: dev-team@company.com
- **슬랙**: #h2o-membership-dev

---

**© 2024 Company Name. All rights reserved.** 