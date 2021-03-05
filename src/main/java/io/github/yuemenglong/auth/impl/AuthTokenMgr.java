package io.github.yuemenglong.auth.impl;

import io.github.yuemenglong.auth.IAuthUser;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AuthTokenMgr {
    static class TokenInfo {
        long userId;
        long expire;

        TokenInfo(long userId, long expire) {
            this.userId = userId;
            this.expire = expire;
        }
    }

    private final ConcurrentHashMap<Long, IAuthUser> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TokenInfo> tokensMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ArrayList<String>> userTokensMap = new ConcurrentHashMap<>();

    synchronized public void login(long id, String name, String[] roles, String token, long expireMs) {
        IAuthUser user;
        if (userMap.containsKey(id)) {
            user = userMap.get(id);
            user.setId(id);
            user.setName(name);
            user.setRoles(roles);
        } else {
            user = new AuthUser(id, name, roles);
        }
        userMap.put(id, user);
        tokensMap.put(token, new TokenInfo(id, System.currentTimeMillis() + expireMs));
        userTokensMap.putIfAbsent(id, new ArrayList<>());
        userTokensMap.get(id).add(token);
    }

    synchronized public void logout(String token) {
        if (!tokensMap.containsKey(token)) {
            return;
        }
        TokenInfo info = tokensMap.get(token);
        ArrayList<String> tokens = userTokensMap.get(info.userId);
        tokensMap.remove(token);
        if (tokens.size() == 0) {
            userTokensMap.remove(info.userId);
        }
        tokensMap.remove(token);
    }

    synchronized public void logoutAll(String token) {
        if (!tokensMap.containsKey(token)) {
            return;
        }
        logoutAll(tokensMap.get(token).userId);
    }

    @SuppressWarnings("unchecked")
    synchronized public void logoutAll(long id) {
        if (!userTokensMap.containsKey(id)) {
            return;
        }
        ArrayList<String> tokens = userTokensMap.get(id);
        ((ArrayList<String>) tokens.clone()).forEach(this::logout);
    }

    synchronized public void reset() {
        userMap.clear();
        tokensMap.clear();
        userTokensMap.clear();
    }

    public IAuthUser getUser(String token) {
        if (!tokensMap.containsKey(token)) {
            return null;
        }
        long userId = tokensMap.get(token).userId;
        if (!userMap.containsKey(userId)) {
            return null;
        }
        return userMap.get(userId);
    }
}
