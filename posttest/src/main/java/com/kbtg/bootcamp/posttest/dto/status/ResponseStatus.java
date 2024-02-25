package com.kbtg.bootcamp.posttest.dto.status;

import lombok.Builder;
import lombok.Data;

//@Data
@Builder
public record ResponseStatus(String status, String message) {

}
