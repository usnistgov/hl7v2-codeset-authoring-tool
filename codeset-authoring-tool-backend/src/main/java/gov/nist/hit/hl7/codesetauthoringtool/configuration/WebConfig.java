package gov.nist.hit.hl7.codesetauthoringtool.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/**.png", "/**.ico")
                .addResourceLocations("classpath:/public/browser/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
    }

}
