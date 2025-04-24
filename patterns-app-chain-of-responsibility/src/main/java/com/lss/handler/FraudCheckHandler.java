package com.lss.handler;

import com.lss.resource.Transaction;

public class FraudCheckHandler extends AbstractHandler {
    @Override
    public void handle(Transaction tx) {
        // примітивно: великі суми під підозрою
        boolean ok = tx.getAmount() < 10_000;
        tx.setFraudFree(ok);
        System.out.println("FraudCheckHandler: не шахрай = " + ok);
        if (!ok) {
            System.out.println(" → Відмова: ймовірне шахрайство");
            return;
        }
        callNext(tx);
    }
}
