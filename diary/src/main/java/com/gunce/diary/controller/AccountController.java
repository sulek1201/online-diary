package com.gunce.diary.controller;



import com.gunce.diary.dto.LoginRequest;
import com.gunce.diary.dto.RegistrationRequest;
import com.gunce.diary.dto.TokenResponse;
import com.gunce.diary.exception.CustomerNotNullException;
import com.gunce.diary.model.User;
import com.gunce.diary.security.JwtTokenUtil;
import com.gunce.diary.service.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/account")
@RestController
public class AccountController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws AuthenticationException {

        if(StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword())){
            throw new CustomerNotNullException("User name or password can not be null");
        }
        final User user = userService.findByUserName(request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(user.getUsername(), token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Boolean> register(@RequestBody RegistrationRequest registrationRequest) throws AuthenticationException {

        if(StringUtils.isBlank(registrationRequest.getUsername())
                || StringUtils.isBlank(registrationRequest.getEmail())
                || StringUtils.isBlank(registrationRequest.getNameSurname())
                || StringUtils.isBlank(registrationRequest.getPassword())){
            throw new CustomerNotNullException("One of the fields of Registiration Request can not be null");
        }

        Boolean response = userService.register(registrationRequest);
        return ResponseEntity.ok(response);
    }
}
