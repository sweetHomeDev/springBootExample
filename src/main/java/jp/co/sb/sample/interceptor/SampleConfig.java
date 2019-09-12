package jp.co.sb.sample.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class SampleConfig {
    @Bean
    public SampleIntercepter acsIntercepter() {
        return new SampleIntercepter();
    }

    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/**"}, acsIntercepter());
    }
}
