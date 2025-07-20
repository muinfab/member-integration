# 🔧 **로그인 API 수도코드** (IDMI-REWARDS-001)

## 📋 **개요**
이 문서는 로그인 API의 구현을 위한 상세한 수도코드입니다. 주니어 개발자가 이해하기 쉽도록 각 단계별로 자세히 설명합니다.

## 🎯 **주요 기능**
1. 일반 로그인 (아이디/비밀번호)
2. 간편 로그인 (카카오 등)
3. 회원 상태 확인
4. 로그인 이력 저장
5. 응답 데이터 생성

## 🔧 **수도코드**

### 📝 **1. Controller Layer (컨트롤러 계층)**

```java
/**
 * 로그인 API 엔드포인트
 * URI: /v1/membership/integration/login
 * Method: POST
 */
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    // 1. 요청 로깅
    log.info("로그인 요청: loginId={}, loginType={}", request.getLoginId(), request.getLoginType());
    
    // 2. 서비스 호출
    LoginResponse response = membershipService.login(request);
    
    // 3. 응답 로깅
    log.info("로그인 응답: code={}, memberType={}", response.getCode(), response.getData().getMemberType());
    
    // 4. HTTP 응답 반환
    return ResponseEntity.ok(response);
}
```

### 📝 **2. Service Layer (서비스 계층)**

```java
/**
 * 로그인 처리 메인 로직
 */
public LoginResponse login(LoginRequest request) {
    // 1. 입력값 검증
    validateLoginRequest(request);
    
    // 2. 회원 정보 조회
    Member member = findMemberByLoginId(request.getLoginId());
    
    // 3. 회원 상태 확인
    validateMemberStatus(member);
    
    // 4. 비밀번호 검증 (일반 로그인만)
    if (isGeneralLogin(request.getLoginType())) {
        validatePassword(member, request.getLoginPw());
    }
    
    // 5. 로그인 이력 생성
    createLoginHistory(member, request);
    
    // 6. 응답 생성
    return createLoginResponse(member);
}

/**
 * 입력값 검증
 */
private void validateLoginRequest(LoginRequest request) {
    // 1. 필수 파라미터 검증
    if (StringUtils.isEmpty(request.getLoginId())) {
        throw new ValidationException("로그인 ID는 필수입니다");
    }
    
    // 2. 로그인 타입별 검증
    if (request.getLoginType() == 0) {
        // 일반 로그인
        if (StringUtils.isEmpty(request.getLoginPw())) {
            throw new ValidationException("일반 로그인 시 비밀번호는 필수입니다");
        }
    } else if (request.getLoginType() == 1) {
        // 간편 로그인
        if (StringUtils.isEmpty(request.getChannelId()) || StringUtils.isEmpty(request.getMemberCi())) {
            throw new ValidationException("간편 로그인 시 채널 ID와 CI 정보는 필수입니다");
        }
    } else {
        throw new ValidationException("지원하지 않는 로그인 타입입니다");
    }
}

/**
 * 회원 정보 조회
 */
private Member findMemberByLoginId(String loginId) {
    Optional<Member> memberOpt = memberRepository.findByLoginId(loginId);
    
    if (memberOpt.isEmpty()) {
        throw new MemberNotFoundException("존재하지 않는 회원입니다");
    }
    
    return memberOpt.get();
}

/**
 * 회원 상태 확인
 */
private void validateMemberStatus(Member member) {
    // 1. 회원 상태 확인
    if (!member.canLogin()) {
        throw new MemberStatusException("로그인이 불가능한 회원입니다");
    }
    
    // 2. 계정 잠금 확인
    if (member.isLocked()) {
        throw new AccountLockedException("계정이 잠겨있습니다");
    }
    
    // 3. 탈퇴 확인
    if (member.isWithdrawn()) {
        throw new WithdrawnMemberException("탈퇴한 회원입니다");
    }
}

/**
 * 비밀번호 검증
 */
private void validatePassword(Member member, String plainPassword) {
    if (!member.getPassword().matches(plainPassword)) {
        // 로그인 실패 횟수 증가
        incrementLoginFailureCount(member);
        
        throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    
    // 로그인 성공 시 실패 횟수 초기화
    resetLoginFailureCount(member);
}

/**
 * 로그인 이력 생성
 */
private void createLoginHistory(Member member, LoginRequest request) {
    LoginHistory loginHistory = new LoginHistory();
    loginHistory.setMemberId(member.getId());
    loginHistory.setLoginType(String.valueOf(request.getLoginType()));
    loginHistory.setChannelInfo(request.getChannelId());
    loginHistory.setLoginTime(LocalDateTime.now());
    loginHistory.setIpAddress(getClientIP());
    loginHistory.setUserAgent(getClientUserAgent());
    
    loginHistoryRepository.save(loginHistory);
}

/**
 * 응답 생성
 */
private LoginResponse createLoginResponse(Member member) {
    LoginResponse response = new LoginResponse();
    response.setCode("1000");
    response.setMessage("Success");
    
    LoginResponse.LoginData data = new LoginResponse.LoginData();
    data.setMemberType(member.getMemberTypeAsString());
    data.setIntegrationType(member.getIntegrationTypeAsString());
    data.setLoginId(member.getMembershipId());
    data.setMemberName(member.getMemberName());
    data.setMemberEmail(member.getMemberEmail().getValue());
    data.setMemberMobile(member.getMemberMobile().getValue());
    data.setMemberGender(member.getMemberGender().getCode());
    data.setMemberBirth(member.getMemberBirth().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    data.setEmployeeStatus(member.getEmployeeStatus());
    
    // 웹 회원인 경우 추가 정보
    if (member.isWebMember()) {
        data.setWebInfoId(member.getWebInfoId());
        data.setWebMemberId(member.getWebMemberId());
    }
    
    // 리워즈 회원인 경우 추가 정보
    if (member.isRewardsMember()) {
        data.setRewardsMembershipNo(member.getRewardsMembershipNo());
        data.setRewardsMembershipId(member.getRewardsMembershipId());
    }
    
    // CMS 연동 회원인 경우 추가 정보
    if (member.hasCmsProfile()) {
        data.setCmsProfileId(member.getCmsProfileId());
    }
    
    response.setData(data);
    return response;
}
```

### 📝 **3. Repository Layer (리포지토리 계층)**

```java
/**
 * 로그인 ID로 회원 조회
 */
public Optional<Member> findByLoginId(String loginId) {
    // 1. 통합 회원 테이블에서 조회
    Optional<Member> memberOpt = memberJpaRepository.findByLoginId(loginId);
    
    if (memberOpt.isPresent()) {
        return memberOpt;
    }
    
    // 2. 리워즈 회원 테이블에서 조회 (통합되지 않은 경우)
    Optional<RewardsMember> rewardsMemberOpt = rewardsMemberRepository.findByLoginId(loginId);
    
    if (rewardsMemberOpt.isPresent()) {
        // 리워즈 회원을 통합 회원으로 변환
        Member member = convertRewardsToMember(rewardsMemberOpt.get());
        return Optional.of(member);
    }
    
    return Optional.empty();
}

/**
 * 로그인 이력 저장
 */
public LoginHistory saveLoginHistory(LoginHistory loginHistory) {
    return loginHistoryJpaRepository.save(loginHistory);
}

/**
 * 로그인 실패 횟수 증가
 */
public void incrementLoginFailureCount(Member member) {
    member.incrementLoginFailureCount();
    memberJpaRepository.save(member);
}

/**
 * 로그인 실패 횟수 초기화
 */
public void resetLoginFailureCount(Member member) {
    member.resetLoginFailureCount();
    memberJpaRepository.save(member);
}
```

### 📝 **4. Domain Layer (도메인 계층)**

```java
/**
 * 회원 도메인 - 로그인 가능 여부 확인
 */
public boolean canLogin() {
    // 1. 회원 상태 확인
    if (this.status != MemberStatus.ACTIVE) {
        return false;
    }
    
    // 2. 계정 잠금 확인
    if (this.isLocked()) {
        return false;
    }
    
    // 3. 탈퇴 확인
    if (this.isWithdrawn()) {
        return false;
    }
    
    // 4. 로그인 실패 횟수 확인
    if (this.loginFailureCount >= MAX_LOGIN_FAILURE_COUNT) {
        return false;
    }
    
    return true;
}

/**
 * 비밀번호 검증
 */
public boolean validatePassword(String plainPassword) {
    return this.password.matches(plainPassword);
}

/**
 * 로그인 실패 횟수 증가
 */
public void incrementLoginFailureCount() {
    this.loginFailureCount++;
    
    // 최대 실패 횟수 도달 시 계정 잠금
    if (this.loginFailureCount >= MAX_LOGIN_FAILURE_COUNT) {
        this.lockAccount();
    }
}

/**
 * 로그인 실패 횟수 초기화
 */
public void resetLoginFailureCount() {
    this.loginFailureCount = 0;
    this.unlockAccount();
}
```

## 🔍 **주요 고려사항**

### 🔒 **보안**
1. **비밀번호 암호화**: BCrypt 사용
2. **로그인 시도 제한**: 5회 실패 시 계정 잠금
3. **세션 관리**: JWT 토큰 발급 고려
4. **IP 기반 제한**: 의심스러운 IP 차단

### 📊 **데이터 처리**
1. **회원 상태 분기**: 통합대상, 전환대상, 미대상
2. **로그인 이력**: 모든 로그인 시도 기록
3. **외부 시스템 연동**: CMS, CRS 시스템 고려

### 🚀 **성능**
1. **인덱스 활용**: login_id 컬럼 인덱스
2. **캐싱**: 자주 조회되는 회원 정보 캐싱
3. **비동기 처리**: 로그인 이력 저장은 비동기로 처리

## ⚠️ **에러 처리**

```java
/**
 * 커스텀 예외 클래스들
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}

public class MemberStatusException extends RuntimeException {
    public MemberStatusException(String message) {
        super(message);
    }
}

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
```

## 📝 **테스트 케이스**

### ✅ **성공 케이스**
1. 일반 로그인 성공
2. 간편 로그인 성공
3. 통합 회원 로그인
4. 리워즈 회원 로그인

### ❌ **실패 케이스**
1. 존재하지 않는 회원
2. 잘못된 비밀번호
3. 잠긴 계정
4. 탈퇴한 회원
5. 필수 파라미터 누락
6. 로그인 시도 횟수 초과 