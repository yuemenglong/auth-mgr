package io.github.yuemenglong.auth.impl;

import io.github.yuemenglong.auth.IAuthUser;

public class AuthUser implements IAuthUser {

    private long id;
    private String name;
    private String[] roles;

    public AuthUser(long id, String name, String[] roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String[] getRoles() {
        return this.roles;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
