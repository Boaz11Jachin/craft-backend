package org.codenova.craft.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private LocalDate dueDate;
    private Integer priority;
    private String status;


    @PrePersist // ?? - 최초 저장(?) 인서트때. 업데이트는 수정때
    protected void prePersist () {
        createdAt = LocalDateTime.now();
        priority = 10;
        status = "CREATED";
    }


}
