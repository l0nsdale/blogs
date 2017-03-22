package com.pashkevich.app.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.pashkevich.app.constants.Constants.Packages.SERVICES;

/**
 * Created by Vlad on 20.03.17.
 */

@Configuration
@ComponentScan(SERVICES)
public class ServiceConfig {
}
