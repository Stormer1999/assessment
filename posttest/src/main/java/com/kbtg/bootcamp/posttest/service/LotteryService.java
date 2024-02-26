package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dao.entity.UserTicket;
import com.kbtg.bootcamp.posttest.dao.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.dao.repository.UserTicketRepository;
import com.kbtg.bootcamp.posttest.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.PurchasedLotteriesResponse;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import com.kbtg.bootcamp.posttest.exception.DatabaseErrorException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LotteryService {

    private static final String USER_NOT_EXISTS_ERR_MESSAGE = "userId: %s is not exists";

    private static final String TICKET_NOT_EXISTS_ERR_MESSAGE = "ticketId: %s is not exists";

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
        List<Lottery> lotteryList = lotteryRepository.findAllByOrderByTicketAsc();

        return wrapLotteryListToStringList(lotteryList);
    }

    @Transactional
    public Long buyLotteryTicket(String userId, String ticketId) {
        try {
            // check lottery existing
            Lottery lottery = lotteryRepository.findAllByTicketOrderByTicket(ticketId)
                    .orElseThrow(() -> new BadRequestException(String.format(TICKET_NOT_EXISTS_ERR_MESSAGE, ticketId)));

            // Retrieve user ticket
            UserTicket userTicket = userTicketRepository.findByUserId(userId)
                    .orElseThrow(() -> new BadRequestException(String.format(USER_NOT_EXISTS_ERR_MESSAGE, userId)));

            // check amount should lower than 1
            if (lottery.getAmount() < 1) {
                throw new BadRequestException("ticket is run out of");
            }

            // update lottery which user just bought (decrease amount)
            int newAmount = lottery.getAmount() - 1;
            lottery.setAmount(newAmount);
            lottery.setUserTicket(userTicket);
            lotteryRepository.save(lottery);

            return userTicket.getId();
        } catch (PersistenceException ex) {
            log.error("buy lottery ticket failed: {}", ex.getMessage());
            throw new DatabaseErrorException(ex.getMessage());
        }
    }

    public PurchasedLotteriesResponse listAllPurchasedTicketByUserId(String userId) {
        UserTicket userTicket = userTicketRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(String.format(USER_NOT_EXISTS_ERR_MESSAGE, userId)));

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

    @Transactional
    public String sellBackTicket(String userId, String ticketId) {
        // check user is existing
        UserTicket userTicket = userTicketRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(String.format(USER_NOT_EXISTS_ERR_MESSAGE, userId)));

        // remove user sell back ticket by set foreign-key to null
        Lottery updateLottery = lotteryRepository
                .findAllByTicketAndUserTicketOrderByTicket(ticketId, userTicket)
                .orElseThrow(() -> new BadRequestException(String.format(TICKET_NOT_EXISTS_ERR_MESSAGE, ticketId)));
        updateLottery.setUserTicket(null);
        lotteryRepository.save(updateLottery);

        return updateLottery.getTicket();
    }

    private List<String> wrapLotteryListToStringList(List<Lottery> lotteryList) {
        List<String> stringList = new ArrayList<>();
        for (Lottery lottery : lotteryList) {
            stringList.add(lottery.getTicket());
        }

        return stringList;
    }
}
