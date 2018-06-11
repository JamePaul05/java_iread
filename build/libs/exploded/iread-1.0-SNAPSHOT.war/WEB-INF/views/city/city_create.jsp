<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>城市管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建城市</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="city" method="post"
                               action="${pageContext.request.contextPath }/admin/city/save?type=create" id="barForm">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">城市名称</label>
                            <div class="col-sm-4">
                                <form:input path="cityName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="cityName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">首字母缩写</label>
                            <div class="col-sm-4">
                                <form:input path="acronym" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="acronym" />
                                    </font>
                                </span>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/city/list">返回</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
</script>