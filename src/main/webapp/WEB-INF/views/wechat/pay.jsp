<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>支付</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath }/resources/wechat/css/vip.css">

    <script src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="${pageContext.request.contextPath }/resources/wechat/js/jquery-3.1.1.min.js"></script>
</head>
<body>
<div class="header"><img src="${pageContext.request.contextPath }/resources/wechat/img/head.png"> </div>
<input type="hidden" id="memberId" value="${memberId}">
<div class="text_input ">
    <div class="firstLabel">
        <p>会员等级：</p>
    </div>
    <div class="firstInput">
        <div class="secondInput" id="member_level">
            <%--<select id="member_level" name="memberLevelID" class="firstStyle_input">
            </select>--%>
        </div>
    </div>
</div>
<%--<div class="text_input ">
    <div class="firstLabel">
        <p>支付金额：</p>
    </div>
    <div class="firstInput">
        <div class="secondInput">
            <input type="text" name="amount" class="firstStyle_input" readonly="readonly" />
        </div>
    </div>
</div>--%>
<div class="buttonLabel">
    <button type="button"   class="submitStyle" id="paymentId">确认支付</button>
</div>
</body>

<script type="text/javascript">
    $(document).ready(function(){
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/getMemberLevel",
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var selectObj = "";
                for (var i in data.obj) {
                    var detailList = data.obj;
                    if (i == 0) {
                        selectObj += '<input type="radio" name="memberLevelID" value=' + detailList[i]['id'] + ' checked=checked />'
                                + detailList[i]['memberLevelName'] + "(" + detailList[i]['amount'] + "元)";
                    } else {
                        selectObj += '<input type="radio" name="memberLevelID" value=' + detailList[i]['id'] + ' />'
                                + detailList[i]['memberLevelName'] + "(" + detailList[i]['amount'] + "元)";
                    }
                    selectObj += '<br/><br/>';
                    /*selectObj += '<option value=' + detailList[i]['id'] + '>' + detailList[i]['memberLevelName'] + '</option>';*/
                }
                $('#member_level').html(selectObj);
            },
            error: function (msg) {
                //alert("出错了！");
            }
        });
    });

    $('#paymentId').click( function () {
        var memberLevelID = $("input[name='memberLevelID']:checked").val();
        $.ajax({
            url: '${pageContext.request.contextPath}/wechat/paymentMember?memberId='+$("#memberId").val()+"&memberLevelID="+memberLevelID,
            type: 'POST',
            cache: false,
            async: false,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var obj = data.obj;
                if (data.msg == '0') {
                    window.location.href = "${pageContext.request.contextPath}/wechat/main";
                    return;
                }
                 console.info(obj);
                 if(parseInt(obj.agent)<5){
                     alert("您的微信版本低于5.0无法使用微信支付");
                     return;
                 }
                 console.info(obj.appId);
                 console.info(obj.timeStamp);
                //如果后台没有异常
                if (data.success) {
                    WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', {
                                "appId":obj.appId,     //公众号名称，由商户传入
                                "timeStamp":obj.timeStamp,         //时间戳，自1970年以来的秒数
                                "nonceStr":obj.nonceStr, //随机串
                                "package":obj.packageValue,
                                "signType":obj.signType,         //微信签名方式：
                                "paySign":obj.paySign //微信签名
                            },
                            function(res){
//                                if(res.err_msg != "get_brand_wcpay_request:cancel" && res.err_msg != "get_brand_wcpay_request:ok" ) {
//                                    alert(res.err_code + ',' + res.err_desc + ',' + res.err_msg);
//                                    return;
//                                } else if(res.err_msg == "get_brand_wcpay_request:ok") {
                                    location.href = '${pageContext.request.contextPath }/wechat/main';
//                                }
                            }
                    );
                } else {
                    alert(data.msg);
                }
        }
     });
 });
</script>
</html>
