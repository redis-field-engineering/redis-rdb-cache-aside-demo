package com.redis.caching.rdb;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.redis.testcontainers.RedisEnterpriseContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractRedisTest {

    static final String REDIS_IMAGE_NAME = "redis.image.name";

    static final Network redis = Network.newNetwork();

    static final String redisImageName = System.getProperty(REDIS_IMAGE_NAME, "redislabs/redis:latest");
    static final DockerImageName redisImage = DockerImageName.parse(redisImageName);

    protected static final RedisEnterpriseContainer redisContainer;

    static {
        redisContainer = new RedisEnterpriseContainer(redisImage)
                .withNetwork(redis)
                .withNetworkAliases("redis")
                .withCreateContainerCmdModifier(CreateContainerCmd::getHostConfig)
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("redis"));
    }

}
