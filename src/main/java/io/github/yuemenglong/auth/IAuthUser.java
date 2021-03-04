package io.github.yuemenglong.auth;

public interface IAuthUser {
    long getId();

    String getName();

    String[] getRoles();
}
