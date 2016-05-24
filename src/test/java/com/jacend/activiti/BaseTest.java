package com.jacend.activiti;

import org.activiti.engine.*;
import org.activiti.engine.test.ActivitiRule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * @author fengxf
 * @since 2016-05-23
 */
public class BaseTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    protected IdentityService identityService;
    protected RuntimeService runtimeService;
    protected FormService formService;
    protected ManagementService managementService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected RepositoryService repositoryService;

    @BeforeClass
    public static void setUpForClass(){
        System.out.println("+++++++开始测试+++++++++");
    }

    @AfterClass
    public static void testOverClass(){
        System.out.println("+++++++结束测试+++++++++");
    }

    @Before
    public void setUp(){
        identityService = activitiRule.getIdentityService();
        runtimeService = activitiRule.getRuntimeService();
        formService = activitiRule.getFormService();
        managementService = activitiRule.getManagementService();
        taskService = activitiRule.getTaskService();
        historyService = activitiRule.getHistoryService();
        repositoryService = activitiRule.getRepositoryService();
    }

}
