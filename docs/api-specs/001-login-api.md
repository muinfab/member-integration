# 로그인 API 명세서 (IDMI-REWARDS-001)

## 📋 개요
일반 및 간편 회원 로그인을 처리하는 API입니다. 웹회원(IWS)과 리워즈회원(The Parnas)의 통합 로그인을 지원합니다.

## 🔗 API 정보
- **URI**: `/v1/membership/integration/login`
- **Method**: `POST`
- **Content-Type**: `application/json`

## 📥 요청 (Request)

### 요청 파라미터

| 파라미터명 | 설명 | 데이터 타입 | 길이 | 필수 | 샘플 값 | 비고 |
|---|---|---|---|---|---|---|
| **loginType** | 로그인 유형 | int | | Y | 0 | 0: 일반 로그인, 1: 간편 로그인 |
| **loginId** | 로그인 아이디 | string | | Y | user123 | 일반 로그인 시 아이디 (평문) |
| **loginPw** | 로그인 패스워드 | string | | | password123 | 일반 로그인 시 패스워드 (평문) |
| **channelId** | 카카오 아이디 | string | | | 43252323 | 간편 로그인 시 카카오 아이디 |
| **memberCi** | Ci 정보 | string | | | ySiSkQvaSj0ifo3ytt760s38dAHMxxxk0xF8Ga7enBhRfsw5q+/HP8qc+rqo5Q8T/Km3WVo1LJ/BZUXAyi013A== | 간편 로그인 시 카카오에서 발행한 Ci 값 |

### 요청 예시

#### 일반 로그인
```json
{
  "loginType": 0,
  "loginId": "user123",
  "loginPw": "password123"
}
```

#### 간편 로그인 (카카오)
```json
{
  "loginType": 1,
  "channelId": "43252323",
  "memberCi": "ySiSkQvaSj0ifo3ytt760s38dAHMxxxk0xF8Ga7enBhRfsw5q+/HP8qc+rqo5Q8T/Km3WVo1LJ/BZUXAyi013A=="
}
```

## 📤 응답 (Response)

### 응답 파라미터

| 파라미터명 | 설명 | 데이터 타입 | 길이 | 필수 | 샘플 값 | 비고 |
|---|---|---|---|---|---|---|
| **resultCode** | 결과 코드 | string | | Y | 1000 | |
| **mesg** | 메시지 | string | | Y | Success | |
| **data** | 결과 Object | object | | Y | | |
| **memberType** | 회원 유형 | string | | Y | WU | WU: 통합회원, R: Rewards, W: Web |
| **integrationType** | 통합 유형 | string | | Y | UU | UU: 통합대상, T: 전환대상, N: 미대상 |
| **webInfoId** | 웹 회원 전용 시퀀스 아이디 | int | | | 200 | 웹 회원에게만 존재 |
| **rewardsMembershipNo** | 리워즈 멤버십 No | string | | | PM00004020 | |
| **rewardsMembershipId** | 리워즈 ID | string | | | test001 | |
| **loginId** | 통합 아이디 / 웹회원 아이디 | string | | Y | axlrose | 통합이 아닌 경우 웹회원 아이디 |
| **webMemberId** | 웹회원 아이디 | string | | | test@gmail.com | |
| **cmsProfileId** | CMS 프로필 아이디 | string | | | 1231231 | 통합회원/Web만 해당 (GUEST_NO) |
| **memberName** | 한글 이름 | string | | Y | 홍길동 | |
| **memberFirstName** | 영문 이름 | string | | | Gildong | |
| **memberMiddleName** | 영문 중간 이름 | string | | | Heo | |
| **memberLastName** | 영문 성 | string | | | Hong | |
| **memberMobile** | 휴대전화 | string | | Y | 01098765432 | |
| **memberEmail** | 이메일 | string | | Y | test@gmail.com | |
| **memberGender** | 성별 | int | | Y | 00 | 00: 남성, 1: 여성, 2: 기타 |
| **memberBirth** | 생년월일 | string | | Y | 19990101 | YYYYMMDD 형식 |
| **employeeStatus** | 임직원여부 | string | | Y | N | |

### 응답 예시

#### 성공 응답 (웹회원)
```json
{
  "resultCode": "1000",
  "mesg": "Success",
  "data": {
    "memberType": "W",
    "integrationType": "T",
    "loginId": "test@gmail.com",
    "webInfoId": 200,
    "webMemberId": "test@gmail.com",
    "memberName": "홍길동",
    "memberEmail": "test@gmail.com",
    "memberMobile": "01098765432",
    "memberGender": 0,
    "memberBirth": "19990101",
    "employeeStatus": "N"
  }
}
```

#### 성공 응답 (리워즈회원)
```json
{
  "resultCode": "1000",
  "mesg": "Success",
  "data": {
    "memberType": "R",
    "integrationType": "T",
    "loginId": "axlrose",
    "rewardsMembershipNo": "PM00004020",
    "rewardsMembershipId": "axlrose",
    "memberName": "홍길동",
    "memberEmail": "test@gmail.com",
    "memberMobile": "01098765432",
    "memberGender": 0,
    "memberBirth": "19990101",
    "employeeStatus": "N"
  }
}
```

#### 성공 응답 (통합회원)
```json
{
  "resultCode": "1000",
  "mesg": "Success",
  "data": {
    "memberType": "WU",
    "integrationType": "UU",
    "loginId": "axlrose",
    "webInfoId": 200,
    "webMemberId": "test@gmail.com",
    "rewardsMembershipNo": "PM00004020",
    "rewardsMembershipId": "axlrose",
    "cmsProfileId": "1231231",
    "memberName": "홍길동",
    "memberEmail": "test@gmail.com",
    "memberMobile": "01098765432",
    "memberGender": 0,
    "memberBirth": "19990101",
    "employeeStatus": "N"
  }
}
```

#### 에러 응답
```json
{
  "resultCode": "ERR102",
  "mesg": "아이디 또는 비밀번호가 틀렸습니다.",
  "data": null
}
```

## ⚠️ 에러 코드

### 로그인 API 관련 에러 코드

| 에러 코드 | 에러 메시지 | 설명 | HTTP 상태 코드 |
|-----------|-------------|------|----------------|
| ERR001 | 필수 데이터가 없습니다. | 필수 파라미터 누락 (loginId, loginPw, loginType 등) | 400 |
| ERR101 | 가입정보가 없습니다. | 존재하지 않는 회원 | 404 |
| ERR102 | 아이디 또는 비밀번호가 틀렸습니다. | 비밀번호 불일치 | 401 |
| ERR103 | 계정 잠금 상태입니다. | 로그인 실패 횟수 초과 | 403 |
| ERR104 | 블락 된 회원입니다. | 관리자에 의해 차단된 회원 | 403 |

### HTTP 상태 코드

| 상태 코드 | 설명 |
|-----------|------|
| 200 | 성공 |
| 400 | 잘못된 요청 (입력값 검증 실패) |
| 401 | 인증 실패 (비밀번호 오류) |
| 403 | 접근 금지 (계정 잠금/블락) |
| 404 | 리소스 없음 (회원 없음) |
| 500 | 서버 내부 오류 |

## 🔄 비즈니스 로직

### 회원 유형 판별
1. **웹회원 (W)**: loginId가 이메일 형식
2. **리워즈회원 (R)**: loginId가 일반 아이디 형식  
3. **통합회원 (WU)**: 이미 통합된 회원

### 로그인 처리 흐름
1. **입력값 검증**: Controller에서 필수 파라미터 검증
2. **회원 조회**: loginId로 회원 정보 조회
3. **상태 확인**: 회원 상태, 잠금 여부, 블락 여부 확인
4. **비밀번호 검증**: 일반 로그인 시에만 BCrypt로 검증
5. **로그인 이력**: 로그인 성공 시 이력 저장
6. **응답 생성**: 회원 유형별 응답 데이터 구성

### 보안 고려사항
- **비밀번호**: BCrypt로 암호화하여 저장/검증
- **로그인 실패**: 5회 실패 시 계정 잠금
- **세션 관리**: JWT 토큰 기반 인증
- **로그 기록**: 모든 로그인 시도 기록

## 🛠️ 구현 가이드

### Controller 구현 예시
```java
@PostMapping("/v1/membership/integration/login")
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    // 1. 입력값 검증
    validateLoginRequest(request);
    
    // 2. 로그인 처리
    LoginResponse response = membershipService.login(request);
    
    return ResponseEntity.ok(response);
}

private void validateLoginRequest(LoginRequest request) {
    if (StringUtils.isEmpty(request.getLoginId())) {
        throw new MissingParameterException("loginId");
    }
    
    if (request.getLoginType() == 0 && StringUtils.isEmpty(request.getLoginPw())) {
        throw new MissingParameterException("loginPw");
    }
    
    if (request.getLoginType() == 1) {
        if (StringUtils.isEmpty(request.getChannelId()) || StringUtils.isEmpty(request.getMemberCi())) {
            throw new MissingParameterException("channelId or memberCi");
        }
    }
}
```

### Service 구현 예시
```java
public LoginResponse login(LoginRequest request) {
    // 1. 회원 조회
    Member member = memberRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new MemberNotFoundException("ERR101"));
    
    // 2. 회원 상태 확인
    if (!member.canLogin()) {
        throw new AccountLockedException("ERR103");
    }
    
    // 3. 비밀번호 검증 (일반 로그인만)
    if (request.getLoginType() == 0) {
        if (!member.getPassword().matches(request.getLoginPw())) {
            memberRepository.incrementLoginFailureCount(member.getId());
            throw new InvalidCredentialsException("ERR102");
        }
        memberRepository.resetLoginFailureCount(member.getId());
    }
    
    // 4. 로그인 이력 저장
    saveLoginHistory(member, request);
    
    // 5. 응답 생성
    return createLoginResponse(member);
}
```

## 📝 개발자 노트

### 테스트 시나리오
1. **정상 로그인**: 유효한 아이디/비밀번호로 로그인
2. **잘못된 비밀번호**: 올바른 아이디, 잘못된 비밀번호
3. **존재하지 않는 회원**: 등록되지 않은 아이디로 로그인
4. **계정 잠금**: 5회 실패 후 로그인 시도
5. **간편 로그인**: 카카오 로그인 테스트

### 성능 고려사항
- **DB 인덱스**: loginId 컬럼에 인덱스 설정
- **캐싱**: 자주 조회되는 회원 정보 캐싱
- **비동기 처리**: 로그인 이력 저장은 비동기로 처리
- **로드 밸런싱**: 대용량 트래픽 대비 로드 밸런싱

### 모니터링 지표
- **로그인 성공률**: 전체 로그인 시도 대비 성공률
- **에러 발생률**: 에러 코드별 발생 빈도
- **응답 시간**: 로그인 API 평균 응답 시간
- **동시 접속자**: 동시 로그인 사용자 수 