package com.kbtg.bootcamp.posttest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LotteryRequestDto(
        @NotNull(message = "tickets is required")
        @Size(min = 6, max = 6, message = "ticket_no size must be 6 characters")
        String ticket,

        @NotNull(message = "price is required")
        Double price,

        @NotNull(message = "amount is required")
        Integer amount) {
}
