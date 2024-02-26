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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotteryServiceTest {

    @Mock
    private LotteryRepository lotteryRepository;

    @Mock
    private UserTicketRepository userTicketRepository;

    @InjectMocks
    private LotteryService lotteryService;

    @BeforeEach
    void setUp() {
        reset(lotteryRepository, userTicketRepository);
    }


    @Test
    void shouldAddLotteryTicket_Success() {
        LotteryRequestDto requestDto = new LotteryRequestDto("ticket123", 10.0, 100);
        when(lotteryRepository.existsByTicket(requestDto.ticket())).thenReturn(false);
        when(lotteryRepository.save(any(Lottery.class))).thenReturn(new Lottery());

        Lottery lottery = lotteryService.addLotteryTicket(requestDto);

        assertNotNull(lottery);
        verify(lotteryRepository).save(any(Lottery.class));
    }

    @Test
    void shouldThrowBadRequest_WhenAddDuplicateLotteryTicket() {
        LotteryRequestDto requestDto = new LotteryRequestDto("duplication_ticket", 10.0, 100);
        when(lotteryRepository.existsByTicket(requestDto.ticket())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> lotteryService.addLotteryTicket(requestDto));
    }

    @Test
    void shouldListAllLotteryTicket_Success() {
        List<Lottery> mockLotteryList = List.of(
                new Lottery(1L, "000001", 80.0, 1, null),
                new Lottery(2L, "000002", 100.0, 1, null)
        );
        when(lotteryRepository.findAllByOrderByTicketAsc()).thenReturn(mockLotteryList);

        List<String> actual = lotteryService.listAllLotteryTicket();

        List<String> expected = List.of("000001", "000002");
        assertEquals(expected, actual);
    }

    @Test
    void shouldBuyLotteryTicket_Success() {
        // arrange
        String userId = "0000000001";
        String ticketId = "000001";

        // mock Lottery
        Lottery mockLottery = new Lottery(1L, "000001", 80.0, 1, null);

        // mock userTicket
        UserTicket mockUserTicket = new UserTicket();
        List<Lottery> userLotteries = List.of(new Lottery(1L, "000001", 80.0, 1, null));
        mockUserTicket.setTickets(userLotteries);

        when(lotteryRepository.findAllByTicketOrderByTicket(ticketId)).thenReturn(Optional.of(mockLottery));
        when(userTicketRepository.findByUserId(userId)).thenReturn(Optional.of(mockUserTicket));
        when(lotteryRepository.save(any(Lottery.class))).thenReturn(mockLottery);

        // act
        Long result = lotteryService.buyLotteryTicket(userId, ticketId);

        // assert
        verify(lotteryRepository).findAllByTicketOrderByTicket(ticketId);
        verify(userTicketRepository).findByUserId(userId);
        verify(lotteryRepository).save(any(Lottery.class));

        assertEquals(mockUserTicket.getId(), result);
        assertEquals(1, userLotteries.size());
        assertEquals(0, mockLottery.getAmount());
    }

    @Test
    void shouldThrowBadRequest_WhenBuyTicket_AndTicketNotfound() {
        String userId = "0000000001";
        String ticketId = "000001";

        when(lotteryRepository.findAllByTicketOrderByTicket(ticketId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> lotteryService.buyLotteryTicket(userId, ticketId));
    }

    @Test
    void shouldThrowBadRequest_WhenBuyTicket_AndTicketIsRunOutOf() {
        String userId = "0000000001";
        String ticketId = "000001";

        Lottery lottery = new Lottery();
        lottery.setAmount(0);

        UserTicket userTicket = new UserTicket();
        List<Lottery> userLotteries = new ArrayList<>();
        userTicket.setTickets(userLotteries);

        when(lotteryRepository.findAllByTicketOrderByTicket(ticketId)).thenReturn(Optional.of(lottery));
        when(userTicketRepository.findByUserId(userId)).thenReturn(Optional.of(userTicket));

        assertThrows(BadRequestException.class, () -> lotteryService.buyLotteryTicket(userId, ticketId));
    }

    @Test
    void shouldThrowDatabaseError_WhenCannotBuyTicket() {
        // arrange
        String userId = "user1";
        String ticketId = "ticket123";
        Lottery lottery = new Lottery(1L, "000001", 80.0, 1, null);
        UserTicket userTicket = new UserTicket(1L, "", "user", null);

        when(lotteryRepository.findAllByTicketOrderByTicket(ticketId)).thenReturn(java.util.Optional.of(lottery));
        when(userTicketRepository.findByUserId(userId)).thenReturn(java.util.Optional.of(userTicket));
        doThrow(PersistenceException.class).when(lotteryRepository).save(any());

        // act & assert
        assertThrows(DatabaseErrorException.class, () -> lotteryService.buyLotteryTicket(userId, ticketId));

        verify(lotteryRepository).save(lottery);
    }

    @Test
    void shouldListAllPurchasedTicketByUserId_Success() {
        // arrange
        String userId = "user1";
        UserTicket mockUserTicket = new UserTicket();
        mockUserTicket.setId(1L);
        List<Lottery> lotteryList = new ArrayList<>();
        lotteryList.add(new Lottery(1L, "000001", 10.0, 1, null));
        lotteryList.add(new Lottery(2L, "000002", 20.0, 1, null));
        lotteryList.add(new Lottery(3L, "000003", 30.0, 1, null));
        mockUserTicket.setTickets(lotteryList);

        when(userTicketRepository.findByUserId(userId)).thenReturn(Optional.of(mockUserTicket));

        // act
        PurchasedLotteriesResponse response = lotteryService.listAllPurchasedTicketByUserId(userId);

        // assert
        verify(userTicketRepository).findByUserId(userId);
        assertEquals(3, response.ticket().size());
        assertEquals(60.0, response.cost());
        assertEquals(3, response.count());
        assertEquals("000001", response.ticket().get(0));
        assertEquals("000002", response.ticket().get(1));
        assertEquals("000003", response.ticket().get(2));
    }

    @Test
    void shouldSellBackTicket_Success() {
        // arrange
        String userId = "0000000001";
        String ticketId = "000001";
        UserTicket mockUserTicket = new UserTicket();
        mockUserTicket.setId(1L);
        Lottery mockLottery = new Lottery();
        mockLottery.setTicket(ticketId);
        mockLottery.setUserTicket(mockUserTicket);

        when(userTicketRepository.findByUserId(userId)).thenReturn(Optional.of(mockUserTicket));
        when(lotteryRepository.findAllByTicketAndUserTicketOrderByTicket(ticketId, mockUserTicket)).thenReturn(Optional.of(mockLottery));

        // act
        String result = lotteryService.sellBackTicket(userId, ticketId);

        // assert
        verify(userTicketRepository).findByUserId(userId);
        verify(lotteryRepository).findAllByTicketAndUserTicketOrderByTicket(ticketId, mockUserTicket);
        verify(lotteryRepository).save(mockLottery);

        assertNull(mockLottery.getUserTicket());
        assertEquals(ticketId, result);
    }
}