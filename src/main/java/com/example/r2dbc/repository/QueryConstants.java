package com.example.r2dbc.repository;

public class QueryConstants {
	/*** No Binding Variable Queries ***/
	public static final String SELECT_ALL_USERS=
			"SELECT * FROM user_table";
	public static final String SELECT_USER_COUNT=
			"SELECT COUNT(user_id) AS user_count FROM user_table";

	/*** IUserR2dbcRepository Queries ***/
	public static final String SELECT_USER_BY_USER_ID=
			"SELECT user_id, user_email, user_password FROM user_table WHERE user_table.user_id = $1";
	
	/*** UserRepository Queries ***/
	public static final String INSERT_OR_UPDATE_USER=
			  "BEGIN TRAN \n"
			+ "IF EXISTS (SELECT * FROM user_table WHERE user_id=@userId) \n"
			+ "BEGIN \n"
			+ "   UPDATE user_table SET user_email=@userEmail, user_password=@userPassword \n"
			+ "   WHERE user_id=@userId \n"
			+ "END \n"
			+ "ELSE \n"
			+ "BEGIN \n"
			+ "   INSERT INTO user_table (user_id, user_email, user_password) \n"
			+ "   VALUES (@userId, @userEmail, @userPassword) \n"
			+ "END \n"
			+ "COMMIT TRAN \n";
	
	public static final String INSERT_OR_UPDATE_AUDIT_CONFIG=
			  "BEGIN TRAN \n"
			+ "IF EXISTS (SELECT * FROM audit.auditconfig WHERE configid=@configId) \n"
			+ "BEGIN \n"
			+ "   UPDATE audit.auditconfig SET clubnbr=@clubNbr, configkey=@configKey, value=@value, valuetype=@valueType \n"
			+ "   WHERE configid=@configId \n"
			+ "END \n"
			+ "ELSE \n"
			+ "BEGIN \n"
			+ "   INSERT INTO audit.auditconfig (clubnbr, configkey, value, valuetype) \n"
			+ "   VALUES (@clubNbr, @configKey, @value, @valueType) \n"
			+ "END \n"
			+ "COMMIT TRAN \n";
}
