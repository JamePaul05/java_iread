<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>角色管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建角色</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="role" method="post"
                               action="${pageContext.request.contextPath }/admin/role/save?type=create" id="barForm">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">角色名称</label>
                            <div class="col-sm-4">
                                <form:input path="roleName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="roleName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">角色代码</label>
                            <div class="col-sm-4">
                                <form:input path="code" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="code" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">是否有效</label>
                            <div class="col-sm-4">
                                <form:select path="enable" class="select2_enable">
                                    <option value="true" selected="selected">是</option>
                                    <option value="false">否</option>
                                </form:select>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/role/list">返回</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(".select2_enable").select2({
        width: "20%",
        //禁用搜索
        minimumResultsForSearch : Infinity,
    });
</script>