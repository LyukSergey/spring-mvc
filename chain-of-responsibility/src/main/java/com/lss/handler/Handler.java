package com.lss.handler;

import com.lss.resource.Transaction;

public interface Handler {
    void setNext(Handler next);
    void handle(Transaction tx);
}
