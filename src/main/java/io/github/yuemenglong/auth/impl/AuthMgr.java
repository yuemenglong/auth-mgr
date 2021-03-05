package io.github.yuemenglong.auth.impl;

import io.github.yuemenglong.auth.AuthMethod;
import io.github.yuemenglong.auth.IAuthMgr;
import io.github.yuemenglong.auth.IAuthUser;

public class AuthMgr implements IAuthMgr {
    private final AuthRuleMgr ruleMgr = new AuthRuleMgr();
    private final AuthTokenMgr tokenMgr = new AuthTokenMgr();

    @Override
    public void login(long id, String name, String[] roles, String token, long expireMs) {
        tokenMgr.login(id, name, roles, token, expireMs);
    }

    @Override
    public void logout(String token) {
        tokenMgr.logout(token);
    }

    @Override
    public void logoutAll(String token) {
        tokenMgr.logoutAll(token);
    }

    @Override
    public void logoutAll(long id) {
        tokenMgr.logoutAll(id);
    }

    @Override
    public void reset() {
        tokenMgr.reset();
    }

    @Override
    public IAuthUser getUser(String token) {
        return tokenMgr.getUser(token);
    }

    @Override
    public IAuthMgr permit(String url, AuthMethod method, String[] roles, int order) {
        ruleMgr.permit(url, method, roles, order);
        return this;
    }

    @Override
    public IAuthMgr permitAll(String url, AuthMethod method, int order) {
        ruleMgr.permitAll(url, method, order);
        return this;
    }

    @Override
    public IAuthMgr deny(String url, AuthMethod method, String[] roles, int order) {
        ruleMgr.deny(url, method, roles, order);
        return this;
    }

    @Override
    public IAuthMgr denyAll(String url, AuthMethod method, int order) {
        ruleMgr.denyAll(url, method, order);
        return this;
    }

    @Override
    public IAuthMgr permit(String url, AuthMethod method, String[] roles) {
        ruleMgr.permit(url, method, roles, 0);
        return this;
    }

    @Override
    public IAuthMgr permitAll(String url, AuthMethod method) {
        ruleMgr.permitAll(url, method, 0);
        return this;
    }

    @Override
    public IAuthMgr deny(String url, AuthMethod method, String[] roles) {
        ruleMgr.deny(url, method, roles, 0);
        return this;
    }

    @Override
    public IAuthMgr denyAll(String url, AuthMethod method) {
        ruleMgr.denyAll(url, method, 0);
        return this;
    }

    @Override
    public boolean authenticate(String url, AuthMethod method, String token) {
        String[] roles = new String[]{};
        if (tokenMgr.isTokenValid(token)) {
            IAuthUser user = getUser(token);
            roles = user.getRoles();
        }
        return ruleMgr.authenticate(url, method, roles);
    }
}
