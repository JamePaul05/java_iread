<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>会员管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建会员</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="member" method="post"
                               action="${pageContext.request.contextPath }/admin/member/save?type=create" id="barForm">

                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员名称</label>
                            <div class="col-sm-4">
                                <form:input path="memberName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="memberName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">性别</label>
                            <div class="col-sm-4">
                                <form:radiobutton path="sex" value="1"></form:radiobutton>男
                                <form:radiobutton path="sex" value="0"></form:radiobutton>女
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">手机号码</label>
                            <div class="col-sm-4">
                                <form:input path="mobile" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="mobile" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">身份证号码</label>
                            <div class="col-sm-4">
                                <form:input path="idNumber" class="form-control" />
                                <span class="idNumber">
                                    <font color="red">
                                        <form:errors path="idNumber" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">电子邮件</label>
                            <div class="col-sm-4">
                                <form:input path="email" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="email" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">城市所在地</label>
                            <div class="col-sm-4">
                                <form:select class="select2_city" path="cityID" items="${cities}" itemValue="id" itemLabel="cityName"></form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">通讯地址</label>
                            <div class="col-sm-4">
                                <form:input path="address" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="address" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员等级</label>
                            <div class="col-sm-4">
                                <form:select class="select2_memberLevel" path="memberLevelID" items="${memberLevels}" itemValue="id" itemLabel="memberLevelName"></form:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">微信openid</label>
                            <div class="col-sm-4">
                                <form:input path="wechatId" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="wechatId" />
                                    </font>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">会员状态</label>
                            <div class="col-sm-4">
                                <form:select path="enable" class="select2_enable">
                                    <form:option value="true">正常</form:option>
                                    <form:option value="false">注销</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">注册时间</label>
                            <div class="col-sm-4 input-group date">
								<span class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </span>
                                <form:input path="registerTime" class="form-control" id='datetimepicker4'/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">支付时间</label>
                            <div class="col-sm-4 input-group date">
								<span class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </span>
                                <form:input path="payTime" class="form-control" id='datetimepicker5'/>
                            </div>
                        </div>

                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/member/list">返回</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function(){
        $('#datetimepicker4').datetimepicker();
        $('#datetimepicker5').datetimepicker();

        $(".select2_city").select2({
            width: "100%"
        });

        $(".select2_memberLevel").select2({
            width: "100%"
        });

        $(".select2_enable").select2({
            width: "100%",
            //禁用搜索
            minimumResultsForSearch : Infinity,
        });
    });
</script>