package com.example.spring.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSearch {
    private String type;
    private String homeId;
    private Date start;
    private Date end;
    private String keyWord;
}
