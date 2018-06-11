<%--
  Created by IntelliJ IDEA.
  User: clyde
  Date: 2016/10/23
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${status}${msg}</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mystyle.css" type="text/css">

    <style type="text/css">
        img {
            display: block;
        }
        .body-error {
            background-color: #87ced4;
            color: #fff;
        }
        .body-error .error-img {
            display: block;
            width: 200px;
            margin: 0 auto;
        }
        .body-error .status {
            line-height: 1.5;
            font-size: 50px;
        }
        .body-error .msg {
            font-size: 20px;
            line-height:1;
        }
        .body-error .btn {
            width: 220px;
            margin: 20px auto;
            text-indent: 20px;
            letter-spacing: 20px;
        }
    </style>
</head>
<body class="body-error">
<div class="container-fluid">
    <div class="row">
        <img class="error-img" src="${pageContext.request.contextPath}/resources/img/error.png" alt="">
    </div>
    <div class="row text-center status">
        ${msg}
    </div>
   <%-- <div class="row text-center msg" >
        ${status}
    </div>--%>
    <div class="row text-center">
        <button class="btn btn-primary" onclick="history.back(-1);">
            退回
        </button>
    </div>
</div>
</body>

</html>
