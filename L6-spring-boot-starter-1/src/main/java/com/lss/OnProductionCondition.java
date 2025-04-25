package com.lss;

import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnProductionCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (GraphicsEnvironment.isHeadless()) {
            return false;
        }
        try {
            return JOptionPane.showConfirmDialog(null, "Це продакшин?", "Увага",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        } catch (Exception e) {
            System.err.println("🚫 Не вдалося показати JOptionPane: " + e.getMessage());
            return false;
        }
    }
}
