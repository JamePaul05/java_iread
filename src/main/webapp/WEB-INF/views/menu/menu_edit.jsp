<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>菜单管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建菜单</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="menu" method="post"
                               action="${pageContext.request.contextPath }/admin/menu/save?type=edit" id="barForm">
                        <form:hidden path="id"/>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">菜单名称</label>
                            <div class="col-sm-4">
                                <form:input path="menuName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="menuName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">相对路径</label>
                            <div class="col-sm-4">
                                <form:input path="url" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="url" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">权限key</label>
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
                            <label class="col-sm-4 control-label">排序key</label>
                            <div class="col-sm-4">
                                <form:input path="sequence" class="form-control" type="number" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="sequence" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">分配角色</label>
                            <div class="col-sm-4">
                                <div>
                                    <form:select path="roleIDS" class="select2_role" items="${roles}" itemValue="id" itemLabel="roleName" multiple="multiple">
                                    </form:select>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">是否有效</label>
                            <div class="col-sm-4">
                                <form:select path="enable" class="select2_enable">
                                    <form:option value="true">是</form:option>
                                    <form:option value="false">否</form:option>
                                </form:select>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/menu/list">返回</a>
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
        placeholder: "至少选择一个角色",
        allowClear: true,
        multiple: true,
    });
    $(".select2_enable").select2({
        width: "20%",
        //禁用搜索
        minimumResultsForSearch : Infinity,
    });
</script>