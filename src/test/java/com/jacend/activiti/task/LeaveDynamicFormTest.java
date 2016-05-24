package com.jacend.activiti.task;

import com.jacend.activiti.BaseTest;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author fengxf
 * @since 2016-05-24
 */
public class LeaveDynamicFormTest extends BaseTest{

    @Test
    @Deployment(resources = "diagrams/LeaveFlow.bpmn")
    public void allApproved() throws Exception{
        String currentUserId = "jacendFeng";
        identityService.setAuthenticatedUserId(currentUserId);
        //这里经测试发现与startEvent applyUserId 变量名本身没有特别联系,可以取有任何字符串代替,但是实际的值和上面set的值是一样的
        //参考 http://www.kafeitu.me/activiti/2012/09/14/activiti-initiator.html

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> variables = new HashMap<String, String>();
        Calendar calendar = Calendar.getInstance();
        String startDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        String endDate = sdf.format(calendar.getTime());
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");

        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);
        assertNotNull(processInstance);

        // 部门领导审批通过
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        variables = new HashMap<String, String>();
        variables.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(deptLeaderTask.getId(), variables);

        // 人事审核通过
        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        variables = new HashMap<String, String>();
        variables.put("hrApproved", "true");
        formService.submitTaskFormData(hrTask.getId(), variables);

        // 销假
        Task reportBackTask = taskService.createTaskQuery().taskAssignee(currentUserId).singleResult();
        variables = new HashMap<String, String>();
        variables.put("reportBackDate", sdf.format(calendar.getTime()));
        formService.submitTaskFormData(reportBackTask.getId(), variables);

        // 验证流程是否结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();
        assertNotNull(historicProcessInstance);
        Map<String, Object> historyVariables = packageVariables(processInstance);
        assertEquals("ok", historyVariables.get("result"));
    }

    private Map<String, Object> packageVariables(ProcessInstance processInstance){
        Map<String, Object> historyVariables = new HashMap<String, Object>();
        List<HistoricDetail> list = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();

        for (HistoricDetail historicDetail : list) {
            if (historicDetail instanceof HistoricFormProperty){
                HistoricFormProperty field = (HistoricFormProperty) historicDetail;
                historyVariables.put(field.getPropertyId(), field.getPropertyValue());
                System.out.println("form field: taskId:"+field.getTaskId()+","
                                + field.getPropertyId() + " = " + field.getPropertyValue());
            }else if (historicDetail instanceof HistoricVariableUpdate){ //普通变量
                HistoricDetailVariableInstanceUpdateEntity variable = (HistoricDetailVariableInstanceUpdateEntity) historicDetail;
                historyVariables.put(variable.getName(), variable.getValue());
                System.out.println("variable: " + variable.getName() + " = " + variable.getValue());
            }
        }

        return historyVariables;
    }
}
