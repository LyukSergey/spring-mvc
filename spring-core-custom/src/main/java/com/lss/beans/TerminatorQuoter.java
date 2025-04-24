package com.lss.beans;

import com.lss.annotations.DeprecatedClass;
import com.lss.annotations.InjectRandomInt;
import com.lss.annotations.PostProxy;
import com.lss.annotations.Profiling;
import jakarta.annotation.PostConstruct;

@Profiling
/*@DeprecatedClass(newImpl = T1000.class)*/
public class TerminatorQuoter implements Quoter {

    private String message;
    @InjectRandomInt(min = 1, max = 10)
    private int repeat;

    @PostConstruct
    public void init() {
        System.out.printf("Phase 2. TerminatorQuoter init method %s%n", repeat);
    }

    public TerminatorQuoter() {
        System.out.printf("Phase 1. TerminatorQuoter constructor %s%n", repeat);
    }

    @Override
    @PostProxy
    public void sayQuote() {
        System.out.printf("Phase 3. TerminatorQuoter sayQuote %s%n", repeat);
        for (int i = 0; i < repeat; i++) {
            System.out.printf("message = %s_%s%n", message, i);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
