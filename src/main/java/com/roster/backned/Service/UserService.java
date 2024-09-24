package com.roster.backned.Service;

import com.roster.backned.Dto.UserDto;
import com.roster.backned.Entity.User;
import com.roster.backned.Enumeration.Gender;
import com.roster.backned.Enumeration.Role;
import com.roster.backned.Exception.ApiException;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.SQLException;
import com.roster.backned.Exception.UnauthorizedException;
import com.roster.backned.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username"));
    }

    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userToUserDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return userToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        if (users.isEmpty()) throw new NotFoundException("Users not found");

        for (User user : users) {
            UserDto userDto = userToUserDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto updateUser(UserDto userDto) {
        try {
            User user = userRepository.findById(userDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
            userDtoToUser(userDto, user);
            user = userRepository.save(user);

            log.info("User {} updated successfully", user.getUserId());
            return userToUserDto(user);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public UserDto deleteUser(long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
            userRepository.delete(user);

            log.info("User {} deleted successfully", user.getUserId());
            return userToUserDto(user);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    private static void userDtoToUser(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setDob(userDto.getDob());
        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setRole(Role.valueOf(userDto.getRole()));
    }

    private static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setDob(user.getDob());
        userDto.setGender(String.valueOf(user.getGender()));
        userDto.setRole(String.valueOf(user.getRole()));
        return userDto;
    }
}
