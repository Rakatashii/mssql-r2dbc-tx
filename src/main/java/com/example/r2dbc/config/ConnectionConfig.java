//package com.example.r2dbc.config;
//
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.r2dbc.function.DatabaseClient;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//
//import io.r2dbc.mssql.MssqlConnection;
//import io.r2dbc.mssql.MssqlConnectionConfiguration;
//import io.r2dbc.mssql.MssqlConnectionFactory;
//import io.r2dbc.spi.ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactoryOptions;
//import io.r2dbc.spi.Option;
//import reactor.core.publisher.Mono;
//
//@Configuration
////@EnableR2dbcRepositories
//public class ConnectionConfig {
//	private String driver;
//	@Value("${spring.application.name}")
//	private String applicationName;
//	@Value("${spring.datasource.username}")
//	private String username;
//	@Value("${spring.datasource.password}")
//	private String password;
//	@Value("${audit.r2dbc.url}")
//	private String url;
//	@Value("${audit.r2dbc.host}")
//	private String host;
//	@Value("${audit.r2dbc.port}")
//	private String port;
//	@Value("${audit.r2dbc.database}")
//	private String database;
//	
//	private Option<String> DRIVER, HOST, PORT, USER, PASSWORD, 
//						   DATABASE, SSL, APPLICATION_NAME, CONNECTION_ID;
//	
//	public void meth() {
//		ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
//			    .option(DRIVER, driver)
//			    .option(HOST, host)
//			    .option(PORT, port) // optional, defaults to 1433
//			    .option(USER, username)
//			    .option(PASSWORD, password)
//			    .option(DATABASE, database) // optional
//			    //.option(SSL, true) // optional, defaults to false
//			    //.option(Option.valueOf("APPLICATION_NAME"), applicationName) // optional
//			    //.option(Option.valueOf("CONNECTION_ID"), new UUID()) // optional
//			    .build();
//	}
//
//	@Bean
//	@Order(1)
//	public MssqlConnectionConfiguration mssqlConnectionConfiguration() {
//		MssqlConnectionConfiguration configuration = MssqlConnectionConfiguration.builder()
//				//.applicationName(applicationName)
//				.host(host)
//				.port(Integer.valueOf(/*port*/MssqlConnectionConfiguration.DEFAULT_PORT))
//				.database(database)
//				.username(username)
//				.password(password)
//				.build();
//		
//		return configuration;
//	}
//	
//	@Bean
//	@Order(2)
//	public MssqlConnectionFactory mssqlConnectionFactory() {
//		MssqlConnectionFactory factory = new MssqlConnectionFactory(mssqlConnectionConfiguration());
//		return factory;
//	}
//	
//	@Bean
//	@Order(3)
//	public Mono<MssqlConnection> connection() {
//		Mono<MssqlConnection> connectMono = mssqlConnectionFactory().create();
//		return connectMono;
//	}
//	
//	@Bean
//	@Order(4)
//	public DatabaseClient databaseClient() {
//		DatabaseClient databaseClient = DatabaseClient.create(mssqlConnectionFactory());
//		return databaseClient;
//	}
//}
