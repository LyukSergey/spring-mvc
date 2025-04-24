package com.lss.l6springboot.service;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class ProphetServiceImpl implements ProphetService {

    @Override
    public boolean willSurvive(String name) {
        return !name.contains("Stark") && ThreadLocalRandom.current().nextBoolean();
    }
}
