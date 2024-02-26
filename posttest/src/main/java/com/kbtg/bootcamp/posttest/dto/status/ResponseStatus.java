package com.kbtg.bootcamp.posttest.dto.status;

import lombok.Builder;

@Builder
public record ResponseStatus(String status, String message) {

}
