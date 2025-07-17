package com.company.membership.infrastructure;

import javax.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "loginId"))
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    private String password;
    private String memberType;
    private String integrationType;
    private Integer webInfoId;
    private String membershipNo;
    private String membershipId;
    private String webMemberId;
    private Integer cmsProfileId;
    private String memberName;
    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;
    private String memberMobile;
    private String memberEmail;
    private Integer memberGender;
    private String memberBirth;
    private String memberJoinDate;
    private String employeeStatus;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = integrationType;
    }

    public Integer getWebInfoId() {
        return webInfoId;
    }

    public void setWebInfoId(Integer webInfoId) {
        this.webInfoId = webInfoId;
    }

    public String getMembershipNo() {
        return membershipNo;
    }

    public void setMembershipNo(String membershipNo) {
        this.membershipNo = membershipNo;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getWebMemberId() {
        return webMemberId;
    }

    public void setWebMemberId(String webMemberId) {
        this.webMemberId = webMemberId;
    }

    public Integer getCmsProfileId() {
        return cmsProfileId;
    }

    public void setCmsProfileId(Integer cmsProfileId) {
        this.cmsProfileId = cmsProfileId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberFirstName() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    public String getMemberMiddleName() {
        return memberMiddleName;
    }

    public void setMemberMiddleName(String memberMiddleName) {
        this.memberMiddleName = memberMiddleName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Integer getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public String getMemberBirth() {
        return memberBirth;
    }

    public void setMemberBirth(String memberBirth) {
        this.memberBirth = memberBirth;
    }

    public String getMemberJoinDate() {
        return memberJoinDate;
    }

    public void setMemberJoinDate(String memberJoinDate) {
        this.memberJoinDate = memberJoinDate;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
} 