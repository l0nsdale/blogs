package com.pashkevich.app.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.pashkevich.app.constants.Constants.Packages.DAO;

/**
 * Created by Vlad on 20.03.17.
 */

@Configuration
@ComponentScan(DAO)
@EnableJpaRepositories(basePackages = DAO, entityManagerFactoryRef = "emf")
public class DaoConfig {
}
