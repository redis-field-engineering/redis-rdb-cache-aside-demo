package com.redis.caching.testcontainers;

import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Future;

public class OracleTestContainer extends JdbcDatabaseContainer<OracleTestContainer> {
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("virag/oracle-12.2.0.1-ee:latest");
    static final String IMAGE;
    private static final int ORACLE_PORT = 1521;
    private static final int APEX_HTTP_PORT = 8080;
    private static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 120;
    static final String DEFAULT_DATABASE_NAME = "ORCLPDB1";
    static final String DEFAULT_SID = "ORCLCDB";
    static final String DEFAULT_SYSTEM_USER = "system";
    static final String DEFAULT_SYS_USER = "sys";
    static final String DEFAULT_SYSTEM_PASSWORD = "Redis123";
    static final String DEFAULT_APP_USER = "rcuser";
    static final String DEFAULT_APP_USER_PASSWORD = "rcpwd";
    private static final List<String> ORACLE_SYSTEM_USERS;
    private String databaseName;
    private String username;
    private String password;
    private boolean usingSid;

    public OracleTestContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public OracleTestContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        this.databaseName = DEFAULT_DATABASE_NAME;
        this.username = DEFAULT_SYS_USER;
        this.password = DEFAULT_SYSTEM_PASSWORD;
        this.usingSid = false;
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        this.preconfigure();
    }

    public OracleTestContainer(Future<String> dockerImageName) {
        super(dockerImageName);
        this.databaseName = DEFAULT_DATABASE_NAME;
        this.username = DEFAULT_APP_USER;
        this.password = DEFAULT_APP_USER_PASSWORD;
        this.usingSid = false;
        this.preconfigure();
    }

    private void preconfigure() {
        this.waitStrategy = (new LogMessageWaitStrategy()).withRegEx(".*DATABASE IS READY TO USE!.*\\s").withTimes(1).withStartupTimeout(Duration.of(6000L, ChronoUnit.SECONDS));
        this.withConnectTimeoutSeconds(DEFAULT_CONNECT_TIMEOUT_SECONDS);
        this.addExposedPorts(ORACLE_PORT, APEX_HTTP_PORT);
    }

    public void waitUntilContainerStarted() {
        this.getWaitStrategy().waitUntilReady(this);
    }

    @NotNull
    public Set<Integer> getLivenessCheckPortNumbers() {
        return Collections.singleton(this.getMappedPort(ORACLE_PORT));
    }

    public String getDriverClassName() {
        return "oracle.jdbc.OracleDriver";
    }

    public String getJdbcUrl() {
        return this.isUsingSid() ? "jdbc:oracle:thin:@" + this.getHost() + ":" + this.getOraclePort() + ":" + this.getSid() : "jdbc:oracle:thin:@" + this.getHost() + ":" + this.getOraclePort() + "/" + this.getDatabaseName();
    }

    public String getUsername() {
        return this.isUsingSid() ? DEFAULT_SYSTEM_USER : this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    protected boolean isUsingSid() {
        return this.usingSid;
    }

    public OracleTestContainer withUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        } else if (ORACLE_SYSTEM_USERS.contains(username.toLowerCase())) {
            throw new IllegalArgumentException("Username cannot be one of " + ORACLE_SYSTEM_USERS);
        } else {
            this.username = username;
            return this.self();
        }
    }

    public OracleTestContainer withPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        } else {
            this.password = password;
            return this.self();
        }
    }

    public OracleTestContainer withDatabaseName(String databaseName) {
        if (StringUtils.isEmpty(databaseName)) {
            throw new IllegalArgumentException("Database name cannot be null or empty");
        } else if (DEFAULT_DATABASE_NAME.equals(databaseName.toLowerCase())) {
            throw new IllegalArgumentException("Database name cannot be set to ORCLCDB");
        } else {
            this.databaseName = databaseName;
            return this.self();
        }
    }

    public OracleTestContainer usingSid() {
        this.usingSid = true;
        return this.self();
    }

    public OracleTestContainer withUrlParam(String paramName, String paramValue) {
        throw new UnsupportedOperationException("The Oracle Database driver does not support this");
    }

    public String getSid() {
        return DEFAULT_SID;
    }

    public Integer getOraclePort() {
        return this.getMappedPort(ORACLE_PORT);
    }

    public Integer getWebPort() {
        return this.getMappedPort(APEX_HTTP_PORT);
    }

    public String getTestQueryString() {
        return "SELECT 1 FROM DUAL";
    }

    protected void configure() {
        this.withEnv("ORACLE_PASSWORD", this.password);
        if (!Objects.equals(this.databaseName, "ORCLCDB")) {
            this.withEnv("ORACLE_DATABASE", this.databaseName);
        }

        this.withEnv("APP_USER", this.username);
        this.withEnv("APP_USER_PASSWORD", this.password);
    }

    static {
        IMAGE = DEFAULT_IMAGE_NAME.getUnversionedPart();
        ORACLE_SYSTEM_USERS = Arrays.asList("system", "sys");
    }
}
