package pers.wesley.common.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/16 14:05
 */
@Component
@Slf4j
public class PermissionUriConfiguration implements InitializingBean {

    private static final Map<String, List<UrlFunction>> PERMISSION_URI = new HashMap<>(16);

    private static final String EVERY_ONE = "everyone";

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("开始加载资源权限...");
        LocalTime start = LocalTime.now();
        UrlFunction urlFunction_1 = new UrlFunction();
        urlFunction_1.setMethod("GET");
        urlFunction_1.setUri("/user/info");
        PERMISSION_URI.put("/user/read", Arrays.asList(urlFunction_1));
        log.info("加载资源权限结束，共耗时{}毫秒...", start.until(LocalTime.now(), ChronoUnit.MILLIS));
    }

    public static List<UrlFunction> get(String authority) {
        List<UrlFunction> urlFunctions = PERMISSION_URI.get(authority);
        if (null == urlFunctions) {
            urlFunctions = Collections.emptyList();
        }
        List<UrlFunction> everyOneUrlFunctions = PERMISSION_URI.get(EVERY_ONE);
        if (null != everyOneUrlFunctions) {
            urlFunctions.addAll(everyOneUrlFunctions);
        }
        return urlFunctions;
    }

    @Setter
    @Getter
    public class UrlFunction {
        /**
         * 请求方式
         */
        private String method;
        /**
         * 请求uri
         */
        private String uri;
    }
}
