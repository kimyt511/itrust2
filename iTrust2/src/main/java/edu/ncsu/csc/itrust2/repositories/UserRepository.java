package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.ncsu.csc.iTrust2.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername ( String username );
    
    List<User> findByUsernameContaining(String keyword);
}
