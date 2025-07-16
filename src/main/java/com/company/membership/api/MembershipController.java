package com.company.membership.api;

import org.springframework.web.bind.annotation.*;
import com.company.membership.api.dto.*;
import com.company.membership.application.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/v1/membership/integration")
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return membershipService.login(request);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return null;
    }

    @GetMapping("/terms")
    public TermsResponse getTerms(@RequestParam String language) {
        return null;
    }

    @GetMapping("/check/id")
    public CheckIdResponse checkId(@RequestParam String checkInfo) {
        return null;
    }

    @GetMapping("/check/email")
    public CheckEmailResponse checkEmail(@RequestParam String checkInfo) {
        return null;
    }

    @PostMapping("/join")
    public JoinResponse join(@RequestBody JoinRequest request) {
        return null;
    }

    @PutMapping("/update")
    public UpdateResponse update(@RequestBody UpdateRequest request) {
        return null;
    }

    @PostMapping("/update")
    public UpdateResponse passwordCheck(@RequestBody PasswordCheckRequest request) {
        return null;
    }

    @PutMapping("/update/pw")
    public UpdatePasswordResponse updatePassword(@RequestBody UpdatePasswordRequest request) {
        return null;
    }

    @GetMapping("/user")
    public UserResponse getUser(@RequestParam String loginId) {
        return null;
    }

    @GetMapping("/user/list")
    public UserListResponse getUserList(@RequestParam String keyword, @RequestParam(required = false) String fields, @RequestParam int pageNo, @RequestParam int limit) {
        return null;
    }
} 