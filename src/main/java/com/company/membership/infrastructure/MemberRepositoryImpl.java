package com.company.membership.infrastructure;

import com.company.membership.domain.Member;
import com.company.membership.domain.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByLoginId(String loginId) {
        MemberEntity entity = memberRepository.findByLoginId(loginId);
        return MemberMapper.toDomain(entity);
    }

    // 필요시 도메인 Member 저장/수정 메서드도 추가 가능
} 