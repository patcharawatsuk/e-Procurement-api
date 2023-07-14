package eprocurementapi.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_orderApproval")
public class OrderApproval {
    @EmbeddedId
    private OrderApprovalPK id;
    private int current;
    private String approver;
}
