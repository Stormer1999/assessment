package com.kbtg.bootcamp.posttest.utils;

import com.kbtg.bootcamp.posttest.exception.BadRequestException;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateUserId(String userId) {
        if (!userId.matches("\\d{10}")) {
            throw new BadRequestException("userId must have 10 digits");
        }
    }

    public static void validateTicketId(String ticketId) {
        if (!ticketId.matches("\\d{6}")) {
            throw new BadRequestException("ticketId must have 6 digits");
        }
    }
}
