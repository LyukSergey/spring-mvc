package com.lss.l6springboot.service;

import com.lss.l6springboot.ProfileConstants;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ProfileConstants.WINTER_IS_COMING)
public class ProphetServiceImpl implements ProphetService {

    @Override
    public boolean willSurvive(String name) {
        return !name.contains("Stark") && ThreadLocalRandom.current().nextBoolean();
    }
}
