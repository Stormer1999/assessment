package com.kbtg.bootcamp.posttest.dto.status;

import lombok.Builder;

//@Data
@Builder
public record ResponseStatus(String status, String message) {

}
