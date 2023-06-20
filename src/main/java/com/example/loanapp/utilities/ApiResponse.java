package com.example.loanapp.utilities;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@Builder
public class ApiResponse {
    private ZonedDateTime timeStamp;
    private String path;
    private  Object data;
}