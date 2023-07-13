package eprocurementapi.dao.request;

import eprocurementapi.entities.ApproverLevel;
import eprocurementapi.entities.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotNull(message = "firstName is required.")
    private String firstName;

    @NotNull(message = "lastName is required.")
    private String lastName;

    @NotNull(message = "email is required.")
    private String email;

    @NotNull(message = "password is required.")
    private String password;

    @NotNull(message = "role is required.")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ApproverLevel spendinglimit;
}
