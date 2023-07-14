package eprocurementapi.dao.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "productId is required.")
    private int productId;

    @NotNull(message = "productName is required.")
    private String productName;

    @NotNull(message = "qty is required.")
    private Double qty;

    @NotNull(message = "price is required.")
    private Double price;
}
