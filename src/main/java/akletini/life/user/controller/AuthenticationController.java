package akletini.life.user.controller;

import akletini.life.user.dto.UserDto;
import akletini.life.user.dto.mapper.UserMapper;
import akletini.life.user.repository.entity.auth.AuthenticationRequest;
import akletini.life.user.repository.entity.auth.AuthenticationResponse;
import akletini.life.user.service.api.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        AuthenticationResponse response =
                authenticationService.register(userMapper.dtoToUser(userDto));
        UserDto responseUser = userMapper.userToDto(response.getUser());
        responseUser.setJwtToken(response.getToken());
        return ResponseEntity.ok(responseUser);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
