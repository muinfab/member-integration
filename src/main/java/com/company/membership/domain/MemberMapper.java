package com.company.membership.domain;

import com.company.membership.infrastructure.MemberEntity;

public class MemberMapper {
    public static Member toDomain(MemberEntity entity) {
        if (entity == null) return null;
        Member m = new Member();
        m.setId(entity.getId());
        m.setLoginId(entity.getLoginId());
        m.setMemberType(entity.getMemberType());
        m.setIntegrationType(entity.getIntegrationType());
        m.setWebInfoId(entity.getWebInfoId());
        m.setMembershipNo(entity.getMembershipNo());
        m.setMembershipId(entity.getMembershipId());
        m.setWebMemberId(entity.getWebMemberId());
        m.setCmsProfileId(entity.getCmsProfileId());
        m.setMemberName(entity.getMemberName());
        m.setMemberFirstName(entity.getMemberFirstName());
        m.setMemberMiddleName(entity.getMemberMiddleName());
        m.setMemberLastName(entity.getMemberLastName());
        m.setMemberMobile(entity.getMemberMobile());
        m.setMemberEmail(entity.getMemberEmail());
        m.setMemberGender(entity.getMemberGender());
        m.setMemberBirth(entity.getMemberBirth());
        m.setMemberJoinDate(entity.getMemberJoinDate());
        m.setEmployeeStatus(entity.getEmployeeStatus());
        m.setPassword(entity.getPassword());
        return m;
    }

    public static MemberEntity toEntity(Member domain) {
        if (domain == null) return null;
        MemberEntity e = new MemberEntity();
        e.setId(domain.getId());
        e.setLoginId(domain.getLoginId());
        e.setMemberType(domain.getMemberType());
        e.setIntegrationType(domain.getIntegrationType());
        e.setWebInfoId(domain.getWebInfoId());
        e.setMembershipNo(domain.getMembershipNo());
        e.setMembershipId(domain.getMembershipId());
        e.setWebMemberId(domain.getWebMemberId());
        e.setCmsProfileId(domain.getCmsProfileId());
        e.setMemberName(domain.getMemberName());
        e.setMemberFirstName(domain.getMemberFirstName());
        e.setMemberMiddleName(domain.getMemberMiddleName());
        e.setMemberLastName(domain.getMemberLastName());
        e.setMemberMobile(domain.getMemberMobile());
        e.setMemberEmail(domain.getMemberEmail());
        e.setMemberGender(domain.getMemberGender());
        e.setMemberBirth(domain.getMemberBirth());
        e.setMemberJoinDate(domain.getMemberJoinDate());
        e.setEmployeeStatus(domain.getEmployeeStatus());
        e.setPassword(domain.getPassword());
        return e;
    }
} 