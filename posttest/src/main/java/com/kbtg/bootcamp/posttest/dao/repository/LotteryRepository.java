package com.kbtg.bootcamp.posttest.dao.repository;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dao.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {

    List<Lottery> findAllByOrderByIdAsc();

    Optional<Lottery> findAllByTicketOrderByTicket(String ticketId);

    Optional<Lottery> findAllByTicketAndUserTicketOrderByTicket(String ticket, UserTicket userTicket);

    Boolean existsByTicket(String ticketId);

}
