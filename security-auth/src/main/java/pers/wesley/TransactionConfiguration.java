package pers.wesley;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/26 13:35
 */
//@Configuration
public class TransactionConfiguration {

    private PlatformTransactionManager platformTransactionManager;

    public TransactionConfiguration(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute transactionAttributeWrite = new RuleBasedTransactionAttribute();
        transactionAttributeWrite.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute((Exception.class))));
        transactionAttributeWrite.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionAttributeWrite.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        RuleBasedTransactionAttribute transactionAttributeRead = new RuleBasedTransactionAttribute();
        transactionAttributeRead.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute((Exception.class))));
        transactionAttributeRead.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        transactionAttributeRead.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        Map<String, TransactionAttribute> txMap = new HashMap<>(16);
        txMap.put("save*", transactionAttributeWrite);
        txMap.put("delete*", transactionAttributeWrite);
        txMap.put("update*", transactionAttributeWrite);
        txMap.put("insert*", transactionAttributeWrite);
        txMap.put("query*", transactionAttributeRead);
        txMap.put("get*", transactionAttributeRead);
        txMap.put("find*", transactionAttributeRead);
        source.setNameMap(txMap);
        return new TransactionInterceptor(platformTransactionManager, source);
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor txAdvice) {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setAdvice(txAdvice);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (* cn.kingnet.utp.operation.service..*.*(..))");
        pointcutAdvisor.setPointcut(pointcut);
        return pointcutAdvisor;
    }
}
