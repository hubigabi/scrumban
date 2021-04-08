package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.dto.request.EditProfileRequest;
import pl.utp.scrumban.dto.request.PasswordChangeRequest;
import pl.utp.scrumban.mapper.UserMapper;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public List<UserDto> getAllUsersDto() {
        return userRepository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDto getUserDto(long id) {
        User user = userRepository.findById(id).orElse(null);
        return userMapper.mapToUserDto(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto getUserDtoByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userMapper.mapToUserDto(user);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Boolean changePassword(long id, PasswordChangeRequest passwordChangeRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null && passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Boolean editProfile(long id, EditProfileRequest editProfileRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null && passwordEncoder.matches(editProfileRequest.getConfirmPassword(), user.getPassword())) {
            user.setName(editProfileRequest.getName());
            user.setEmail(editProfileRequest.getEmail());
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
