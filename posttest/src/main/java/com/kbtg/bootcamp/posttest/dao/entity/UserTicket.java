package com.kbtg.bootcamp.posttest.dao.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_ticket")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "userTicket", cascade = CascadeType.ALL)
    private List<Lottery> tickets;

}
