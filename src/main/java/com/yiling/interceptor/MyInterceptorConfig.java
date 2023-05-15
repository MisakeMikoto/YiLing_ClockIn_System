package com.yiling.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/8 17:07
 */
@Configuration
public class MyInterceptorConfig extends WebMvcConfigurationSupport {

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/img/**").addResourceLocations("File:"+ UPLOAD_FOLDER);
        super.addResourceHandlers(registry);
    }

}
