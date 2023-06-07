package com.redis.caching.rdb;

import java.time.Duration;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractMSSQLTest extends AbstractRedisTest {

    static final String RDB_IMAGE_NAME = "mssql.image.name";

    static final String rdbImageName = System.getProperty(RDB_IMAGE_NAME, "mcr.microsoft.com/mssql/server:2017-latest");
    static final DockerImageName rdbImage = DockerImageName.parse(rdbImageName);

    protected static final MSSQLServerContainer<?> mssqlContainer;

    static {
        mssqlContainer = new MSSQLServerContainer<>(rdbImage)
                .withEnv("ACCEPT_EULA", "Y")
                .withEnv("SA_PASSWORD", "Redis@123")
                .withEnv("MSSQL_AGENT_ENABLED", "true")
                .withNetwork(redis)
                .withNetworkAliases("redis")
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("mssql"))
                .waitingFor(Wait.forLogMessage("*SQL Server is now ready for client connections*", 1))
                .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(20)));
    }

}
