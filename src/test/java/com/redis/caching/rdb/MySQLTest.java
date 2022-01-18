package com.redis.caching.rdb;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySQLTest extends AbstractMySQLTest {

    private static final Logger LOGGER = LoggerFactory.getLogger("cache-aside-demo");
    private static final String instanceId = ManagementFactory.getRuntimeMXBean().getName();

    private Connection getMySQLConnection() throws SQLException {
        return DriverManager.getConnection(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(),
                mysqlContainer.getPassword());
    }

    private StatefulRedisConnection<String, String> getRedisConnection() {
        return RedisClient.create(redisContainer.getRedisURI()).connect();
    }

    @BeforeAll
    static void beforeAll() {
        LOGGER.info("Instance: {} Running beforeAll", instanceId);
        // Start Redis Enterprise container
        LOGGER.info("Instance: {} Starting Redis Image: {}", instanceId, redisImageName);
        redisContainer.start();
        LOGGER.info("Instance: {} {} container is running now", instanceId, redisContainer.getContainerName());

        // Start and setup MySQL container
        LOGGER.info("Instance: {} Starting MySQL Image: {}", instanceId, rdbImageName);
        mysqlContainer.start();
        LOGGER.info("Instance: {} {} container is running now", instanceId, mysqlContainer.getContainerName());
    }

    @Test
    @Order(1)
    void testRedisConnection() {
        // Setup Redis Enterprise
        LOGGER.info("Instance: {} Redis Database is up! Now testing PING command..", instanceId);
        Instant startedAt = Instant.now();
        try (StatefulRedisConnection<String, String> redisConnection = getRedisConnection()) {
            assertEquals("PONG", redisConnection.sync().ping());
            LOGGER.info("Instance: {} Total Redis connection test time: {}", instanceId, Duration.between(startedAt, Instant.now()));
        }
    }

    @Test
    @Order(2)
    void testMySQLConnection() {
        LOGGER.info("Instance: {} MySQL Database is up! Now testing JDBC URL and Test Query..", instanceId);

        LOGGER.info("Instance: {} JDBC URL: {}", instanceId, mysqlContainer.getJdbcUrl());
        LOGGER.info("Instance: {} Query String: {}", instanceId, mysqlContainer.getTestQueryString());

        Instant startedAt = Instant.now();
        String jdbcUrl = mysqlContainer.getJdbcUrl();
        MatcherAssert.assertThat(jdbcUrl, containsString("/"));
        LOGGER.info("Instance: {} Total MySQL connection test time: {}", instanceId, Duration.between(startedAt, Instant.now()));
    }

    @Test
    @Order(3)
    public void testInsert() throws Exception {
        try (Connection MySQLConnection = getMySQLConnection()) {
            Statement statement = MySQLConnection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS emp (\n" +
                    "  empno int NOT NULL,\n" +
                    "  fname varchar(50),\n" +
                    "  lname varchar(50),\n" +
                    "  job varchar(50),\n" +
                    "  mgr int,\n" +
                    "  hiredate datetime,\n" +
                    "  sal decimal(13, 4),\n" +
                    "  comm decimal(13, 4),\n" +
                    "  dept int,\n" +
                    "  PRIMARY KEY (empno)\n" +
                    "  );";
            String insertQuery = "insert into RedisConnect.emp values (1, 'Virag', 'Tripathi', 'PFE', 19, '2018-08-05 04:07:50', 90101.34, 1235.13, 96)\n";
            LOGGER.info("Instance: {} Creating EMP table", instanceId);
            statement.execute(createTableQuery);
            LOGGER.info("Instance: {} Inserting a record into EMP table", instanceId);
            statement.execute(insertQuery);
            statement.execute("SELECT empno FROM RedisConnect.emp");
            try (ResultSet resultSet = statement.getResultSet()) {
                resultSet.next();
                String empno = resultSet.getString(1);
                assertEquals("1", empno, "Value from emp should equal real value");

                LOGGER.info("Instance: {} emp {} record in MySQL", instanceId, empno);
                ResultSet rs = statement.executeQuery("SELECT * FROM RedisConnect.emp");
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = rs.getString(i);
                        LOGGER.info("{} : {}", rsmd.getColumnName(i), columnValue);
                    }
                }
            }
        }
    }

    @Test
    @Order(4)
    public void testCacheAside() throws Exception {
        StatefulRedisConnection<String, String> redisConnection = getRedisConnection();
        Map<String, String> record = redisConnection.sync().hgetall("emp:1");
        Map<String, String> values = new HashMap<>();

        if (record.isEmpty()) {
            LOGGER.info("Instance: {} Employee 1 not found in Redis", instanceId);

            long pgStartTime = System.currentTimeMillis();
            try (Connection MySQLConnection = getMySQLConnection()) {
                Statement statement = MySQLConnection.createStatement();
                statement.execute("SELECT * FROM RedisConnect.emp");
                try (ResultSet resultSet = statement.getResultSet()) {
                    resultSet.next();
                    String empno = String.valueOf(resultSet.getInt(1));
                    values.put("empno", empno);
                    values.put("fname", resultSet.getString(2));
                    values.put("lname", resultSet.getString(3));
                    values.put("job", resultSet.getString(4));
                    values.put("mgr", String.valueOf(resultSet.getInt(5)));
                    values.put("hiredate", String.valueOf(resultSet.getDate(6)));
                    values.put("sal", String.valueOf(resultSet.getDouble(7)));
                    values.put("comm", String.valueOf(resultSet.getDouble(8)));
                    values.put("dept", String.valueOf(resultSet.getInt(9)));

                    LOGGER.info("Instance: {} Employee {} found in MySQL", instanceId, empno);
                    LOGGER.info("Instance: {} MySQL Query time: {} ms", instanceId, System.currentTimeMillis() - pgStartTime);

                    long redisStartTime = System.currentTimeMillis();
                    LOGGER.info("Instance: {} Caching Employee {} in Redis", instanceId, empno);
                    redisConnection.sync().hset("emp:" + empno, values);
                    LOGGER.info("Instance: {} Employee {} record in Redis {}", instanceId, empno, redisConnection.sync().hgetall("emp:1"));
                    LOGGER.info("Instance: {} Redis Query time: {} ms", instanceId, System.currentTimeMillis() - redisStartTime);
                }
            }
        }
    }

    @AfterAll
    static void afterAll() {
        LOGGER.info("Instance: {} running afterAll", instanceId);
    }

}
