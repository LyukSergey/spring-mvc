package com.lss.handler;

import com.lss.resource.Transaction;

public abstract class AbstractHandler implements Handler {

    private Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    protected void callNext(Transaction tx) {
        if (next != null) {
            next.handle(tx);
        }
    }
}
