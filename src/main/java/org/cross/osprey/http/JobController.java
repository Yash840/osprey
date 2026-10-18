package org.cross.osprey.http;

import lombok.RequiredArgsConstructor;
import org.cross.osprey.share.NewJobDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String submitJob(@RequestBody NewJobDto dto) {
        return jobService.submitJob(dto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidJobRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
