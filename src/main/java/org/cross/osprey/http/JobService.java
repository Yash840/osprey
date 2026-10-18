package org.cross.osprey.http;

import lombok.RequiredArgsConstructor;
import org.cross.osprey.core.JobRepository;
import org.cross.osprey.share.NewJobDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public String submitJob(NewJobDto dto) {
        var job = jobRepository.save(jobMapper.toJob(dto));
        return job.getFriendlyID();
    }
}
