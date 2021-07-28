package com.shzx.application.common.tool;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class ToolTransaction {
    @Autowired
    private PlatformTransactionManager transactionManager;

    public ToolTransaction() {
    }

    public boolean transact(Consumer<Object> consumer) {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            consumer.accept((Object)null);
            this.transactionManager.commit(status);
            return true;
        } catch (Exception var4) {
            this.transactionManager.rollback(status);
            var4.printStackTrace();
            return false;
        }
    }
}