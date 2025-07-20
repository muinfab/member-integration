# 🎯 **로그인 API 개발 가이드** (IDMI-REWARDS-001)

## 📋 **개요**
이 문서는 더 파르나스 멤버십 통합 시스템의 로그인 API 개발을 위한 전체적인 가이드입니다. 주니어 개발자가 이해하기 쉽도록 상세히 설명합니다.

## 🎯 **API 목적**
- **기존 웹회원(IWS)**과 **리워즈(멤버십) 회원**을 통합 관리
- 로그인 후 회원 상태에 따른 차별화된 처리
- 일반 로그인과 간편 로그인 지원

## 🔄 **시스템 아키텍처**

### 📊 **전체 시스템 구성**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   클라이언트     │    │   H2O 시스템     │    │   외부 시스템    │
│  (User/IWS)     │◄──►│  (멤버십 통합)    │◄──►│  (CMS, CRS)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🏗️ **레이어 구조**
```
┌─────────────────────────────────────────────────────────────┐
│                    Controller Layer                         │
│              (MembershipController)                         │
├─────────────────────────────────────────────────────────────┤
│                     Service Layer                           │
│               (MembershipService)                           │
├─────────────────────────────────────────────────────────────┤
│                   Repository Layer                          │
│              (MemberRepository)                             │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer                             │
│                   (Member, etc.)                            │
└─────────────────────────────────────────────────────────────┘
```

## 🔍 **핵심 개념**

### 🎯 **회원 유형 (memberType)**
| 코드 | 설명 | 비고 |
|------|------|------|
| **WU** | 통합회원 | Web + Unified (웹 + 통합) |
| **R** | Rewards 회원 | 리워즈 전용 회원 |
| **W** | Web 회원 | 웹 전용 회원 |

### 🔄 **통합 유형 (integrationType)**
| 코드 | 설명 | 처리 방식 |
|------|------|-----------|
| **UU** | 통합대상 | 이미 통합된 회원, 정상 로그인 |
| **T** | 전환대상 | 통합이 필요한 회원, 리워즈 가입 유도 |
| **N** | 미대상 | 통합 대상이 아닌 회원, 로그인 실패 |

### 🔐 **로그인 유형 (loginType)**
| 코드 | 설명 | 필수 파라미터 |
|------|------|---------------|
| **0** | 일반 로그인 | loginId, loginPw |
| **1** | 간편 로그인 | channelId, memberCi |

## 📊 **데이터베이스 구조**

### 🗄️ **주요 테이블**
```sql
-- 통합 회원 테이블
CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    login_id VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255),
    member_type VARCHAR(10) NOT NULL,  -- WU, R, W
    integration_type VARCHAR(10) NOT NULL,  -- UU, T, N
    member_name VARCHAR(100) NOT NULL,
    member_email VARCHAR(100),
    member_mobile VARCHAR(20),
    member_gender INT,
    member_birth DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    login_failure_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 로그인 이력 테이블
CREATE TABLE login_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    login_type VARCHAR(10) NOT NULL,
    channel_info VARCHAR(100),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    FOREIGN KEY (member_id) REFERENCES members(id)
);
```

## 🔧 **개발 단계별 가이드**

### 📝 **1단계: API 명세 이해**
1. **요청/응답 구조 파악**
   - 요청 파라미터의 의미와 제약사항
   - 응답 데이터의 구조와 필드 설명
   - 에러 코드와 메시지

2. **비즈니스 로직 이해**
   - 회원 유형별 처리 방식
   - 로그인 타입별 검증 로직
   - 상태별 분기 처리

### 📝 **2단계: 도메인 모델 설계**
1. **Member 엔티티 설계**
   ```java
   @Entity
   public class Member {
       @Id @GeneratedValue
       private Long id;
       
       @Column(unique = true)
       private String loginId;
       
       @Embedded
       private Password password;
       
       @Enumerated(EnumType.STRING)
       private MemberType memberType;
       
       @Enumerated(EnumType.STRING)
       private IntegrationType integrationType;
       
       // 비즈니스 메서드들
       public boolean canLogin() { ... }
       public boolean validatePassword(String plainPassword) { ... }
   }
   ```

2. **Value Object 설계**
   ```java
   @Embeddable
   public class Password {
       private String hashedPassword;
       
       public boolean matches(String plainPassword) {
           return BCrypt.checkpw(plainPassword, hashedPassword);
       }
   }
   ```

### 📝 **3단계: Repository 구현**
1. **기본 CRUD 메서드**
   ```java
   public interface MemberRepository {
       Optional<Member> findByLoginId(String loginId);
       Member save(Member member);
       void incrementLoginFailureCount(Long memberId);
       void resetLoginFailureCount(Long memberId);
   }
   ```

2. **쿼리 최적화**
   - login_id 컬럼 인덱스 생성
   - 복합 인덱스 고려 (member_type + status)

### 📝 **4단계: Service 로직 구현**
1. **메인 로그인 로직**
   - 입력값 검증
   - 회원 조회 및 상태 확인
   - 비밀번호 검증
   - 로그인 이력 생성

2. **예외 처리**
   - 커스텀 예외 클래스 정의
   - 적절한 HTTP 상태 코드 반환

### 📝 **5단계: Controller 구현**
1. **API 엔드포인트**
   - 요청/응답 DTO 정의
   - 입력값 검증 (@Valid)
   - 로깅 및 모니터링

2. **응답 형식 통일**
   - 공통 응답 구조 사용
   - 에러 응답 표준화

## 🔒 **보안 고려사항**

### 🔐 **인증 보안**
1. **비밀번호 암호화**
   - BCrypt 알고리즘 사용
   - Salt 자동 생성
   - 적절한 해시 강도 설정

2. **로그인 시도 제한**
   - 5회 실패 시 계정 잠금
   - 일정 시간 후 자동 해제
   - 관리자 해제 기능

3. **세션 관리**
   - JWT 토큰 발급
   - 토큰 만료 시간 설정
   - 리프레시 토큰 구현

### 🛡️ **데이터 보안**
1. **개인정보 보호**
   - 민감 정보 암호화 저장
   - 로그에서 개인정보 마스킹
   - 접근 권한 제한

2. **SQL Injection 방지**
   - PreparedStatement 사용
   - 입력값 검증 및 이스케이프

## 🚀 **성능 최적화**

### 📊 **데이터베이스 최적화**
1. **인덱스 전략**
   ```sql
   -- 로그인 조회용 인덱스
   CREATE INDEX idx_members_login_id ON members(login_id);
   
   -- 상태별 조회용 인덱스
   CREATE INDEX idx_members_status ON members(status, member_type);
   ```

2. **쿼리 최적화**
   - 필요한 컬럼만 조회
   - N+1 문제 방지
   - 페이징 처리

### ⚡ **캐싱 전략**
1. **회원 정보 캐싱**
   - Redis를 이용한 회원 정보 캐싱
   - TTL 설정 (30분)
   - 캐시 무효화 전략

2. **로그인 실패 횟수 캐싱**
   - 메모리 캐시 활용
   - 빠른 실패 횟수 체크

## 📝 **테스트 전략**

### 🧪 **단위 테스트**
```java
@Test
public void 로그인_성공_테스트() {
    // Given
    LoginRequest request = new LoginRequest("user123", "password123", 0);
    Member member = createTestMember();
    
    // When
    LoginResponse response = membershipService.login(request);
    
    // Then
    assertThat(response.getCode()).isEqualTo("1000");
    assertThat(response.getData().getMemberType()).isEqualTo("WU");
}
```

### 🔄 **통합 테스트**
1. **API 엔드포인트 테스트**
   - 실제 HTTP 요청/응답 테스트
   - 데이터베이스 연동 테스트

2. **시나리오 테스트**
   - 일반 로그인 → 간편 로그인 전환
   - 로그인 실패 → 계정 잠금
   - 회원 상태별 로그인 처리

## 📊 **모니터링 및 로깅**

### 📈 **성능 모니터링**
1. **응답 시간 측정**
   - API 응답 시간 모니터링
   - 데이터베이스 쿼리 시간 측정

2. **에러율 모니터링**
   - 로그인 실패율 추적
   - 시스템 에러율 모니터링

### 📝 **로깅 전략**
```java
// 요청 로깅
log.info("로그인 요청: loginId={}, loginType={}", request.getLoginId(), request.getLoginType());

// 성공 로깅
log.info("로그인 성공: memberId={}, memberType={}", member.getId(), member.getMemberType());

// 실패 로깅
log.warn("로그인 실패: loginId={}, reason={}", request.getLoginId(), errorMessage);
```

## 🔄 **배포 및 운영**

### 🚀 **배포 전 체크리스트**
- [ ] 단위 테스트 통과
- [ ] 통합 테스트 통과
- [ ] 보안 검토 완료
- [ ] 성능 테스트 완료
- [ ] 문서화 완료

### 📊 **운영 모니터링**
1. **시스템 상태 모니터링**
   - 서버 리소스 사용량
   - 데이터베이스 연결 상태
   - 외부 시스템 연동 상태

2. **비즈니스 지표 모니터링**
   - 일일 로그인 사용자 수
   - 로그인 성공/실패율
   - 회원 유형별 분포

## 📚 **참고 자료**

### 📖 **관련 문서**
- [API 명세서](./001-login-api.md)
- [시퀀스 다이어그램](../sequence-diagrams/001-login-sequence.puml)
- [수도코드](../pseudocode/001-login-pseudocode.md)

### 🔗 **기술 스택**
- **Framework**: Spring Boot 2.7+
- **Database**: MySQL 8.0+
- **Security**: Spring Security + BCrypt
- **Cache**: Redis
- **Monitoring**: Prometheus + Grafana

---

## 💡 **개발 팁**

### 🎯 **주니어 개발자를 위한 조언**
1. **단계별 구현**: 한 번에 모든 기능을 구현하려 하지 말고, 단계별로 구현하세요.
2. **테스트 우선**: 기능 구현 전에 테스트 케이스를 먼저 작성하세요.
3. **로깅 활용**: 디버깅을 위해 적절한 로깅을 추가하세요.
4. **코드 리뷰**: 다른 개발자와 코드 리뷰를 통해 개선점을 찾으세요.

### ⚠️ **주의사항**
1. **보안**: 비밀번호는 절대 평문으로 저장하지 마세요.
2. **성능**: 데이터베이스 쿼리를 최적화하고 인덱스를 적절히 사용하세요.
3. **예외 처리**: 모든 예외 상황을 고려하여 적절히 처리하세요.
4. **문서화**: 코드 변경 시 관련 문서도 함께 업데이트하세요. 