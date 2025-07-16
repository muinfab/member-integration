package com.company.membership.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String memberType;         // 회원 유형 (WU: 통합회원, R: Rewards, W: Web)
    private String integrationType;    // 통합 유형 (UU: 통합대상, T: 전환대상, N: 미대상)
    private Integer webInfoId;         // 웹 회원 전용 시퀀스 아이디
    private String membershipNo;       // 멤버십 No
    private String membershipId;       // 멤버십 아이디 / 통합 아이디
    private String webMemberId;        // 웹회원 아이디
    private Integer cmsProfileId;      // CMS 프로필 아이디
    private String memberName;         // 한글 이름
    private String memberFirstName;    // 영문 이름
    private String memberMiddleName;   // 영문 중간 이름
    private String memberLastName;     // 영문 성
    private String memberMobile;       // 휴대전화
    private String memberEmail;        // 이메일
    private Integer memberGender;      // 성별 (00: 남성, 1: 여성, 2: 기타)
    private String memberBirth;        // 생년월일 (YYYYMMDD)
    private String memberJoinDate;     // 가입일 (YYYYMMDD)
    private String employeeStatus;     // 임직원여부 (Y/N)
    private String password;           // 비밀번호
} 