package com.redis.caching.rdb;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractMySQLTest extends AbstractRedisTest {

    static final String RDB_IMAGE_NAME = "mysql.image.name";

    static final String rdbImageName = System.getProperty(RDB_IMAGE_NAME, "mysql:latest");
    static final DockerImageName rdbImage = DockerImageName.parse(rdbImageName);

    protected static final MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>(rdbImage)
                .withEnv("MYSQL_ROOT_PASSWORD", "Redis@123")
                .withUsername("redisconnect")
                .withPassword("Redis@123")
                .withDatabaseName("RedisConnect")
                .withNetwork(redis)
                .withNetworkAliases("redis")
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("mysql"));
    }

}
