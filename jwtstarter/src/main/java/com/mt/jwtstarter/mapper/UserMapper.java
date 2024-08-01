package com.mt.jwtstarter.mapper;



import com.mt.jwtstarter.dto.Auth.UserResponseDto;
import com.mt.jwtstarter.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public static UserResponseDto mapToUserResponseDto(UserEntity userEntity){
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .about(userEntity.getAbout())
                .photoUrl(userEntity.getProfileImage())
                .createdAt(userEntity.getCreatedAt())
                .roles(userEntity.getRoles())
                .build();
    }
}
