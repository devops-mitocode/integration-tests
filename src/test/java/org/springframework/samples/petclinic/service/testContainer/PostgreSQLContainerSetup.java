package org.springframework.samples.petclinic.service.testContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.time.Duration;

@Testcontainers
public abstract class PostgreSQLContainerSetup {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSQLContainerSetup.class);

    // Configuración centralizada y clara
    private static final String DEFAULT_BUILD_TAG = "default_build_tag";
    private static final String DATABASE_NAME = "petclinic";
    private static final String DATABASE_USER = "petclinic";
    private static final String DATABASE_PASSWORD = "petclinic";
    private static final String POSTGRES_IMAGE = "postgres:16.3";
    private static final String NETWORK_ALIAS = "db";
    private static final String CONTAINER_SUFFIX = "-db-1";

    @Container
    static final PostgreSQLContainer<?> postgreSQLContainer = createPostgreSQLContainer();

    static {
        LOGGER.info("🐳 TESTCONTAINER SETUP INICIADO");
    }

    private static PostgreSQLContainer<?> createPostgreSQLContainer() {
        String buildTag = getBuildTag();
        String networkName = buildNetworkName(buildTag);
        String containerName = buildContainerName(buildTag);

        logContainerConfiguration(networkName, containerName);

        return new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USER)
            .withPassword(DATABASE_PASSWORD)
            .waitingFor(createWaitStrategy())
            .withStartupTimeout(Duration.ofMinutes(3));
    }

    private static String getBuildTag() {
        String tag = System.getenv("BUILD_TAG");
        if (tag == null || tag.isEmpty()) {
            LOGGER.warn("Environment variable BUILD_TAG is not set. Using default: {}", DEFAULT_BUILD_TAG);
            return DEFAULT_BUILD_TAG;
        }
        LOGGER.info("Using BUILD_TAG: {}", tag);
        return tag;
    }

    private static String buildNetworkName(String buildTag) {
        return buildTag + "_default";
    }

    private static String buildContainerName(String buildTag) {
        return buildTag + CONTAINER_SUFFIX;
    }

    private static WaitAllStrategy createWaitStrategy() {
        return new WaitAllStrategy()
            .withStartupTimeout(Duration.ofMinutes(3))
            .withStrategy(Wait.forListeningPort())
            .withStrategy(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 1))
            .withStrategy(Wait.forSuccessfulCommand("pg_isready -U " + DATABASE_USER));
    }

    private static void logContainerConfiguration(String networkName, String containerName) {
        LOGGER.info("PostgreSQL Container Configuration:");
        LOGGER.info("  Image: {}", POSTGRES_IMAGE);
        LOGGER.info("  Database: {}", DATABASE_NAME);
        LOGGER.info("  Database: {}", DATABASE_NAME);
        LOGGER.info("  User: {}", DATABASE_USER);
        LOGGER.info("  Network: {}", networkName);
        LOGGER.info("  Container Name: {}", containerName);
        LOGGER.info("  Network Alias: {}", NETWORK_ALIAS);
    }

    @DynamicPropertySource
    static void configurePostgreSQLProperties(DynamicPropertyRegistry registry) {
        LOGGER.info("Configuring Spring Boot with PostgreSQL container");
        LOGGER.info("JDBC URL: {}", postgreSQLContainer.getJdbcUrl());

        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // JPA Configuration
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");

        // SQL Initialization
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.schema-locations", () -> "classpath:db/postgres/schema.sql");
        registry.add("spring.sql.init.data-locations", () -> "classpath:db/postgres/data.sql");
    }

    // Utility methods for subclasses
    protected static String getJdbcUrl() {
        if (!isContainerRunning()) {
            throw new IllegalStateException("PostgreSQL container is not running");
        }
        return postgreSQLContainer.getJdbcUrl();
    }

    protected static boolean isContainerRunning() {
        return postgreSQLContainer != null && postgreSQLContainer.isRunning();
    }

    protected static PostgreSQLContainer<?> getContainer() {
        return postgreSQLContainer;
    }

    protected static String getDatabaseName() {
        return DATABASE_NAME;
    }

    protected static String getDatabaseUser() {
        return DATABASE_USER;
    }
}
