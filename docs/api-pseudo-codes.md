# 📝 API Pseudo Codes

## 🔐 Day 1: 기본 API

### 1. IDMI-REWARDS-004: 아이디 중복 체크

```java
// Controller
@GetMapping("/check/id")
public ResponseEntity<CheckIdResponse> checkId(@RequestParam String checkInfo) {
    CheckIdResponse response = membershipService.checkId(checkInfo);
    return ResponseEntity.ok(response);
}

// Service
public CheckIdResponse checkId(String checkInfo) {
    // 1. 입력값 검증
    if (StringUtils.isEmpty(checkInfo)) {
        throw new IllegalArgumentException("아이디는 필수입니다");
    }
    
    // 2. 아이디 형식 검증 (영문, 숫자, 4-20자)
    if (!checkInfo.matches("^[a-zA-Z0-9]{4,20}$")) {
        throw new IllegalArgumentException("아이디는 영문, 숫자 4-20자로 입력해주세요");
    }
    
    // 3. 데이터베이스에서 중복 체크
    boolean isDuplicate = memberRepository.existsByLoginId(checkInfo) ||
                         memberRepository.existsByMembershipId(checkInfo) ||
                         memberRepository.existsByWebMemberId(checkInfo);
    
    // 4. 응답 생성
    return CheckIdResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(isDuplicate ? "DUPLICATE" : "AVAILABLE")
        .build();
}

// Repository
@Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.loginId = :loginId")
boolean existsByLoginId(@Param("loginId") String loginId);

@Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.membershipId = :membershipId")
boolean existsByMembershipId(@Param("membershipId") String membershipId);

@Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.webMemberId = :webMemberId")
boolean existsByWebMemberId(@Param("webMemberId") String webMemberId);
```

### 2. IDMI-REWARDS-005: 이메일 중복 체크

```java
// Controller
@GetMapping("/check/email")
public ResponseEntity<CheckEmailResponse> checkEmail(@RequestParam String checkInfo) {
    CheckEmailResponse response = membershipService.checkEmail(checkInfo);
    return ResponseEntity.ok(response);
}

// Service
public CheckEmailResponse checkEmail(String checkInfo) {
    // 1. 입력값 검증
    if (StringUtils.isEmpty(checkInfo)) {
        throw new IllegalArgumentException("이메일은 필수입니다");
    }
    
    // 2. 이메일 형식 검증
    if (!isValidEmail(checkInfo)) {
        throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다");
    }
    
    // 3. 데이터베이스에서 중복 체크
    boolean isDuplicate = memberRepository.existsByMemberEmail(checkInfo);
    
    // 4. 응답 생성
    return CheckEmailResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(isDuplicate ? "DUPLICATE" : "AVAILABLE")
        .build();
}

// 이메일 형식 검증
private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
}
```

### 3. IDMI-REWARDS-003: 가입 약관 조회

```java
// Controller
@GetMapping("/terms")
public ResponseEntity<TermsResponse> getTerms(@RequestParam String language) {
    TermsResponse response = membershipService.getTerms(language);
    return ResponseEntity.ok(response);
}

// Service
public TermsResponse getTerms(String language) {
    // 1. 언어 설정 (ko, en, jp, ch)
    String targetLanguage = determineLanguage(language);
    
    // 2. 약관 정보 조회
    List<TermInfo> termInfos = termsRepository.findByLanguageAndUseYn(targetLanguage, "Y");
    
    // 3. 응답 생성
    return TermsResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .termInfos(termInfos)
        .build();
}

// 언어 결정 로직
private String determineLanguage(String language) {
    if ("ko".equals(language)) return "ko";
    if ("en".equals(language)) return "en";
    if ("jp".equals(language)) return "jp";
    if ("ch".equals(language)) return "ch";
    return "ko"; // 기본값
}
```

### 4. IDMI-REWARDS-006: 회원 가입

```java
// Controller
@PostMapping("/join")
public ResponseEntity<JoinResponse> join(@Valid @RequestBody JoinRequest request) {
    JoinResponse response = membershipService.join(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

// Service
@Transactional
public JoinResponse join(JoinRequest request) {
    // 1. 입력값 검증
    validateJoinRequest(request);
    
    // 2. 중복 체크
    checkDuplicates(request);
    
    // 3. 회원 엔티티 생성
    Member member = createMemberFromRequest(request);
    
    // 4. 비밀번호 암호화
    member.setPasswordHash(passwordEncoder.encode(request.getLoginPw()));
    
    // 5. 회원 저장
    Member savedMember = memberRepository.save(member);
    
    // 6. 약관 동의 정보 저장
    saveTermsAgreements(savedMember.getId(), request.getTermInfos());
    
    // 7. CMS 동기화 (The Parnas 회원인 경우)
    if (isParnasMember(savedMember)) {
        syncToCms(savedMember);
    }
    
    // 8. 응답 생성
    return JoinResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(createMemberInfo(savedMember))
        .build();
}

// 회원 생성
private Member createMemberFromRequest(JoinRequest request) {
    return Member.builder()
        .memberType(MemberType.W) // 웹회원으로 가입
        .integrationType(IntegrationType.U) // 통합 대상
        .loginId(request.getLoginId())
        .webMemberId(request.getWebMemberId())
        .memberName(request.getMemberName())
        .memberMobile(request.getMemberMobile())
        .memberEmail(request.getMemberEmail())
        .memberGender(request.getMemberGender())
        .memberBirth(request.getMemberBirth())
        .memberZipCode(request.getMemberZipCode())
        .memberAddress1(request.getMemberAddress1())
        .memberAddress2(request.getMemberAddress2())
        .memberJoinDate(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
        .employeeStatus("N")
        .memberStatus("00")
        .build();
}

// The Parnas 회원 여부 확인
private boolean isParnasMember(Member member) {
    // 비즈니스 로직에 따라 The Parnas 회원 여부 판단
    return member.getMemberEmail().contains("parnas") || 
           member.getMemberName().contains("파르나스");
}
```

### 5. IDMI-REWARDS-001: 로그인

```java
// Controller
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = membershipService.login(request);
    return ResponseEntity.ok(response);
}

// Service
public LoginResponse login(LoginRequest request) {
    // 1. 로그인 타입에 따른 처리
    if (request.getLoginType() == 0) {
        return processNormalLogin(request);
    } else {
        return processSimpleLogin(request);
    }
}

// 일반 로그인
private LoginResponse processNormalLogin(LoginRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 비밀번호 검증
    if (!passwordEncoder.matches(request.getLoginPw(), member.getPasswordHash())) {
        throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    
    // 3. 계정 상태 확인
    if (!"00".equals(member.getMemberStatus())) {
        throw new AccountLockedException("잠긴 계정입니다");
    }
    
    // 4. 로그인 히스토리 저장
    saveLoginHistory(member.getId(), request.getLoginId());
    
    // 5. 응답 생성
    return LoginResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(createMemberInfo(member))
        .build();
}

// 간편 로그인
private LoginResponse processSimpleLogin(LoginRequest request) {
    // 1. CI 값으로 회원 조회
    Member member = memberRepository.findByMemberCi(request.getMemberCi())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 계정 상태 확인
    if (!"00".equals(member.getMemberStatus())) {
        throw new AccountLockedException("잠긴 계정입니다");
    }
    
    // 3. 로그인 히스토리 저장
    saveLoginHistory(member.getId(), member.getLoginId());
    
    // 4. 응답 생성
    return LoginResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(createMemberInfo(member))
        .build();
}
```

## 👤 Day 2: 회원 관리 API

### 6. IDMI-REWARDS-007: 비밀번호 확인

```java
// Controller
@PostMapping("/update")
public ResponseEntity<UpdateResponse> passwordCheck(@Valid @RequestBody PasswordCheckRequest request) {
    UpdateResponse response = membershipService.passwordCheck(request);
    return ResponseEntity.ok(response);
}

// Service
public UpdateResponse passwordCheck(PasswordCheckRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 비밀번호 검증
    boolean isMatch = passwordEncoder.matches(request.getLoginPw(), member.getPasswordHash());
    
    // 3. 응답 생성
    return UpdateResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(isMatch ? "MATCH" : "NOT_MATCH")
        .build();
}
```

### 7. IDMI-REWARDS-008: 회원 정보 수정

```java
// Controller
@PutMapping("/update")
public ResponseEntity<UpdateResponse> update(@Valid @RequestBody UpdateRequest request) {
    UpdateResponse response = membershipService.update(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public UpdateResponse update(UpdateRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 이메일 중복 체크 (변경된 경우)
    if (!member.getMemberEmail().equals(request.getMemberEmail())) {
        if (memberRepository.existsByMemberEmail(request.getMemberEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다");
        }
    }
    
    // 3. 회원 정보 업데이트
    updateMemberInfo(member, request);
    
    // 4. 회원 정보 저장
    memberRepository.save(member);
    
    // 5. CMS 동기화
    syncToCms(member);
    
    // 6. 응답 생성
    return UpdateResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(createMemberInfo(member))
        .build();
}

// 회원 정보 업데이트
private void updateMemberInfo(Member member, UpdateRequest request) {
    member.setMemberName(request.getMemberName());
    member.setMemberEmail(request.getMemberEmail());
    member.setMemberMobile(request.getMemberMobile());
    member.setMemberGender(request.getMemberGender());
    member.setMemberBirth(request.getMemberBirth());
    member.setMemberZipCode(request.getMemberZipCode());
    member.setMemberAddress1(request.getMemberAddress1());
    member.setMemberAddress2(request.getMemberAddress2());
    member.setUpdatedAt(LocalDateTime.now());
}
```

### 8. IDMI-REWARDS-009: 비밀번호 변경

```java
// Controller
@PutMapping("/update/pw")
public ResponseEntity<UpdatePasswordResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
    UpdatePasswordResponse response = membershipService.updatePassword(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 현재 비밀번호 검증
    if (!passwordEncoder.matches(request.getCurrentPw(), member.getPasswordHash())) {
        throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다");
    }
    
    // 3. 새 비밀번호 암호화
    String newPasswordHash = passwordEncoder.encode(request.getNewPw());
    member.setPasswordHash(newPasswordHash);
    member.setUpdatedAt(LocalDateTime.now());
    
    // 4. 회원 정보 저장
    memberRepository.save(member);
    
    // 5. 응답 생성
    return UpdatePasswordResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data("PASSWORD_UPDATED")
        .build();
}
```

### 9. IDMI-REWARDS-011: 탈퇴 요청

```java
// Controller
@PostMapping("/hub/member/withdrawal/request")
public ResponseEntity<WithdrawalResponse> requestWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
    WithdrawalResponse response = membershipService.requestWithdrawal(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public WithdrawalResponse requestWithdrawal(WithdrawalRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 비밀번호 검증
    if (!passwordEncoder.matches(request.getLoginPw(), member.getPasswordHash())) {
        throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    
    // 3. 탈퇴 처리
    member.setMemberStatus("2"); // 탈퇴 상태
    member.setWithdrawlYn("Y");
    member.setWithdrawlDate(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    member.setUpdatedAt(LocalDateTime.now());
    
    // 4. 회원 정보 저장
    memberRepository.save(member);
    
    // 5. 탈퇴 히스토리 저장
    saveWithdrawalHistory(member.getId(), request.getReason());
    
    // 6. CMS 동기화
    syncToCms(member);
    
    // 7. 응답 생성
    return WithdrawalResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data("WITHDRAWAL_REQUESTED")
        .build();
}
```

### 10. IDMI-REWARDS-022: 회원 정보 조회

```java
// Controller
@GetMapping("/user")
public ResponseEntity<UserResponse> getUser(@RequestParam String loginId) {
    UserResponse response = membershipService.getUser(loginId);
    return ResponseEntity.ok(response);
}

// Service
public UserResponse getUser(String loginId) {
    // 1. 회원 정보 조회 (다중 ID 지원)
    Member member = memberRepository.findByLoginIdOrMembershipIdOrWebMemberId(loginId, loginId, loginId)
        .orElse(null);
    
    // 2. 응답 생성
    return UserResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(member != null ? createMemberInfo(member) : null)
        .build();
}

// Repository
@Query("SELECT m FROM Member m WHERE m.loginId = :loginId OR m.membershipId = :membershipId OR m.webMemberId = :webMemberId")
Optional<Member> findByLoginIdOrMembershipIdOrWebMemberId(
    @Param("loginId") String loginId,
    @Param("membershipId") String membershipId,
    @Param("webMemberId") String webMemberId
);
```

## 🚀 Day 3: 고급 기능 API

### 11. IDMI-REWARDS-002: 회원 통합

```java
// Controller
@PostMapping("/register")
public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
    RegisterResponse response = membershipService.register(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public RegisterResponse register(RegisterRequest request) {
    // 1. Rewards 회원 조회
    Member rewardsMember = memberRepository.findByMembershipId(request.getRewardsMembershipId())
        .orElseThrow(() -> new MemberNotFoundException("Rewards 회원 정보를 찾을 수 없습니다"));
    
    // 2. Web 회원 조회
    Member webMember = memberRepository.findByWebMemberId(request.getWebMemberId())
        .orElseThrow(() -> new MemberNotFoundException("Web 회원 정보를 찾을 수 없습니다"));
    
    // 3. 통합 아이디 중복 체크
    if (memberRepository.existsByLoginId(request.getUnifiedId())) {
        throw new DuplicateIdException("이미 사용 중인 통합 아이디입니다");
    }
    
    // 4. 통합 회원 생성
    Member unifiedMember = createUnifiedMember(request, rewardsMember, webMember);
    
    // 5. 기존 회원 정보 업데이트 (통합 완료 표시)
    rewardsMember.setIntegrationType(IntegrationType.N);
    webMember.setIntegrationType(IntegrationType.N);
    memberRepository.save(rewardsMember);
    memberRepository.save(webMember);
    
    // 6. 약관 동의 정보 저장
    saveTermsAgreements(unifiedMember.getId(), request.getTermInfos());
    
    // 7. 응답 생성
    return RegisterResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(createMemberInfo(unifiedMember))
        .build();
}

// 통합 회원 생성
private Member createUnifiedMember(RegisterRequest request, Member rewardsMember, Member webMember) {
    return Member.builder()
        .memberType(MemberType.U) // 통합회원
        .integrationType(IntegrationType.N) // 통합 완료
        .loginId(request.getUnifiedId())
        .membershipId(request.getRewardsMembershipId())
        .webMemberId(request.getWebMemberId())
        .cmsProfileId(webMember.getCmsProfileId())
        .memberName(rewardsMember.getMemberName())
        .memberMobile(rewardsMember.getMemberMobile())
        .memberEmail(rewardsMember.getMemberEmail())
        .memberGender(rewardsMember.getMemberGender())
        .memberBirth(rewardsMember.getMemberBirth())
        .memberJoinDate(rewardsMember.getMemberJoinDate()) // Rewards 가입일 기준
        .passwordHash(passwordEncoder.encode(request.getUnifiedPw()))
        .memberStatus("00")
        .build();
}
```

### 12. IDMI-REWARDS-012: 회원 아이디 찾기

```java
// Controller
@PostMapping("/find/id")
public ResponseEntity<FindIdResponse> findId(@Valid @RequestBody FindIdRequest request) {
    FindIdResponse response = membershipService.findId(request);
    return ResponseEntity.ok(response);
}

// Service
public FindIdResponse findId(FindIdRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByMemberNameAndMemberMobile(
        request.getMemberName(), request.getMemberMobile())
        .orElse(null);
    
    // 2. 아이디 마스킹 처리
    String maskedId = maskLoginId(member != null ? member.getLoginId() : null);
    
    // 3. 응답 생성
    return FindIdResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(maskedId)
        .build();
}

// 아이디 마스킹
private String maskLoginId(String loginId) {
    if (loginId == null || loginId.length() < 3) {
        return null;
    }
    
    int length = loginId.length();
    String prefix = loginId.substring(0, 2);
    String suffix = loginId.substring(length - 1);
    String mask = "*".repeat(length - 3);
    
    return prefix + mask + suffix;
}
```

### 13. IDMI-REWARDS-013: 비밀번호 찾기

```java
// Controller
@PostMapping("/find/pw")
public ResponseEntity<FindPasswordResponse> findPassword(@Valid @RequestBody FindPasswordRequest request) {
    FindPasswordResponse response = membershipService.findPassword(request);
    return ResponseEntity.ok(response);
}

// Service
public FindPasswordResponse findPassword(FindPasswordRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginIdAndMemberEmail(
        request.getLoginId(), request.getMemberEmail())
        .orElse(null);
    
    if (member == null) {
        throw new MemberNotFoundException("회원 정보를 찾을 수 없습니다");
    }
    
    // 2. 임시 비밀번호 생성
    String temporaryPassword = generateTemporaryPassword();
    
    // 3. 임시 비밀번호로 업데이트
    member.setPasswordHash(passwordEncoder.encode(temporaryPassword));
    member.setUpdatedAt(LocalDateTime.now());
    memberRepository.save(member);
    
    // 4. 이메일 발송
    sendTemporaryPasswordEmail(member.getMemberEmail(), temporaryPassword);
    
    // 5. 응답 생성
    return FindPasswordResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data("TEMPORARY_PASSWORD_SENT")
        .build();
}

// 임시 비밀번호 생성
private String generateTemporaryPassword() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    
    for (int i = 0; i < 10; i++) {
        sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    
    return sb.toString();
}
```

### 14. IDMI-REWARDS-020: 이메일 인증 번호 확인

```java
// Controller
@PostMapping("/email/certification")
public ResponseEntity<EmailCertificationResponse> verifyEmailCertification(@Valid @RequestBody EmailCertificationRequest request) {
    EmailCertificationResponse response = membershipService.verifyEmailCertification(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public EmailCertificationResponse verifyEmailCertification(EmailCertificationRequest request) {
    // 1. 인증 정보 조회
    EmailCertification certification = emailCertificationRepository
        .findByEmailAndCertificationKey(request.getEmail(), request.getCertificationKey())
        .orElse(null);
    
    // 2. 인증 결과 판단
    boolean isVerified = false;
    if (certification != null) {
        // 만료 시간 체크
        if (certification.getExpiresAt().isAfter(LocalDateTime.now())) {
            // 인증 성공 처리
            certification.setStatus("VERIFIED");
            certification.setVerifiedAt(LocalDateTime.now());
            emailCertificationRepository.save(certification);
            isVerified = true;
        }
    }
    
    // 3. 응답 생성
    return EmailCertificationResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .rejectYn(isVerified ? "Y" : "N")
        .build();
}
```

### 15. IDMI-REWARDS-021: 이메일 인증 번호 발송

```java
// Controller
@GetMapping("/email/certification")
public ResponseEntity<EmailCertificationResponse> sendEmailCertification(@RequestParam String email) {
    EmailCertificationResponse response = membershipService.sendEmailCertification(email);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public EmailCertificationResponse sendEmailCertification(String email) {
    // 1. 이메일 형식 검증
    if (!isValidEmail(email)) {
        throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다");
    }
    
    // 2. 기존 인증번호 무효화
    emailCertificationRepository.invalidateExistingCertifications(email);
    
    // 3. 새로운 인증번호 생성
    String certificationKey = generateCertificationKey();
    
    // 4. 인증 정보 저장
    EmailCertification certification = EmailCertification.builder()
        .email(email)
        .certificationKey(certificationKey)
        .status("PENDING")
        .expiresAt(LocalDateTime.now().plusMinutes(30))
        .build();
    
    emailCertificationRepository.save(certification);
    
    // 5. 이메일 발송
    sendCertificationEmail(email, certificationKey);
    
    // 6. 응답 생성
    return EmailCertificationResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(null)
        .build();
}

// 인증번호 생성
private String generateCertificationKey() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 6; i++) {
        sb.append(random.nextInt(10));
    }
    return sb.toString();
}
```

### 16. IDMI-REWARDS-023: 멤버십 가입

```java
// Controller
@PostMapping("/payment")
public ResponseEntity<MembershipPaymentResponse> registerMembership(@Valid @RequestBody MembershipPaymentRequest request) {
    MembershipPaymentResponse response = membershipService.registerMembership(request);
    return ResponseEntity.ok(response);
}

// Service
@Transactional
public MembershipPaymentResponse registerMembership(MembershipPaymentRequest request) {
    // 1. 회원 정보 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    
    // 2. 멤버십 유형 검증
    validateMembershipType(request.getExtraMembershipInfo().getExtraMembershipType());
    
    // 3. 멤버십 결제 정보 저장
    MembershipPayment payment = MembershipPayment.builder()
        .memberId(member.getId())
        .membershipType(request.getExtraMembershipInfo().getExtraMembershipType())
        .paymentBillNo(request.getExtraMembershipInfo().getPaymentBillNo())
        .paymentAmount(request.getExtraMembershipInfo().getPaymentAmount())
        .paymentDate(request.getExtraMembershipInfo().getPaymentDate())
        .build();
    
    membershipPaymentRepository.save(payment);
    
    // 4. 약관 동의 정보 저장
    saveTermsAgreements(member.getId(), request.getTermInfos());
    
    // 5. 회원 멤버십 등급 업데이트
    member.setMembershipType(request.getExtraMembershipInfo().getExtraMembershipType());
    memberRepository.save(member);
    
    // 6. 응답 생성
    return MembershipPaymentResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .data(null)
        .build();
}

// 멤버십 유형 검증
private void validateMembershipType(String membershipType) {
    if (!Arrays.asList("G", "P", "L").contains(membershipType)) {
        throw new IllegalArgumentException("유효하지 않은 멤버십 유형입니다");
    }
}
```

### 17. IDMI-REWARDS-024: 회원관리 목록 조회

```java
// Controller
@GetMapping("/user/list")
public ResponseEntity<UserListResponse> getUserList(
    @RequestParam String keyword,
    @RequestParam(required = false) String fields,
    @RequestParam int pageNo,
    @RequestParam int limit) {
    UserListResponse response = membershipService.getUserList(keyword, fields, pageNo, limit);
    return ResponseEntity.ok(response);
}

// Service
public UserListResponse getUserList(String keyword, String fields, int pageNo, int limit) {
    // 1. 페이지네이션 검증
    if (pageNo < 1 || limit < 1 || limit > 30) {
        throw new IllegalArgumentException("잘못된 페이지네이션 파라미터입니다");
    }
    
    // 2. 검색 조건 파싱
    SearchCriteria criteria = parseSearchCriteria(keyword, fields);
    
    // 3. 회원 목록 조회
    Pageable pageable = PageRequest.of(pageNo - 1, limit, Sort.by("createdAt").descending());
    Page<Member> memberPage = memberRepository.findBySearchCriteria(criteria, pageable);
    
    // 4. 응답 생성
    return UserListResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .memberInfo(memberPage.getContent().stream()
            .map(this::createMemberInfo)
            .collect(Collectors.toList()))
        .pagination(createPaginationInfo(memberPage, pageNo, limit))
        .build();
}

// 검색 조건 파싱
private SearchCriteria parseSearchCriteria(String keyword, String fields) {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setKeyword(keyword);
    
    if (fields != null) {
        criteria.setSearchFields(Arrays.asList(fields.split(",")));
    }
    
    return criteria;
}

// Repository
@Query("SELECT m FROM Member m WHERE " +
       "(:criteria.searchFields IS NULL OR 'name' IN :criteria.searchFields AND m.memberName LIKE %:criteria.keyword%) OR " +
       "(:criteria.searchFields IS NULL OR 'mobile' IN :criteria.searchFields AND m.memberMobile = :criteria.keyword) OR " +
       "(:criteria.searchFields IS NULL OR 'email' IN :criteria.searchFields AND m.memberEmail = :criteria.keyword)")
Page<Member> findBySearchCriteria(@Param("criteria") SearchCriteria criteria, Pageable pageable);
```

### 18. IDMI-REWARDS-010: 공통코드 조회

```java
// Controller
@GetMapping("/reason")
public ResponseEntity<CommonCodeResponse> getCommonCodes(@RequestParam String classCode) {
    CommonCodeResponse response = membershipService.getCommonCodes(classCode);
    return ResponseEntity.ok(response);
}

// Service
public CommonCodeResponse getCommonCodes(String classCode) {
    // 1. 공통코드 조회
    List<CommonCode> codes = commonCodeRepository.findByClassCodeAndUseYnOrderBySortOrder(classCode, "Y");
    
    // 2. 응답 생성
    return CommonCodeResponse.builder()
        .resultCode("1000")
        .msg("Success")
        .codes(codes)
        .build();
}

// Repository
@Query("SELECT c FROM CommonCode c WHERE c.classCode = :classCode AND c.useYn = :useYn ORDER BY c.sortOrder")
List<CommonCode> findByClassCodeAndUseYnOrderBySortOrder(@Param("classCode") String classCode, @Param("useYn") String useYn);
```

## 🔗 외부 시스템 연동 (추후 구현)

### 19. CMS 동기화 클라이언트

```java
@Component
public class CmsClient {
    
    @Value("${external.cms.url}")
    private String cmsUrl;
    
    @Value("${external.cms.timeout}")
    private int timeout;
    
    private final RestTemplate restTemplate;
    
    public CmsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public CmsResponse syncMemberToCms(Member member) {
        try {
            CmsRequest request = CmsRequest.builder()
                .memberType(member.getMemberType().getValue())
                .membershipNo(member.getMembershipNo())
                .membershipId(member.getMembershipId())
                .webMemberId(member.getWebMemberId())
                .cmsProfileId(member.getCmsProfileId())
                .memberName(member.getMemberName())
                .memberMobile(member.getMemberMobile())
                .memberEmail(member.getMemberEmail())
                .memberGender(member.getMemberGender())
                .memberBirth(member.getMemberBirth())
                .memberStatus(member.getMemberStatus())
                .joinDate(member.getMemberJoinDate())
                .withdrawlYn(member.getWithdrawlYn())
                .withdrawlDate(member.getWithdrawlDate())
                .build();
            
            ResponseEntity<CmsResponse> response = restTemplate.postForEntity(
                cmsUrl + "/api/member/sync", request, CmsResponse.class);
            
            return response.getBody();
        } catch (Exception e) {
            throw new CmsSyncException("CMS 동기화에 실패했습니다: " + e.getMessage());
        }
    }
}
```

### 20. CRS 동기화 클라이언트

```java
@Component
public class CrsClient {
    
    @Value("${external.crs.url}")
    private String crsUrl;
    
    @Value("${external.crs.timeout}")
    private int timeout;
    
    private final RestTemplate restTemplate;
    
    public CrsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public CrsResponse syncMemberToCrs(Member member) {
        try {
            CrsRequest request = CrsRequest.builder()
                .memberType(member.getMemberType().getValue())
                .membershipNo(member.getMembershipNo())
                .membershipId(member.getMembershipId())
                .webMemberId(member.getWebMemberId())
                .cmsProfileId(member.getCmsProfileId())
                .memberName(member.getMemberName())
                .memberMobile(member.getMemberMobile())
                .memberEmail(member.getMemberEmail())
                .memberGender(member.getMemberGender())
                .memberBirth(member.getMemberBirth())
                .memberStatus(member.getMemberStatus())
                .joinDate(member.getMemberJoinDate())
                .withdrawlYn(member.getWithdrawlYn())
                .withdrawlDate(member.getWithdrawlDate())
                .build();
            
            ResponseEntity<CrsResponse> response = restTemplate.postForEntity(
                crsUrl + "/api/member/sync", request, CrsResponse.class);
            
            return response.getBody();
        } catch (Exception e) {
            throw new CrsSyncException("CRS 동기화에 실패했습니다: " + e.getMessage());
        }
    }
}
```

## 🧪 테스트 코드 예시

### 단위 테스트

```java
@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private MembershipService membershipService;
    
    @Test
    @DisplayName("회원 가입 성공 테스트")
    void join_Success() {
        // Given
        JoinRequest request = createJoinRequest();
        Member savedMember = createMember();
        
        when(memberRepository.existsByLoginId(anyString())).thenReturn(false);
        when(memberRepository.existsByMemberEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        // When
        JoinResponse response = membershipService.join(request);
        
        // Then
        assertThat(response.getResultCode()).isEqualTo("1000");
        assertThat(response.getMsg()).isEqualTo("Success");
        verify(memberRepository).save(any(Member.class));
    }
    
    @Test
    @DisplayName("중복 아이디로 회원 가입 실패 테스트")
    void join_DuplicateId_ThrowsException() {
        // Given
        JoinRequest request = createJoinRequest();
        when(memberRepository.existsByLoginId(anyString())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> membershipService.join(request))
            .isInstanceOf(DuplicateIdException.class)
            .hasMessage("이미 사용 중인 아이디입니다");
    }
}
```

### 통합 테스트

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MembershipControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Test
    @DisplayName("회원 가입 API 통합 테스트")
    void join_IntegrationTest() {
        // Given
        JoinRequest request = JoinRequest.builder()
            .loginId("testuser")
            .loginPw("password123")
            .webMemberId("test@gmail.com")
            .memberName("홍길동")
            .memberMobile("01012345678")
            .memberEmail("test@gmail.com")
            .memberGender(0)
            .memberBirth("19900101")
            .build();
        
        // When
        ResponseEntity<JoinResponse> response = restTemplate.postForEntity(
            "/v1/membership/integration/join", request, JoinResponse.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getResultCode()).isEqualTo("1000");
        
        // 데이터베이스 확인
        Optional<Member> savedMember = memberRepository.findByLoginId("testuser");
        assertThat(savedMember).isPresent();
        assertThat(savedMember.get().getMemberName()).isEqualTo("홍길동");
    }
}
```

이제 모든 API의 상세한 pseudo 코드가 완성되었습니다! 주니어 개발자들이 이 코드를 참고하여 3일 내에 구현할 수 있을 것입니다. 