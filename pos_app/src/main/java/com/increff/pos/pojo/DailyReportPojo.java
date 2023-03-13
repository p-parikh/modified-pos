package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity(name = "Dailyreport")
@Getter
@Setter
public class DailyReportPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private ZonedDateTime reportDate;
    @NotNull
    private Integer totalOrders;
    @NotNull
    private Integer totalItems;
    @NotNull
    private Double totalRevenue;

}
