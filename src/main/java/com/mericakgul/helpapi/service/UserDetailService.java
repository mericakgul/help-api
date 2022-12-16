package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectExistence;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final BusyPeriodService busyPeriodService;
    private final ObjectExistence objectExistence;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return objectExistence.checkIfUserExistsAndReturn(username);
    }

    public UserResponse save(UserRequest userRequest) {
        User user = this.dtoMapper.mapModel(userRequest, User.class);
        List<BusyPeriod> busyPeriods = this.busyPeriodService.saveAll(user.getBusyPeriods());
        user.setBusyPeriods(busyPeriods);
        // We do not need to save addresses explicitly since we added cascade to addresses property of User entity.
        User savedUser = this.userRepository.save(user);
        return this.dtoMapper.mapModel(savedUser, UserResponse.class);
    }
}
