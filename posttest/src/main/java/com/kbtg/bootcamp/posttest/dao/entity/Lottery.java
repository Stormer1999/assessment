package com.kbtg.bootcamp.posttest.dao.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "lottery")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Lottery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket", unique = true)
    @Size(min = 6, max = 6, message = "tickets size must have 6 digits")
    private String ticket;

    @Column(name = "price")
    private Double price;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ticket_user_id")
    private UserTicket userTicket;
}
