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
@Table(name = "_approver")
public class Approver {
    @Id
    private String email;

    @Enumerated(EnumType.STRING)
    private ApproverLevel level;
}
