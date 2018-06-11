<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<script src="${pageContext.request.contextPath }/resources/js/plugins/dataTables/datatables.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="${pageContext.request.contextPath }/resources/js/inspinia.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/plugins/pace/pace.min.js"></script>

<!-- Sweet alert -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/sweetalert/sweetalert.min.js"></script>

<!-- Chosen -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/chosen/chosen.jquery.js"></script>

<!-- Select2 -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/select2/select2.full.min.js"></script>

<!-- 处理日期时间 -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/dateTimePicker/moment.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/plugins/dateTimePicker/bootstrap-datetimepicker.min.js"></script>
<!-- 本地化语言 -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/dateTimePicker/zh-cn.js"></script>

<!-- Input Mask-->
<script src="${pageContext.request.contextPath }/resources/js/plugins/jasny/jasny-bootstrap.min.js"></script>

<!-- 二维码 -->
<script src="${pageContext.request.contextPath }/resources/js/plugins/qrcode/qrcode.min.js"></script>

<%--百度图表--%>
<script src="${pageContext.request.contextPath }/resources/js/plugins/echarts/echarts.js"></script>

<script>
    function ckeckAll(obj){
        var $checked=$(obj).prop("checked");
        $("td input[type='checkbox']").each(function(){
            $(this).prop("checked",$checked);
            addOrRemoveClass($checked,$(this).parent().parent());
        });
        $(obj).prop("checked",$checked);
    }

    function addOrRemoveClass($checked,obj){
        if($checked){
            obj.addClass('selected');
        }else{
            obj.removeClass('selected');
        }
        var objs=datatable.rows('.selected').data();
        var length1=objs.length;
        var all=$('#datatable tbody').find("input[type='checkbox']");
        var length2=all.length;
        if(length1==length2){
            $('#ckeckAll').prop('checked','checked');
        }else{
            $('#ckeckAll').prop('checked',false);
        }
    }

    function checkOne(obj,event){
        $checked=$(obj).prop('checked');
        addOrRemoveClass($checked,$(obj).parent().parent());
        event.stopPropagation();
    }

    $(document).ready(function(){
        $('#datatable tbody').on( 'click', 'tr', function () {
            $(this).find("td > input").click();
        } );
    });

    /**
     * 判断是否是空
     * @param value
     * @returns {boolean}
     */
    function isNotNull(value) {
        if (value == null || value == "") {
            return false;
        }
        return true;
    }
</script>