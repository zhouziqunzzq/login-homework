<%--
  Created by IntelliJ IDEA.
  User: harry
  Date: 18-5-14
  Time: 下午8:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="include/title.jsp" flush="true">
        <jsp:param name="title" value="登录"/>
    </jsp:include>
    <link href="static/css/login.css" rel="stylesheet"/>
</head>
<body>
<div id="app" class="flex-container-column app-container">
    <div class="flex-container-column main-container blur">
        <div class="flex-container-row chooser" id="chooser">
            <a href="#" id="login-chooser" class="chooser-text">登录</a>
            <hr class="vertical-hr"/>
            <a href="#" id="register-chooser" class="chooser-text">注册</a>
        </div>
        <jsp:include page="component/login.jsp" flush="true"/>
        <jsp:include page="component/register.jsp" flush="true"/>
        <p id="welcome-msg" style="color: #ffffff; display: none;"></p>
    </div>
    <div class="flex-container-row message-container">
        <p id="error-msg" class="error-msg" style="display: none;"></p>
        <p id="msg" class="msg" style="display: none;"></p>
    </div>
</div>
<%@include file="include/basic-css.jsp" %>
<%@include file="include/basic-js.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/login.js"></script>
</body>
</html>
