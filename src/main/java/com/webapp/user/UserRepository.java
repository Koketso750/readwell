package com.webapp.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    public Long countById(Integer id);
    public User findByEmailAndPassword(String username, String email);
    public User findByEmail(String email);
}
