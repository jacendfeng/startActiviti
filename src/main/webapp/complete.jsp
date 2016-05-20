<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.activiti.engine.ProcessEngine" %>
<%@ page import="org.activiti.engine.TaskService" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
    ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");

    TaskService taskService = processEngine.getTaskService();

    // 传递参数的交互方式
    Map<String, Object> map = new HashMap();
    map.put("superior", "superior");
    taskService.complete(request.getParameter("id"), map);

    response.sendRedirect("index.jsp");
%>