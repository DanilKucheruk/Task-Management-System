package com.tsm.mapper;

import com.tsm.dto.UserDto;
import com.tsm.entity.Role;
import com.tsm.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserDto>{
    @Override
    public UserDto map(User object) {
        UserDto userDto = new UserDto();
        userDto.setId(object.getId());
        userDto.setEmail(object.getEmail());
        userDto.setPassword(object.getPassword());
        userDto.setRole(object.getRole().name());
        return userDto;
    }
    
    public User mapToEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());   
        user.setRole(Role.valueOf(userDto.getRole())); 
        return user;
    }
}
