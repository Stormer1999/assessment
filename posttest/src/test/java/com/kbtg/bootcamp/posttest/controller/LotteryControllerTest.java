package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dto.*;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotteryControllerTest {

    @Mock
    private LotteryService lotteryService;

    @InjectMocks
    private LotteryController lotteryController;

    @Test
    void shouldCallAddLottery_Success() {
        // arrange
        LotteryRequestDto request = new LotteryRequestDto("000001", 10.0, 1);
        Lottery lottery = new Lottery();
        lottery.setTicket("000001");
        when(lotteryService.addLotteryTicket(request)).thenReturn(lottery);

        // act
        ResponseEntity<TicketResponseDto> responseEntity = lotteryController.addLottery(request);

        // assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("000001", Objects.requireNonNull(responseEntity.getBody()).ticket());
    }

    @Test
    void shouldCallListAllLottery_Success() {
        // arrange
        List<String> lotteryTickets = new ArrayList<>();
        lotteryTickets.add("ticket1");
        lotteryTickets.add("ticket2");
        when(lotteryService.listAllLotteryTicket()).thenReturn(lotteryTickets);

        // act
        ResponseEntity<TicketListResponseDto> responseEntity = lotteryController.listAllLottery();

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).tickets().size());
        assertEquals("ticket1", responseEntity.getBody().tickets().get(0));
        assertEquals("ticket2", responseEntity.getBody().tickets().get(1));
    }

    @Test
    void shouldCallBuyLottery_Success() {
        // arrange
        String userId = "0000000001";
        String ticketId = "000001";
        when(lotteryService.buyLotteryTicket(userId, ticketId)).thenReturn(3L);

        // act
        ResponseEntity<TicketIdResponseDto> responseEntity = lotteryController.buyLottery(userId, ticketId);

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("3", Objects.requireNonNull(responseEntity.getBody()).id());
    }

    @Test
    void shouldCallListAllBoughtLottery_Success() {
        // arrange
        String userId = "0000000001";
        List<String> lotteryTickets = new ArrayList<>();
        lotteryTickets.add("000001");
        lotteryTickets.add("000002");
        PurchasedLotteriesResponse purchasedLotteriesResponse =
                new PurchasedLotteriesResponse(lotteryTickets, lotteryTickets.size(), 50.0);
        when(lotteryService.listAllPurchasedTicketByUserId(userId)).thenReturn(purchasedLotteriesResponse);

        // act
        ResponseEntity<PurchasedLotteriesResponse> responseEntity = lotteryController.listAllBoughtLottery(userId);

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(lotteryTickets, Objects.requireNonNull(responseEntity.getBody()).ticket());
        assertEquals(lotteryTickets.size(), responseEntity.getBody().count());
        assertEquals(50.0, responseEntity.getBody().cost());
    }

    @Test
    void shouldCallSellBackLottery_Success() {
        // arrange
        String userId = "0000000001";
        String ticketId = "000001";
        when(lotteryService.sellBackTicket(userId, ticketId)).thenReturn(ticketId);

        // act
        ResponseEntity<TicketResponseDto> responseEntity = lotteryController.sellBackLottery(userId, ticketId);

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ticketId, Objects.requireNonNull(responseEntity.getBody()).ticket());
    }

    @Test
    void shouldThrowBadRequest_WhenCallSellBackLottery_WithInvalidUserId() {
        String userId = "invalid";
        String ticketId = "000001";

        assertThrows(BadRequestException.class, () -> lotteryController.sellBackLottery(userId, ticketId));
    }
}