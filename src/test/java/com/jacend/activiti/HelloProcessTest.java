package com.jacend.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxf
 * @since 2016-05-20
 */
@RunWith(Junit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:activiti.cfg.xml"})
public class HelloProcessTest {

    @Test
    public void testStartProcess(){
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("diagrams/hello.bpmn").deploy();

        // 如果遇到 file cannot find 请参照 https://forums.activiti.org/content/resource-not-found-exception

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Assert.assertEquals("hello", processDefinition.getKey());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("hello");
        Assert.assertNotNull(processInstance);

        System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());

    }

    @Test
    public void addUserTask() throws FileNotFoundException {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();

        String bpnmFileName = "/Users/souche/Codes/gitCodes/LetActiviti/src/main/resources/diagrams/hello.bpmn";
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addInputStream("/Users/souche/Codes/gitCodes/LetActiviti/src/main/resources/diagrams/hello.bpmn",
                        new FileInputStream(bpnmFileName))
                .deploy();

//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.createDeployment().addClasspathResource("diagrams/hello.bpmn").deploy();


        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Assert.assertEquals("hello", processDefinition.getKey());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUsere", "empol0yee1");
        variables.put("days", "3");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("hello");
        Assert.assertNotNull(processInstance);
        System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());

        // 创建任务
        TaskService taskService = processEngine.getTaskService();
        Task firstFlowTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        Assert.assertNotNull(firstFlowTask);
        Assert.assertEquals("领导审批", firstFlowTask.getName());

        // 分配任务,执行任务
        taskService.claim(firstFlowTask.getId(), "leaderUser");
        variables.clear();
        variables.put("approved", true);
        taskService.complete(firstFlowTask.getId(), variables);
        firstFlowTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        Assert.assertNull(firstFlowTask);

        // 查询活动历史
        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricActivityInstanceQuery().finished().count();
        System.out.println(count);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testForGithubPushInIDEA(){
        System.out.println("haha");
    }

}
