package com.jacend.activiti.user_group_deploy;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author fengxf
 * @since 2016-05-23
 */
public class IdentifyServiceTest {

    @Rule //Junit4 的新特性,让我们回归元测试
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");
    // 在junit 范畴内方便 activiti 初始化,还是需要单独启动连接的数据库 H2Database

    @Test
    public void testUser(){
        // 获取 IdentityService 实例
        IdentityService identityService = activitiRule.getIdentityService();

        // 创建一个用户对象
        User user = identityService.newUser("JasonKate");
        user.setFirstName("Jason");
        user.setLastName("Kate");
        user.setEmail("jasonKate@gmail.com");
        identityService.saveUser(user);

        // 查询刚刚创建的用户对象
        User dbUser = identityService.createUserQuery().userId("JasonKate").singleResult();
        assertNotNull(dbUser);

        // 删除刚刚创建的用户对象
        identityService.deleteUser("JasonKate");
        dbUser = identityService.createUserQuery().userId("JasonKate").singleResult();
        assertNull(dbUser);
    }

    @Test
    public void testGroup(){
        IdentityService identityService = activitiRule.getIdentityService();

        Group group = identityService.newGroup("deptLeader");
        group.setName("部门领导");
        group.setType("assignment");

        identityService.saveGroup(group);
        List<Group> groupList = identityService.createGroupQuery().groupId("deptLeader").list();
        assertEquals(1, groupList.size());

        identityService.deleteGroup("deptLeader");
        groupList = identityService.createGroupQuery().groupId("deptLeader").list();
        assertEquals(0, groupList.size());
    }

    @Test
    public void testUserAndGroupMembership(){
        IdentityService identityService = activitiRule.getIdentityService();

        Group group = identityService.newGroup("deptLeader");
        group.setName("部门领导");
        group.setType("assignment");
        identityService.saveGroup(group);

        User user = identityService.newUser("jacendFeng");
        user.setFirstName("jacend");
        user.setLastName("Feng");
        user.setEmail("jacendfeng@gmail.com");
        identityService.saveUser(user);

        identityService.createMembership("jacendfeng", "deptLeader");

        // 查询 deptLeader 组中 用户就是 jacendFeng
        User userInGroup = identityService.createUserQuery().memberOfGroup("deptLeader").singleResult();
        assertNotNull(userInGroup);
        assertEquals("jacendfeng", userInGroup.getId());

        // 查询 jacendFeng 所在组 就是 deptLeader
        Group groupContainJacendFeng = identityService.createGroupQuery().groupMember("jacendFeng").singleResult();
        assertNotNull(groupContainJacendFeng);
        assertEquals("deptLeader", groupContainJacendFeng.getId());
    }
}
