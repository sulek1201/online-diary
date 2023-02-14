package com.gunce.diary.service;



import com.gunce.diary.dto.RegistrationRequest;
import com.gunce.diary.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Boolean register(RegistrationRequest registrationRequest);

    User findByUserName(String userName);


}
