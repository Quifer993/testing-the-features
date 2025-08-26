package ru.zolo.service.auto;

import org.springframework.stereotype.Service;

@Service
public class SampleJobService {
    public void executeSampleJob() {
        System.out.println("Executing SampleJobService");
    }
}
