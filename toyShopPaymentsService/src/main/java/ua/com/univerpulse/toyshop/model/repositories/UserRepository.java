package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.univerpulse.toyshop.model.entities.security.User;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  10.10.2017 for springSecToken project.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
