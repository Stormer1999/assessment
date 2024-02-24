package com.kbtg.bootcamp.posttest.dto;

import java.util.List;

public record BoughLotteryResponse(
        List<String> ticket,
        int count,
        double cost) {
}
