package ua.com.univerpulse.toyshop.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Entity
@Table(name = "payments")
@Component
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Payment  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer paymentId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customerID", foreignKey = @ForeignKey(name = "fk_customer_payment"))
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "paymentAmount")
    private Double paymentAmount;

    @Column
    private String channel;

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", customer=" + customer.getId() +
                ", paymentDate=" + paymentDate +
                ", paymentAmount=" + paymentAmount +
                ", channel='" + channel + '\'' +
                '}';
    }
}
