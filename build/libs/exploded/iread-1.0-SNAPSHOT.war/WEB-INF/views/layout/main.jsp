<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><tiles:getAsString name="title" ignore="true"/></title>
    <jsp:include page="../init/init_css.jsp"></jsp:include>
    <jsp:include page="../init/init_js.jsp"></jsp:include>
</head>
<body>
<div id="wrapper">
    <tiles:insertAttribute name="left" />
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <tiles:insertAttribute name="header" />
        </div>

        <tiles:insertAttribute name="content" />

        <div class="footer">
            <tiles:insertAttribute name="footer" />
        </div>
    </div>

</div>

<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel" id="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">密码修改</h4>
            </div>
            <div class="modal-body">
                <form method="post" class="mt10 add-form" id="userForm">

                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label id="projectobject">旧密码:</label>
                            <input class="form-control" id="psd" name="psd" type="password"/><span id="type1" style="color: red;"></span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label>新密码:</label>
                            <input class="form-control" id="newpsd" name="newpsd" type="password"/><span id="type2" style="color: red;"></span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label> 确认密码:</label>
                            <input class="form-control" id="newpsd1" name="newpsd1" type="password"/><span id="type3" style="color: red;"></span>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="changePsd();">确认修改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
</body>
<script type="text/javascript">
    function changePsd() {

        var psd = $("#psd").val().trim();
        var newpsd = $("#newpsd").val().trim();
        var newpsd1 = $("#newpsd1").val().trim();
        if(psd==''){
            $("#type4").html('请输入旧密码！');
        }

        if(newpsd==''){
            $("#type2").html('请输入新密码！');
        }

        if(newpsd1==''){
            $("#type3").html('请输入确认密码！');
        }

        if(psd==''||newpsd==''||newpsd1==''){
            return;
        }
        $.ajax({
            url: '${pageContext.request.contextPath }/admin/user/changePsd',
            cache: false,
            type: "post",
            dataType: "json",
            data: $("#userForm").serialize(),
            success: function (r) {
                // r = eval(r);
                $("#type4").html('');
                $("#type2").html('');
                $("#type3").html('');
                if (r.success) {
                    $("#type4").html(r.msg);
                    setTimeout("logout()",3000)
                } else {
                    var type=r.type;
                    $("#type"+type).html(r.msg);
                }
            }
        });
    }

    function logout(){
        location.href="${pageContext.request.contextPath }/admin/j_spring_security_logout";
    }
</script>
</html>