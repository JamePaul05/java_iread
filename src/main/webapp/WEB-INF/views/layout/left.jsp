<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element"> <span>
                            <img alt="image" class="img-circle" src="${pageContext.request.contextPath}/resources/img/profile_small.jpg"/>
                             </span>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear"> <span class="block m-t-xs">
                                <strong class="font-bold"><sec:authentication property="principal.nickName"/></strong>
                             </span></span> </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a href="javascript:void(0);" id="changePsd"><i class="fa fa-gear"></i> 修改密码</a></li>
                        <li><a href="${pageContext.request.contextPath }/admin/j_spring_security_logout"><i class="fa fa-sign-out"></i> 退出登录</a></li>
                    </ul>
                </div>
                <div class="logo-element">
                    IN+
                </div>
            </li>

        </ul>

    </div>


</nav>


<script type="text/javascript">
    $(function () {
        $.ajax({
            url: '${pageContext.request.contextPath }/admin/menu/showMenus',
            cache: false,
            async: false,
            dataType: "json",
            success: function (r) {
                var menus = r.obj;
                var length = r.obj.length;
                var str = "";
                for (var i = 0; i < length; i++) {
                    var children = menus[i].children;
                    str += "<li onclick=checkin(this);>";
                    var url = menus[i].url;
                    var isChildNode = menus[i].isChildNode;
                    //判断是否是子节点
                    if (isChildNode == 0){
                    if (url == '#') {
                        str += "<a href='" + menus[i].url + "'>";
                    } else {
                        str += "<a href='${pageContext.request.contextPath }" + menus[i].url + "'>";
                    }
                    str += "<i class='fa fa-th-large'></i>" +
                            "<span class='nav-label'>" + menus[i].menuName + " </span>";
                    if (children.length > 0) {
                        str += "<span class='fa arrow'></span>";
                    }
                    str += "</a>";
                    if (children.length > 0) {
                        str += "<ul class='nav nav-second-level collapse'>";
                        for (var j = 0; j < children.length; j++) {
                            str += "<li><a href='${pageContext.request.contextPath }" + children[j].url + "'>" +
                                    children[j].menuName + "</a></li>"
                        }
                        str += "</ul>";
                    }
                }
                }
                $('#side-menu').append(str);

                menu();

            }
        });

        $("#changePsd").on("click",function(){
            $("#gridSystemModalLabel").toggle("show");
        });
    });

    /**
     *
     */
    function menu() {
        //如果标题和菜单名相同就增加active
        var titleValue = $.trim($('title').html());
        var objs = $('.nav-label');
        for (var i = 0; i < objs.length; i++) {
            if ($.trim($(objs[i]).text()) === titleValue) {
                $(objs[i]).parent().parent().addClass('active');
                return;
            }
        }
        var objsUl = $('.nav-second-level > li > a');
        for (var j = 0; j < objsUl.length; j++) {
            if ($.trim($(objsUl[j]).text()) === titleValue) {
                $(objsUl[j]).parent().addClass('active');
                $(objsUl[j]).parent().parent().addClass('nav nav-second-level collapse in');
            }
        }
    }

    function checkin(obj) {
       $(obj).addClass('active').find('ul.nav').toggle();
//        $(obj).siblings().removeClass('active').toggle();
    }
</script>