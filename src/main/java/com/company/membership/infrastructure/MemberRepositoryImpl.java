package com.company.membership.infrastructure;

import com.company.membership.domain.Member;
import com.company.membership.domain.MemberMapper;
import com.company.membership.domain.repository.MemberRepository;
import com.company.membership.domain.value.Email;
import com.company.membership.domain.value.MemberId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        MemberEntity entity = MemberMapper.toEntity(member);
        MemberEntity savedEntity = memberRepository.save(entity);
        return MemberMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Member> findById(MemberId id) {
        if (id == null) return Optional.empty();
        Optional<MemberEntity> entityOpt = memberRepository.findById(id.getValue());
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        Optional<MemberEntity> entityOpt = memberRepository.findByLoginId(loginId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        if (email == null) return Optional.empty();
        Optional<MemberEntity> entityOpt = memberRepository.findByMemberEmail(email.toString());
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByMembershipNo(String membershipNo) {
        Optional<MemberEntity> entityOpt = memberRepository.findByMembershipNo(membershipNo);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByWebMemberId(String webMemberId) {
        Optional<MemberEntity> entityOpt = memberRepository.findByWebMemberId(webMemberId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByCmsProfileId(Integer cmsProfileId) {
        Optional<MemberEntity> entityOpt = memberRepository.findByCmsProfileId(cmsProfileId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public List<Member> findByMemberType(String memberType) {
        List<MemberEntity> entities = memberRepository.findByMemberType(memberType);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByIntegrationType(String integrationType) {
        List<MemberEntity> entities = memberRepository.findByIntegrationType(integrationType);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByStatus(String status) {
        List<MemberEntity> entities = memberRepository.findByStatus(status);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByKeyword(String keyword, String fields, int pageNo, int limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        List<MemberEntity> entities;
        
        if (fields != null && !fields.isEmpty()) {
            entities = memberRepository.findByFields(keyword, fields, pageable);
        } else {
            entities = memberRepository.findByKeyword(keyword, pageable);
        }
        
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByEmail(Email email) {
        if (email == null) return false;
        return memberRepository.existsByMemberEmail(email.toString());
    }

    @Override
    public boolean existsByMembershipNo(String membershipNo) {
        return memberRepository.existsByMembershipNo(membershipNo);
    }

    @Override
    public boolean existsByWebMemberId(String webMemberId) {
        return memberRepository.existsByWebMemberId(webMemberId);
    }

    @Override
    public void delete(Member member) {
        MemberEntity entity = MemberMapper.toEntity(member);
        memberRepository.delete(entity);
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> entities = memberRepository.findAll();
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }
} 