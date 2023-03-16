package com.shopee.service.impl;

import com.shopee.entity.RegistrationUserTokenEntity;
import com.shopee.entity.UserShopEntity;
import com.shopee.enumerations.UserStatus;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.repositories.RegistrationUserTokenRepository;
import com.shopee.repositories.UserRepository;
import com.shopee.request.user.SignUpRequest;
import com.shopee.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationUserTokenRepository registrationUserTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserShopEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserShopEntity findByUsername(String username) {
        UserShopEntity user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User not found");
        return user;
    }

    @Override
    public UserShopEntity signUp(SignUpRequest request) throws Exception {
        Optional<UserShopEntity> checkUserExists = userRepository.findByEmailOrUsernameOrPhoneNumber(request.getEmail(), request.getUsername(), request.getPhoneNumber());
        if(checkUserExists.isPresent())
            throw new Exception("User has exists");
        UserShopEntity newUser = modelMapper.map(request, UserShopEntity.class);
        return userRepository.save(newUser);
    }

    @Override
    public void activeUser(String token) {
        RegistrationUserTokenEntity registrationUserToken = registrationUserTokenRepository.findByToken(token);
        if(registrationUserToken == null)
            throw new AppException("Can not active this user");

        UserShopEntity user = registrationUserToken.getUser();

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        registrationUserTokenRepository.deleteByRegistrationId(registrationUserToken.getRegistrationId());
    }

    @Override
    public UserShopEntity findByEmail(String email) {
        UserShopEntity user = userRepository.findByEmail(email);
        if(user == null)
            throw new NotFoundException("This user is not exists");
        return user;
    }

    @Override
    public UserShopEntity findByUserId(Long userId) {
        UserShopEntity user = userRepository.findByUserId(userId);
        if(user == null)
            throw new NotFoundException("Can not find this user");
        return user;
    }

    @Override
    public void updateUser(UserShopEntity user) {
        userRepository.save(user);
    }

}
