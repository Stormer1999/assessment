package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dao.entity.Lottery;
import com.kbtg.bootcamp.posttest.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketIdResponseDto;
import com.kbtg.bootcamp.posttest.dto.TicketListResponseDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    /*
        EXP01: As an admin, I want to add a new lottery tickets So that I can have a lottery store
    */
    @PostMapping("/admin/lotteries")
    public ResponseEntity<TicketResponseDto> addLottery(@RequestBody @Valid LotteryRequestDto request) {
        Lottery lottery = lotteryService.addLotteryTicket(request);
        TicketResponseDto response = new TicketResponseDto(lottery.getTicket());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
        EXP02: As a user, I want a list all lottery tickets So that I can pick what I want to buy
     */
    @GetMapping("/lotteries")
    public ResponseEntity<TicketListResponseDto> listAllLottery() {
        TicketListResponseDto response = new TicketListResponseDto(lotteryService.listAllLotteryTicket());

        return ResponseEntity.ok().body(response);
    }


    /*
        EXP03: As a user, I want to buy a lottery tickets So that I can get a change to win
     */
    @PostMapping("/users/{userId}/lotteries/{ticketId}")
    public ResponseEntity<TicketIdResponseDto> buyLottery(@PathVariable("userId") Long userId,
                                                          @PathVariable("ticketId") String ticketId) {
        // return ticketId from db
        Long boughtUserId = lotteryService.buyLotteryTicket(userId, ticketId);
        TicketIdResponseDto response = new TicketIdResponseDto(String.valueOf(boughtUserId));

        return ResponseEntity.ok().body(response);
    }

    /*
        EXP04: As a user, I want to list all my lottery tickets So that I can see which one I have already bought and it cost
     */
    @GetMapping("/users/{userId}/lotteries")
    public String listAllBoughtLottery(@PathVariable("userId") Long userId) {

        return null;
    }

    /*
        EXP05: As a user, I want to sell back my lottery tickets So that I can get my money back
     */
    @DeleteMapping("/users/{userId}/lotteries/{ticketId}")
    public String sellLottery(@PathVariable("userId") Long userId,
                              @PathVariable("ticketId") Long ticketId) {

        return null;
    }
}
