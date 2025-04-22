package com.lss.handler;

import com.lss.resource.Transaction;

public class AuthenticationHandler extends AbstractHandler {

    @Override
    public void handle(Transaction tx) {
        // у «справжньому» коді — перевірка токена/пароля
        boolean ok = tx.getAccountId().startsWith("USER");
        tx.setAuthenticated(ok);
        System.out.println("AuthenticationHandler: аутентифікований = " + ok);
        if (!ok) {
            System.out.println(" → Відмова: неавторизований користувач");
            return;
        }
        callNext(tx);
    }
}
