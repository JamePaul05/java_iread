<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>会员等级管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建会员等级</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="memberLevel" method="post"
                               action="${pageContext.request.contextPath }/admin/memberLevel/save?type=create" id="barForm">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员等级名称</label>
                            <div class="col-sm-4">
                                <form:input path="memberLevelName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="memberLevelName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">入会金额</label>
                            <div class="col-sm-4">
                                <form:input path="amount" class="form-control" type="number"  step="0.01"/>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="amount" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">可同时借数量</label>
                            <div class="col-sm-4">
                                <form:input path="maxBorrowNum" class="form-control" type="number"/>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="maxBorrowNum" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员权益说明</label>
                            <div class="col-sm-4">
                                <form:textarea class="form-control" path="remark" rows="6"/>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="remark" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员等级状态</label>
                            <div class="col-sm-4">
                                <form:select path="enable" class="select2_enable">
                                    <form:option value="true">启用</form:option>
                                    <form:option value="false">禁用</form:option>
                                </form:select>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/memberLevel/list">返回</a>
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
        width: "100%",
        //禁用搜索
        minimumResultsForSearch : Infinity,
    });
</script>