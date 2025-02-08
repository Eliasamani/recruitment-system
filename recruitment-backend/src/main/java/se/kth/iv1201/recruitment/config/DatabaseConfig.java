package se.kth.iv1201.recruitment.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the application's database connection.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Creates a datasource bean using the DATABASE_URL environment variable.
     * DATABASE_URL should be in the format: postgres://<username>:<password>@<host>/<dbname>
     * @return a DataSource bean
     * @throws URISyntaxException if the DATABASE_URL is not a valid URI
     */
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        // DATABASE_URL should be in the format: postgres://<username>:<password>@<host>/<dbname>
        String databaseUrl = System.getenv("DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://" + databaseUrl.split("@")[1] + "?sslmode=prefer");
        dataSourceBuilder.username(dbUri.getUserInfo().split(":")[0]);
        dataSourceBuilder.password(dbUri.getUserInfo().split(":")[1]);

        return dataSourceBuilder.build();
    }
}
