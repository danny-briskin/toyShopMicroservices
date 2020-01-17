package ua.com.univerpulse.toyshop.model.entities.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  27.10.2017 for springSecToken project.
 */
@Entity
@Table(name = "st_roles")
@Data
@NoArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer roleId;
    private String roleName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_user_role"))
    private User user;
}
