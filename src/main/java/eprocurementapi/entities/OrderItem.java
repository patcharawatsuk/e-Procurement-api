package eprocurementapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_orderItem")
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id;
    private String productName;
    private Double qty;
    private Double price;
}
