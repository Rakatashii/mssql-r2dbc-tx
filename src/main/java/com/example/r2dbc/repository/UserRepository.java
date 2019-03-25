package com.example.r2dbc.repository;

import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;
import org.springframework.stereotype.Repository;
import org.synchronoss.cloud.nio.stream.storage.Disposable;

import com.example.r2dbc.entity.User;

import io.r2dbc.mssql.MssqlConnection;
import io.r2dbc.mssql.MssqlConnectionFactory;
import io.r2dbc.mssql.MssqlResult;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.r2dbc.repository.QueryConstants.*;

import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private final ConnectionFactory connectionFactory;

    public UserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Mono<Integer> getCount() {
        return this.connection()
//// None of these work as desired...
//        	.doOnNext(conn -> conn.beginTransaction())
//        	.doAfterSuccessOrError((conn, e)-> conn.commitTransaction())
        		
//       	.doOnNext(conn -> {
//   			conn.beginTransaction();
//   			System.out.println("BEGIN TRANS");
//   		})
//   		.doAfterSuccessOrError((conn, e) -> {
//   			conn.commitTransaction();
//   			System.out.println("COMMIT TRANS");
//   		})
                .flatMapMany(connection -> 
                	Flux.from(connection.createStatement(SELECT_USER_COUNT).execute())
                    .flatMap(r ->
                        r.map((row, rowMetadata) ->
                        	new Integer(row.get("user_count", Integer.class))
                        )
                    )
                ).single();
    }
    
    public Mono<Void> deleteById(Integer id) {
        return this.connection()
            .flatMapMany(c -> c.createStatement("delete from user_table where user_id = @userId")
                .bind("userId", id)
                .execute()
            )
            .then();
    }

    public Flux<User> findAll() {
        return this.connection()
            .flatMapMany(connection ->
                Flux.from(connection.createStatement("select * from user_table").execute())
                    .flatMap(r ->
                        r.map((row, rowMetadata) ->
                            new User(row.get("user_id", Integer.class), row.get("user_email", String.class), row.get("user_password", String.class)
                        )
                    )
            ));
    }

    public Flux<User> save(User user) {
    	Mono<Connection> connection = this.connection();
        Flux<? extends Result> flux = connection
		    .flatMapMany(conn -> conn.createStatement(INSERT_OR_UPDATE_USER)
					.bind("userId", user.getUserId())
					.bind("userEmail", user.getEmail())
					.bind("userPassword", user.getPassword())
				.execute()
			);

        return flux.switchMap(x -> Flux.just(new User(user.getUserId(), user.getEmail(), user.getPassword())));
    }

    private Mono<Connection> connection() {
        return Mono.from(this.connectionFactory.create());
    }
}
