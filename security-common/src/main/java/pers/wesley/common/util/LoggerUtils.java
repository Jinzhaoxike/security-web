package pers.wesley.common.util;

import org.slf4j.Logger;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/20 15:46
 */
public abstract class LoggerUtils {

    public static void debug(Logger logger, String format, Supplier<Object>... suppliers) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, Stream.of(suppliers).map(supplier -> supplier.get()).toArray());
        }
    }
}
