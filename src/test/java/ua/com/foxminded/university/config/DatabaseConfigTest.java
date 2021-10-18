package ua.com.foxminded.university.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import ua.com.foxminded.university.config.DatabaseConfig;

import javax.sql.DataSource;

@Configuration
@Import({DatabaseConfig.class})
public class DatabaseConfigTest{

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("schema.sql")
            .build();
    }
}
