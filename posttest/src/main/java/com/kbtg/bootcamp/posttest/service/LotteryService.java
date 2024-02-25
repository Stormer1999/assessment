package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dao.entity.UserTicket;
import com.kbtg.bootcamp.posttest.dao.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.dao.repository.UserTicketRepository;
import com.kbtg.bootcamp.posttest.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.PurchasedLotteriesResponse;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import com.kbtg.bootcamp.posttest.exception.DatabaseErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<Lottery> lotteryList = lotteryRepository.findAll();

        return wrapLotteryListToStringList(lotteryList);
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

            // check amount should lower than 1
            if (lottery.getAmount() < 1) {
                throw new BadRequestException("ticket is run out of");
            }

            // add new lottery ticket to old lotteries
            List<Lottery> lotteryList = userTicket.getTickets();
            lotteryList.add(lottery);

            // update lottery which user just bought
            int newAmount = lottery.getAmount() - 1;
            lottery.setAmount(newAmount);
            lottery.setUserTicket(userTicket);
            lotteryRepository.save(lottery);

            return userTicket.getId();
        } catch (Exception ex) {
            log.error("buy lottery ticket failed: {}", ex.getMessage());
            throw new DatabaseErrorException(ex.getMessage());
        }
    }

    public PurchasedLotteriesResponse listAllPurchasedTicket(Long userId) {
        UserTicket userTicket = userTicketRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("user not found"));

        List<Lottery> lotteryList = userTicket.getTickets();
        List<String> stringList = wrapLotteryListToStringList(lotteryList);

        // calculate total cost of all purchased tickets
        double totalCost = lotteryList.stream()
                .mapToDouble(Lottery::getPrice)
                .sum();

        return new PurchasedLotteriesResponse(
                stringList,
                stringList.size(),
                totalCost
        );
    }

    private List<String> wrapLotteryListToStringList(List<Lottery> lotteryList) {
        List<String> stringList = new ArrayList<>();
        for (Lottery lottery : lotteryList) {
            stringList.add(lottery.getTicket());
        }

        return stringList;
    }
}
