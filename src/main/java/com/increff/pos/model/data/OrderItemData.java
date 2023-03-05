package com.increff.pos.model.data;

import com.increff.pos.model.forms.OrderItemForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemData extends OrderItemForm {

    private Integer id;

    private Integer orderId;

    private Integer productId;
}
