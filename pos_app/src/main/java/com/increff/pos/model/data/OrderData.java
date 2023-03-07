package com.increff.pos.model.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class OrderData {

    private Integer orderId;

    private Timestamp datetime;
}
