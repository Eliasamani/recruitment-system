package se.kth.iv1201.recruitment.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configures the application's database connection.
 */
@Configuration
public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());

    /**
     * Creates a datasource bean using the DATABASE_URL environment variable.
     * DATABASE_URL should be in the format: postgres://<username>:<password>@<host>/<dbname>
     * @return a DataSource bean
     * @throws URISyntaxException if the DATABASE_URL is not a valid URI
     */
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        try {
            String databaseUrl = System.getenv("DATABASE_URL");
            if (databaseUrl == null || databaseUrl.isEmpty()) {
                LOGGER.severe("DATABASE_URL environment variable is missing or empty.");
                throw new URISyntaxException("", "DATABASE_URL is not set");
            }
            
            URI dbUri = new URI(databaseUrl);
            LOGGER.info("Connecting to database at: " + dbUri.getHost());

            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url("jdbc:postgresql://" + databaseUrl.split("@")[1] + "?sslmode=prefer");
            dataSourceBuilder.username(dbUri.getUserInfo().split(":")[0]);
            dataSourceBuilder.password(dbUri.getUserInfo().split(":")[1]);

            LOGGER.info("Database connection successfully configured.");
            return dataSourceBuilder.build();
        } catch (URISyntaxException | NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Error configuring database connection: " + e.getMessage(), e);
            throw e;
        }
    }
}
