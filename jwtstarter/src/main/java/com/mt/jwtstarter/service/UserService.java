package com.mt.jwtstarter.service;

import com.mt.jwtstarter.dto.Auth.UserResponseDto;
import org.springframework.data.domain.Page;


public interface UserService {

    Long getUserId();

    UserResponseDto getUserProfileById(Long userId);

    Boolean checkUsernameAvailability(String username);

    Boolean checkEmailAvailability(String email);

    Page<UserResponseDto> searchForUser(String query, int pageNumber, int pageSize);

}
