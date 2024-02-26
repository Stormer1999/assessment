package com.kbtg.bootcamp.posttest.dao.repository;

import com.kbtg.bootcamp.posttest.dao.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

    Optional<UserTicket> findByUserId(String userId);
}
