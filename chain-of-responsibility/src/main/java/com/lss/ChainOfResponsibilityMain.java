package com.lss;

import com.lss.chain.ChainBuilder;
import com.lss.handler.AuthenticationHandler;
import com.lss.handler.BalanceCheckHandler;
import com.lss.handler.ExecutionHandler;
import com.lss.handler.FraudCheckHandler;
import com.lss.handler.Handler;
import com.lss.handler.TerminalHandler;
import com.lss.resource.Transaction;

public class ChainOfResponsibilityMain {

    public static void main(String[] args) {
        Handler chain = ChainBuilder.buildChain(
                new AuthenticationHandler(),
                new FraudCheckHandler(),
                new BalanceCheckHandler(),
                new ExecutionHandler(),
                new TerminalHandler()
        );
        System.out.println();
        System.out.println("=== Спроба 1: валідна транзакція. Знімаємо 5000 грн.===");
        Transaction tx1 = new Transaction("USER123", 5000, 10_000);
        chain.handle(tx1);

        System.out.println("\n=== Спроба 2: неавторизований користувач. Знімаємо 100 грн.===");
        Transaction tx2 = new Transaction("GUEST456", 100, 500);
        chain.handle(tx2);

        System.out.println("\n=== Спроба 3: підозра шахрайства. Знімаємо 20_000 грн.===");
        Transaction tx3 = new Transaction("USER789", 20_000, 50_000);
        chain.handle(tx3);

        System.out.println("\n=== Спроба 4: недостатньо коштів. Знімаємо 2_000 грн. ===");
        Transaction tx4 = new Transaction("USER321", 2_000, 1_000);
        chain.handle(tx4);
    }
}
