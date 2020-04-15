package pers.wesley.common.security;

import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/09 10:04
 */
@Getter
public class User implements UserDetails, CredentialsContainer {

    /**
     * 登录密码
     */
    private String password;
    /**
     * 登录用户名
     */
    private final String username;
    /**
     * 用户昵称
     */
    private final String nickname;
    /**
     * 授权资源权限
     */
    private final Set<GrantedAuthority> authorities;
    /**
     * 账户非锁定状态
     */
    private final boolean accountNonLocked;
    /**
     * 账户非过期状态
     */
    private final boolean accountNonExpired;
    /**
     * 凭证非过期状态
     */
    private final boolean credentialsNonExpired;
    /**
     * 账户可用
     */
    private final boolean enabled;
    /**
     * 预留
     */
    private final Map<String, Object> reserve;

    public User(String username,
                String password,
                String nickname,
                Collection<? extends GrantedAuthority> authorities,
                Map<String, Object> reserve) {
        this(username, password, nickname, true, true, true, true, authorities, reserve);
    }

    public User(String username,
                String password,
                String nickname,
                boolean enabled,
                boolean accountNonExpired,
                boolean credentialsNonExpired,
                boolean accountNonLocked,
                Collection<? extends GrantedAuthority> authorities,
                Map<String, Object> reserve) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.reserve = Collections.unmodifiableMap(reserve == null ? Collections.EMPTY_MAP : reserve);
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof pers.wesley.common.security.User) {
            return username.equals(((pers.wesley.common.security.User) rhs).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Nickname: ").append(this.nickname).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired)
                .append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");
            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }
        if (!reserve.isEmpty()) {
            sb.append("; Reserves: ");
            boolean first = true;
            for(String key : reserve.keySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(key).append("=").append(reserve.get(key));
            }
        } else {
            sb.append("Not any reserves");
        }

        return sb.toString();
    }


    private static SortedSet<GrantedAuthority> sortAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                new pers.wesley.common.security.User.AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority,
                    "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>,
            Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    public static UserBuilder withUsername(String username) {
        return builder().username(username);
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static UserBuilder withUserDetails(UserDetails userDetails) {

        UserBuilder userBuilder = withUsername(userDetails.getUsername())
                .password(userDetails.getPassword())
                .accountExpired(!userDetails.isAccountNonExpired())
                .accountLocked(!userDetails.isAccountNonLocked())
                .authorities(userDetails.getAuthorities())
                .credentialsExpired(!userDetails.isCredentialsNonExpired())
                .disabled(!userDetails.isEnabled());

        if (userDetails instanceof User) {
            User userDetail = (User) userDetails;
            userBuilder.reserve(userDetail.getReserve());
            userBuilder.nickname(userDetail.getNickname());
        } else {
            userBuilder.reserve(Collections.emptyMap());
        }
        return userBuilder;
    }


    /**
     * Builds the user to be added. At minimum the username, password, and authorities
     * should provided. The remaining attributes have reasonable defaults.
     */
    public static class UserBuilder {
        private String username;
        private String password;
        private String nickname;
        private List<GrantedAuthority> authorities;
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;
        private Function<String, String> passwordEncoder = password -> password;
        private Map<String, Object> reserve;

        private UserBuilder() {
        }

        public pers.wesley.common.security.User.UserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder nickname(String nickname) {
            Assert.notNull(nickname, "nickname cannot be null");
            this.nickname = nickname;
            return this;
        }


        public pers.wesley.common.security.User.UserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }

        public pers.wesley.common.security.User.UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }


        public pers.wesley.common.security.User.UserBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public pers.wesley.common.security.User.UserBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public pers.wesley.common.security.User.UserBuilder reserve(Map<String, Object> reserve) {
            this.reserve = reserve;
            return this;
        }

        public UserDetails build() {
            String encodedPassword = this.passwordEncoder.apply(password);
            return new pers.wesley.common.security.User(username, encodedPassword, nickname, !disabled, !accountExpired,
                    !credentialsExpired, !accountLocked, authorities, reserve);
        }
    }
}
