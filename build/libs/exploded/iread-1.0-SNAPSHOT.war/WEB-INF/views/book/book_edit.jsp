<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>书本管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>编辑书本</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="book" method="post"
                               action="${pageContext.request.contextPath }/admin/book/update" id="barForm">
                        <form:hidden path="id" />

                        <div class="form-group">
                            <label class="col-sm-4 control-label">ISBN</label>
                            <div class="col-sm-4">
                                <form:input path="isbn" class="form-control" readonly="true" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="isbn" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">书本名称</label>
                            <div class="col-sm-4">
                                <form:input path="bookName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="bookName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">作者</label>
                            <div class="col-sm-4">
                                <form:input path="author" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="author" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">出版社</label>
                            <div class="col-sm-4">
                                <form:input path="press" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="press" />
                                    </font>
                                </span>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">出版年</label>
                            <div class="col-sm-4">
                                <form:input path="publicationDate" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="publicationDate" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">页数</label>
                            <div class="col-sm-4">
                                <form:input path="pages" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="pages" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">定价</label>
                            <div class="col-sm-4">
                                <form:input path="price" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="price" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">评分</label>
                            <div class="col-sm-4">
                                <form:input path="grade" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="grade" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">简介</label>
                            <div class="col-sm-4">
                                <form:textarea path="summary" rows="8" class="form-control" ></form:textarea>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="summary" />
                                    </font>
                                </span>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/book/list">返回</a>
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