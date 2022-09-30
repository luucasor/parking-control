package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    final
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<UserModel> findById(UUID id) {
        return userRepository.findById(id);
    }

    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }
}
