package com.company.membership.infrastructure;

import com.company.membership.domain.Member;
import com.company.membership.domain.enums.Gender;
import com.company.membership.domain.enums.IntegrationType;
import com.company.membership.domain.enums.MemberStatus;
import com.company.membership.domain.enums.MemberType;
import com.company.membership.domain.value.Email;
import com.company.membership.domain.value.MemberId;
import com.company.membership.domain.value.Password;
import com.company.membership.domain.value.PhoneNumber;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemberMapper {
    public static Member toDomain(MemberEntity entity) {
        if (entity == null) return null;
        
        Member member = new Member();
        member.setId(MemberId.of(entity.getId()));
        member.setMemberType(MemberType.fromCode(entity.getMemberType()));
        member.setIntegrationType(IntegrationType.fromCode(entity.getIntegrationType()));
        member.setWebInfoId(entity.getWebInfoId());
        member.setMembershipNo(entity.getMembershipNo());
        member.setMembershipId(entity.getMembershipId());
        member.setWebMemberId(entity.getWebMemberId());
        member.setCmsProfileId(entity.getCmsProfileId());
        member.setMemberName(entity.getMemberName());
        member.setMemberFirstName(entity.getMemberFirstName());
        member.setMemberMiddleName(entity.getMemberMiddleName());
        member.setMemberLastName(entity.getMemberLastName());
        
        // Value Object 변환
        if (entity.getMemberMobile() != null) {
            member.setMemberMobile(PhoneNumber.of(entity.getMemberMobile()));
        }
        if (entity.getMemberEmail() != null) {
            member.setMemberEmail(Email.of(entity.getMemberEmail()));
        }
        if (entity.getMemberGender() != null) {
            member.setMemberGender(Gender.fromCode(entity.getMemberGender()));
        }
        if (entity.getMemberBirth() != null) {
            member.setMemberBirth(LocalDate.parse(entity.getMemberBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        if (entity.getMemberJoinDate() != null) {
            member.setMemberJoinDate(LocalDate.parse(entity.getMemberJoinDate(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        if (entity.getPassword() != null) {
            member.setPassword(Password.fromHashed(entity.getPassword()));
        }
        if (entity.getStatus() != null) {
            member.setStatus(MemberStatus.fromCode(entity.getStatus()));
        }
        
        member.setEmployeeStatus(entity.getEmployeeStatus());
        
        return member;
    }

    public static MemberEntity toEntity(Member domain) {
        if (domain == null) return null;
        
        MemberEntity entity = new MemberEntity();
        entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
        entity.setMemberType(domain.getMemberTypeAsString());
        entity.setIntegrationType(domain.getIntegrationTypeAsString());
        entity.setWebInfoId(domain.getWebInfoId());
        entity.setMembershipNo(domain.getMembershipNo());
        entity.setMembershipId(domain.getMembershipId());
        entity.setWebMemberId(domain.getWebMemberId());
        entity.setCmsProfileId(domain.getCmsProfileId());
        entity.setMemberName(domain.getMemberName());
        entity.setMemberFirstName(domain.getMemberFirstName());
        entity.setMemberMiddleName(domain.getMemberMiddleName());
        entity.setMemberLastName(domain.getMemberLastName());
        
        // Value Object를 String으로 변환
        entity.setMemberMobile(domain.getMemberMobileAsString());
        entity.setMemberEmail(domain.getMemberEmailAsString());
        entity.setMemberGender(domain.getMemberGenderAsInteger());
        entity.setMemberBirth(domain.getMemberBirthAsString());
        entity.setMemberJoinDate(domain.getMemberJoinDateAsString());
        entity.setPassword(domain.getPassword() != null ? domain.getPassword().getHashedValue() : null);
        entity.setStatus(domain.getMemberStatusAsString());
        entity.setEmployeeStatus(domain.getEmployeeStatus());
        
        return entity;
    }
} 