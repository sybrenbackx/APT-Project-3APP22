package fact.it.doctorservice.controller;

import fact.it.doctorservice.dto.DoctorResponse;
import fact.it.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
}
