package com.github.maitmus.springrole.service;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.dto.user.DeleteCurrentUserResponse;
import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.entity.user.User;
import com.github.maitmus.springrole.exception.NotFoundException;
import com.github.maitmus.springrole.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetails getUser(Long id) {
        User user = userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() ->
                new NotFoundException("User not found, id: " + id));

        return new UserDetails(user);
    }

    @Transactional
    public UserDetails updateUserRole(Long userId, List<Role> roles) {
        User user = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE).orElseThrow(() ->
                new NotFoundException("User not found, id: " + userId));

        user.updateRoles(roles);

        User updatedUser = this.userRepository.save(user);

        return new UserDetails(updatedUser);
    }

    @Transactional
    public DeleteCurrentUserResponse deleteCurrentUser(Long id) {
        User currentUser = userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found, id: " + id));

        currentUser.delete();

        User deletedUser = this.userRepository.save(currentUser);

        return new DeleteCurrentUserResponse(deletedUser.getId());
    }
}
