package com.jay.SocialMedia.Repository;

import com.jay.SocialMedia.Entity.User;
//import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;//No need to use @Repository

public interface UserRepository extends JpaRepository<User, Long> {

}
