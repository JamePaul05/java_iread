<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>书本管理</h2>
    </div>
</div>
<!-- https://api.douban.com/v2/admin/book/isbn/9787516119198?fields=title,author,publisher,price -->
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <form:form class="form-horizontal" commandName="book" method="post"
                   action="${pageContext.request.contextPath }/admin/book/create" id="barForm">

        <div class="col-sm-8">
            <div class="ibox">
                <div class="ibox-content" style="height: 370px;">
                    <!-- 111111 -->
                    <h3>书本信息</h3>
                    <div class="hr-line-dashed"></div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">ISBN</label>
                            <div class="col-sm-4">
                                <%--<form:input path="isbn" readonly="true" class="form-control"/>--%>
                                <label class="col-sm-12 control-label" style="text-align: left;" id="isbnLabel">空</label>
                                <form:hidden path="isbn"></form:hidden>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">书名</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="bookNameLabel">空</label>
                                <form:hidden path="bookName" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">作者</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="authorLabel">空</label>
                                <form:hidden path="author" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">出版社</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="pressLabel">空</label>
                                <form:hidden path="press" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">出版年</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="publicationDateLabel">空</label>
                                <form:hidden path="publicationDate" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">页数</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="pagesLabel">空</label>
                                <form:hidden path="pages" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">定价</label>
                            <div class="col-sm-4">
                                <label class="col-sm-12 control-label" style="text-align: left;" id="priceLabel">空</label>
                                <form:hidden path="price" />
                            </div>
                        </div>
                    <form:hidden path="summary" />
                    <form:hidden path="grade" />
                    <form:hidden path="simgUrl" />

                    <!-- 111111 -->
                </div>
            </div>
        </div>

        <div class="col-sm-4">
            <div class="ibox">
                <div class="ibox-content" style="height: 370px;">
                    <h3>书本搜索</h3>
                    <div class="hr-line-dashed"></div>
                    <div class="input-group">
                        <input type="text" class="form-control" data-mask="999-99-999-9999-9" placeholder="输入ISBN号" id="isbnContent">
                        <span class="input-group-btn">
                                        <button type="button" class="btn btn btn-primary" id="search"> <i class="fa fa-search"></i> 搜索</button>

									</span>
                    </div>
                    <label class="error" id="error" style="display: none;">请输入正确的ISBN号！</label>
                    <!-- <div class="div1"> -->
                    <!-- <button class="btn	btn-success dim btn-large-dim" type="button" ><i class="fa fa-check"></i></button> -->
                    <!-- </div> -->
                    <div class="ibox-content div1">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">开始编号</label>
                                <div class="col-sm-8">
                                    <input class="form-control" id="start" name="start" type="number" min="1" max="99"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">结束编号</label>
                                <div class="col-sm-8	">
                                    <input class="form-control" id="end" name="end" type="number" min="1" max="99"/>
                                </div>
                            </div>

                            <%--如果后台发现已经存在编号--%>
                            <c:if test="${!success}">
                                <label class="error">${error}</label>
                            </c:if>

                            <label class="error" id="error2" style="display: none;"></label>

                            <div class="col-sm-10 col-sm-offset-6">
                                <input type="button" class="btn btn-primary" onclick="check()" value="保存" />
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/book/list">返回</a>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="hr-line-dashed"></div> -->
                    <!-- <div class=""> -->
                    <!-- </div> -->

                </div>
            </div>
        </div>

            <%--<div class="col-sm-10 col-sm-offset-6">
                <button class="btn btn-primary" type="submit">保存</button>
                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/book/list">返回</a>
            </div>--%>

        </form:form>
    </div>
</div>

<script>
    var isbn;
    var error1 = "请输入正确的isbn号！";
    var error2 = "isbn不能为空！";
    var error3 = "请输入正确的开始编号和结束编号！";
    var error4 = "请搜索验证下isbn号是否准确";
    $('#search').click( function () {
        isbn = $("#isbnContent").val().trim().replace(/-/g, "");
        <!-- 因为浏览器同源策略的限制，ajax不能访问域外的文件。所以改用jsonp格式。 -->
        $.ajax({
            type : "get",
            url : "https://api.douban.com/v2/book/isbn/" + isbn + "?fields=title,author,publisher,pubdate,pages,price,summary,rating,images",
            dataType : "jsonp",
            jsonp : 'callback', //指定一个查询参数名称来覆盖默认的 jsonp 回调参数名 callback
            jsonpCallback: 'handleResponse', //设置回调函数名
            error : function() {
                $("#error").html(error1).show();
            }
        });
    } );

    <!-- 利用正则表达式来匹配金额 -->
    function getMoney(price) {
        var reg = /[1-9][0-9]*(\.[0-9]+)?/g;
        return price.match(reg)[0];
    }
    function getPages(pages) {
        if (pages == "" || pages == null || pages == undefined) {
            return 0;
        }
        var reg = /[1-9]+/;
        return pages.match(reg);
    }
    function handleResponse(r) {
        $("#isbnLabel").html(isbn);
        $("#bookNameLabel").html(r.title);
        $("#authorLabel").html(r.author);
        $("#pressLabel").html(r.publisher);
        $("#publicationDateLabel").html(r.pubdate);
        var pages = getPages(r.pages);
        $("#pagesLabel").html(pages);
        $("#priceLabel").html(r.price);
        $("#isbn").val(isbn);
        $("#bookName").val(r.title);
        $("#author").val(r.author);
        $("#press").val(r.publisher);
        $("#publicationDate").val(r.pubdate);
        $("#pages").val(pages);
        var price = getMoney(r.price);
        console.log(price);
        $("#price").val(price);
        $("#summary").val(r.summary);
        $("#grade").val(r.rating.average);
        $("#simgUrl").val(r.images.small);
    }

    $("#isbnContent").focus(function() {
        $("#error").hide();
    });
    
    function check() {
        var start = $("#start").val();
        var end = $("#end").val();
        var isbn = $("#isbnContent").val();
        if (!isNotNull(isbn)) {
            $("#error").html(error2).show();
            return false;
        }
        if (!isNotNull(start) || !isNotNull(end) || start > end) {
            $("#error2").html(error3).show();
            return false;
        }
        var s = $("#isbnLabel").html();
        if (!isNotNull(s) || s == "空") {
            $("#error").html(error4).show();
            return false;
        }
        $("#barForm").submit();
    }

    function isNotNull(value) {
        if (value == null || value == "") {
            return false;
        }
        return true;
    }
</script>