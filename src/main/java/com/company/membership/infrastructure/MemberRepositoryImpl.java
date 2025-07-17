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
    private final MemberEntityRepository memberEntityRepository;

    @Autowired
    public MemberRepositoryImpl(MemberEntityRepository memberEntityRepository) {
        this.memberEntityRepository = memberEntityRepository;
    }

    @Override
    public Member save(Member member) {
        MemberEntity entity = MemberMapper.toEntity(member);
        MemberEntity savedEntity = memberEntityRepository.save(entity);
        return MemberMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Member> findById(MemberId id) {
        if (id == null) return Optional.empty();
        Optional<MemberEntity> entityOpt = memberEntityRepository.findById(id.getValue());
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByLoginId(loginId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        if (email == null) return Optional.empty();
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByMemberEmail(email.toString());
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByMembershipNo(String membershipNo) {
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByMembershipNo(membershipNo);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByWebMemberId(String webMemberId) {
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByWebMemberId(webMemberId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByCmsProfileId(Integer cmsProfileId) {
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByCmsProfileId(cmsProfileId);
        return entityOpt.map(MemberMapper::toDomain);
    }

    @Override
    public List<Member> findByMemberType(String memberType) {
        List<MemberEntity> entities = memberEntityRepository.findByMemberType(memberType);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByIntegrationType(String integrationType) {
        List<MemberEntity> entities = memberEntityRepository.findByIntegrationType(integrationType);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByStatus(String status) {
        List<MemberEntity> entities = memberEntityRepository.findByStatus(status);
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findByKeyword(String keyword, String fields, int pageNo, int limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        List<MemberEntity> entities;
        
        if (fields != null && !fields.isEmpty()) {
            entities = memberEntityRepository.findByFields(keyword, fields, pageable).getContent();
        } else {
            entities = memberEntityRepository.findByKeyword(keyword, pageable).getContent();
        }
        
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return memberEntityRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByEmail(Email email) {
        if (email == null) return false;
        return memberEntityRepository.existsByMemberEmail(email.toString());
    }

    @Override
    public boolean existsByMembershipNo(String membershipNo) {
        return memberEntityRepository.existsByMembershipNo(membershipNo);
    }

    @Override
    public boolean existsByWebMemberId(String webMemberId) {
        return memberEntityRepository.existsByWebMemberId(webMemberId);
    }

    @Override
    public void delete(Member member) {
        MemberEntity entity = MemberMapper.toEntity(member);
        memberEntityRepository.delete(entity);
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> entities = memberEntityRepository.findAll();
        return entities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findByNameAndEmail(String memberName, Email email) {
        if (email == null) return Optional.empty();
        Optional<MemberEntity> entityOpt = memberEntityRepository.findByMemberNameAndMemberEmail(memberName, email.toString());
        return entityOpt.map(MemberMapper::toDomain);
    }
} 