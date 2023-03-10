package akletini.life.user.controller;

import akletini.life.user.dto.UserDto;
import akletini.life.user.dto.mapper.UserMapper;
import akletini.life.user.repository.entity.User;
import akletini.life.user.service.api.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        User storedUser = userService.store(userMapper.dtoToUser(userDto));
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(storedUser));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateTodo(@RequestBody UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        userService.getById(user.getId());
        User updatedUser = userService.store(user);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(updatedUser));
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(user));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToDto(user));
    }

}
