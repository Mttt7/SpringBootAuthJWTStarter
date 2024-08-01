package com.mt.jwtstarter.service;



import com.mt.jwtstarter.model.UserEntity;

public interface AuthService {

    UserEntity getLoggedUser();
}
