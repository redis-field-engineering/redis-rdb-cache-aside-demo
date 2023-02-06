package com.redis.caching.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    public static String getContainerName(String imageName) {
        imageName = StringUtils.substringAfterLast(imageName, "/");
        imageName = StringUtils.substringBeforeLast(imageName, ":");

        return imageName;
    }

    public static void createDirectory(String dir) {
        Path path = null;
        try {
            path = Paths.get(dir);
            Files.createDirectories(path);
            LOGGER.info("Directory {} is created.", path);
        } catch (IOException e) {
            LOGGER.error("Failed to create directory, {}, {}", path, e.getMessage());
        }
    }

}
