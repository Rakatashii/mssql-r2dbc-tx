package com.example.r2dbc.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.FetchSpec;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.r2dbc.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.r2dbc.repository.QueryConstants.*;

public interface UserR2dbcRepository extends R2dbcRepository<User, Integer> {
	@Query(SELECT_ALL_USERS)
	Flux<User> findAllUsers();
	@Query(SELECT_USER_BY_USER_ID)
	Flux<User> findByUserId(Integer userId);
}