package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.univerpulse.toyshop.model.entities.security.Role;

import java.util.List;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  10.10.2017 for springSecToken project.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT ROLENAME FROM ST_ROLES r JOIN ST_USERS s on s.userId=r.userid"
            + " WHERE s.email= :email"
            , nativeQuery = true)
    List<String> getRoleNamesByUser(String email);
}
