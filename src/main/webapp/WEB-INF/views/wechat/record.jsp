<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 申明使用h5编写 -->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<!-- 字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- viewport就是设备的屏幕上能用来显示我们的网页的那一块区域 -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <title>爱阅读公益</title>
	
	<!-- <link href="../css/bootstrap.min.css" rel="stylesheet"> -->

	<link href="${pageContext.request.contextPath}/resources/css/weui.min.css" rel="stylesheet">
	
	<link href="https://cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css" rel="stylesheet">

	<link href="${pageContext.request.contextPath}/resources/css/mystyle.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">


	<style type="text/css">
		.content {
			position: absolute;
			top: 130.97px;
			left: 0;
			bottom: 0;
			right: 0;
			overflow: auto;
		}
		.recordPanel {
			margin-top: 30px;
			width: 95%;
			margin-left: auto;
			margin-right: auto;
		}
		.row {
			width: 100%;
			height: 100px;
		}
		.row .col-sm-4 {
			width: 33.3%;
			float: left;
		}
		.row .col-sm-8 {
			float: right;
			width: 66.6%;
		}
	</style>
	
</head>
<body ontouchstart>
<div class="weui-cells no-margin">
	<div class="weui-cell">
	<div class="weui-cell__bd">
		<a class='' id="back"><i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回</a>
	</div>
	</div>
</div>

<div class="content" style="top: 60px;">
	<div class="weui-panel">
		<div class="weui-panel__bd" id="records">


		</div>
	</div>
</div>
<div id="pop" class="weui-popup__container">
	<div class="weui-popup__overlay"></div>
	<div class="weui-popup__modal">
		<%--<!-- 弹出层内容开始 -->--%>
		<%--<div class="weui-cells no-margin">--%>
			<%--<div class="weui-cell">--%>
				<%--<div class="weui-cell__bd">--%>
					<%--<a class='close-popup'><i class="fa fa-angle-left" id="backToRecord" style="margin-right: 5px;"></i>我的记录</a>--%>
				<%--</div>--%>
			<%--</div>--%>
		<%--</div>--%>
		
		<%--<div class="recordPanel">--%>
			<%--<div class="row">--%>
				<%--<div class="col-sm-4">--%>
					<%--<div class="weui-cells">--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>书名</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>编号</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>作者</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>借阅地点</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>借阅日期</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>归还地点</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd" style="text-align: right;">--%>
								<%--<p>归还日期</p>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="col-sm-8">--%>
					<%--<div class="weui-cells">--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="bookName">游戏人间</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="bianhao">9787550020016-01</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="author">贾平凹</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="borrowShopName">森时咖啡店</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="borrowTime">2016.9.6</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="returnShopName">森时咖啡店</p>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="weui-cell">--%>
							<%--<div class="weui-cell__bd">--%>
								<%--<p id="returnTime">2017.9.6</p>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>

		</div>
		
	</div>
</div>

<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script>
	var recordList = [];

	$(function() {
		getAllRecord();
	});

	function getAllRecord() {
		$.ajax({
			url:"${pageContext.request.contextPath}/wechat/getAllRecord",
			dataType:"json",
			data: {
			},
			success: function (r) {
				var records = r.records;
				$("#records").html("");
				for (var i = 0; i < records.length; i++) {
					var record = records[i];
					var str = "<div class='weui-media-box weui-media-box_text pointer open-popup' id='" + record.id + "'>";
					str += "<h4 class='weui-media-box__title'>" + record.bookName + "</h4>";
					str += "<p class='weui-media-box__desc'><div class='weui-media-box__desc' style='margin-bottom: 2px;'> 借阅地点：" + record.borrowShopName +  "</div><div class='weui-media-box__desc' style='margin-bottom: 2px;'> 借阅时间："+ record.borrowTime +"</div></p>";
					str += "<p class='weui-media-box__desc'><div class='weui-media-box__desc' style='margin-bottom: 2px;'> 归还地点：" + record.returnShopName +  "</div><div class='weui-media-box__desc' style='margin-bottom: 2px;'> 归还时间："+ record.returnTime +"</div></p>";
					/*str += "<ul class='weui-media-box__info'>";
					str += "<li class='weui-media-box__info__meta'>" + record.borrowTime + "</li>";
					str += "<li class='weui-media-box__info__meta weui-media-box__info__meta_extra'>" + record.returnTime + "</li>";
					str += "</ul>";*/
					str += "</div>";
					$("#records").append(str);
				}
				/*保存至全局变量*/
				saveToGlobalVar(records, recordList);
			}
		});
	}

	function saveToGlobalVar(records, recordList) {
		for (var i = 0; i < records.length; i++) {
			var record = records[i];
			recordList.push({"id": record.id, "data": record});
		}
	}

	<!-- 从数组中取出 -->
	function getBookDataFromArray(id) {
		for(var i = 0; i < recordList.length; i++) {
			var record = recordList[i];
			if(record.id == id) {
				return recordList[i].data;
			}
		}
		return null;
	}

	/*给每一个书单a标签绑定事件*/
	/*因为ajax生成的元素，所以必须用以下这种方式或者bind来绑定click事件*/
	$('body').on('click', '.open-popup', function () {
		var id = $(this).attr("id");
		var record = getBookDataFromArray(id);
		$("#bookName").html("").html(record.bookName);
		$("#bianhao").html("").html(record.bianhao);
		$("#author").html("").html(record.author);

		$("#borrowShopName").html("").html(record.borrowShopName);
		$("#borrowTime").html("").html(record.borrowTime);
		$("#returnShopName").html("").html(record.returnShopName);
		$("#returnTime").html("").html(record.returnTime);
		/*为pop层手动添加id，再弹出*/
		var id = id + "pop";
		$("div.weui-popup__container").attr("id", id);
		$("#" + id).popup();
	});

$("#back").on("click", function() {
	window.location.href="${pageContext.request.contextPath}/wechat/main";
})


</script>
</body>
</html>