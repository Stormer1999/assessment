package com.kbtg.bootcamp.posttest.dao.repository;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {

    Optional<Lottery> findAllByTicket(String ticketId);

    Boolean existsByTicket(String ticketId);
}
