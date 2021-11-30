package ua.com.foxminded.university.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.com.foxminded.university.service.ClassroomService;

@Configuration
public class TestContext {

    @Bean
    public ClassroomService classroomService() {
        return Mockito.mock(ClassroomService.class);
    }
}
