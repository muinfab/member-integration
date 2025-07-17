package com.company.membership.api;

import org.springframework.web.bind.annotation.*;
import com.company.membership.api.dto.*;
import com.company.membership.application.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/v1/membership/integration")
@Validated
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    /**
     * 로그인
     * API 명세서 코드: IDMI-REWARDS-001
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = membershipService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 (외국인 전용)
     * API 명세서 코드: IDMI-REWARDS-002
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = membershipService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 약관 조회
     * API 명세서 코드: IDMI-REWARDS-003
     */
    @GetMapping("/terms")
    public ResponseEntity<TermsResponse> getTerms(
            @NotBlank(message = "언어는 필수입니다") @RequestParam String language) {
        TermsResponse response = membershipService.getTerms(language);
        return ResponseEntity.ok(response);
    }

    /**
     * ID 중복 확인
     * API 명세서 코드: IDMI-REWARDS-004
     */
    @GetMapping("/check/id")
    public ResponseEntity<CheckIdResponse> checkId(
            @NotBlank(message = "확인할 ID는 필수입니다") @RequestParam String checkInfo) {
        CheckIdResponse response = membershipService.checkId(checkInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 중복 확인
     * API 명세서 코드: IDMI-REWARDS-005
     */
    @GetMapping("/check/email")
    public ResponseEntity<CheckEmailResponse> checkEmail(
            @NotBlank(message = "확인할 이메일은 필수입니다") @RequestParam String checkInfo) {
        CheckEmailResponse response = membershipService.checkEmail(checkInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 가입
     * API 명세서 코드: IDMI-REWARDS-006
     */
    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@Valid @RequestBody JoinRequest request) {
        JoinResponse response = membershipService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 정보 수정
     * API 명세서 코드: IDMI-REWARDS-008
     */
    @PutMapping("/update")
    public ResponseEntity<UpdateResponse> update(@Valid @RequestBody UpdateRequest request) {
        UpdateResponse response = membershipService.update(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 확인
     * API 명세서 코드: IDMI-REWARDS-007
     */
    @PostMapping("/update/password-check")
    public ResponseEntity<UpdateResponse> passwordCheck(@Valid @RequestBody PasswordCheckRequest request) {
        UpdateResponse response = membershipService.passwordCheck(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 변경
     * API 명세서 코드: IDMI-REWARDS-009
     */
    @PutMapping("/update/pw")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        UpdatePasswordResponse response = membershipService.updatePassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 조회
     * API 명세서 코드: IDMI-REWARDS-022
     */
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(
            @NotBlank(message = "로그인 ID는 필수입니다") @RequestParam String loginId) {
        UserResponse response = membershipService.getUser(loginId);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 목록 조회
     * API 명세서 코드: IDMI-REWARDS-024
     */
    @GetMapping("/user/list")
    public ResponseEntity<UserListResponse> getUserList(
            @NotBlank(message = "검색 키워드는 필수입니다") @RequestParam String keyword,
            @RequestParam(required = false) String fields,
            @NotNull(message = "페이지 번호는 필수입니다") @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다") @RequestParam int pageNo,
            @NotNull(message = "페이지 크기는 필수입니다") @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다") @RequestParam int limit) {
        UserListResponse response = membershipService.getUserList(keyword, fields, pageNo, limit);
        return ResponseEntity.ok(response);
    }

    // ========== 미구현 API들 ==========

    /**
     * 공통코드 조회
     * API 명세서 코드: IDMI-REWARDS-010
     */
    @GetMapping("/reason")
    public ResponseEntity<CommonCodeResponse> getCommonCodes(
            @NotBlank(message = "공통코드는 필수입니다") @RequestParam String classCode) {
        CommonCodeResponse response = membershipService.getCommonCodes(classCode);
        return ResponseEntity.ok(response);
    }

    /**
     * 탈퇴 요청
     * API 명세서 코드: IDMI-REWARDS-011
     */
    @PostMapping("/hub/member/withdrawal/request")
    public ResponseEntity<WithdrawalResponse> requestWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
        WithdrawalResponse response = membershipService.requestWithdrawal(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 아이디 찾기
     * API 명세서 코드: IDMI-REWARDS-012
     */
    @PostMapping("/find/id")
    public ResponseEntity<FindIdResponse> findId(@Valid @RequestBody FindIdRequest request) {
        FindIdResponse response = membershipService.findId(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 찾기
     * API 명세서 코드: IDMI-REWARDS-013
     */
    @PostMapping("/find/pw")
    public ResponseEntity<FindPasswordResponse> findPassword(@Valid @RequestBody FindPasswordRequest request) {
        FindPasswordResponse response = membershipService.findPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 인증 번호 확인 (외국인 전용)
     * API 명세서 코드: IDMI-REWARDS-020
     */
    @PostMapping("/email/certification")
    public ResponseEntity<EmailCertificationResponse> verifyEmailCertification(@Valid @RequestBody EmailCertificationRequest request) {
        EmailCertificationResponse response = membershipService.verifyEmailCertification(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 인증 번호 발송 (외국인 전용)
     * API 명세서 코드: IDMI-REWARDS-021
     */
    @GetMapping("/email/certification")
    public ResponseEntity<EmailCertificationResponse> sendEmailCertification(
            @NotBlank(message = "이메일은 필수입니다") @RequestParam String email) {
        EmailCertificationResponse response = membershipService.sendEmailCertification(email);
        return ResponseEntity.ok(response);
    }

    /**
     * 멤버십 가입
     * API 명세서 코드: IDMI-REWARDS-023
     */
    @PostMapping("/payment")
    public ResponseEntity<MembershipPaymentResponse> registerMembership(@Valid @RequestBody MembershipPaymentRequest request) {
        MembershipPaymentResponse response = membershipService.registerMembership(request);
        return ResponseEntity.ok(response);
    }

    // ========== 시스템 연동 API들 (URI 미정) ==========

    /**
     * 신규 회원 정보 연동
     * API 명세서 코드: IDMI-REWARDS-014
     * TODO: URI 확인 필요
     */
    @PostMapping("/integration/new-member")
    public ResponseEntity<Object> integrateNewMember(@Valid @RequestBody Object request) {
        // TODO: 실제 구현 필요
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 정보 동기화 (H2O > CMS)
     * API 명세서 코드: IDMI-REWARDS-015
     * TODO: URI 확인 필요
     */
    @PostMapping("/integration/sync/h2o-to-cms")
    public ResponseEntity<Object> syncH2oToCms(@Valid @RequestBody Object request) {
        // TODO: 실제 구현 필요
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 정보 동기화 (CMS > H2O)
     * API 명세서 코드: IDMI-REWARDS-016
     */
    @PostMapping("/cms/sync")
    public ResponseEntity<Object> syncCmsToH2o(@Valid @RequestBody Object request) {
        // TODO: 실제 구현 필요
        return ResponseEntity.ok().build();
    }

    /**
     * The Parnas 추가 가입
     * API 명세서 코드: IDMI-REWARDS-017
     * TODO: URI 확인 필요
     */
    @PostMapping("/integration/parnas-additional")
    public ResponseEntity<Object> registerParnasAdditional(@Valid @RequestBody Object request) {
        // TODO: 실제 구현 필요
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 정보 동기화 (H2O > CRS)
     * API 명세서 코드: IDMI-REWARDS-018
     * TODO: URI 확인 필요
     */
    @PostMapping("/integration/sync/h2o-to-crs")
    public ResponseEntity<Object> syncH2oToCrs(@Valid @RequestBody Object request) {
        // TODO: 실제 구현 필요
        return ResponseEntity.ok().build();
    }
} 