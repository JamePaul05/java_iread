<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/resources/wechat/css/read.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/resources/wechat/js/read.js"></script>
    <script src="${pageContext.request.contextPath }/resources/wechat/js/jquery-3.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath }/resources/wechat/js/validate/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath }/resources/wechat/js/validate/validate.expand.js"></script>
    <style type="text/css">
        .error {
            color: red;
            font-size: 15px;
        }
        .hide {display: none;}
        .list{}
        .on {}
    </style>
</head>
<body>
<div class="header"><img src="${pageContext.request.contextPath }/resources/wechat/img/1.png"> </div>
<div class="content">
    <div class="article_content">
        <div class="head_articleContent">
            <div class="logo_articleContent"><img src="${pageContext.request.contextPath }/resources/wechat/img/logo_articleContent.png"></div>
            <h3>爱阅读 · 理想家会员</h3></div>
        <p>请认真填写下面信息，我们会保护您的隐私。我们会不断优化流程，争取为您提供更好的服务。
            请关注“爱阅读公众号”及时了解新书单和公益活动。</p>
    </div>
    <div class="picture_content">
        <div class="logo_pictureContent"><img src="${pageContext.request.contextPath }/resources/wechat/img/2(1).png"> </div>
        <div class="weiXin">
            <div class="logo_weiXin"><img src="${pageContext.request.contextPath }/resources/wechat/img/3(1).png"></div>
            <div class="text_weiXin"><p>爱阅读服务中心</p></div>
        </div>
    </div>
    <div class="article_content">
        <div class="head_articleContent">
            <div class="logo_articleContent"><img src="${pageContext.request.contextPath }/resources/wechat/img/logo_articleContent.png"></div>
            <h3>宣言</h3></div>
        <p>“爱阅读·理想家”宗旨是促进书籍阅读和分享。我自愿加入爱阅读·理想家会员。秉承非营利组
            织的公益思想，我把会员费捐赠作为“爱阅读公益”的公益基金，用于让更多的人分享这份关爱。</p>
    </div>
</div>
<div class="register">
    <div class="firstParagraph_register">
        <div class="head_firstParagraph">“爱阅读·理想家”会员为终身制，无需再缴费。为规范管理，会员权益不可转让。</div>

        <div class="body_firstParagraph f-cb">
            <div class="list_bodyFirstParagraph">
                <div class="item_bodyFirstParagraph">
                    <ul>
                        <li>爱心会员享有的权益：</li>
                        <li>1.在公益活动点阅读</li>
                        <li>2.每次一本书的外借权利</li>
                        <li>3.可参加“爱阅读·理想家”各种公益课程活动</li>
                        <li>4.赠送爱阅读标识开瓶器冰箱贴一枚。</li>
                    </ul>
                </div>
            </div>
            <div class="image_bodyFirstParagraph"><img src="${pageContext.request.contextPath }/resources/wechat/img/image_bodyFirstParagraph.png"></div>
        </div>
    </div>
    <div class="secondParagraph_register">
        <ul>
            <li>一次捐助，一生阅读。</li>
            <li><p>会员费说明：全部捐入爱阅读公益基金作为“爱阅读·理想家”公益项目专项基金，
                每年在爱阅读公益公众服务号公开捐赠明细，接受巢湖市
                民政局监督和年审。</p></li>
        </ul>
        <div class="tipParagraph_register"><img src="${pageContext.request.contextPath }/resources/wechat/img/text_content.png"> </div>
    </div>
</div>
<form  action="#" method="post" id="registerForm">
    <div class="head_form">
        <ul id="tabs">
            <li class="list login">
                <div class="text">登录</div>
            </li>
            <li class="list on reg">
                <div class="text ">注册</div>
            </li>
        </ul>
    </div>

    <div class="bottom_form">
        <input type="hidden" id="wechatId" name="wechatId" value="${SESSION_OPEN_ID}" />
        <div id="panels">
            <div class="box hide">
                <div class="text_input ">
                    <div class="firstLabel">
                        <p>用户名<span class="fistSpan">*</span>：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" name="userName" id="userName" class="firstStyle_input">
                        </div>
                    </div>
                </div>
                <div class="text_input ">
                    <div class="firstLabel">
                        <p>密码<span class="fistSpan">*</span>：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="password" name="passWord" id="passWord" class="firstStyle_input">
                        </div>
                    </div>
                </div>
                <div class="buttonLabel">
                    <button type="button"   class="submitStyle" onclick="loginSubmit();">确定</button>
                </div>
            </div>
            <div class="box ">
                <div class="text_input ">
                    <div class="firstLabel">
                        <p>姓名<span class="fistSpan">*</span>：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" name="memberName" id="memberName" class="firstStyle_input">
                        </div>
                    </div>
                </div>
                <div class="text_input ">
                    <div class="firstLabel"><p> 性别<span class="fistSpan">*</span></span>：</p></div>
                    <div class="firstInput">
                        <div class="secondInput secondInputSex" >
                            <input type="radio" name="sex" value="1" class=" sexRadio" checked="checked">先生
                            <input type="radio" name="sex" value="0" class=" secondRadio">女士
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel"><p>身份证号<span class="fistSpan">*</span></span>：</p></div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" name="idNumber" id="idNumber" class="firstStyle_input isIdCardNo">
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel"><p>图形码<span class="fistSpan">*</span>：</p></div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" id="kaptcha" class="firstStyle_input firstStyle_inputContent">
                            <img src="${pageContext.request.contextPath}/kaptcha/image" width="70" height="30" id="kaptchaImage" style="float: right;" onclick="this.src='${pageContext.request.contextPath}/kaptcha/image?' + Math.random()">
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel">
                        <p> 手机<span class="fistSpan">*</span>：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" id="mobile" name="mobile" class="firstStyle_input firstStyle_inputContent">
                            <div class="secondSubmit">
                                <button type="button" id="btn" class="secondSubmitStyle" onclick="sendCheckCode(this)">
                                    <p>获取验证码</p>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="text_input">
                    <div class="firstLabel">
                        <p> 短信验证：</p>
                    </div>

                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" name="sendCode" id="sendCode" class="firstStyle_input">
                            <label id="sendCodeMsg" style="color: red"></label>
                        </div>
                    </div>
                </div>

                <div class="text_input">
                    <div class="firstLabel">
                        <p> 登录密码：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="password" id="memberPassword" name="memberPassword" class="firstStyle_input">
                        </div>
                    </div>
                </div>

                <div class="text_input">
                    <div class="firstLabel">
                        <p> 确认密码：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="password" id="confirmPassword" name="confirmPassword" class="firstStyle_input confirmPassword">
                        </div>
                    </div>
                </div>

                <div class="text_input">
                    <div class="firstLabel">
                        <p> E-mail：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="email" id="email" name="email" class="firstStyle_input">
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel">
                        <p> 所在城市：</p></div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <select id="city_type" name="cityID" class="firstStyle_input">
                            </select>
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel">
                        <p> 通讯地址：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <input type="text" name="address" class="firstStyle_input" />
                        </div>
                    </div>
                </div>
                <div class="text_input">
                    <div class="firstLabel">
                        <p> 留言：</p>
                    </div>
                    <div class="firstInput">
                        <div class="secondInput">
                            <textarea  name="additional" id="additional" class="fiveStyle_input" style="height: 80px"></textarea>
                        </div>
                    </div>
                </div>
                <div class="buttonLabel">
                    <button type="button" class="submitStyle" onclick="registerSubmit()">提 交</button>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $('#tabs .list').on('click', function () {
        var index = $(this).index();
        $(this).addClass('on').siblings('.list').removeClass('on');
        $('#panels .box').eq(index).removeClass('hide');
        $('#panels .box').eq(index).siblings('.box').addClass('hide');
    });
    var validate;
    $(document).ready(function(){
        /*注册*/
        /*$('.reg').addClass('on').siblings('.list').removeClass('on');
        $('#panels .box:first-child').addClass('hide');
        $('#panels .box:nth-child(2)').removeClass('hide');*/

        if ('${msg}' != null && '${msg}' != '') {
            alert('${msg}');
            $("#userName").val(localStorage.getItem("userName"));
            $("#passWord").val(localStorage.getItem("passWord"));

            $('.login').addClass('on').siblings('.list').removeClass('on');
            $('#panels .box:first-child').removeClass('hide');
            $('#panels .box:nth-child(2)').addClass('hide');
            return;
        }
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/getCityList",
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
//                var selectObj = '<option value=>请选择</option>';
                var selectObj = "";
                for (var i in data.obj) {
                    var detailList = data.obj;
                    /*if (i == 0) {
                        selectObj += '<option value=' + detailList[i]['id'] + '>' + detailList[i]['cityName'] + '</option>';
                    }*/
                    selectObj += '<option value=' + detailList[i]['id'] + '>' + detailList[i]['cityName'] + '</option>';
                }
                $('#city_type').html(selectObj);
            },
            error: function (msg) {
                //alert("出错了！");
            }
        });

        // 身份证验证
        jQuery.validator.addMethod("isIdCardNo", function(value, element) {
            return this.optional(element) || IdCardValidate(value);
        }, "请正确输入您的身份证号码");

        jQuery.validator.addMethod("confirmPassword", function(value, element) {
            var memberPassword = $("#memberPassword").val();
            return memberPassword == value;
        }, "密码输入不一致");

        // 验证码验证
        $("#sendCode").blur(function () {
            var obj = checkCodeValidate();
            if (!obj.flag) {
                var msg = obj.msg;
                $("#sendCodeMsg").html("").html(msg);
            } else {
                $("#sendCodeMsg").html("");
            }
        });

        /*jQuery.validator.addMethod("checkCodeValidate", function(value, element) {
            var flag = checkCodeValidate(value);
            console.info(flag);
            return this.optional(element) || flag;
        }, "验证码不正确");*/

        validate = $("#registerForm").validate({
            rules: {
                memberName: {
                    required:true
                },
                idNumber: {
                    required:true
                },
                mobile: {
                    required:true
                }
            },
            messages: {
                memberName: {
                    required:"必填"
                },
                idNumber: {
                    required:"必填"
                },
                mobile: {
                    required:"必填"
                }
            }
        });
    });

    var clock = '';
    var nums = 60;
    var btn;
    //检查图形码是否输入正确
    function checkKaptcha() {
        var input = $("#kaptcha").val();
        if (input == "" || input == null) {
            alert("请输入图形码！");
            return false;
        }
        var flag;
        $.ajax({
            url: "${pageContext.request.contextPath}/kaptcha/checkKaptcha",
            async: false,   //同步请求
            data: {
                input: input
            },
            dataType: "json",
            success: function (r) {
                flag = r.flag;
            }
        });
        if (!flag) {
            alert("图形码不正确！");
            return false;
        }
        return true;
    }
    /**
     * 发送手机验证码
     */
    function sendCheckCode(thisBtn) {
        if (checkKaptcha()) {
            $.ajax({
                url : '${pageContext.request.contextPath }/sms/sendCheckCode?mobile='+$("#mobile").val(),
                cache : false,
                type : "GET",
                dataType : "json",
                success : function(r) {
                    if(r.flag){
                        btn = thisBtn;
                        btn.disabled = true; //将按钮置为不可点击
                        $("#btn").html(nums+'秒后可重新获取验证码');
                        clock = setInterval(doLoop, 1000); //一秒执行一次
                    } else {
                        alert(r.msg);
                    }
                }
            });
        }
    }
    function doLoop()
    {
        nums--;
        if(nums > 0){
            $("#btn").html(nums+'秒后可重新获取验证码');
        }else{
            clearInterval(clock); //清除js定时器
            btn.disabled = false;
            $("#btn").html('点击发送验证码');
            nums = 60; //重置时间
        }
    }

    function checkCodeValidate() {
        var obj;
        $.ajax({
            url : '${pageContext.request.contextPath }/sms/checkCodeValidate?sendCode='+$("#sendCode").val()+"&mobile="+$("#mobile").val(),
            cache : false,
            async: false,
            type : "GET",
            dataType : "json",
            success : function(r) {
                obj = r;
            }
        });
        return obj;
    }
    
    function registerSubmit() {
        $("#registerForm").attr("action", '${pageContext.request.contextPath}/wechat/saveMember?checkCode=' + $("#sendCode").val() );
        $("#registerForm").submit();
    }
    
    function loginSubmit() {
        localStorage.setItem("userName", $("#userName").val());
        localStorage.setItem("passWord", $("#passWord").val());
        $("#registerForm").attr("action", '${pageContext.request.contextPath}/wechat/userLogin');
        $("#registerForm").submit();
    }
</script>
</body>
</html>