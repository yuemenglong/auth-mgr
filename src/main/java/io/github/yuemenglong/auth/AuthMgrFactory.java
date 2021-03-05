package io.github.yuemenglong.auth;

import io.github.yuemenglong.auth.impl.AuthMgr;

public class AuthMgrFactory {
    public static IAuthMgr createDefault() {
        return new AuthMgr();
    }
}
