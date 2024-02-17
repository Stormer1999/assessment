package com.kbtg.bootcamp.posttest.dao.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;

@Entity
@Table(name = "lottery")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Lottery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lotteryNumber;
}
