<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.activiti.engine.ProcessEngine" %>
<%@ page import="org.activiti.engine.RuntimeService" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: souche
  Date: 16/5/19
  Time: 下午7:55
  To change this template use File | Settings | File Templates.
--%>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
    ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");

    RuntimeService runtimeService = processEngine.getRuntimeService();

    // 流程中需要一些外部变量协作的话, 可以使用下面这种方式
    Map params = new HashMap<String,String>();
    params.put("assignee", "Lingo");
    params.put("participants", "user1, user2");
    runtimeService.startProcessInstanceById(request.getParameter("id"), params);

    response.sendRedirect("index.jsp");
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>