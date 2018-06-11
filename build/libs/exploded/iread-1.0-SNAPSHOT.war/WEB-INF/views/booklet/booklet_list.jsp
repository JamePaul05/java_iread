<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">

    #recordModal .table th, #recordModal .table td {
        text-align: center;
        vertical-align: middle!important;
    }

    .modal-dialog {
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
    }

    .modal-content {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 100%;
    }

    .modal-body {
        overflow-y: scroll;
        position: absolute;
        top: 85px;
        bottom: 72px;
        width: 100%;
    }

    .modal-header .close {margin-right: 15px;}

    .modal-footer {
        position: absolute;
        width: 100%;
        bottom: 0;
    }

    /*重改select2的样式，否则下拉框会被modal盖住*/
    .select2-close-mask{
        z-index: 2099;
    }
    .select2-dropdown{
        z-index: 3051;
    }

    .float-e-margins .btn {
        margin-bottom: 0px;
    }

    img {
        margin: auto;
    }
</style>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>书册管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-sm-5">
                            <p>
                                <!-- <button class="btn btn-primary" type="button" onclick="fnClickAddRow();"><i class="fa fa-plus-square-o"></i>&nbsp;新增</button> -->
                                <button class="btn btn-danger" type="button" id="fnClickDelRows"><i class="fa fa-trash"></i> <span>删除</span></button>
                                <button class="btn btn-info" type="button" id="fnClickAllocation"><i class="fa fa-arrow-circle-o-right"></i> <span>调拨</span></button>
                                <%--<button class="btn btn-success" type="button" id="fnClickExist"><i class="fa fa-bars"></i> <span>盘点存在</span></button>--%>
                                <button class="btn btn-default" type="button" id="fnClickRecord"><i class="fa fa-bars"></i> <span>借阅记录</span></button>
                                <button class="btn btn-warning" type="button" id="fnClickQrcode"><i class="fa fa-qrcode"></i> <span>生成二维码</span></button>
                            </p>
                        </div>
                        <div class="col-sm-4  form-group">
                            <label class="col-sm-3" style="padding-top: 7px;">公益点</label>
                            <div>
                                <select class="select2_shop1">
                                    <option value="all">所有</option>
                                    <c:forEach items="${shops}" var="shop">
                                        <option value="${shop.id}">${shop.shopName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-3  form-group">
                            <label class="col-sm-4" style="padding-top: 7px;">状态</label>
                            <div>
                                <select class="select2_state">
                                    <option value="all">所有</option>
                                    <c:forEach items="${states}" var="state">
                                        <option value="${state}">${state}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- 调拨按钮modal -->
                    <div class="modal fade" id="allocationModal" tabindex="-1" role="dialog" aria-labelledby="allocationModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h3 class="modal-title" id="allocationModalLabel">您要将以下这些书籍</h3>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <table class="table table-striped">
                                                <thead>
                                                <th>编号</th>
                                                <th>书名</th>
                                                <th>现属公益点</th>
                                                </thead>
                                                <tbody id="allocationTable">
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-2">
                                            <strong class="text-left">调拨到</strong>
                                        </div>

                                        <div class="col-sm-5">
                                            <label class="control-label">城市</label>
                                            <div>
                                                <select class="select2_city">
                                                    <c:forEach items="${cities}" var="city">
                                                        <option value="${city.id}" >${city.cityName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-5">
                                            <label>公益点</label>
                                            <div>
                                                <select class="select2_shop2">
                                                    <c:forEach items="${shops}" var="shop">
                                                        <option value="${shop.id}">${shop.shopName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal" id="closeAllocate">关闭</button>
                                    <button type="button" class="btn btn-primary" onclick="allocate()">确定</button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal -->
                    </div>
                    <!-- 222 -->

                    <!-- 借书记录modal -->
                    <div class="modal fade" id="recordModal" tabindex="-1" role="dialog" aria-labelledby="recordModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <p id="bookNo">编号：9787516119198-01</p>
                                    <p id="bookName">书名：吕著中国通史</p>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="table-responsive">
                                                <table class="table table-striped">
                                                    <thead>
                                                    <th>借出时间</th>
                                                    <th>借出地</th>
                                                    <th>归还时间</th>
                                                    <th>归还地</th>
                                                    <th>借阅人</th>
                                                    </thead>
                                                    <tbody id="recordTable">

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal -->
                    </div>


                    <!-- 生成二维码modal -->
                    <div class="modal fade" id="qrcodeModal" tabindex="-1" role="dialog" aria-labelledby="qrcodeModalLabel" aria-hidden="true">
                        <div class="modal-dialog" style="width:1000px">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <div class="row">
                                        <div class="col-md-10">
                                            <h3 class="modal-title" id="qrcodeModalLabel">生成二维码</h3>

                                        </div>
                                        <div class="col-md-2">
                                            <button type="button" class="btn btn-primary pull-right" onclick="saveToPDF()">保存至pdf</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-body" id="qrcodePanel">
                                </div>
                                <input type="hidden" id="qrcodeNum">
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal -->
                    </div>

                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="datatable">
                            <thead>
                            <tr>
                                <th style="width: 20px;"><input type="checkbox" id="ckeckAll" onclick="ckeckAll(this,event);"/></th>
                                <th>编号</th>
                                <th>书名</th>
                                <th>公益点归属</th>
                                <th>状态</th>
                                <th>是否被借阅</th>
                                <th>借阅人</th>
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

<!-- 用来保存pdf路径 -->
<input type="file" id="file" style="display:none">

<script>
    var datatable;
    var select2_shop2;
    $(document).ready(function(){
        $(".select2_shop1").select2({
            width: "70%"
        });

        $(".select2_shop1").change(function () {
            var shopName = $(this).children("option:selected").html();
            var search = "";
            if (shopName != "所有") {
                search = shopName;
            }
            var shopColumn = datatable.column(3);
            shopColumn.search(search).draw();
        });

        $(".select2_state").select2({
            width: "60%"
        });

        $(".select2_state").change(function () {
            var shopName = $(".select2_shop1").children("option:selected").html();
            var state = $(this).children("option:selected").html();
            var search = "";
            if (state != "所有") {
                search = state;
            }
            var stateColumn = datatable.column(4);
            stateColumn.search(search).draw();
        });

        k();

        $(".select2_city").select2({
            width: "50%"
        });

        select2_shop2 = $(".select2_shop2").select2({
            width: "100%"
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
                url: "${pageContext.request.contextPath }/admin/booklet/fetchBooklets",
            },
            "aoColumns": [//服务器返回的数据处理 此时返回的是 {}
                { "mData": function(obj){
                    return '<input type="checkbox" value="'+obj.id+'" onclick="checkOne(this,event);">';
                },"sWidth": "20px"},
                { "mData": "bianHao" },
                { "mData": "bookName" },
                { "mData": "shopName" },
                { "mData": "state" },
                { "mData": "borrowed" },
                { "mData": "borrowerName" },
                { "mData": function(obj){
                    return '<a class="btn btn-danger" id='+obj.id+' href="javascript:void(0);" onclick="delById(this.id);"><i class="fa fa-trash"></i></a>';
                }}
            ],
            "columnDefs": [
                {
                    "targets": [0,4,5,6,7],
                    "orderable": false
                }
            ],
            "order": [[ 1, "asc" ]]
        });
    });

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
                url: "${pageContext.request.contextPath}/admin/booklet/remove",
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

    $("#fnClickAllocation").click( function () {
        if(hasSelected()) {
            $("#allocationTable").html("");
            var table = $("#datatable").dataTable();
            var trs = table.fnGetNodes();
        <!-- var trs = datatable.rows(".selected").data();fnGetNodes获取表格所有行，trs[i]表示第i行tr对象 -->
            for(var i = 0; i < trs.length; i++){
                if($(trs[i]).hasClass('selected')){
                    var str = "";
                    var tr = table.fnGetData(trs[i]);
                    str += "<tr><td>" + tr.bianHao + "</td><td>" + tr.bookName+ "</td><td>" + tr.shopName + "</td></tr>";
                    $("#allocationTable").append(str);
                }
            }
            $("#allocationModal").modal();
        }
    } );

    $("#fnClickExist").click( function () {
        if(hasSelected()) {
            swal({
                title: "确定盘点都存在吗？",
                text: ""
            });
        }
    } );

    $("#fnClickRecord").click( function () {
        if(hasSelected()) {
            if(selectedOnlyOne()) {
                var table = $("#datatable").dataTable();
                var trs = table.fnGetNodes();
                var bianhao;
                for(var i = 0; i < trs.length; i++) {
                    if ($(trs[i]).hasClass('selected')) {
                        var tr = table.fnGetData(trs[i]);
                        bianhao = tr.bianHao;
                        break;
                    }
                }
                $.ajax({
                    url: "${pageContext.request.contextPath}/wechat/getRecordByBianhao",
                    dataType: "json",
                    data: {
                        bianhao: bianhao
                    },
                    success: function (r) {
                        var records = r.records;
                        $("#recordTable").html("");
                        for (var i = 0; i < records.length; i++) {
                            var record = records[i];
                            var str = "";
                            str += "<tr><td>" + record.borrowTime + "</td>";
                            str += "<td>" + record.borrowShopName + "</td>";
                            str += "<td>" + record.returnTime + "</td>";
                            str += "<td>" + record.returnShopName + "</td>";
                            str += "<td>" + record.borrowerName + "</td></tr>";
                            $("#recordTable").append(str);
                        }
                        $("#bookNo").html(r.bookNo);
                        $("#bookName").html(r.bookName);
                        $("#recordModal").modal();
                    }
                });
            }
        }
    } );

    var qrcodeOption = {
        text: '',
        width: 100,
        height: 100,
        colorDark : '#000000',
        colorLight : '#ffffff',
        correctLevel : QRCode.CorrectLevel.H
    };

    //每一行显示的二维码个数
    var max = 4;

    //生成二维码
    $("#fnClickQrcode").click( function () {
        //如果有选中的书册记录
        if(hasSelected()) {
            $("#qrcodePanel").html("");
            var table = $("#datatable").dataTable();
            var trs = table.fnGetNodes();
            //记录有多少条选中的记录
            var count = 0;
            //获取有多少条选中的记录
            for(var i = 0; i < trs.length; i++){
                if($(trs[i]).hasClass('selected')){
                    count++;
                }
            }
            $("#qrcodeNum").val(count);
            //计算行数
            var rows;
            if (count % max == 0) {
                rows = count / max;
            } else {
                rows = parseInt(count / max) + 1;
            }
            var str = "";
            str += "<table style='width: 100%; border-collapse:separate; border-spacing:30px;'>";
            //循环手动添加div等元素
            var n = 0;
            for (var i = 1; i <= rows; i++) {
                <!-- -->
                str += "<tr style='text-align: center;'>";
                for (var j = 0; j < max; j++) {
                    if (n == count) {
                        break;
                    }
                    var id = max * (i - 1) + j + 1;
                    str += "<td><div style='margin-bottom: 15px;' id='qrcode" + id + "'></div>";
                    str += "<p class='' id='bianhao" + id + "' ></p></td>";
                    n++;
                }
                str += "</tr>"
            }
            str += "</table>";
            $("#qrcodePanel").append(str);
            //计数器
            var num = 1;
            for(var i = 0; i < trs.length; i++){
                if($(trs[i]).hasClass('selected')){
                    //生成二维码
                    var tr = table.fnGetData(trs[i]);
                    qrcodeOption.text = tr.bianHao;
                    new QRCode('qrcode' + num, qrcodeOption);
                    $("#bianhao" + num).html(tr.bianHao);
                    num++;
                }
            }
            $("#qrcodeModal").modal();
        }
    } );

    //保存至pdf
    //原本采用html转pdf，但是样式难以控制，放弃
    //现在直接手动画出
    function saveToPDF() {
        $("#qrcodeModal").modal("hide");
        //二维码base64数组
        var base64s = [];
        //编号数组
        var bianhaos = [];
        var num = $("#qrcodeNum").val();
        //获取所有二维码图片的src（base64）
        for (var i = 1; i <= num; i++) {
            var base64 =  $("#qrcode" + i).find("img")[0].src;
            base64s.push(base64.trim());
            bianhaos.push($("#bianhao" + i).html());
            //为了方便，我们把img的src都设成服务端的对应的图片名称
//            $("#qrcode" + i).find("img")[0].src = i + ".png";
        }
        //获取html传到后台，转成pdf
//        var html = $("#qrcodePanel").html();


        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/admin/booklet/saveToPDF",
            data: {
                base64s: base64s,
                bianhaos: bianhaos,
                maxPerRow: max
            },
            dataType: 'json',
            success: function (r) {
                var flag = r.flag;
                var msg = r.msg;
                if (flag) {
//                    var href = $("#download").href;
                    window.location.href = "${pageContext.request.contextPath}/admin/booklet/download/pdf";
                } else {
                    swal(msg, "", "error");
                }
            }
        });
    }


    function selectedOnlyOne() {
        var checked = $("#datatable tbody").find(".selected").length;
        if(checked > 1) {
            swal({
                title: "最多只能选择一条记录！",
                text: "",
                type: "warning",
            });
            return false;
        }
        return true;
    }

    function hasSelected() {
        var checked = $("#datatable tbody").find(".selected").length;
        if(checked < 1) {
            swal({
                title: "请至少选择一个记录！",
                text: "",
                type: "warning",
            });
            return false;
        }
        return true;
    }

    $(".select2_city").change(function () {
        k();
    });

    function k() {
        $(".select2_shop2").empty();
        var cityID = $(".select2_city").find("option:selected").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/shop/findByCity",
            cache: false,
            dataType: "json",
            data: {
                id: cityID
            },
            success: function (r) {
                var shops = r.shops;
                for (var i = 0; i < shops.length; i++) {
                    var shop = shops[i];
                    var option = $("<option>").val(shop.shopID).text(shop.shopName);
                    $(".select2_shop2").append(option);
                }
                //默认第一个被选中
                if (shops.length > 0) {
                    var shopID = shops[0].shopID;
                    select2_shop2.val(shopID).trigger("change");
                } else {
                    select2_shop2.val("").trigger("change");
                }

            }
        });
    }

    //调拨接口
    function allocate() {
        //获取公益点id
        var shopID = $(".select2_shop2").find("option:selected").val();
        if (!isNotNull(shopID)) {
            swal({
                title: "必须选择一个公益点！",
                text: "",
                type: "warning",
            });
            return false;
        }
        //获取datatable   api
        var table = $("#datatable").dataTable();
        var trs = table.fnGetNodes();
        var ids = [];
        for(var i = 0; i < trs.length; i++){
            if($(trs[i]).hasClass('selected')){
                var tr = table.fnGetData(trs[i]);
                ids.push(tr.id);
            }
        }
        ids = ids.join(",");
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/booklet/allocate",
            dataType: "json",
            data: {
                shopID: shopID,
                ids: ids
            },
            success: function (r) {
                var msg = r.msg;
                var flag = r.flag;
                if (flag) {
                    swal({
                        title: msg,
                        text: "",
                        type: "success",
                    }, function () {
                        $('#allocationModal').modal('hide')
                    });
                } else {
                    swal(msg, "", "error");
                }
                datatable.draw();
            }
        });
    }


</script>
