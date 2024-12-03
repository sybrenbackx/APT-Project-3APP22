package fact.it.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private Long id;
    private String patientNumber;
    private String fullName;
    private String email;
    private String address;
}
