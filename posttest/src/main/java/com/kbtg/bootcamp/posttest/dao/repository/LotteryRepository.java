package com.kbtg.bootcamp.posttest.dao.repository;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {

    @Query(value = "SELECT ticket FROM lottery", nativeQuery = true)
    List<String> findTickets();
}
