<%--
  Created by IntelliJ IDEA.
  User: harry
  Date: 18-4-7
  Time: 下午5:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="login" style="width: 100%;">
    <div class="input-wrapper">
        <input type="text" class="text-input text-input-longer"
               value=""
               name="username" id="login-username" placeholder="用户名"/>
    </div>
    <div class="input-wrapper">
        <input type="password" class="text-input text-input-longer"
               value=""
               name="password" id="login-password" placeholder="密码"/>
    </div>
    <div class="input-wrapper flex-container-column">
        <button type="submit" class="basic-button register-button"
                id="login-submit">登录
        </button>
    </div>
</form>
