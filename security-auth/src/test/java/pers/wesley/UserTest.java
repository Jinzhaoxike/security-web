package pers.wesley;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pers.wesley.common.security.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/09 10:55
 */
@Slf4j
public class UserTest {

    @Test
    public void testUser() {
        List<GrantedAuthority> authorities = new ArrayList<>(16);
        authorities.add(new SimpleGrantedAuthority("admin"));
        authorities.add(new SimpleGrantedAuthority("pltadmin"));

        Map<String, Object> reserve = new HashMap<>(16);
        reserve.put("relation", "/0000/00001");
        reserve.put("userType", "pltadmin");

        User user = new User("admin", "123456", true, true, true, true, authorities, reserve);
        log.info("user = {}", user);
    }
}
