package org.cross.osprey;

import org.cross.osprey.config.OspreyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(OspreyProperties.class)
@SpringBootApplication
public class OspreyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OspreyApplication.class, args);
    }

}
