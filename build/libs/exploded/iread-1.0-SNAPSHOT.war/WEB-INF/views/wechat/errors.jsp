<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/29
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- 字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport就是设备的屏幕上能用来显示我们的网页的那一块区域 -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Title</title>
    <style type="text/css">
        .error_title ul li {
            display: block;
            width: 100%;
            height:auto;
        }
        .error_title ul {
            margin:0;
            padding: 0;
        }
        .error_title ul li img {
            width: 100%;
            /*height:100%;*/
            vertical-align: middle;
        }
    </style>
</head>
<body>
<div class="error_title">
    <ul>
        <li >
            <img src="${pageContext.request.contextPath}/resources/wechat/img/1.png" alt="爱阅读">
        </li>
        <li>
            <img src="${pageContext.request.contextPath}/resources/wechat/img/123.png" alt="敬请期待">
        </li>
    </ul>


</div>
<div class="text"  style="text-align: center; font-size: 14px; margin: 0 auto">
    移动互联协同创新中心
    <p style="text-align: center; font-size: 14px;"></p>
</div>
</body>
</html>
