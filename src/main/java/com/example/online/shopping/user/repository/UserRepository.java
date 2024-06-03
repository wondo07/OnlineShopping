package com.example.online.shopping.user.repository;

import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.queryDsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {


    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
