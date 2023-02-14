package com.gunce.diary.service;



import com.gunce.diary.dto.RegistrationRequest;
import com.gunce.diary.exception.CustomerNotFoundException;
import com.gunce.diary.exception.DuplicateKeyValueException;
import com.gunce.diary.model.User;
import com.gunce.diary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Boolean register(RegistrationRequest registrationRequest) {
        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setNameSurname(registrationRequest.getNameSurname());
            user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
            user.setUsername(registrationRequest.getUsername());
            user.setStatus(true);
            Date date = new Date();
            user.setCreatedAt(date);
            userRepository.save(user);
            return Boolean.TRUE;
        }catch (DataIntegrityViolationException ex){
            throw new DuplicateKeyValueException(ex.getMessage());
        }
        catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public User findByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw new CustomerNotFoundException("User not found");
        }
        return user;
    }
}
