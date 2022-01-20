package com.tamudatathon.bulletin.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private SubmissionInterceptor submissionInterceptor;

    @Autowired
    private AllInterceptor allInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Value("${app.api.basepath}")
    private String basePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.allInterceptor)
            .order(Ordered.HIGHEST_PRECEDENCE);
        registry.addInterceptor(this.submissionInterceptor)
            .addPathPatterns(this.basePath + "/submission/**")
            .excludePathPatterns("**/accolades/**");
        registry.addInterceptor(this.adminInterceptor)
            .addPathPatterns(this.basePath + "/events/**");
    }
};