package com.company.membership.domain;

import com.company.membership.domain.enums.Gender;
import com.company.membership.domain.enums.IntegrationType;
import com.company.membership.domain.enums.MemberStatus;
import com.company.membership.domain.enums.MemberType;
import com.company.membership.domain.value.Email;
import com.company.membership.domain.value.MemberId;
import com.company.membership.domain.value.Password;
import com.company.membership.domain.value.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Member {
    private MemberId id;
    private MemberType memberType;
    private IntegrationType integrationType;
    private Integer webInfoId;
    private String membershipNo;
    private String membershipId;
    private String webMemberId;
    private Integer cmsProfileId;
    private String memberName;
    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;
    private PhoneNumber memberMobile;
    private Email memberEmail;
    private Gender memberGender;
    private LocalDate memberBirth;
    private LocalDate memberJoinDate;
    private String employeeStatus;
    private Password password;
    private MemberStatus status;
    private List<TermsAgreement> termsAgreements;
    
    public Member() {
        this.termsAgreements = new ArrayList<>();
        this.status = MemberStatus.ACTIVE;
        this.memberJoinDate = LocalDate.now();
    }
    
    // 비즈니스 메서드들
    public void changePassword(Password newPassword) {
        this.password = newPassword;
    }
    
    public void changeEmail(Email newEmail) {
        this.memberEmail = newEmail;
    }
    
    public void changeMobile(PhoneNumber newMobile) {
        this.memberMobile = newMobile;
    }
    
    public void withdraw(String reason) {
        this.status = MemberStatus.WITHDRAWN;
        // 탈퇴 이력 생성 로직은 서비스에서 처리
    }
    
    public void lock() {
        this.status = MemberStatus.LOCKED;
    }
    
    public void unlock() {
        this.status = MemberStatus.ACTIVE;
    }
    
    public boolean canLogin() {
        return this.status.canLogin();
    }
    
    public boolean isUnifiedMember() {
        return this.memberType.isUnified();
    }
    
    public boolean isRewardsMember() {
        return this.memberType.isRewards();
    }
    
    public boolean isWebMember() {
        return this.memberType.isWeb();
    }
    
    public boolean isUnifiedTarget() {
        return this.integrationType.isUnifiedTarget();
    }
    
    public boolean isTransitionTarget() {
        return this.integrationType.isTransitionTarget();
    }
    
    public boolean isNotTarget() {
        return this.integrationType.isNotTarget();
    }
    
    public void addTermsAgreement(TermsAgreement termsAgreement) {
        this.termsAgreements.add(termsAgreement);
    }
    
    public boolean hasAgreedToTerms(String termInfoId) {
        return this.termsAgreements.stream()
                .anyMatch(terms -> terms.getTermInfoId().equals(termInfoId) && terms.getAgree());
    }
    
    public int getAge() {
        if (this.memberBirth == null) {
            return 0;
        }
        return LocalDate.now().getYear() - this.memberBirth.getYear();
    }
    
    public boolean isAdult() {
        return getAge() >= 19;
    }
    
    public boolean isEmployee() {
        return "Y".equals(this.employeeStatus);
    }
    
    // 편의 메서드들
    public String getMemberBirthAsString() {
        return this.memberBirth != null ? 
            this.memberBirth.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null;
    }
    
    public String getMemberJoinDateAsString() {
        return this.memberJoinDate != null ? 
            this.memberJoinDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null;
    }
    
    public String getMemberMobileAsString() {
        return this.memberMobile != null ? this.memberMobile.toString() : null;
    }
    
    public String getMemberEmailAsString() {
        return this.memberEmail != null ? this.memberEmail.toString() : null;
    }
    
    public Integer getMemberGenderAsInteger() {
        return this.memberGender != null ? this.memberGender.getCode() : null;
    }
    
    public String getMemberTypeAsString() {
        return this.memberType != null ? this.memberType.getCode() : null;
    }
    
    public String getIntegrationTypeAsString() {
        return this.integrationType != null ? this.integrationType.getCode() : null;
    }
    
    public String getMemberStatusAsString() {
        return this.status != null ? this.status.getCode() : null;
    }
} 