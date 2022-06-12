package com.alkemy.ong.dataseed;

import com.alkemy.ong.auth.repository.UserRepository;
import com.alkemy.ong.enums.RolName;
import com.alkemy.ong.model.Role;
import com.alkemy.ong.model.UserEntity;
import com.alkemy.ong.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() {

        if (roleRepository.count() == 0) {
            this.roleRepository.save(new Role(RolName.ROLE_ADMIN.toString()));
            this.roleRepository.save(new Role(RolName.ROLE_USER.toString()));
        }
        List<Role> adminRoles = new ArrayList<>();
        List<Role> userRoles = new ArrayList<>();

        Optional<Role> rolAdmin = this.roleRepository.findByName(RolName.ROLE_ADMIN.toString());
        Optional<Role> rolUser = this.roleRepository.findByName(RolName.ROLE_USER.toString());

        if (rolAdmin.isEmpty()) {
            Role admin = this.roleRepository.save(new Role(RolName.ROLE_ADMIN.toString()));
            adminRoles.add(admin);
        } else {
            adminRoles.add(rolAdmin.get());
        }

        if (rolUser.isEmpty()) {
            Role user = this.roleRepository.save(new Role(RolName.ROLE_USER.toString()));
            adminRoles.add(user);
            userRoles.add(user);
        } else {
            adminRoles.add(rolUser.get());
            userRoles.add(rolUser.get());
        }

        if (userRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                userRepository.save(userBuilder("carlos" + (i + 1) + "@admin.com", "test1234", adminRoles));
                userRepository.save(userBuilder("maria" + (i + 1) + "@user.com", "test1234", userRoles));
            }
        }
        System.out.println(userRepository.count());
    }

    private UserEntity userBuilder(String username, String password, List<Role> roles) {
        UserEntity user = new UserEntity();
        user.setFirstName(username.split("@")[0]);
        user.setLastName(username.split("@")[1].split("\\.")[0]);
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        return user;
    }

}

