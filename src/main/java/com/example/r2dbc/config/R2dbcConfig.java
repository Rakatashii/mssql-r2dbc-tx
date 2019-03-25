package com.example.r2dbc.config;

import java.time.Duration;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.example.r2dbc.repository.AuditConfigRepository;
import com.example.r2dbc.repository.UserRepository;

import io.r2dbc.mssql.MssqlConnection;
import io.r2dbc.mssql.MssqlConnectionConfiguration;
import io.r2dbc.mssql.MssqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

@Configuration
class R2dbcConfig extends AbstractR2dbcConfiguration {

	@Value("${audit.r2dbc.host}")
    private String host;
    @Value("${audit.r2dbc.port}")
    private String port;
	@Value("${audit.r2dbc.database}")
    private String database;
	@Value("${audit.r2dbc.username}")
    private String username;
	@Value("${audit.r2dbc.password}")
    private String password;
    
    public final int DEFAULT_PORT=1433;
    
    private boolean ssl;
    public final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(30);
    @Nullable private String applicationName;
    @Nullable private UUID connectionId;
    private Duration connectTimeout;

	@Bean
	@Override
	public ConnectionFactory connectionFactory() {

		MssqlConnectionConfiguration config = MssqlConnectionConfiguration.builder()
				.enableSsl()
				.host(host)
				.port(Integer.parseInt(port))
				.database(database)
				.username(username)
				.password(password)
				.connectTimeout(DEFAULT_CONNECT_TIMEOUT)
				.build();

		return new MssqlConnectionFactory(config);
	}
	
	@Bean
	DatabaseClient databaseClient() {
		return DatabaseClient.create((ConnectionFactory) connectionFactory());
	}
	
	@Bean
	public Publisher<? extends Connection> connection() {
		Publisher<? extends Connection> publisher = connectionFactory().create();
		return publisher;
	}
	
	@Bean
	public UserRepository userRepository(){
		return new UserRepository(connectionFactory());
	}
	@Bean
	public AuditConfigRepository auditConfigRepositories(){
		return new AuditConfigRepository(connectionFactory());
	}

}