<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>爱阅读后台管理系统</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg" style="background: url(${pageContext.request.contextPath}/resources/img/bg.png) no-repeat;background-size: 100% auto;">

    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div id="eventForm" style="margin: 170px auto">




            <h2>爱阅读后台管理系统</h2>
            <p>阅读是人类获取知识、增长智慧的重要方式，是一个国家、一个民族精神发育、文明传承的重要途径。
                <!--Continually expanded and constantly improved Inspinia Admin Them (IN+)-->
            </p>
            
            <form class="m-t" role="form" action="${pageContext.request.contextPath }/admin/j_spring_security_check" id="loginForm" method="post">
                <div class="form-group">
                    <input type="text" class="form-control" name="username" id="username" placeholder="用户名" required="">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control"name="password" id="password" placeholder="密码" required="">
                </div>
                <button type="submit" class="btn btn-primary block full-width m-b" onclick="checkLogin();">登录</button>
                <div class="form-group ft-red" id="msg">
                    ${msg}
                </div>
                <!-- <a href="#"><small>Forgot password?</small></a> -->
                <!-- <p class="text-muted text-center"><small>Do not have an account?</small></p> -->
                <!-- <a class="btn btn-sm btn-white btn-block" href="register.html">Create an account</a> -->
            </form>

        </div>
    </div>

    <!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script>
    $(function(){
        $('#eventForm').find('input').on('keyup', function(event) {/* 增加回车提交功能 */
            if (event.keyCode == '13') {
                checkLogin();
            }
        });
    });
    function checkLogin() {
        var username = $("#username").val();
        var password = $("#password").val();
        if (!validate_required(username)) {
//            $("#msg").html("请输入用户名！");
            return;
        }
        if (!validate_required(password)) {
//            $("#msg").html("请输入密码！");
            return;
        }
        $("#loginForm").submit();
    }

    /*验证必填项目是否填写*/
    function validate_required(value) {
        if (value == null || value == "") {
            return false;
        }
        return true;
    }


</script>


</body>

</html>
