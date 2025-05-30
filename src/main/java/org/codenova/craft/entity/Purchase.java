package org.codenova.craft.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.ToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int orderQuantity;

    private String status;

    @ManyToOne
    private Order sourceOrderId;

    @OneToOne
    private OrderItem sourceOrderItemId;

    private LocalDateTime createdAt;

    private LocalDate expectedAt;

}
