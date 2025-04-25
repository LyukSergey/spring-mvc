package com.lss.l6springboot.service;

import com.lss.l6springboot.ProfileConstants;
import com.lss.l6springboot.properties.ProphetProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ProfileConstants.WINTER_IS_HERE)
@RequiredArgsConstructor
public class WhiteListBackendProphetServiceImpl implements ProphetService {

    private final ProphetProperties prophetProperties;

    @Override
    public boolean willSurvive(String name) {
        return prophetProperties.getWhoReturnDebts().contains(name);
    }
}
