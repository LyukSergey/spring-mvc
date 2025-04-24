package com.lss.chain;

import com.lss.handler.Handler;

public class ChainBuilder {

    public static Handler buildChain(Handler... handlers) {
        for (int i = 0; i < handlers.length - 1; i++) {
            handlers[i].setNext(handlers[i + 1]);
        }
        return handlers[0];
    }
}
