package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dao.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.dao.repository.UserTicketRepository;
import com.kbtg.bootcamp.posttest.dto.LotteryRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;

    private final UserTicketRepository userTicketRepositoryl;

    public LotteryService(LotteryRepository lotteryRepository, UserTicketRepository userTicketRepositoryl) {
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepositoryl = userTicketRepositoryl;
    }

    public Lottery addLotteryTicket(LotteryRequestDto lotteryRequestDto) {
        Lottery lottery = new Lottery();
        lottery.setTicket(lotteryRequestDto.ticket());
        lottery.setPrice(lotteryRequestDto.price());
        lottery.setAmount(lotteryRequestDto.amount());
        lottery.setUserTicket(null);

        return lotteryRepository.save(lottery);
    }

    public List<String> listAllLottery() {
        return lotteryRepository.findTickets();
    }
}
