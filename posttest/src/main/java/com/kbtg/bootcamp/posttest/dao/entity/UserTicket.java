package com.kbtg.bootcamp.posttest.dao.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_ticket")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(min = 10, max = 10, message = "user_ticket id size is invalid")
    private Long id;

    @Column(name = "name")
    private String name;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userTicket")
    @OneToMany(mappedBy = "userTicket", cascade = CascadeType.ALL)
    private List<Lottery> tickets;

}
