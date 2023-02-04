package akletini.life.user.controller;

import akletini.life.user.dto.UserDto;
import akletini.life.user.dto.mapper.UserMapper;
import akletini.life.user.repository.entity.User;
import akletini.life.user.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        User storedUser = userService.store(userMapper.dtoToUser(userDto));
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(storedUser));
    }

    @PostMapping("/logout")
    public void logout() {

    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(user));
    }

}
