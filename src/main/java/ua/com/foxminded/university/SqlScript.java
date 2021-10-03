package ua.com.foxminded.university;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

public class SqlScript {

    private DataSource dataSource;

    public SqlScript(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void executeScript(String scriptName){
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
            new ClassPathResource(scriptName));
        databasePopulator.execute(dataSource);
    }
}
