package fact.it.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private String id;
    private String doctorNumber;
    private String email;
    private String fullName;
    private String address;
    private String role;
    private String specialization;
}
