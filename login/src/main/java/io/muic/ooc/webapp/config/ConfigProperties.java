package io.muic.ooc.webapp.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // lombok doesnt work for some reason -> Download Plugins
@Setter
@NoArgsConstructor
@Builder
public class ConfigProperties {

    private String databaseDriverClassName;
    private String databaseConnectionUrl;
    private String databaseUsername;
    private String databasePassword;


}
