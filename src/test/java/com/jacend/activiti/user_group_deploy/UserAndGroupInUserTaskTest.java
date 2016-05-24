package com.jacend.activiti.user_group_deploy;

import com.jacend.activiti.BaseTest;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author fengxf
 * @since 2016-05-23
 */
public class UserAndGroupInUserTaskTest extends BaseTest{

    @Before
    public void setUp(){
        super.setUp();

        Group group = identityService.newGroup("deptLeader");
        group.setName("部门领导");
        group.setType("assignment");
        identityService.saveGroup(group);

        User user = identityService.newUser("jacendFeng");
        user.setFirstName("jacend");
        user.setLastName("Feng");
        user.setEmail("jacendfeng@gmail.com");
        identityService.saveUser(user);
        identityService.createMembership("jacendFeng", "deptLeader");
    }

    @After
    public void afterInvokeTestMethod(){
        identityService.deleteMembership("jacendFeng", "deptLeader");
        identityService.deleteGroup("deptLeader");
        identityService.deleteUser("jacendFeng");
    }

    @Test
    @Deployment(resources = {"./diagrams/user_group_deploy/userAndGroupInUserTask.bpmn"})
    public void testUserAndGroupInUserTask(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTask");
        assertNotNull(processInstance);

        Task task = taskService.createTaskQuery().taskCandidateUser("jacendFeng").singleResult();
        taskService.claim(task.getId(), "jacendFeng");
        taskService.complete(task.getId());
    }

    @Test
    @Deployment(resources = {"./diagrams/user_group_deploy/userAndGroupInUserTask.bpmn"})
    public void testUserTaskWithGroupContainsTwoUser() throws Exception{
        // 在setUp() 基础上再添加一个用户, 然后加入到组 deptLeader 中去
        User user = identityService.newUser("jackchen");
        user.setFirstName("jack");
        user.setLastName("chen");
        user.setEmail("jakechen@gmail.com");
        identityService.saveUser(user);

        // jackchen 用户加入到 deptLeader 组中
        identityService.createMembership("jackchen", "deptLeader");
        // 启动 userAndGroupInUserTask 流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTask");
        assertNotNull(processInstance);

        // 两个人都在 deptLeader 组中,所以都可以查询得到
        Task jackchenTask = taskService.createTaskQuery().taskCandidateUser("jackchen").singleResult();
        assertNotNull(jackchenTask);
        Task jacendTask = taskService.createTaskQuery().taskCandidateUser("jacendFeng").singleResult();
        assertNotNull(jacendTask);

        // jackchen 签收任务
        taskService.claim(jacendTask.getId(), "jackchen");

        // 再次查询 jacendFeng 是否拥有刚刚的候选任务
        jacendTask = taskService.createTaskQuery().taskCandidateUser("jacendFeng").singleResult();
        assertNull(jacendTask);
    }

    @Test
    @Deployment(resources = {"./diagrams/user_group_deploy/userAndGroupInUserTask.bpmn"})
    public void testMultiCadidateUserInUserTask() throws Exception{
        // 在setUp() 基础上再添加一个用户, 然后加入到组 deptLeader 中去
        User user = identityService.newUser("jackchen");
        user.setFirstName("jack");
        user.setLastName("chen");
        user.setEmail("jakechen@gmail.com");
        identityService.saveUser(user);

        // 启动 userAndGroupInUserTask 流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTask");
        assertNotNull(processInstance);

        // 两个人都在 deptLeader 组中,所以都可以查询得到
        Task jackchenTask = taskService.createTaskQuery().taskCandidateUser("jackchen").singleResult();
        assertNotNull(jackchenTask);
        Task jacendTask = taskService.createTaskQuery().taskCandidateUser("jacendFeng").singleResult();
        assertNotNull(jacendTask);

        // jackchen 签收任务
        taskService.claim(jacendTask.getId(), "jackchen");

        // 再次查询 jacendFeng 是否拥有刚刚的候选任务
        jacendTask = taskService.createTaskQuery().taskCandidateUser("jacendFeng").singleResult();
        assertNull(jacendTask);
    }
}
