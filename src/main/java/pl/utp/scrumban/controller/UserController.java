package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.dto.request.EditProfileRequest;
import pl.utp.scrumban.dto.request.PasswordChangeRequest;
import pl.utp.scrumban.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsersDto();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") long id) {
        UserDto userDto = userService.getUser(id);

        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        UserDto userDto = userService.getUserByEmail(email);

        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("changePassword/{id}")
    public ResponseEntity<Boolean> changePassword(@PathVariable("id") long id,
                                                  @RequestBody @Validated PasswordChangeRequest passwordChangeRequest) {
        Boolean isChanged = userService.changePassword(id, passwordChangeRequest);
        return new ResponseEntity<>(isChanged, HttpStatus.OK);
    }

    @PutMapping("editProfile/{id}")
    public ResponseEntity<Boolean> editProfile(@PathVariable("id") long id,
                                               @RequestBody @Validated EditProfileRequest editProfileRequest) {
        Boolean isEdited = userService.editProfile(id, editProfileRequest);
        return new ResponseEntity<>(isEdited, HttpStatus.OK);
    }

}