package kr.addictionliberation.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(authSetting())
                .addSecurityItem(new SecurityRequirement().addList("jwt"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Addiction Liberation API") // API 제목
                .description("Addiction Liberation 프로젝트 API 명세서") // API 설명
                .version("1.0.0"); // API 버전
    }
    private Components authSetting() {
        return new Components()
                .addSecuritySchemes("jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization"));
    }
}