package com.company.membership.application;

import com.company.membership.api.dto.LoginRequest;
import com.company.membership.api.dto.LoginResponse;
import com.company.membership.domain.Member;
import com.company.membership.infrastructure.MemberRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

    private final MemberRepositoryImpl memberRepositoryImpl;

    @Autowired
    public MembershipService(MemberRepositoryImpl memberRepositoryImpl) {
        this.memberRepositoryImpl = memberRepositoryImpl;
    }

    public LoginResponse login(LoginRequest request) {
        // 1. 회원 정보 조회
        Member member = memberRepositoryImpl.findByLoginId(request.getLoginId());

        LoginResponse response = new LoginResponse();
        if (member == null) {
            response.setCode("404");
            response.setMessage("존재하지 않는 회원입니다.");
            response.setData(null);
            return response;
        }

        // 2. 비밀번호 검증 (일반 로그인만)
        if (request.getLoginType() != null && request.getLoginType() == 0) {
            if (member.getPassword() == null || !member.getPassword().equals(request.getLoginPw())) {
                response.setCode("401");
                response.setMessage("비밀번호가 일치하지 않습니다.");
                response.setData(null);
                return response;
            }
        }

        // 3. 응답 데이터 세팅 (모든 필드)
        response.setCode("1000");
        response.setMessage("Success");
        LoginResponse.LoginData data = new LoginResponse.LoginData();
        data.setMemberType(member.getMemberType());
        data.setIntegrationType(member.getIntegrationType());
        data.setWebInfoId(member.getWebInfoId());
        data.setRewardsMembershipNo(null);
        data.setRewardsMembershipId(null);
        data.setLoginId(member.getLoginId());
        data.setWebMemberId(member.getWebMemberId());
        data.setCmsProfileId(member.getCmsProfileId() != null ? String.valueOf(member.getCmsProfileId()) : null);
        data.setMemberName(member.getMemberName());
        data.setMemberFirstName(member.getMemberFirstName());
        data.setMemberMiddleName(member.getMemberMiddleName());
        data.setMemberLastName(member.getMemberLastName());
        data.setMemberMobile(member.getMemberMobile());
        data.setMemberEmail(member.getMemberEmail());
        data.setMemberGender(member.getMemberGender());
        data.setMemberBirth(member.getMemberBirth());
        data.setEmployeeStatus(member.getEmployeeStatus());
        response.setData(data);
        return response;
    }
} 