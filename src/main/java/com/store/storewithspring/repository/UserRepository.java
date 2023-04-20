package com.store.storewithspring.repository;

import com.store.storewithspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
     User findByUsername(String userName);
}
