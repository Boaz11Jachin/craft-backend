package org.codenova.craft.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderItemSummary {
    private Long id;
    private String productId;
    private String productName;
    private Integer quantity;
    private String status;
}
