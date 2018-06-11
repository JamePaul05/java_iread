<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>公益点管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-sm-3">
                            <p>
                                <button class="btn btn-primary" type="button" onclick="fnClickAddRow();"><i class="fa fa-plus-square-o"></i>&nbsp;新增</button>
                                <button class="btn btn-warning" type="button" id="fnClickDelRows"><i class="fa fa-trash"></i> <span>删除</span></button>
                            </p>
                        </div>
                        <div class="col-sm-5">
                            <label class="col-sm-2" style="padding-top: 7px;">城市</label>
                            <div>
                                <select class="select2_city">
                                    <option value="all" >所有</option>
                                    <c:forEach items="${cities}" var="city">
                                        <option value="${city.id}" >${city.cityName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="datatable">
                            <thead>
                            <tr>
                                <th style="width: 20px;"><input type="checkbox" id="ckeckAll" onclick="ckeckAll(this,event);"/></th>
                                <th>公益点名称</th>
                                <th>城市</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var datatable;
    var select2_city;
    $(document).ready(function(){
        $(".select2_city").select2({
            width: "40%"
        });

        select2_city = $(".select2_city").change(function () {
            var cityName = $(this).children("option:selected").html();
            var search = "";
            if (cityName != "所有") {
                search = cityName;
            }
            datatable.search(search).draw();
        });


        datatable = $('#datatable').DataTable({
            responsive: true,
            "oLanguage": {
                "sLengthMenu": "每页显示  _MENU_ 条记录",
                "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "前一页",
                    "sNext": "后一页",
                    "sLast": "尾页"
                },
                "sZeroRecords": "抱歉， 没有找到数据",
                "sInfoEmpty": "没有数据",
                "sSearch" : "搜索",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            },
            "bAutoWidth": false,
            "iDisplayLength": 10,
            "bProcessing": true,
            //开启服务器模式
            "serverSide": true,
            "ajax": {
                url: "${pageContext.request.contextPath }/admin/shop/fetchShops",
            },
            "aoColumns": [//服务器返回的数据处理 此时返回的是 {}
                { "mData": function(obj){
                    return '<input type="checkbox" value="'+obj.id+'" onclick="checkOne(this,event);">';
                },"sWidth": "20px"},
                { "mData": "shopName" },
                { "mData": "cityName" },
                { "mData": function(obj){
                    return '<a class="btn btn-primary" id='+obj.id+' href="javascript: void(0);" onclick="editById(this.id)"><i class="fa fa-edit"></i></a>' +'&nbsp;&nbsp;'+'<a class="btn btn-danger" id='+obj.id+' href="javascript:void(0);" onclick="delById(this.id);"><i class="fa fa-trash"></i></a>';
                }}
            ],
            "columnDefs": [
                {
                    "targets": [0, 3],
                    "orderable": false
                }
            ],
            "order": [[1, "desc"]],

        });
    });

    function fnClickAddRow(){
        location.href='${pageContext.request.contextPath}/admin/shop/create';
    }

    function editById(id) {
        //停止事件
        event.stopPropagation();
        window.location.href = "${pageContext.request.contextPath}/admin/shop/edit/" + id;
    }

    function delById(id){
        //停止事件
        event.stopPropagation();
        swal({
            title: "确定删除吗？",
            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "删除",
            cancelButtonText:"取消",
            closeOnConfirm: false,
        }, function () {
            $.ajax({
                url: "${pageContext.request.contextPath}/admin/shop/remove",
                cache: false,
                async: false,
                dataType: "json",
                data: {
                    ids: id
                },
                success: function (r) {
                    var msg = r.msg;
                    var flag = r.flag;
                    if (flag) {
                        swal(msg, "", "success");
                    } else {
                        swal(msg, "", "error");
                    }
                    datatable.draw();
                }
            })
        });

    }

    $('#fnClickDelRows').click( function () {
        var objs = datatable.rows('.selected').data();
        var length = objs.length;
        if (length == 0) {
            swal("至少要选择一条数据！", "", "warning");
            return;
        }
        var ids = [];
        for(var i = 0; i < length; i++){
            ids.push(objs[i].id);
        }
        delById(ids.join(','));
    } );

    <!-- 在页面包括图片、dom等都加载完毕执行 -->
    window.onload = loadJScript;  //异步加载地图

    function loadJScript() {
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=uEEhkAc1vQlG3tGGuhoOCXuf9RydExqn&callback=init";
        document.body.appendChild(script);
    }

    var map;

    function init() {
        map = new BMap.Map("map");   // 创建Map实例
        getCurrentCity();
    }

    function getCurrentCity() {
        var localCity = new BMap.LocalCity({renderOptions:{map: map}});
        localCity.get(function (localCityResult) {
            var cityName = localCityResult.name.substr(0, 2);
            $.ajax({
                url: "${pageContext.request.contextPath}/admin/city/getID",
                dataType: "json",
                data: {
                    cityName: cityName,
                },
                success: function (r) {
                    if(r.flag) {
                        select2_city.val(r.id).trigger("change");
                    }
                }
            });
        });
    }

</script>