package com.shoppingmall.cashshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 이미지 리소스를 제공하는 경로 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + itemImgLocation + "/");
    }
}
