package ua.com.univerpulse.toyshop.config;

import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableJpaRepositories(basePackages = {"ua.com.univerpulse.toyshop.model.repositories"})
@EntityScan(basePackages = {"ua.com.univerpulse.toyshop.model.entities"})
@EnableTransactionManagement
public class RepositoryConfiguration {
    /**
     * To be able to connect to DB from outside the application
     *
     * @return server
     * @throws SQLException when unable to create
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }
}
