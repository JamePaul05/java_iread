 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <script type="text/javascript" src="https://3gimg.qq.com/lightmap/components/geolocation/geolocation.min.js "></script>

    <%--微信js sdk--%>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

    <style type="text/css">
        .head {
            position: fixed;
            top:  0;
            left: 0;
            width: 100%;
            z-index: 500
        }
        .content {
            position: absolute;
            top: 175px;
            left: 0;
            bottom: 0;
            right: 0;
            overflow: auto;
        }
        .myP {
            font-size: 13px;
            float: right;
        }
        .zuigao {
            z-index: 501;
        }
        .myTitle {
            /*margin-top*/
            padding-left: 20px;
        }
        .bookInfo {
            margin-left: 15px;
            position: absolute;
            top: 49.33px;
            left: 0;
            bottom: 0;
            right: 0;
            overflow: auto;
        }
        .bookInfo_title {
            margin: 7px 0 0 10px;
        }
        .bookInfo_img {
            margin-top: 7px;
            margin-right: 10px;
            margin-bottom: 0;
        }
        .bookInfo_many {
            margin: 7px 0 0 10px;
        }
        .bookInfo_many .bookInfo_one {
            margin: 5px 0 5px 0;
        }
    </style>

</head>
<body ontouchstart>

<div class="weui-cells no-margin">
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <a class='close-popup'><i class="fa fa-angle-left" id="back" style="margin-right: 5px;"></i>返回</a>
            <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary" style="float: right;" onclick="borrowBook()">确定归还</a>
        </div>
    </div>
</div>

<div class="bookInfo">
    <div class="weui-flex bookInfo_title">
        <div class="weui-flex__item">
            <h2 class="myTitle" id="bookName">${book.bookName}</h2>
        </div>
    </div>
    <div class="weui-flex">
        <div class="bookInfo_img">
            <img class="weui-media-box__thumb" src="${book.simgUrl}" alt="" id="simg">
        </div>
        <div class="weui-flex__item bookInfo_many">
            <div class="weui-flex">
                <div class="weui-flex__item bookInfo_one">
                    作者：<span id="author">${book.author}</span>
                </div>
            </div>
            <div class="weui-flex">
                <div class="weui-flex__item bookInfo_one">
                    出版社：<span id="press">${book.press}</span>
                </div>
            </div>
            <div class="weui-flex">
                <div class="weui-flex__item bookInfo_one">
                    编号：<span id="bianhao">${bianhao}</span>
                </div>
            </div>
            <div class="weui-flex bookInfo_one">
                <div class="weui-flex__item">
                    定价：<span id="price">${book.price}</span>
                </div>
            </div>
        </div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item" style="font-size: 20px;">评分：<span id="grade">${book.grade}</span></div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <h4>简介：</h4><p id="summary">${book.summary}</p>
        </div>
    </div>
</div>
</div>
</div>




<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script>
    
    $("#back").on("click", function (r) {
        window.location.href="${pageContext.request.contextPath}/wechat/main";
    });
    
    
    function borrowBook() {
        var bianhao = $("#bianhao").html();
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/returnBook",
            dataType: "json",
            data: {
                bianhao: bianhao
            },
            success: function (r) {
                var flag = r.flag;
                var msg = r.msg;
                if (flag) {
                    $.toast(msg);
                } else {
                    $.toast(msg, "forbidden");
                }
            }
        });
    }
</script>

</body>
</html>