<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.activiti.engine.ProcessEngine" %>
<%@ page import="org.activiti.engine.RepositoryService" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String action = request.getParameter("action");
    if ("deploy".equals(action)){
        String xml = request.getParameter("xml");

        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
        ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");

        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.createDeployment()
                .addInputStream("process.bpmn20.xml", new ByteArrayInputStream(xml.getBytes("UTF-8")))
                .deploy();

        // process.bpmn20.xml 默认后缀, 表示是一个符合bpmn2.0标准的
        response.sendRedirect("index.jsp");
    }
%>
<html>
<head>
    <title>deploy the process definition</title>
</head>
<body>
    <form action="deploy.jsp?action=deploy" method="post">
        <button>deploy</button>
        <textarea name="xml" rows="20" cols="120"></textarea>
    </form>
</body>
</html>
