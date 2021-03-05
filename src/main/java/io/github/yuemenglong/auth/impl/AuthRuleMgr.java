package io.github.yuemenglong.auth.impl;

import io.github.yuemenglong.auth.AuthMethod;
import io.github.yuemenglong.auth.AuthType;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
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
        String[] roles;
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

    private static AtomicLong counter = new AtomicLong();
    private static ConcurrentSkipListSet<Rule> rules = new ConcurrentSkipListSet<>();
    private static ConcurrentHashMap<String, Rule> ruleMap = new ConcurrentHashMap<>();

    // 已有的order不能修改
    synchronized private void addRule(String url, AuthMethod method, String[] roles, int order, AuthType type) {
        String key = String.format("%s:%s", method, url);
        if (!ruleMap.containsKey(key)) {
            Rule rule = new Rule();
            rule.order = order;
            rules.add(rule);
            ruleMap.put(key, rule);
        }
        Rule rule = ruleMap.get(key);
        rule.url = url;
        rule.method = method;
        rule.roles = roles;
        rule.type = type;
    }

    synchronized public void permit(String url, AuthMethod method, String[] roles, int order) {
        addRule(url, method, roles, order, AuthType.PERMIT);
    }

    public void permitAll(String url, AuthMethod method, int order) {
        addRule(url, method, new String[]{}, order, AuthType.PERMIT);
    }

    public void deny(String url, AuthMethod method, String[] roles, int order) {
        addRule(url, method, roles, order, AuthType.DENY);
    }

    public void denyAll(String url, AuthMethod method, int order) {
        addRule(url, method, new String[]{}, order, AuthType.DENY);
    }

    public void clean(String url, AuthMethod method) {

    }

    public void clean(String url) {

    }

    public boolean authenticate(String url, AuthMethod method, String[] roles) {
        return false;
    }

    public static void main() {

    }
}
