package ua.com.univerpulse.toyshop.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Entity
@Table(name = "customers")
@Component
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "name")
    private String customerName;

    @Column
    private String billingAddress;

    @Column
    private Double customerBalance;

    @Column(nullable = false)
    private LocalDateTime customerActivated;

    @Column
    private LocalDateTime customerDeactivated;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Payment> paymentList = new ArrayList<>();
}
