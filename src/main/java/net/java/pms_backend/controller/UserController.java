package net.java.pms_backend.controller;


import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    //Build Add User REST API
    @PostMapping
    public ResponseEntity<UserDto> createUser (@RequestBody UserDto userDto){
        UserDto savedUser = userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


}
