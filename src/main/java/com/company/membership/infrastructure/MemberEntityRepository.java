package com.company.membership.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberEntityRepository extends JpaRepository<MemberEntity, Long> {
    
    /**
     * 로그인 ID로 회원 조회
     */
    Optional<MemberEntity> findByLoginId(String loginId);
    
    /**
     * 이메일로 회원 조회
     */
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    
    /**
     * 멤버십 번호로 회원 조회
     */
    Optional<MemberEntity> findByMembershipNo(String membershipNo);
    
    /**
     * 웹 회원 ID로 회원 조회
     */
    Optional<MemberEntity> findByWebMemberId(String webMemberId);
    
    /**
     * CMS 프로필 ID로 회원 조회
     */
    Optional<MemberEntity> findByCmsProfileId(Integer cmsProfileId);
    
    /**
     * 회원 유형으로 회원 목록 조회
     */
    List<MemberEntity> findByMemberType(String memberType);
    
    /**
     * 통합 유형으로 회원 목록 조회
     */
    List<MemberEntity> findByIntegrationType(String integrationType);
    
    /**
     * 회원 상태로 회원 목록 조회
     */
    List<MemberEntity> findByStatus(String status);
    
    /**
     * 로그인 ID 존재 여부 확인
     */
    boolean existsByLoginId(String loginId);
    
    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByMemberEmail(String memberEmail);
    
    /**
     * 멤버십 번호 존재 여부 확인
     */
    boolean existsByMembershipNo(String membershipNo);
    
    /**
     * 웹 회원 ID 존재 여부 확인
     */
    boolean existsByWebMemberId(String webMemberId);
    
    /**
     * 키워드로 회원 검색
     */
    @Query("SELECT m FROM MemberEntity m WHERE " +
           "m.memberName LIKE %:keyword% OR " +
           "m.memberEmail LIKE %:keyword% OR " +
           "m.memberMobile LIKE %:keyword% OR " +
           "m.membershipId LIKE %:keyword%")
    Page<MemberEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 특정 필드로 회원 검색
     */
    @Query("SELECT m FROM MemberEntity m WHERE " +
           "CASE WHEN :fields LIKE '%name%' THEN m.memberName LIKE %:keyword% " +
           "WHEN :fields LIKE '%email%' THEN m.memberEmail LIKE %:keyword% " +
           "WHEN :fields LIKE '%mobile%' THEN m.memberMobile LIKE %:keyword% " +
           "WHEN :fields LIKE '%id%' THEN m.membershipId LIKE %:keyword% " +
           "ELSE (m.memberName LIKE %:keyword% OR m.memberEmail LIKE %:keyword% OR m.memberMobile LIKE %:keyword% OR m.membershipId LIKE %:keyword%) END")
    Page<MemberEntity> findByFields(@Param("keyword") String keyword, @Param("fields") String fields, Pageable pageable);
} 