package com.lss.l8springdata.sql;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.springframework.stereotype.Component;

@Component
public class PrettySqlMessageFormatter implements MessageFormattingStrategy {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        } else {
            String upper = sql.trim().toUpperCase();
            String color;

            if (upper.startsWith("SELECT")) {
                color = ANSI_GREEN;
            } else if (upper.startsWith("INSERT")) {
                color = ANSI_BLUE;
            } else if (upper.startsWith("UPDATE")) {
                color = ANSI_YELLOW;
            } else if (upper.startsWith("DELETE")) {
                color = ANSI_RED;
            } else {
                color = ANSI_RESET;
            }

            return color + "[SQL] " + elapsed + " ms | " + P6Util.singleLine(sql) + ANSI_RESET;
        }
    }
}
