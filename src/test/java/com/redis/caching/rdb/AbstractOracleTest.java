package com.redis.caching.rdb;

import com.redis.caching.testcontainers.OracleTestContainer;
import com.redis.caching.util.TestUtils;
import org.testcontainers.utility.DockerImageName;

import java.io.File;

public abstract class AbstractOracleTest extends AbstractRedisTest {

    static final String RDB_IMAGE_NAME = "oracle.image.name";

    static final String rdbImageName = System.getProperty(RDB_IMAGE_NAME, "virag/oracle-12.2.0.1-ee:latest");
    static final DockerImageName rdbImage = DockerImageName.parse(rdbImageName)
            .asCompatibleSubstituteFor("virag/oracle-12.2.0.1-ee:latest");

    static final String oraDataPath = System.getProperty("user.home") + File.separator + "oradata" + File.separator + TestUtils.getContainerName(rdbImageName);
    public static final String recoveryPath = oraDataPath + File.separator + "recovery_area";

    protected static final OracleTestContainer oracleContainer;

    static {
        oracleContainer = new OracleTestContainer(rdbImage)
                .withEnv("ORACLE_PWD", "Redis123")
                .withStartupTimeoutSeconds(9000)
                .withConnectTimeoutSeconds(9000)
                .withNetwork(redis)
                .withNetworkAliases("redis")
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("oracle"))
                .withPassword("Redis123")
                .withFileSystemBind(oraDataPath, "/opt/oracle/oradata");
    }

}
