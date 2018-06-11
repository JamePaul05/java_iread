<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>城市管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <p>
                        <button class="btn btn-primary" type="button" onclick="fnClickAddRow();"><i class="fa fa-plus-square-o"></i>&nbsp;新增</button>
                        <button class="btn btn-warning" type="button" id="fnClickDelRows"><i class="fa fa-trash"></i> <span>删除</span></button>
                    </p>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="datatable">
                            <thead>
                            <tr>
                                <th style="width: 20px;"><input type="checkbox" id="ckeckAll" onclick="ckeckAll(this,event);"/></th>
                                <th>城市名称</th>
                                <th>首字母缩写</th>
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
    $(document).ready(function(){
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
                "sSearch" : "搜索"
            },
            "bAutoWidth": false,
            "iDisplayLength": 10,
            "bProcessing": true,
            //开启服务器模式
            "serverSide": true,
            "ajax": {
                url: "${pageContext.request.contextPath }/admin/city/fetchCities",
            },
            "aoColumns": [//服务器返回的数据处理 此时返回的是 {}
                { "mData": function(obj){
                    return '<input type="checkbox" value="'+obj.id+'" onclick="checkOne(this,event);">';
                },"sWidth": "20px"},
                { "mData": "cityName" },
                { "mData": "acronym" },
                { "mData": function(obj){
                    return '<a class="btn btn-primary" id='+obj.id+' href="javascript: void(0);" onclick="editById(this.id)"><i class="fa fa-edit"></i></a>' +'&nbsp;&nbsp;'+'<a class="btn btn-danger" id='+obj.id+' href="javascript:void(0);" onclick="delById(this.id);"><i class="fa fa-trash"></i></a>';
                }}
            ],
            "columnDefs": [
                {
                    "targets": [0,3],
                    "orderable": false,
                }
            ],
            "order": [[ 1, "asc" ]]
        });
    });

    function fnClickAddRow(){
        location.href='${pageContext.request.contextPath}/admin/city/create';
    }

    function editById(id) {
        //停止事件
        event.stopPropagation();
        window.location.href = "${pageContext.request.contextPath}/admin/city/edit/" + id;
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
                url: "${pageContext.request.contextPath}/admin/city/remove",
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

</script>