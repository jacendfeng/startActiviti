<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.activiti.engine.ProcessEngine" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.activiti.engine.impl.interceptor.Command" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd" %>
<%@ page import="com.jacend.activiti.ProcessInstanceDigramCmd" %>
<%@ page import="org.activiti.engine.task.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
    ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");

    String processDefinitionId = request.getParameter("processDefinitionId");
    String processInstanceId = request.getParameter("processInstanceId");
    String taskId = request.getParameter("taskId");

    response.setContentType("image/png");

    Command<InputStream> cmd = null;

    if (processDefinitionId != null){
        cmd = new GetDeploymentProcessDiagramCmd(processDefinitionId);
    }

    if (processInstanceId != null){
        cmd = new ProcessInstanceDigramCmd(processInstanceId);
    }

    if (taskId != null){
        Task task  = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        cmd = new ProcessInstanceDigramCmd(task.getProcessInstanceId());
    }

    if (cmd != null){
        InputStream is = processEngine.getManagementService().executeCommand(cmd);
        int len = 0;
        byte[] b = new byte[1024];
        while((len = is.read(b, 0, 1024)) != -1){
            response.getOutputStream().write(b, 0 ,len);
        }
        response.getOutputStream().flush();
    }
    out.clear();
    out = pageContext.pushBody();
%>

<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
