<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>用户管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>编辑用户</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="user" method="post"
                               action="${pageContext.request.contextPath }/admin/user/save?type=edit" id="barForm">
                        <form:hidden path="id"></form:hidden>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">用户名</label>
                            <div class="col-sm-4">
                                <form:input path="userName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="userName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">密码</label>
                            <div class="col-sm-4">
                                <form:password path="passWord" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="passWord" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">姓名</label>
                            <div class="col-sm-4">
                                <form:input path="nickName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="nickName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">角色</label>
                            <div class="col-sm-4">
                                <div>
                                    <form:select path="roleIDs" class="select2_role" items="${roles}" itemValue="id" itemLabel="roleName">
                                    </form:select>
                                </div>
                            </div>
                        </div>

                        <c:if test="${userType == 'role_admin'}">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">城市</label>
                                <div class="col-sm-4">
                                    <div>
                                        <select class="select2_city">
                                            <c:forEach items="${cities}" var="city">
                                                <option value="${city.id}">${city.cityName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">公益点</label>
                            <div class="col-sm-4">
                                <div>
                                    <select class="select2_shop">
                                        <c:forEach items="${shops}" var="shop">
                                            <option value="${shop.id}">${shop.shopName}</option>
                                        </c:forEach>
                                    </select>
                                    <form:hidden path="shopID" id="shopID"></form:hidden>
                                </div>
                            </div>
                        </div>

                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/user/list">返回</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(".select2_role").select2({
        width: "100%",
        placeholder: "角色(多选)",
        multiple: "multiple",
        allowClear: true
    });
    $(".select2_city").select2({
        width: "100%",
        placeholder: "城市"

    });
    var select2_city = $(".select2_shop").select2({
        width: "100%",
        placeholder: "公益点"

    });

    $(document).ready(function(){
        if ('${userType}' == 'role_admin') {
            $(".select2_shop").empty();
            var cityID = $(".select2_city").find("option:selected").val();
            setShop(cityID);
        }

    });

    $(".select2_city").change(function () {
        $(".select2_shop").empty();
        var cityID = $(".select2_city").find("option:selected").val();
        cityChangedShop(cityID);
    })

    $(".select2_shop").change(function () {
        var shopID = $(".select2_shop").find("option:selected").val();
        $("#shopID").val(shopID);
    })

    function setShop(cityID) {
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/shop/findByCity",
            cache: false,
            dataType: "json",
            data: {
                id: cityID
            },
            success: function (r) {
                var shops = r.shops;
                for (var i = 0; i < shops.length; i++) {
                    var shop = shops[i];
                    var option = $("<option>").val(shop.shopID).text(shop.shopName);
                    $(".select2_shop").append(option);
                }
                //初始化shop
                var shopID = $("#shopID").val();
                select2_city.val(shopID).trigger("change");
            }
        });
    }

    function cityChangedShop(cityID) {
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/shop/findByCity",
            cache: false,
            dataType: "json",
            data: {
                id: cityID
            },
            success: function (r) {
                var shops = r.shops;
                for (var i = 0; i < shops.length; i++) {
                    var shop = shops[i];
                    var option = $("<option>").val(shop.shopID).text(shop.shopName);
                    $(".select2_shop").append(option);
                }
                //默认第一个被选中
                if (shops.length > 0) {
                    var shopID = shops[0].shopID;
                    select2_city.val(shopID).trigger("change");
                } else {
                    select2_city.val("").trigger("change");
                }
            }
        });
    }

</script>