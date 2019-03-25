package com.example.r2dbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.stereotype.Component;

import com.example.r2dbc.entity.AuditConfig;
import com.example.r2dbc.entity.User;
import com.example.r2dbc.repository.AuditConfigR2dbcRepository;
import com.example.r2dbc.repository.AuditConfigRepository;
import com.example.r2dbc.repository.UserR2dbcRepository;
import com.example.r2dbc.repository.UserRepository;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;

@SpringBootApplication
@ComponentScan(basePackages= "com.example.r2dbc.config")
@EnableR2dbcRepositories
public class R2dbcApplication {
	@Autowired public ConnectionFactory connectionFactory;
	@Autowired public Publisher<? extends Connection> connection;
	
	@Autowired public UserR2dbcRepository userR2dbcRepo;
	
	@Autowired public AuditConfigR2dbcRepository auditConfigR2dbcRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(R2dbcApplication.class, args);
	}
	
	/***** AUDIT_CONFIG *****/
	@Component @Order(1)
	class TestAuditConfigRepo implements CommandLineRunner {
		@Autowired AuditConfigRepository auditConfigRepo;

		@Override
		public void run(String... args) throws Exception {
			ArrayList<List<String>> list = getSampleAuditConfigFields();
			Flux<AuditConfig> saveConfigs = Flux.just(list.get(0), list.get(1), list.get(2), 
											 list.get(3), list.get(4), list.get(5))
					.map(fields -> new AuditConfig(
							Integer.valueOf(fields.get(0)),
							Integer.valueOf(fields.get(1)),
							fields.get(2), fields.get(3), fields.get(4)))
					.flatMap(auditConfig -> this.auditConfigRepo.save(auditConfig));
			saveConfigs.subscribe(
					auditConfig -> System.out.println("Inserted or update auditConfig: " + auditConfig),
					error -> error.printStackTrace()/*,
					() -> auditConfigRepo.getCount().subscribe(count -> System.out.println("count [userRepo - before delete]: " + count))*/
			);
		}
	}
	
	@Component @Order(2)
	class TestAuditConfigR2dbcRepo implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			auditConfigR2dbcRepo.count().subscribe(count -> System.out.println(count));
		}
	}

	/***** USER *****/
	//@Component @Order(5)
	class TestUserRepo implements CommandLineRunner {
		@Autowired public UserRepository userRepo;
		
		@Override
		public void run(String... args) throws Exception {
			Random rand = new Random();
			ArrayList<List<String>> list = getSampleUserFields();
			Flux<User> saveUsers = Flux.just(list.get(0), list.get(1), list.get(2), 
											 list.get(3), list.get(4), list.get(5))
					.map(fields -> new User(Integer.valueOf(fields.get(0)),fields.get(1),fields.get(2)))
					.flatMap(user -> this.userRepo.save(user));
			saveUsers.subscribe(
					user -> System.out.println("Inserted or update user: " + user),
					error -> error.printStackTrace(),
					() -> userRepo.getCount().subscribe(count -> System.out.println("count [userRepo - before delete]: " + count))
			);
			
			userRepo.deleteById(rand.nextInt(5)).subscribe(deleted -> {
						System.out.println(deleted);
					},
					error -> error.printStackTrace(),
					() -> userRepo.getCount().subscribe(count -> System.out.println("count [userRepo - after delete]: " + count))
			);
		}
	}
	
	//@Component @Order(6)
	class TestR2dbcRepo implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			
			userR2dbcRepo.count().log().subscribe(
					count -> System.out.println("count [r2dbcRepo]: " + count),
					e -> e.printStackTrace()
			);
		}
	}

	/***** HELPERS *****/
	public ArrayList<List<String>> getSampleUserFields() {
		ArrayList<List<String>> list = new ArrayList<>();
		list.add(Arrays.asList("0", "qwertyuiop@gmail.org", "skjdabc23"));
		list.add(Arrays.asList("1", "poiuytrewq@gmail.org", "skjdbxj91"));
		list.add(Arrays.asList("2", "zxcvbnm@wal-mart.com", "oisdbsk00"));
		list.add(Arrays.asList("3", "mnbvcxz@wal-mart.com", "uksbds$56"));
		list.add(Arrays.asList("4", "asdfghjkl@mailer.com", "rzkjwb*@!"));
		list.add(Arrays.asList("5", "lkjhgfdsa@mailer.com", "kjdbsd@ii"));
		return list;
	}
	
	public ArrayList<List<String>> getSampleAuditConfigFields() {
		ArrayList<List<String>> list = new ArrayList<>();
		list.add(Arrays.asList("18", "3924", "sng", "4", "String"));
		list.add(Arrays.asList("19", "1923", "sng", "4", "String"));
		list.add(Arrays.asList("20", "2934", "sng", "6", "String"));
		list.add(Arrays.asList("21", "9134", "sng", "6", "String"));
		list.add(Arrays.asList("22", "4466", "sc" , "10", "String"));
		list.add(Arrays.asList("23", "4466", "sc" , "10", "String"));
		return list;
	}
}
