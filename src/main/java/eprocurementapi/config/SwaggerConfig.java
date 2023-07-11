package eprocurementapi.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${git.commit.message.short:UNKNOWN}")
    private String commitMessage;

    @Value("${git.branch:UNKNOWN}")
    private String branch;

    @Value("${git.commit.id.abbrev:UNKNOWN}")
    private String commitId;

    @Value("${git.tags:UNKNOWN}")
    private String tags;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.description}")
    private String appDescription;
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(info());
    }

    private Info info() {
        return new Info()
                .title(applicationName)
                .description(getDescription())
                .version(appVersion);
    }

    private String getDescription() {
        return "<div>" + appDescription + "</div>" +
                "<br/>" +
                "<b>Git Commit Information</b>" +
                "<ul style=\"list-style-type:none; padding-left: 0px; margin-top: 0px;\">" +
                "    <li><span style=\"float: left; width: 75px\">Branch:</span>" + branch + "</li>" +
                "    <li><span style=\"float: left; width: 75px\">Commit:</span>" + (StringUtils.isEmpty(tags) ? commitId : tags) + "</li>" +
                "</ul>";
    }
}
