package com.waggle.config;

import com.waggle.global.request.OctetStreamReadMsgConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OctetStreamReadMsgConverter octetStreamReadMsgConverter;

    @Autowired
    public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter) {
        this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(octetStreamReadMsgConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            //.allowedOrigins("https://waggle.o-r.kr")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true)
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
