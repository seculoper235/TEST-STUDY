package com.example.demo.persistence;

import com.example.demo.model.common.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<RefreshToken, String> {
}
