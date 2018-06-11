<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 申明使用h5编写 -->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<!-- 字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- viewport就是设备的屏幕上能用来显示我们的网页的那一块区域 -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <title>爱阅读公益</title>

    <link href="${pageContext.request.contextPath}/resources/css/weui.min.css" rel="stylesheet">
	
	<link href="https://cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/css/mystyle.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">
	

</head>
<body ontouchstart>
<div class="weui-cells no-margin">
	<div class="weui-cell">
	<div class="weui-cell__bd">
		<a class='close-popup' id="back"><i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回</a>
		<%--<a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary" style="float: right;">提交</a>--%>
	</div>
	</div>
</div>

<div class="weui-cells">
	<div class="weui-cell">
        <div class="weui-cell__bd">
            <p>会员等级</p>
        </div>
        <div class="weui-cell__ft" id="memberLevel">
            ${member.memberLevel.memberLevelName}
        </div>
    </div>
</div>

<div class="weui-cells">
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <p>姓名</p>
        </div>
        <div class="weui-cell__ft" id="memberName">
            ${member.memberName}
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <p>性别</p>
        </div>
        <div class="weui-cell__ft" id="sex">
            <c:if test="${member.sex == 1}">
                <c:out value="男" />
            </c:if>
            <c:if test="${member.sex == 0}">
                <c:out value="女" />
            </c:if>

        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <p>城市</p>
        </div>
        <div class="weui-cell__ft" id="city">
            ${member.city.cityName}
        </div>
    </div>
    <%--<div class="weui-cell">
        <div class="weui-cell__hd"><label for="city" class="weui-label">城市</label></div>
        <div class="weui-cell__bd">
          <input class="weui-input" id="city" type="text" value="" readonly="">
        </div>
      </div>--%>
</div>



<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script>

$("#back").on("click", function() {
    window.location.href="${pageContext.request.contextPath}/wechat/main";
});

</script>
</body>
</html>