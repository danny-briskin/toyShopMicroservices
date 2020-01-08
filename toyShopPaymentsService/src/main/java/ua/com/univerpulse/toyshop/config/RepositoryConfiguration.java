package ua.com.univerpulse.toyshop.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableJpaRepositories(basePackages = {"ua.com.univerpulse.toyshop.model.repositories"})
@EntityScan(basePackages = {"ua.com.univerpulse.toyshop.model.entities"})
@EnableTransactionManagement
public class RepositoryConfiguration {

}
