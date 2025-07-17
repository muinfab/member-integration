package com.company.membership.domain.repository;

import com.company.membership.domain.Member;
import com.company.membership.domain.value.Email;
import com.company.membership.domain.value.MemberId;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    
    /**
     * 회원 저장
     */
    Member save(Member member);
    
    /**
     * 회원 ID로 조회
     */
    Optional<Member> findById(MemberId id);
    
    /**
     * 로그인 ID로 회원 조회
     */
    Optional<Member> findByLoginId(String loginId);
    
    /**
     * 이메일로 회원 조회
     */
    Optional<Member> findByEmail(Email email);
    
    /**
     * 멤버십 번호로 회원 조회
     */
    Optional<Member> findByMembershipNo(String membershipNo);
    
    /**
     * 웹 회원 ID로 회원 조회
     */
    Optional<Member> findByWebMemberId(String webMemberId);
    
    /**
     * CMS 프로필 ID로 회원 조회
     */
    Optional<Member> findByCmsProfileId(Integer cmsProfileId);
    
    /**
     * 회원 유형으로 회원 목록 조회
     */
    List<Member> findByMemberType(String memberType);
    
    /**
     * 통합 유형으로 회원 목록 조회
     */
    List<Member> findByIntegrationType(String integrationType);
    
    /**
     * 회원 상태로 회원 목록 조회
     */
    List<Member> findByStatus(String status);
    
    /**
     * 키워드로 회원 검색
     */
    List<Member> findByKeyword(String keyword, String fields, int pageNo, int limit);
    
    /**
     * 로그인 ID 존재 여부 확인
     */
    boolean existsByLoginId(String loginId);
    
    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(Email email);
    
    /**
     * 멤버십 번호 존재 여부 확인
     */
    boolean existsByMembershipNo(String membershipNo);
    
    /**
     * 웹 회원 ID 존재 여부 확인
     */
    boolean existsByWebMemberId(String webMemberId);
    
    /**
     * 회원 삭제
     */
    void delete(Member member);
    
    /**
     * 모든 회원 조회
     */
    List<Member> findAll();
    
    /**
     * 이름과 이메일로 회원 조회
     */
    Optional<Member> findByNameAndEmail(String memberName, Email email);
} 