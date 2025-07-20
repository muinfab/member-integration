-- H2O 멤버십 통합 시스템 데이터베이스 스키마
-- MySQL 8.0 기준

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS h2o_membership
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE h2o_membership;

-- 1. 회원 테이블 (member)
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 ID',
    member_type VARCHAR(1) NOT NULL COMMENT 'U:통합회원, R:Rewards, W:Web',
    integration_type VARCHAR(1) NOT NULL COMMENT 'U:통합대상, T:전환대상, N:미대상',
    login_id VARCHAR(50) UNIQUE NOT NULL COMMENT '통합 로그인 ID',
    membership_id VARCHAR(50) COMMENT '멤버십 ID',
    web_member_id VARCHAR(100) COMMENT '웹회원 ID',
    web_info_id BIGINT COMMENT '웹회원 전용 시퀀스',
    cms_profile_id BIGINT COMMENT 'CMS 프로필 ID',
    member_name VARCHAR(100) NOT NULL COMMENT '한글 이름',
    member_first_name VARCHAR(50) COMMENT '영문 이름',
    member_middle_name VARCHAR(50) COMMENT '영문 중간 이름',
    member_last_name VARCHAR(50) COMMENT '영문 성',
    member_mobile VARCHAR(20) NOT NULL COMMENT '휴대전화',
    member_email VARCHAR(100) NOT NULL COMMENT '이메일',
    member_gender INT COMMENT '0:남성, 1:여성, 2:기타',
    member_birth VARCHAR(8) COMMENT '생년월일 YYYYMMDD',
    member_zip_code VARCHAR(10) COMMENT '우편번호',
    member_address1 VARCHAR(200) COMMENT '주소1',
    member_address2 VARCHAR(100) COMMENT '주소2',
    member_join_date VARCHAR(8) COMMENT '가입일 YYYYMMDD',
    employee_status VARCHAR(1) DEFAULT 'N' COMMENT '임직원여부',
    password_hash VARCHAR(255) COMMENT '암호화된 비밀번호',
    member_status VARCHAR(1) DEFAULT '00' COMMENT '00:유효, 1:계정잠금, 2:탈퇴',
    withdrawl_yn VARCHAR(1) DEFAULT 'N' COMMENT '탈퇴여부',
    withdrawl_date VARCHAR(8) COMMENT '탈퇴일자 YYYYMMDD',
    membership_type VARCHAR(1) COMMENT 'G:골드, P:플래티넘, L:스마트',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    INDEX idx_login_id (login_id),
    INDEX idx_membership_id (membership_id),
    INDEX idx_web_member_id (web_member_id),
    INDEX idx_member_email (member_email),
    INDEX idx_member_mobile (member_mobile),
    INDEX idx_member_name (member_name),
    INDEX idx_member_status (member_status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보 테이블';

-- 2. 이메일 인증 테이블 (email_certification)
CREATE TABLE email_certification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '인증 고유 ID',
    email VARCHAR(100) NOT NULL COMMENT '이메일 주소',
    certification_key VARCHAR(6) NOT NULL COMMENT '인증번호',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, VERIFIED, EXPIRED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    expires_at TIMESTAMP NOT NULL COMMENT '만료일시',
    verified_at TIMESTAMP NULL COMMENT '인증완료일시',
    
    INDEX idx_email_key (email, certification_key),
    INDEX idx_status (status),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이메일 인증 테이블';

-- 3. 멤버십 결제 테이블 (membership_payment)
CREATE TABLE membership_payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    membership_type VARCHAR(1) NOT NULL COMMENT 'G:골드, P:플래티넘, L:스마트',
    payment_bill_no VARCHAR(50) NOT NULL COMMENT '결제 빌번호',
    payment_amount INT NOT NULL COMMENT '결제 금액',
    payment_date VARCHAR(14) NOT NULL COMMENT '결제일시 YYYYMMDDhhmmss',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id),
    INDEX idx_payment_bill_no (payment_bill_no),
    INDEX idx_payment_date (payment_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='멤버십 결제 테이블';

-- 4. 약관 동의 테이블 (terms_agreement)
CREATE TABLE terms_agreement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '약관동의 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    term_info_id VARCHAR(20) NOT NULL COMMENT '약관 유형 ID',
    agree_yn VARCHAR(1) NOT NULL COMMENT 'Y:동의, N:거부',
    version INT NOT NULL COMMENT '약관 버전',
    title VARCHAR(100) NOT NULL COMMENT '약관 제목',
    agree_date VARCHAR(14) NOT NULL COMMENT '동의일시 YYYYMMDDhhmmss',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id),
    INDEX idx_term_info_id (term_info_id),
    INDEX idx_agree_date (agree_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='약관 동의 테이블';

-- 5. 공통코드 테이블 (common_code)
CREATE TABLE common_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '공통코드 고유 ID',
    class_code VARCHAR(20) NOT NULL COMMENT '분류 코드',
    code VARCHAR(20) NOT NULL COMMENT '코드',
    code_name VARCHAR(100) NOT NULL COMMENT '코드명',
    sort_order INT DEFAULT 0 COMMENT '정렬 순서',
    use_yn VARCHAR(1) DEFAULT 'Y' COMMENT '사용여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    UNIQUE KEY uk_class_code (class_code, code),
    INDEX idx_class_code (class_code),
    INDEX idx_use_yn (use_yn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='공통코드 테이블';

-- 6. 로그인 히스토리 테이블 (login_history)
CREATE TABLE login_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '로그인 히스토리 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    login_id VARCHAR(50) NOT NULL COMMENT '로그인 ID',
    login_type INT NOT NULL COMMENT '0:일반로그인, 1:간편로그인',
    login_ip VARCHAR(45) COMMENT '로그인 IP',
    user_agent TEXT COMMENT '사용자 에이전트',
    login_result VARCHAR(20) NOT NULL COMMENT 'SUCCESS, FAIL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '로그인일시',
    
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id),
    INDEX idx_login_id (login_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='로그인 히스토리 테이블';

-- 7. 탈퇴 히스토리 테이블 (withdrawal_history)
CREATE TABLE withdrawal_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '탈퇴 히스토리 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    reason VARCHAR(500) COMMENT '탈퇴 사유',
    withdrawal_date VARCHAR(8) NOT NULL COMMENT '탈퇴일자 YYYYMMDD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id),
    INDEX idx_withdrawal_date (withdrawal_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='탈퇴 히스토리 테이블';

-- 8. 약관 정보 테이블 (terms_info)
CREATE TABLE terms_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '약관 정보 고유 ID',
    term_info_id VARCHAR(20) NOT NULL COMMENT '약관 유형 ID',
    term_item_info_id INT COMMENT '약관 세부 ID',
    apply_date VARCHAR(8) NOT NULL COMMENT '적용일자 YYYYMMDD',
    expire_date VARCHAR(8) COMMENT '만료일자 YYYYMMDD',
    required_yn VARCHAR(1) NOT NULL COMMENT 'Y:필수, N:선택',
    version INT NOT NULL COMMENT '약관 버전',
    title VARCHAR(100) NOT NULL COMMENT '약관 유형 명칭',
    sub_title VARCHAR(200) COMMENT '약관 세부 제목',
    contents TEXT COMMENT '약관 세부 내용',
    language VARCHAR(2) DEFAULT 'ko' COMMENT '언어 코드',
    receive_methods JSON COMMENT '수신 방법 배열',
    use_yn VARCHAR(1) DEFAULT 'Y' COMMENT '사용여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    UNIQUE KEY uk_term_version (term_info_id, version, language),
    INDEX idx_term_info_id (term_info_id),
    INDEX idx_language (language),
    INDEX idx_use_yn (use_yn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='약관 정보 테이블';

-- 샘플 데이터 삽입

-- 공통코드 샘플 데이터
INSERT INTO common_code (class_code, code, code_name, sort_order) VALUES
('GENDER', '0', '남성', 1),
('GENDER', '1', '여성', 2),
('GENDER', '2', '기타', 3),
('MEMBER_STATUS', '00', '유효', 1),
('MEMBER_STATUS', '1', '계정잠금', 2),
('MEMBER_STATUS', '2', '탈퇴', 3),
('MEMBERSHIP_TYPE', 'G', '골드', 1),
('MEMBERSHIP_TYPE', 'P', '플래티넘', 2),
('MEMBERSHIP_TYPE', 'L', '스마트', 3),
('WITHDRAWAL_REASON', 'PERSONAL', '개인정보 보호', 1),
('WITHDRAWAL_REASON', 'SERVICE', '서비스 불만', 2),
('WITHDRAWAL_REASON', 'DUPLICATE', '중복 가입', 3),
('WITHDRAWAL_REASON', 'ETC', '기타', 4);

-- 약관 정보 샘플 데이터
INSERT INTO terms_info (term_info_id, term_item_info_id, apply_date, expire_date, required_yn, version, title, sub_title, contents, language) VALUES
('TOS', 10, '20250101', '99991231', 'Y', 4, '이용약관', '더파르나스 이용약관', '더파르나스 이용약관 내용입니다.', 'ko'),
('PRIVACY', 20, '20250101', '99991231', 'Y', 3, '개인정보처리방침', '개인정보 수집 및 이용', '개인정보 수집 및 이용에 대한 동의입니다.', 'ko'),
('MARKETING', 30, '20250101', '99991231', 'N', 2, '마케팅 정보 활용 동의', '마케팅 정보 수신 동의', '마케팅 정보 수신에 대한 동의입니다.', 'ko'),
('TOS', 10, '20250101', '99991231', 'Y', 4, 'Terms of Service', 'The Parnas Terms of Service', 'The Parnas Terms of Service content.', 'en'),
('PRIVACY', 20, '20250101', '99991231', 'Y', 3, 'Privacy Policy', 'Privacy Collection and Use', 'Consent for privacy collection and use.', 'en');

-- 테스트용 회원 데이터
INSERT INTO member (member_type, integration_type, login_id, membership_id, web_member_id, member_name, member_mobile, member_email, member_gender, member_birth, member_join_date, password_hash, member_status) VALUES
('U', 'N', 'testuser1', 'PM00004020', 'test1@gmail.com', '홍길동', '01012345678', 'test1@gmail.com', 0, '19900101', '20240101', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '00'),
('R', 'U', 'testuser2', 'PM00004021', NULL, '김철수', '01023456789', 'test2@gmail.com', 0, '19900202', '20240102', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '00'),
('W', 'U', 'testuser3', NULL, 'test3@gmail.com', '이영희', '01034567890', 'test3@gmail.com', 1, '19900303', '20240103', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '00');

-- 뷰 생성 (자주 사용되는 조회를 위한 뷰)
CREATE VIEW v_member_info AS
SELECT 
    m.id,
    m.member_type,
    m.integration_type,
    m.login_id,
    m.membership_id,
    m.web_member_id,
    m.web_info_id,
    m.cms_profile_id,
    m.member_name,
    m.member_first_name,
    m.member_middle_name,
    m.member_last_name,
    m.member_mobile,
    m.member_email,
    m.member_gender,
    m.member_birth,
    m.member_join_date,
    m.employee_status,
    m.member_status,
    m.withdrawl_yn,
    m.withdrawl_date,
    m.membership_type,
    m.created_at,
    m.updated_at,
    CASE 
        WHEN m.membership_id IS NOT NULL AND m.web_member_id IS NOT NULL THEN 'U'
        WHEN m.membership_id IS NOT NULL THEN 'R'
        ELSE 'W'
    END AS calculated_member_type
FROM member m;

-- 인덱스 최적화를 위한 추가 인덱스
CREATE INDEX idx_member_composite ON member(member_type, member_status, created_at);
CREATE INDEX idx_member_search ON member(member_name, member_mobile, member_email);
CREATE INDEX idx_email_certification_email_status ON email_certification(email, status);
CREATE INDEX idx_terms_agreement_member_term ON terms_agreement(member_id, term_info_id);
CREATE INDEX idx_membership_payment_member_type ON membership_payment(member_id, membership_type); 