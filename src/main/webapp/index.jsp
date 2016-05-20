<%@ page import="org.activiti.engine.ProcessEngine" %>
<%@ page import="org.activiti.engine.RepositoryService" %>
<%@ page import="org.activiti.engine.RuntimeService" %>
<%@ page import="org.activiti.engine.TaskService" %>
<%@ page import="org.activiti.engine.repository.ProcessDefinition" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.activiti.engine.runtime.ProcessInstance" %>
<%@ page import="org.activiti.engine.task.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);

    ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");

    RepositoryService repositoryService = processEngine.getRepositoryService();
    RuntimeService runtimeService = processEngine.getRuntimeService();
    TaskService taskService = processEngine.getTaskService();
%>

<html>
<head>首页</head>
<body>
    <a href="deploy.jsp">部署一个流程定义</a>
    <a href="h2database">查看 h2 Databasae</a>
    <hr/>

    <table border="1" width="100%">
        <legend>process definition 流程定义</legend>
        <thead>
            <tr>
                <th>key 流程定义的Id</th>
                <th>name 流程定义的name</th>
                <th>&nbsp;</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (ProcessDefinition processDefinition : repositoryService.createProcessDefinitionQuery().list()){
                    pageContext.setAttribute("processDefinition", processDefinition);
            %>
            <tr>
                <td>${processDefinition.key}</td>
                <td>${processDefinition.name}</td>
                <td>
                    <a href="start.jsp?id=${processDefinition.id}">start</a>
                    <a href="graph.jsp?processDefinitionId=${processDefinition.id}">graph</a>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <br>
    <table border="1" width="100%">
        <legend>process instance 流程实例</legend>
        <thead>
        <tr>
            <th>id 流程实例的Id</th>
            <th>process definition id 对应流程定义的Id</th>
            <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <%
            // System.out.println(runtimeService.createProcessInstanceQuery().list().size());
            for (ProcessInstance processInstance : runtimeService.createProcessInstanceQuery().list()){
                pageContext.setAttribute("processInstance", processInstance);
        %>
        <tr>
            <td>${processInstance.id}</td>
            <td>${processInstance.processDefinitionId}</td>
            <td>
                <a href="graph.jsp?processInstanceId=${processInstance.id}">graph</a>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <br/>
    <table border="1" width="100%">
        <legend>task 用户任务</legend>
        <thead>
        <tr>
            <th>id 任务编号</th>
            <th>name 任务名称</th>
            <th>assginee 执行人</th>
            <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Task task : taskService.createTaskQuery().list()){
                pageContext.setAttribute("task", task);
        %>
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>${task.assignee}</td>
            <td>
                <a href="complete.jsp?id=${task.id}">完成任务</a>
                <a href="graph.jsp?taskId=${task.id}">graph</a>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</body>
</html>
