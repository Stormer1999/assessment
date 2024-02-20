package com.kbtg.bootcamp.posttest.dao.repository;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {

    @Query(value = "SELECT ticket FROM lottery", nativeQuery = true)
    List<String> findTickets();

    //    List<String> findLotteriesByAmountGreaterThan(int amount);
    @Query(value = "SELECT ticket FROM lottery WHERE amount > 0", nativeQuery = true)
    List<String> findAllValidLotteryTicket();

    Optional<Lottery> findAllByTicket(String ticketId);

    Boolean existsByTicket(String ticketId);
}
