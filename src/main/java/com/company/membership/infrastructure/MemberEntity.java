package com.company.membership.infrastructure;

import javax.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "loginId"))
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    private String password;
    private String memberType;
    private String integrationType;
    private Integer webInfoId;
    private String membershipNo;
    private String membershipId;
    private String webMemberId;
    private Integer cmsProfileId;
    private String memberName;
    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;
    private String memberMobile;
    private String memberEmail;
    private Integer memberGender;
    private String memberBirth;
    private String memberJoinDate;
    private String employeeStatus;

    // getter/setter 생략
} 