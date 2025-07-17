package com.company.membership.infrastructure;

import com.company.membership.domain.Member;
import com.company.membership.domain.value.Email;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    
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
     * 키워드로 회원 검색 (이름, 휴대폰, 이메일)
     */
    @Query("""
        SELECT m
        FROM MemberEntity m
        WHERE (:keyword IS NULL OR
               m.memberName LIKE %:keyword% OR
               m.memberMobile = :keyword OR
               m.memberEmail = :keyword)
        """)
    List<MemberEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 특정 필드로 회원 검색
     * fields 파라미터 예시: "name,mobile,email"
     */
    @Query("""
        SELECT m
        FROM MemberEntity m
        WHERE (
            (:fields LIKE %:#{'name'}% AND m.memberName LIKE %:keyword%) OR
            (:fields LIKE %:#{'mobile'}% AND m.memberMobile = :keyword) OR
            (:fields LIKE %:#{'email'}% AND m.memberEmail = :keyword)
        )
        """)
    List<MemberEntity> findByFields(@Param("keyword") String keyword, @Param("fields") String fields, Pageable pageable);
    
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
} 