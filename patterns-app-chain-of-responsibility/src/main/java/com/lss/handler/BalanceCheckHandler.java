package com.lss.handler;

import com.lss.resource.Transaction;

public class BalanceCheckHandler extends AbstractHandler {
    @Override
    public void handle(Transaction tx) {
        boolean ok = tx.getBalance() >= tx.getAmount();
        System.out.println("BalanceCheckHandler: достатній баланс = " + ok);
        if (!ok) {
            System.out.println(" → Відмова: недостатньо коштів");
            return;
        }
        callNext(tx);
    }
}
