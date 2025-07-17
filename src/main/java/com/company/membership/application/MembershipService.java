package com.company.membership.application;

import com.company.membership.api.dto.*;
import com.company.membership.domain.*;
import com.company.membership.domain.enums.Gender;
import com.company.membership.domain.enums.IntegrationType;
import com.company.membership.domain.enums.MemberStatus;
import com.company.membership.domain.enums.MemberType;
import com.company.membership.domain.value.Email;
import com.company.membership.domain.value.Password;
import com.company.membership.domain.value.PhoneNumber;
import com.company.membership.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembershipService {

    private final MemberRepository memberRepository;

    @Autowired
    public MembershipService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 로그인 처리
     */
    public LoginResponse login(LoginRequest request) {
        // 1. 회원 정보 조회
        Optional<Member> memberOpt = memberRepository.findByLoginId(request.getLoginId());
        
        if (memberOpt.isEmpty()) {
            return createErrorResponse(404, "존재하지 않는 회원입니다.");
        }
        
        Member member = memberOpt.get();
        
        // 2. 회원 상태 확인
        if (!member.canLogin()) {
            return createErrorResponse(43, "로그인이 불가능한 회원입니다.");
        }
        
        // 3. 비밀번호 검증 (일반 로그인만)
        if (request.getLoginType() != null && request.getLoginType() == 0) {
            if (member.getPassword() == null || !member.getPassword().matches(request.getLoginPw())) {
                return createErrorResponse(41, "비밀번호가 일치하지 않습니다.");
            }
        }
        
        // 4. 로그인 이력 생성
        LoginHistory loginHistory = new LoginHistory(member.getMembershipId(), String.valueOf(request.getLoginType()));
        loginHistory.setChannelInfo(request.getChannelId());
        // TODO: IP, UserAgent 설정
        
        // 5. 응답 생성
        return createLoginResponse(member);
    }

    /**
     * 회원 통합 등록
     */
    public RegisterResponse register(RegisterRequest request) {
        // 1. 기존 회원 확인
        Optional<Member> existingMember = memberRepository.findByLoginId(request.getUnifiedId());
        if (existingMember.isPresent()) {
            return createErrorResponse(409, "이미 존재하는 통합 아이디입니다.");
        }
        
        // 2. 회원 생성
        Member member = new Member();
        member.setMemberType(MemberType.UNIFIED);
        member.setIntegrationType(IntegrationType.UNIFIED_TARGET);
        member.setMembershipId(request.getUnifiedId());
        member.setWebMemberId(request.getWebMemberId());
        member.setPassword(Password.of(request.getUnifiedPw()));
        
        // 3. 약관 동의 처리
        if (request.getTermInfos() != null) {
            request.getTermInfos().forEach(termInfo -> {
                TermsAgreement agreement = new TermsAgreement();
                agreement.setTermInfoId(termInfo.getTermInfoId());
                agreement.setTermItemInfoId(termInfo.getTermItemInfoId());
                agreement.setVersion(termInfo.getVersion());
                agreement.setReceiveTypes(termInfo.getReceiveTypes());
                if ("Y".equals(termInfo.getAgreeYn())) {
                    agreement.agree();
                }
                member.addTermsAgreement(agreement);
            });
        }
        
        // 4. 회원 저장
        Member savedMember = memberRepository.save(member);
        
        return createRegisterResponse(savedMember);
    }

    /**
     * 회원 가입
     */
    public JoinResponse join(JoinRequest request) {
        // 1. 아이디 중복 확인
        Optional<Member> existingMember = memberRepository.findByLoginId(request.getJoinId());
        if (existingMember.isPresent()) {
            return createErrorResponse(409, "미 존재하는 아이디입니다.");
        }
        
        // 2. 회원 생성
        Member member = new Member();
        member.setMemberType(MemberType.WEB);
        member.setIntegrationType(IntegrationType.TRANSITION_TARGET);
        member.setMembershipId(request.getJoinId());
        member.setWebMemberId(request.getWebMemberId());
        member.setPassword(Password.of(request.getJoinPw()));
        member.setMemberName(request.getMemberName());
        member.setMemberFirstName(request.getMemberFirstName());
        member.setMemberMiddleName(request.getMemberMiddleName());
        member.setMemberLastName(request.getMemberLastName());
        member.setMemberMobile(PhoneNumber.of(request.getMemberMobile()));
        member.setMemberEmail(Email.of(request.getMemberEmail()));
        member.setMemberGender(Gender.fromCode(request.getMemberGender()));
        member.setMemberBirth(LocalDate.parse(request.getMemberBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        
        // 3. 약관 동의 처리
        if (request.getTermInfos() != null) {
            request.getTermInfos().forEach(termInfo -> {
                TermsAgreement agreement = new TermsAgreement();
                agreement.setTermInfoId(termInfo.getTermInfoId());
                agreement.setTermItemInfoId(termInfo.getTermItemInfoId());
                agreement.setVersion(termInfo.getVersion());
                agreement.setReceiveTypes(termInfo.getReceiveTypes());
                if ("Y".equals(termInfo.getAgreeYn())) {
                    agreement.agree();
                }
                member.addTermsAgreement(agreement);
            });
        }
        
        // 4. 회원 저장
        Member savedMember = memberRepository.save(member);
        
        return createJoinResponse(savedMember);
    }

    /**
     * 아이디 중복 체크
     */
    public CheckIdResponse checkId(String checkInfo) {
        Optional<Member> existingMember = memberRepository.findByLoginId(checkInfo);
        
        CheckIdResponse response = new CheckIdResponse();
        if (existingMember.isPresent()) {
            response.setResultCode("409");
            response.setMessage("이미 사용 중인 아이디입니다.");
        } else {
            response.setResultCode("10");
            response.setMessage("사용 가능한 아이디입니다.");
        }
        response.setData(null);
        return response;
    }

    /**
     * 이메일 중복 체크
     */
    public CheckEmailResponse checkEmail(String checkInfo) {
        Optional<Member> existingMember = memberRepository.findByEmail(Email.of(checkInfo));
        
        CheckEmailResponse response = new CheckEmailResponse();
        if (existingMember.isPresent()) {
            response.setResultCode("409");
            response.setMessage("이미 사용 중인 이메일입니다.");
        } else {
            response.setResultCode("10");
            response.setMessage("사용 가능한 이메일입니다.");
        }
        response.setData(null);
        return response;
    }

    /**
     * 약관 조회
     */
    public TermsResponse getTerms(String language) {
        // TODO: 실제 약관 데이터베이스에서 조회하도록 구현
        TermsResponse response = new TermsResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        TermsResponse.TermsData data = new TermsResponse.TermsData();
        data.setLanguage(language);
        data.setTerms("이용약관 내용...");
        data.setPrivacyPolicy("개인정보처리방침 내용...");
        
        response.setData(data);
        return response;
    }

    /**
     * 비밀번호 확인
     */
    public UpdateResponse passwordCheck(PasswordCheckRequest request) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(request.getLoginId());
        
        if (memberOpt.isEmpty()) {
            return createErrorResponse(404, "존재하지 않는 회원입니다.");
        }
        
        Member member = memberOpt.get();
        
        if (member.getPassword() == null || !member.getPassword().matches(request.getLoginPw())) {
            return createErrorResponse(401, "비밀번호가 일치하지 않습니다.");
        }
        
        return createUpdateResponse(member);
    }

    /**
     * 비밀번호 확인 (내부용)
     */
    public boolean checkPassword(PasswordCheckRequest request) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(request.getLoginId());
        
        if (memberOpt.isEmpty()) {
            return false;
        }
        
        Member member = memberOpt.get();
        return member.getPassword() != null && member.getPassword().matches(request.getLoginPw());
    }

    /**
     * 회원 정보 수정
     */
    public UpdateResponse update(UpdateRequest request) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(request.getMembershipId());
        
        if (memberOpt.isEmpty()) {
            return createErrorResponse(404, "존재하지 않는 회원입니다.");
        }
        
        Member member = memberOpt.get();
        
        // 정보 업데이트
        member.setMemberName(request.getMemberName());
        member.setMemberFirstName(request.getMemberFirstName());
        member.setMemberMiddleName(request.getMemberMiddleName());
        member.setMemberLastName(request.getMemberLastName());
        member.setMemberMobile(PhoneNumber.of(request.getMemberMobile()));
        member.setMemberEmail(Email.of(request.getMemberEmail()));
        member.setMemberBirth(LocalDate.parse(request.getMemberBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        
        // 약관 동의 업데이트
        if (request.getTermInfos() != null) {
            member.getTermsAgreements().clear();
            request.getTermInfos().forEach(termInfo -> {
                TermsAgreement agreement = new TermsAgreement();
                agreement.setTermInfoId(termInfo.getTermInfoId());
                agreement.setTermItemInfoId(termInfo.getTermItemInfoId());
                agreement.setVersion(termInfo.getVersion());
                agreement.setReceiveTypes(termInfo.getReceiveTypes());
                if ("Y".equals(termInfo.getAgreeYn())) {
                    agreement.agree();
                }
                member.addTermsAgreement(agreement);
            });
        }
        
        Member updatedMember = memberRepository.save(member);
        
        return createUpdateResponse(updatedMember);
    }

    /**
     * 비밀번호 변경
     */
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(request.getLoginId());
        
        if (memberOpt.isEmpty()) {
            return createErrorResponse(404, "존재하지 않는 회원입니다.");
        }
        
        Member member = memberOpt.get();
        
        // 기존 비밀번호 확인
        if (!member.getPassword().matches(request.getOldLoginPw())) {
            return createErrorResponse(401, "기존 비밀번호가 일치하지 않습니다.");
        }
        
        // 새 비밀번호 설정
        member.changePassword(Password.of(request.getNewLoginPw()));
        memberRepository.save(member);
        
        return createSuccessResponse("비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 회원 정보 조회
     */
    public UserResponse getUser(String loginId) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(loginId);
        
        if (memberOpt.isEmpty()) {
            return createErrorResponse(404, "존재하지 않는 회원입니다.");
        }
        
        return createUserResponse(memberOpt.get());
    }

    /**
     * 회원 목록 조회
     */
    public UserListResponse getUserList(String keyword, String fields, int pageNo, int limit) {
        List<Member> members = memberRepository.findByKeyword(keyword, fields, pageNo, limit);
        
        UserListResponse response = new UserListResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        UserListResponse.UserListData data = new UserListResponse.UserListData();
        data.setUsers(members.stream()
                .map(this::createUserSummary)
                .toList());
        response.setData(data);
        
        return response;
    }

    // Private helper methods
    private LoginResponse createLoginResponse(Member member) {
        LoginResponse response = new LoginResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        LoginResponse.LoginData data = new LoginResponse.LoginData();
        data.setMemberType(member.getMemberTypeAsString());
        data.setIntegrationType(member.getIntegrationTypeAsString());
        data.setWebInfoId(member.getWebInfoId());
        data.setRewardsMembershipNo(member.getMembershipNo());
        data.setRewardsMembershipId(member.getMembershipId());
        data.setLoginId(member.getMembershipId());
        data.setWebMemberId(member.getWebMemberId());
        data.setCmsProfileId(member.getCmsProfileId() != null ? String.valueOf(member.getCmsProfileId()) : null);
        data.setMemberName(member.getMemberName());
        data.setMemberFirstName(member.getMemberFirstName());
        data.setMemberMiddleName(member.getMemberMiddleName());
        data.setMemberLastName(member.getMemberLastName());
        data.setMemberMobile(member.getMemberMobileAsString());
        data.setMemberEmail(member.getMemberEmailAsString());
        data.setMemberGender(member.getMemberGenderAsInteger());
        data.setMemberBirth(member.getMemberBirthAsString());
        data.setEmployeeStatus(member.getEmployeeStatus());
        
        response.setData(data);
        return response;
    }

    private RegisterResponse createRegisterResponse(Member member) {
        RegisterResponse response = new RegisterResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        RegisterResponse.RegisterData data = new RegisterResponse.RegisterData();
        data.setMemberType(member.getMemberTypeAsString());
        data.setRewardsMembershipNo(member.getMembershipNo());
        data.setLoginId(member.getMembershipId());
        data.setMemberName(member.getMemberName());
        data.setMemberFirstName(member.getMemberFirstName());
        data.setMemberMiddleName(member.getMemberMiddleName());
        data.setMemberLastName(member.getMemberLastName());
        data.setMemberMobile(member.getMemberMobileAsString());
        data.setMemberEmail(member.getMemberEmailAsString());
        data.setMemberGender(member.getMemberGenderAsInteger());
        data.setMemberBirth(member.getMemberBirthAsString());
        data.setEmployeeStatus(member.getEmployeeStatus());
        
        response.setData(data);
        return response;
    }

    private JoinResponse createJoinResponse(Member member) {
        JoinResponse response = new JoinResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        JoinResponse.JoinData data = new JoinResponse.JoinData();
        data.setMemberType(member.getMemberTypeAsString());
        data.setMembershipUserInfoId(member.getId() != null ? member.getId().getValue().intValue() : null);
        data.setMembershipNo(member.getMembershipNo());
        data.setMembershipId(member.getMembershipId());
        data.setMemberName(member.getMemberName());
        data.setMemberFirstName(member.getMemberFirstName());
        data.setMemberMiddleName(member.getMemberMiddleName());
        data.setMemberLastName(member.getMemberLastName());
        data.setMemberMobile(member.getMemberMobileAsString());
        data.setMemberEmail(member.getMemberEmailAsString());
        data.setMemberGender(member.getMemberGenderAsInteger());
        data.setMemberBirth(member.getMemberBirthAsString());
        data.setEmployeeStatus(member.getEmployeeStatus());
        
        response.setData(data);
        return response;
    }

    private UpdateResponse createUpdateResponse(Member member) {
        UpdateResponse response = new UpdateResponse();
        response.setCode("1000");
        response.setMessage("회원 정보가 성공적으로 수정되었습니다.");
        response.setData(null);
        return response;
    }

    private UserResponse createUserResponse(Member member) {
        UserResponse response = new UserResponse();
        response.setCode("1000");
        response.setMessage("Success");
        
        UserResponse.UserData data = new UserResponse.UserData();
        data.setMemberType(member.getMemberTypeAsString());
        data.setIntegrationType(member.getIntegrationTypeAsString());
        data.setWebInfoId(member.getWebInfoId());
        data.setMembershipNo(member.getMembershipNo());
        data.setMembershipId(member.getMembershipId());
        data.setWebMemberId(member.getWebMemberId());
        data.setCmsProfileId(member.getCmsProfileId());
        data.setMemberName(member.getMemberName());
        data.setMemberFirstName(member.getMemberFirstName());
        data.setMemberMiddleName(member.getMemberMiddleName());
        data.setMemberLastName(member.getMemberLastName());
        data.setMemberMobile(member.getMemberMobileAsString());
        data.setMemberEmail(member.getMemberEmailAsString());
        data.setMemberGender(member.getMemberGenderAsInteger());
        data.setMemberBirth(member.getMemberBirthAsString());
        data.setMemberJoinDate(member.getMemberJoinDateAsString());
        data.setEmployeeStatus(member.getEmployeeStatus());
        
        response.setData(data);
        return response;
    }

    private UserListResponse.UserSummary createUserSummary(Member member) {
        UserListResponse.UserSummary summary = new UserListResponse.UserSummary();
        summary.setMemberType(member.getMemberTypeAsString());
        summary.setIntegrationType(member.getIntegrationTypeAsString());
        summary.setWebInfoId(member.getWebInfoId());
        summary.setMembershipNo(member.getMembershipNo());
        summary.setMembershipId(member.getMembershipId());
        summary.setWebMemberId(member.getWebMemberId());
        summary.setCmsProfileId(member.getCmsProfileId());
        summary.setMemberName(member.getMemberName());
        summary.setMemberFirstName(member.getMemberFirstName());
        summary.setMemberMiddleName(member.getMemberMiddleName());
        summary.setMemberLastName(member.getMemberLastName());
        summary.setMemberMobile(member.getMemberMobileAsString());
        summary.setMemberEmail(member.getMemberEmailAsString());
        summary.setMemberGender(member.getMemberGenderAsInteger());
        summary.setMemberBirth(member.getMemberBirthAsString());
        summary.setMemberJoinDate(member.getMemberJoinDateAsString());
        summary.setEmployeeStatus(member.getEmployeeStatus());
        return summary;
    }

    private LoginResponse createErrorResponse(int code, String message) {
        LoginResponse response = new LoginResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private RegisterResponse createErrorResponse(int code, String message) {
        RegisterResponse response = new RegisterResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private JoinResponse createErrorResponse(int code, String message) {
        JoinResponse response = new JoinResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private UpdateResponse createErrorResponse(int code, String message) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private UpdatePasswordResponse createErrorResponse(int code, String message) {
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private UserResponse createErrorResponse(int code, String message) {
        UserResponse response = new UserResponse();
        response.setCode(String.valueOf(code));
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    private UpdatePasswordResponse createSuccessResponse(String message) {
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.setCode("1000");
        response.setMessage(message);
        response.setData(null);
        return response;
    }
} 