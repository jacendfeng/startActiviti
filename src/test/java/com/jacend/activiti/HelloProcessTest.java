package com.jacend.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fengxf
 * @since 2016-05-20
 */
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

}
