package io.github.yuemenglong.auth;

public interface IAuthMgr {
    void login(long id, String name, String[] roles, String token, long expireMin);

    void logout(String token);

    void logoutAll(String token);

    void logoutAll(long id);

    void clean();

    boolean authenticate(String url, String token);

    IAuthUser getUser(String token);

    IAuthMgr permit(String url, AuthMethod method, String... roles);

    IAuthMgr permitAll(String url, AuthMethod method);

    IAuthMgr deny(String url, AuthMethod method, String... roles);

    IAuthMgr denyAll(String url, AuthMethod method);
}
