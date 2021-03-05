package io.github.yuemenglong.auth;

import org.junit.Assert;
import org.junit.Test;

public class TestCase {
    @Test
    public void testNormal() throws InterruptedException {
        IAuthMgr authMgr = AuthMgrFactory.createDefault();

        authMgr.permit("/admin/**", AuthMethod.GET, new String[]{"ADMIN"});
        authMgr.denyAll("/admin/**", AuthMethod.GET);
        authMgr.permitAll("/login", AuthMethod.GET);
        authMgr.permit("/**", AuthMethod.GET, new String[]{"USER", "ADMIN"});
        authMgr.denyAll("/**", AuthMethod.GET);
        authMgr.permitAll("/**", AuthMethod.POST);

        String adminToken = "1";
        String userToken = "2";
        authMgr.login(1, "admin", new String[]{"ADMIN"}, "1", 10 * 1000);
        authMgr.login(2, "user", new String[]{"USER"}, "2", 10 * 1000);

        Assert.assertTrue(authMgr.authenticate("/", AuthMethod.GET, userToken));
        Assert.assertTrue(authMgr.authenticate("/", AuthMethod.GET, adminToken));
        Assert.assertTrue(authMgr.authenticate("/a/b/c", AuthMethod.GET, adminToken));
        Assert.assertTrue(authMgr.authenticate("/a/b/c", AuthMethod.GET, userToken));
        Assert.assertFalse(authMgr.authenticate("/admin", AuthMethod.GET, userToken));
        Assert.assertTrue(authMgr.authenticate("/admin", AuthMethod.GET, adminToken));
        Assert.assertFalse(authMgr.authenticate("/admin/a/b/c", AuthMethod.GET, userToken));
        Assert.assertTrue(authMgr.authenticate("/admin/a/b/c", AuthMethod.GET, adminToken));
        Assert.assertTrue(authMgr.authenticate("/login", AuthMethod.GET, userToken));
        Assert.assertTrue(authMgr.authenticate("/login", AuthMethod.GET, adminToken));
        Assert.assertTrue(authMgr.authenticate("/login", AuthMethod.GET, ""));
        Assert.assertTrue(authMgr.authenticate("/login", AuthMethod.GET, null));
        Assert.assertFalse(authMgr.authenticate("/a/b/c", AuthMethod.GET, ""));
        Assert.assertFalse(authMgr.authenticate("/a/b/c", AuthMethod.GET, null));
        Assert.assertTrue(authMgr.authenticate("/a/b/c", AuthMethod.POST, null));
        authMgr.logout(userToken);
        Assert.assertFalse(authMgr.authenticate("/", AuthMethod.GET, userToken));
        Assert.assertFalse(authMgr.authenticate("/a/b/c", AuthMethod.GET, userToken));
        Thread.sleep(10 * 1000); // 等待超期
        Assert.assertFalse(authMgr.authenticate("/", AuthMethod.GET, adminToken));
        Assert.assertFalse(authMgr.authenticate("/a/b/c", AuthMethod.GET, adminToken));
    }
}
