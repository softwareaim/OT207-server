package com.alkemy.ong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alkemy.ong.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String username);
}
