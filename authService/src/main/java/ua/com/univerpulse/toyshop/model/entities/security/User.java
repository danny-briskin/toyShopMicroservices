package ua.com.univerpulse.toyshop.model.entities.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  10.10.2017 for springSecToken project.
 */
@Entity
@Table(name = "st_users")
@Data
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Role> roleList = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime created;
}
