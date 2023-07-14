package eprocurementapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppovalOrderInfo {
    private int order_id;
    private int step;
    private String approver;
    private int current;
    private String requester;
}
