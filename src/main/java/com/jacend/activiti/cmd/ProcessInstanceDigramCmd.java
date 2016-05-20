package com.jacend.activiti.cmd;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import java.io.InputStream;
import java.util.List;

/**
 * @author fengxf
 * @since 2016-05-20
 */
public class ProcessInstanceDigramCmd implements Command<InputStream> {

    protected String processInstanceId;

    public ProcessInstanceDigramCmd(String processInstanceId){
        this.processInstanceId = processInstanceId;
    }


    @Override
    public InputStream execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        ExecutionEntity executionEntity = executionEntityManager.findExecutionById(processInstanceId);

        List<String> activitiIds = executionEntity.findActiveActivityIds();
        String processDefinitionId = executionEntity.getProcessDefinitionId();

        GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(processDefinitionId);
        BpmnModel bpmnModel = getBpmnModelCmd.execute(commandContext);

        // 这个 ProcessDiagramGenerator 5.14 版本后需要在 pom.xml 添加 activiti-image-generator 依赖
        InputStream is = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitiIds);
        return is;
    }
}
