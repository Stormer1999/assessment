package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dao.entity.UserTicket;
import com.kbtg.bootcamp.posttest.dao.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.dao.repository.UserTicketRepository;
import com.kbtg.bootcamp.posttest.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class LotteryService {

    private final LotteryRepository lotteryRepository;

    private final UserTicketRepository userTicketRepository;

    public LotteryService(LotteryRepository lotteryRepository, UserTicketRepository userTicketRepository) {
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
    }

    public Lottery addLotteryTicket(LotteryRequestDto lotteryRequestDto) {
        if (Boolean.TRUE.equals(lotteryRepository.existsByTicket(lotteryRequestDto.ticket()))) {
            throw new BadRequestException("ticket is duplicated");
        }

        Lottery lottery = new Lottery();
        lottery.setTicket(lotteryRequestDto.ticket());
        lottery.setPrice(lotteryRequestDto.price());
        lottery.setAmount(lotteryRequestDto.amount());
        lottery.setUserTicket(null);

        return lotteryRepository.save(lottery);
    }

    public List<String> listAllLotteryTicket() {
        return lotteryRepository.findAllValidLotteryTicket();
    }

    @Transactional
    public Long buyLotteryTicket(Long userId, String ticketId) {
        try {
            // check lottery existing
            Lottery lottery = lotteryRepository.findAllByTicket(ticketId)
                    .orElseThrow(() -> new BadRequestException("ticketId: " + ticketId + "is not exists"));

            // Retrieve user ticket
            UserTicket userTicket = userTicketRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("userId: " + userId + " is not exists"));

            // add new lottery ticket to old lotteries
            List<Lottery> lotteryList = userTicket.getTickets();
            lotteryList.add(lottery);

            // update lottery which user just bought
//            userTicket.setTickets(lotteryList);
//            userTicketRepository.save(userTicket);
            int newAmount = lottery.getAmount() - 1;
            lottery.setAmount(newAmount);
            lottery.setUserTicket(userTicket);
            lotteryRepository.save(lottery);

            // decrease amount of lottery
//            decreaseLotteryAmount(lottery);

            return userTicket.getId();
        } catch (Exception ex) {
            log.error("buy lottery ticket failed: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void decreaseLotteryAmount(Lottery lottery) {
        lottery.setAmount(lottery.getAmount() - 1);
        lotteryRepository.save(lottery);
    }
}
