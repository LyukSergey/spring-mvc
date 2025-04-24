package com.lss.handler;

import com.lss.resource.Transaction;

public class ExecutionHandler extends AbstractHandler {

    @Override
    public void handle(Transaction tx) {
        tx.setBalance(tx.getBalance() - tx.getAmount());
        System.out.println("ExecutionHandler: транзакція успішна, новий баланс=" + tx.getBalance());
        callNext(tx);
    }
}

