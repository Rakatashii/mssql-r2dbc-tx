package com.example.r2dbc.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.r2dbc.entity.AuditConfig;

public interface AuditConfigR2dbcRepository extends R2dbcRepository<AuditConfig, Integer> {

}
