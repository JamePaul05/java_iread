<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="pull-right">
    <!--10GB of <strong>250GB</strong> Free.-->
</div>
<div id="show">
</div>

<script type="text/javascript">
$(function(){
	distime();
})

function distime(){
	var time = new Date( ); //获得当前时间
	var year = time.getFullYear( );
    if (year == 2017) {
        $('#show').html("爱阅读·理想家管理系统 &copy; 2018");
    }
	$('#show').html("爱阅读·理想家管理系统 &copy; 2018-"+year);
	
}
</script>
