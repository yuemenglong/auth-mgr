package io.github.yuemenglong.auth;

public interface IAuthUser {
    long getId();

    String getName();

    String[] getRoles();

    void setId(long id);

    void setName(String name);

    void setRoles(String[] roles);
}
