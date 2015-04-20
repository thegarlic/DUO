<%--
  Created by IntelliJ IDEA.
  User: kws
  Date: 15. 3. 31.
  Time: 오후 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>DUO - Registeration</title>
    <link rel="stylesheet" href="/stylesheet/common.css"/>
    <link rel="stylesheet" href="/stylesheet/reset.css"/>
</head>
<body>

<div id="container">
    <div id="inner-container">
        <h1>회원가입</h1>

        <form action="/user" method="post">
            <input type="email" name="email" placeholder="이메일"/>
            <input type="password" name="password" placeholder="비밀번호"/>
            <input type="text" name="name" placeholder="닉네임"/>
            <input type="number" name="age" placeholder="나이" min="0" step="1"/>
            <input type="submit" content="가입"/>
        </form>
        <a href="/user/login">로그인</a>
    </div>
</div>

</body>
</html>