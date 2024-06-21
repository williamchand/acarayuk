package com.amenodiscovery.authentication.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "com.amenodiscovery.authentication.task" })
public class SpringTaskConfig {

}
