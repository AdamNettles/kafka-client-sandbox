import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
@PropertySource("classpath:application-local-test.yml")
class ExPropertySourcesPlaceholderConfigurer {

    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
