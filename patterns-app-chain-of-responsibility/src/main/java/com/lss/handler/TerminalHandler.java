package com.lss.handler;

import com.lss.resource.Transaction;

public class TerminalHandler extends AbstractHandler {

    @Override
    public void handle(Transaction tx) {
        System.out.println("TerminalHandler: ланцюг завершено");
    }
}
