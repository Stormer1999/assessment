package com.kbtg.bootcamp.posttest.dto;

import java.util.List;

public record PurchasedLotteriesResponse(
        List<String> ticket,
        int count,
        double cost) {
}
