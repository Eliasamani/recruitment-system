package se.kth.iv1201.recruitment.config;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class RecruitmentConfig {
@Bean
public DataSource getDataSource() throws URISyntaxException{


        //DATABASE_URL environment variable needs to be in format postgres://<username>:<password>@<host>/<dbname> 
        String postEnv = System.getenv("DATABASE_URL");
        URI postURI = new URI(postEnv);
        DataSourceBuilder dataSb = DataSourceBuilder.create();
        dataSb.driverClassName("org.postgresql.Driver");
        dataSb.url("jdbc:postgresql://"+postEnv.split("@")[1]+"?sslmode=prefer");
        dataSb.password(postURI.getUserInfo().split(":")[1]);
        dataSb.username(postURI.getUserInfo().split(":")[0]);

        return dataSb.build();

}
}
