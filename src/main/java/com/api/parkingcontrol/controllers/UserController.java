package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid UserDto userDto){
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id")UUID id){
        Optional<UserModel> userModelOptional = userService.findById(id);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id")UUID id){
        Optional<UserModel> userModelOptional = userService.findById(id);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id")UUID id, @RequestBody @Valid UserDto userDto){
        Optional<UserModel> userModelOptional = userService.findById(id);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        var userModel = userModelOptional.get();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserId(userModelOptional.get().getUserId());
        userModel.setRegistrationDate(userModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(userService.save(userModel));
    }
}
