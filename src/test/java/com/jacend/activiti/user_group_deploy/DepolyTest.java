package com.jacend.activiti.user_group_deploy;

import com.jacend.activiti.BaseTest;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * @author fengxf
 * @since 2016-05-24
 */
public class DepolyTest extends BaseTest{

    @Test
    public void testClasspathDeployment(){
        // 定义classpath
        String bpmnClasspath = "./diagrams/hello.bpmn";
        String pngClasspath = "./diagrams/hello.png";

        // 创建部署构建器
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // 添加资源
        deploymentBuilder.addClasspathResource(bpmnClasspath);
        deploymentBuilder.addClasspathResource(pngClasspath);
        // 执行部署
        deploymentBuilder.deploy();
        // 验证是否部署成功
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.processDefinitionKey("hello").count();
        assertEquals(1, count);
        // 读取图片文件
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        String diagramResourceName = processDefinition.getDiagramResourceName();
        assertEquals(pngClasspath, diagramResourceName);
    }

    @Test
    public void testInputStreamFromAAbsoluteFilePath() throws Exception{
        String filepath = "/Users/souche/Codes/gitCodes/LetActiviti/src/main/resources/diagrams/hello.bpmn";

        FileInputStream fileInputStream = new FileInputStream(filepath);
        repositoryService.createDeployment().addInputStream("hello.bpmn", fileInputStream).deploy();

        // 验证是否部署成功
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        long count = pdq.processDefinitionKey("hello").count();
        assertEquals(1, count);
    }

    // 还可以使用 DeploymentBuilder.addString 来部署

    @Test
    public void testCharsDeployment() {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:tns=\"http://www.activiti.org/test\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" expressionLanguage=\"http://www.w3.org/1999/XPath\" id=\"m1463654133003\" name=\"\" targetNamespace=\"http://www.activiti.org/test\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "  <process id=\"hello\" isClosed=\"false\" isExecutable=\"true\" name=\"我的第一个流程定义\" processType=\"None\">\n" +
                "    <startEvent id=\"_2\" name=\"StartEvent\"/>\n" +
                "    <userTask activiti:candidateGroups=\"deptLeader\" activiti:exclusive=\"true\" id=\"_3\" name=\"领导审批\"/>\n" +
                "    <sequenceFlow id=\"_5\" sourceRef=\"_2\" targetRef=\"_3\"/>\n" +
                "    <scriptTask activiti:exclusive=\"true\" id=\"_4\" name=\"输出审批结果\" scriptFormat=\"javascript\">\n" +
                "      <script><![CDATA[approved]]></script>\n" +
                "    </scriptTask>\n" +
                "    <endEvent id=\"_6\" name=\"EndEvent\"/>\n" +
                "    <sequenceFlow id=\"_7\" sourceRef=\"_3\" targetRef=\"_4\"/>\n" +
                "    <sequenceFlow id=\"_8\" sourceRef=\"_4\" targetRef=\"_6\"/>\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram documentation=\"background=#3C3F41;count=1;horizontalcount=1;orientation=0;width=842.4;height=1195.2;imageableWidth=832.4;imageableHeight=1185.2;imageableX=5.0;imageableY=5.0\" id=\"Diagram-_1\" name=\"New Diagram\">\n" +
                "    <bpmndi:BPMNPlane bpmnElement=\"hello\">\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"_2\" id=\"Shape-_2\">\n" +
                "        <omgdc:Bounds height=\"32.0\" width=\"32.0\" x=\"170.0\" y=\"70.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"32.0\" width=\"32.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"_3\" id=\"Shape-_3\">\n" +
                "        <omgdc:Bounds height=\"55.0\" width=\"85.0\" x=\"150.0\" y=\"190.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"55.0\" width=\"85.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"_4\" id=\"Shape-_4\">\n" +
                "        <omgdc:Bounds height=\"55.0\" width=\"85.0\" x=\"150.0\" y=\"310.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"55.0\" width=\"85.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"_6\" id=\"Shape-_6\">\n" +
                "        <omgdc:Bounds height=\"32.0\" width=\"32.0\" x=\"175.0\" y=\"445.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"32.0\" width=\"32.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"_5\" id=\"BPMNEdge__5\" sourceElement=\"_2\" targetElement=\"_3\">\n" +
                "        <omgdi:waypoint x=\"186.0\" y=\"102.0\"/>\n" +
                "        <omgdi:waypoint x=\"186.0\" y=\"190.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"0.0\" width=\"0.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"_7\" id=\"BPMNEdge__7\" sourceElement=\"_3\" targetElement=\"_4\">\n" +
                "        <omgdi:waypoint x=\"192.5\" y=\"245.0\"/>\n" +
                "        <omgdi:waypoint x=\"192.5\" y=\"310.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"0.0\" width=\"0.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"_8\" id=\"BPMNEdge__8\" sourceElement=\"_4\" targetElement=\"_6\">\n" +
                "        <omgdi:waypoint x=\"191.0\" y=\"365.0\"/>\n" +
                "        <omgdi:waypoint x=\"191.0\" y=\"445.0\"/>\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds height=\"0.0\" width=\"0.0\" x=\"0.0\" y=\"0.0\"/>\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>";
        repositoryService.createDeployment().addString("hello.bpmn", text).deploy();
        // 验证流程定义是否部署成功
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        long count = pdq.processDefinitionKey("hello").count();
        assertEquals(1, count);
    }


}
