package com.amenodiscovery.authentication.spring;

import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import ua_parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class LoginNotificationConfig {

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }

    @Value("classpath:maxmind/GeoLite2-City.mmdb")
    Resource resourceFile;

    @Bean(name="GeoIPCity")
    public DatabaseReader databaseReader() throws IOException {
        InputStream dbAsStream = resourceFile.getInputStream(); // <-- this is the difference
        return new DatabaseReader
            .Builder(dbAsStream)
            .fileMode(FileMode.MEMORY)
            .build();
    }
}
