package com.example.r2dbc.repository;

import org.springframework.stereotype.Repository;

import com.example.r2dbc.entity.AuditConfig;
import com.example.r2dbc.entity.User;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.r2dbc.repository.QueryConstants.*;

@Repository
public class AuditConfigRepository {
    private final ConnectionFactory connectionFactory;

    public AuditConfigRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    public Flux<AuditConfig> save(AuditConfig auditConfig) {
    	Mono<Connection> connection = this.connection();
        Flux<? extends Result> flux = connection
		    .flatMapMany(conn -> conn.createStatement(INSERT_OR_UPDATE_AUDIT_CONFIG)
					.bind("configId",  auditConfig.getAuditConfigId()).returnGeneratedValues("configid")
					.bind("clubNbr",   auditConfig.getClubNbr())
					.bind("configKey", auditConfig.getKey())
					.bind("value",     auditConfig.getValue())
					.bind("valueType", auditConfig.getValueType())
				.execute()
			);

        return flux.switchMap(x -> Flux.just(new AuditConfig(auditConfig.getAuditConfigId(), 
        		auditConfig.getClubNbr(), auditConfig.getKey(), 
        		auditConfig.getValue(), auditConfig.getValueType())));
    }
    
    private Mono<Connection> connection() {
        return Mono.from(this.connectionFactory.create());
    }
}
