package io.github.yuemenglong.auth;

public interface IAuthMgr {
    void login(long id, String name, String[] roles, String token, long expireMs);

    void logout(String token);

    void logoutAll(String token);

    void logoutAll(long id);

    void reset();

    IAuthUser getUser(String token);

    IAuthMgr permit(String url, AuthMethod method, String[] roles, int order);

    IAuthMgr permitAll(String url, AuthMethod method, int order);

    IAuthMgr deny(String url, AuthMethod method, String[] roles, int order);

    IAuthMgr denyAll(String url, AuthMethod method, int order);

    IAuthMgr permit(String url, AuthMethod method, String[] roles);

    IAuthMgr permitAll(String url, AuthMethod method);

    IAuthMgr deny(String url, AuthMethod method, String[] roles);

    IAuthMgr denyAll(String url, AuthMethod method);

    boolean authenticate(String url, String token);
}
