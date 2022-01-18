package com.redis.caching.rdb;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractPostgresTest extends AbstractRedisTest {

    static final String RDB_IMAGE_NAME = "postgres.image.name";

    static final String rdbImageName = System.getProperty(RDB_IMAGE_NAME, "postgres:12.5");
    static final DockerImageName rdbImage = DockerImageName.parse(rdbImageName);

    protected static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>(rdbImage)
                .withDatabaseName("RedisConnect")
                .withUsername("redisconnect")
                .withPassword("Redis@123")
                .withNetwork(redis)
                .withNetworkAliases("redis")
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("postgres"));
    }

}
