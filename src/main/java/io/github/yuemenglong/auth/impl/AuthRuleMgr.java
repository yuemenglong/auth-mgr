package io.github.yuemenglong.auth.impl;

import io.github.yuemenglong.auth.AuthMethod;
import io.github.yuemenglong.auth.AuthType;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

//　　字符wildcard　　　　描述
//　　 ?　　　　　　　　　匹配一个字符
// 　　*　　　　　　　　　匹配0个及以上字符
// 　　**　　　　　　　　 匹配0个及以上目录directories
public class AuthRuleMgr {
    static class Rule implements Comparable<Rule> {
        int order;
        String url;
        AuthMethod method;
        AuthType type;

        // 对应空集表明全部匹配
        Set<String> roles;
        int idx = (int) counter.incrementAndGet();

        @Override
        public int compareTo(Rule o) {
            if (this.order == o.order) {
                return this.idx - o.idx;
            } else {
                return this.order - o.order;
            }
        }
    }

    private static final AtomicLong counter = new AtomicLong();
    private static final ConcurrentSkipListSet<Rule> rules = new ConcurrentSkipListSet<>();

    // 已有的order不能修改
    synchronized private void addRule(String url, AuthMethod method, String[] roles, int order, AuthType type) {
        Rule rule = new Rule();
        rule.order = order;
        rule.url = url;
        rule.method = method;
        rule.roles = new HashSet<>(Arrays.asList(roles));
        rule.type = type;
        rules.add(rule);
    }

    synchronized public void permit(String url, AuthMethod method, String[] roles, int order) {
        addRule(url, method, roles, order, AuthType.PERMIT);
    }

    synchronized public void permitAll(String url, AuthMethod method, int order) {
        addRule(url, method, new String[]{}, order, AuthType.PERMIT);
    }

    synchronized public void deny(String url, AuthMethod method, String[] roles, int order) {
        addRule(url, method, roles, order, AuthType.DENY);
    }

    synchronized public void denyAll(String url, AuthMethod method, int order) {
        addRule(url, method, new String[]{}, order, AuthType.DENY);
    }

    synchronized public void clean(String url, AuthMethod method) {
        rules.removeIf(rule -> rule.method.equals(method) && rule.url.equals(url));
    }

    synchronized public void clean(String url) {
        rules.removeIf(rule -> rule.url.equals(url));
    }

    public boolean authenticate(String url, AuthMethod method, String[] roles) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (Rule rule : rules) {
            if (method == rule.method && matcher.match(rule.url, url)) {
                // 不分角色
                if (rule.roles.size() == 0) {
                    return rule.type == AuthType.PERMIT;
                }
                for (String role : roles) {
                    if (rule.roles.contains(role)) {
                        return rule.type == AuthType.PERMIT;
                    }
                }
            }
        }
        return false;
    }
}
