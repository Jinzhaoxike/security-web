package pers.wesley.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/11 14:54
 */
public class NegatedRequestMatcher implements RequestMatcher {

    private final Log logger = LogFactory.getLog(getClass());
    private final List<RequestMatcher> requestMatchers;

    public NegatedRequestMatcher(List<RequestMatcher> requestMatchers) {
        Assert.notEmpty(requestMatchers, "requestMatchers must contain a value");
        if (requestMatchers.contains(null)) {
            throw new IllegalArgumentException(
                    "requestMatchers cannot contain null values");
        }
        this.requestMatchers = requestMatchers;
    }

    public NegatedRequestMatcher(RequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher matcher : requestMatchers) {
            if (logger.isDebugEnabled()) {
                logger.debug("Trying to match using " + matcher);
            }
            if (matcher.matches(request)) {
                logger.debug("matched skip");
                return false;
            }
        }
        logger.debug("No matches found");
        return true;
    }

    @Override
    public String toString() {
        return "OrRequestMatcher [requestMatchers=" + requestMatchers + "]";
    }
}
