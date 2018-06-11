<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	<link href="${pageContext.request.contextPath}/resources/css/weui.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/wechat/js/jquery-ui/jquery-ui.min.css">



	<link href="${pageContext.request.contextPath}/resources/css/mystyle.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">
    <script type="text/javascript" src="https://3gimg.qq.com/lightmap/components/geolocation/geolocation.min.js "></script>


    <%--微信js sdk--%>

    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

	<style type="text/css">
	.head {
		position: fixed;
		top:  0;
		left: 0;
		width: 100%;
		z-index: 500
	}
	.content {
		position: absolute;
		top: 138px;
		left: 0;
		bottom: 0;
		right: 0;
		overflow: auto;
	}
	.myRight {
		font-size: 16px;
		float: right;
	}
    .myLeft {
        font-size: 16px;
        float: left;
    }
	.zuigao {
		z-index: 501;
        display: none;
	}
	.myTitle {
		/*margin-top*/
		padding-left: 20px;
	}
	.bookInfo {
		margin-left: 15px;
		position: absolute;
		top: 49.33px;
		left: 0;
		bottom: 0;
		right: 0;
		overflow: auto;
	}
	.bookInfo_title {
		margin: 7px 0 0 10px;
	}
	.bookInfo_img {
		    margin-top: 7px;
			margin-right: 10px;
			margin-bottom: 0;
	}
	.bookInfo_many {
		margin: 7px 0 0 10px;
	}
	.bookInfo_many .bookInfo_one {
		margin: 5px 0 5px 0;
	}
	</style>

</head>
<body ontouchstart>
<!-- 用来存放session中的shopID，如果为空，说明是用户第一次进入main页面，要获取当前地理位置 -->
<input type="hidden" id="selectedShopID" value="${sessionScope.selectedShopID}">
<!-- 用来存放当前公益点id  -->
<input type="hidden" id="currentShopID">
<div class="head">
	<div class="weui-cells no-margin">
	  <a class="weui-cell weui-cell_access" href="javascript: void(0);" id="position">
		<div class="weui-cell__bd">
		  <p>公益点</p>
		</div>
		<div class="weui-cell__ft" id="shop">${selectedShopName}
		</div>
	  </a>
	</div>
	<!-- 搜索栏，需要优化 -->
	<div class="weui-search-bar" id="searchBar">
	  <form class="weui-search-bar__form">
		<div class="weui-search-bar__box">
		  <i class="weui-icon-search"></i>
		  <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜书名" onchange="searchChange()" >
		  <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
		</div>
		<label class="weui-search-bar__label" id="searchText">
		  <i class="weui-icon-search"></i>
		  <span>搜书名</span>
		</label>
	  </form>
	  <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
	</div>
	
	<div class="weui-tab">
		<div class="weui-navbar">
			<div class="weui-navbar__item weui-bar__item_on" id="bookListTab">
			  本店书单
			</div>
			<div class="weui-navbar__item" id="personalCenterTab">
			  个人中心
			</div>
		</div>
	</div>
</div>

<div class="content">

    <!--本店书单面板-->
	<div class="weui-panel weui-panel_access" id="bookList">
        <div class="weui-panel__bd" id="bookPanel">

        </div>

        <!-- 弹出层 -->
        <div id="pop" class="weui-popup__container zuigao">
            <div class="weui-popup__overlay"></div>
            <div class="weui-popup__modal">

                <!-- 弹出层内容开始 -->
                <div class="weui-cells no-margin">
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <a class='close-popup'><i class="fa fa-angle-left" id="back" style="margin-right: 5px;"></i>返回</a>


                                <a href="#"
                                   class="weui-btn weui-btn_mini weui-btn_primary borrowButton" style="float: right;">我要借阅</a>

                        </div>
                    </div>
                </div>
                <%--<div class="bookInfo">--%>
                    <%--<div class="weui-flex bookInfo_title">--%>
                        <%--<div class="weui-flex__item">--%>
                            <%--<h2 class="myTitle" id="bookName">游戏人间</h2>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="weui-flex">--%>
                        <%--<div class="bookInfo_img">--%>
                            <%--<img class="weui-media-box__thumb" src="https://img3.doubanio.com\/mpic\/s29327231.jpg" alt="" id="simg">--%>
                        <%--</div>--%>
                        <%--<div class="weui-flex__item bookInfo_many">--%>
                            <%--<div class="weui-flex">--%>
                                <%--<div class="weui-flex__item bookInfo_one">--%>
                                    <%--作者：<span id="author">贾平凹</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="weui-flex">--%>
                                <%--<div class="weui-flex__item bookInfo_one">--%>
                                    <%--出版社：<span id="press">百花洲文艺出版社</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="weui-flex">--%>
                                <%--<div class="weui-flex__item bookInfo_one">--%>
                                    <%--ISBN：<span id="isbn">9787550020016</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="weui-flex bookInfo_one">--%>
                                <%--<div class="weui-flex__item">--%>
                                    <%--定价：<span id="price">42</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="weui-flex">--%>
                        <%--<div class="weui-flex__item" style="font-size: 20px;">评分：<span id="grade">9.6</span></div>--%>
                    <%--</div>--%>
                    <%--<div class="weui-flex">--%>
                        <%--<div class="weui-flex__item">--%>
                            <%--<h4>简介：</h4><p id="summary">这部散文集是《自在独行》的姐妹篇，收录了平凹老师的60篇散文，大部分文章为近10年来的散文佳作，其中包括平凹老师从未发表过的私房新作《养鼠》；近三年来最满意的风土名篇《条子沟》；平凹老师最念念不忘的故乡恋曲《棣花》等从未在任何单行本图书中收录过的散文新作。--%>
                            <%--人立于世间， 既要面对内在生命的孤独，还要面对外在生命的烦恼。人生是三劫四劫过的，哪能一直走平路? 不要计较太多，不热羡，不怨恨，以自己的步调走路。--%>
                            <%--平凹先生觉得，生命要有乐气，人活着就要学会自寻欢喜，读书、写作、交朋友、旅行都可以给生命带来这样的乐气。乐气多了，生活就会来劲！而只有拥有了这样由内而外的生命劲头，才能真正过得潇洒从容，活得像你自己。</p>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            </div>
        </div>
        <!-- 弹出层结束 -->
    </div>
    <!--本店书单面板结束-->

  <div id="personalCenter" style="display: none">

      <c:if test="${userType == 'member'}">
          <div class="weui-cells">
              <a href="javascript: void(0);" class="weui-cell weui-cell_access" id="personInfo">
                  <div class="weui-cell__bd">
                      个人资料
                  </div>
                  <div class="weui-cell__ft">
                      <i class="home__icon-qrcode___OF6bm home__icon___2XgfG"></i>
                  </div>
              </a>
          </div>
      </c:if>


      <%--js-sdk要用的参数--%>
      <input id="timestamp" type="hidden" value="${timestamp}" />
      <input id="noncestr" type="hidden" value="${nonceStr}" />
      <input id="signature" type="hidden" value="${signature}" />
    <div class="weui-cells">
        <c:if test="${userType == 'member'}">
            <a href="javascript:void(0);" class="weui-cell weui-cell_access borrowButton">
                <div class="weui-cell__bd">
                    借书
                </div>
                <div class="weui-cell__ft">
                    <i class="home__icon-qrcode___OF6bm home__icon___2XgfG"></i>
                </div>
            </a>
            <a href="#" class="weui-cell weui-cell_access">
                <div class="weui-cell__bd" id="record">
                    我的记录
                </div>
                <div class="weui-cell__ft">
                    <i class="home__icon-qrcode___OF6bm home__icon___2XgfG"></i>
                </div>
            </a>
        </c:if>
        <c:if test="${userType == 'user'}">
            <a href="#" class="weui-cell weui-cell_access returnButton">
                <div class="weui-cell__bd">
                    归还
                </div>
                <div class="weui-cell__ft">
                    <i class="home__icon-qrcode___OF6bm home__icon___2XgfG"></i>
                </div>
            </a>
        </c:if>
    </div>

    <div class="weui-btn-area">
			<button class="weui-btn weui-btn_warn" id="exitBtn" onclick="exitLogin()">退出登录</button>
    </div>
		
	  </div>	
</div>



	
<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/wechat/js/jquery-ui/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/wechat/js/jquery-ui/jquery-weui.min.js"></script>

<script>

var bookList = [];
var isbn;
function searchChange() {
    localStorage.setItem("bookName", $("#searchInput").val());
}
$(function() {

    console.log("${userType}");

    //获取选择来的id
    var selectedShopID = $("#selectedShopID").val();


    if (!isNotNull(selectedShopID)) {
        //如果是空，就定位
        getPosition();
    } else {
        $("#currentShopID").val(selectedShopID);
        showBooks(selectedShopID);
    }

	if (location.hash) {
		var tab = location.hash.split("=")[1];
		$("#" + tab).click();
	}

    $('#searchInput').on('keyup', function(event) {/* 增加回车提交功能 */
        if (event.keyCode == '13') {
            var bookName = $("#searchInput").val();
            searchBook(bookName);
        }
    });

    //配置微信js-sdk
    jssdkConfig();
});


function jssdkConfig() {
    var pageUrl = window.location.href.split('#')[0];
    console.info(pageUrl);
    /*url中最好不要带中文，因为中文传到后台时会被解析为ASCII，而客户端的url仍然是中文形式，这就造成了生成签名错误*/
    pageUrl = pageUrl.replace(/\&/g, '%26');
    $.ajax({
        url : '${pageContext.request.contextPath }/wechat/createSignature?url='+pageUrl,
        cache : false,
        dataType : "json",
        success : function(r) {
                wx.config({
                    debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId : r.appId, // 必填，公众号的唯一标识
                    timestamp : r.timestamp, // 必填，生成签名的时间戳
                    nonceStr : r.nonceStr, // 必填，生成签名的随机串
                    signature : r.signature,// 必填，签名
                    jsApiList : [ 'scanQRCode' ]
                    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
        }
    });
}


/*4FMBZ-WFU26-22PSQ-M36AU-PGU4Z-NYFRP*/
function getPosition() {
    var geolocation = new qq.maps.Geolocation("4FMBZ-WFU26-22PSQ-M36AU-PGU4Z-NYFRP", "iread");
    var options = {timeout: 8000};
    geolocation.getLocation(getNearestShopName, showErr, options);
}

/*获取定位，找出距离最近的公益点*/
function getNearestShopName(position) {
    var lat = position.lat;
    var lng = position.lng;
    $.ajax({
        url: "${pageContext.request.contextPath}/wechat/getNearestShopName",
        dataType: "json",
        data: {
            lat: lat,
            lng: lng
        },
        success: function (r) {
            var shopName = r.nearestShopName;
            if (shopName.length > 18) {
                shopName = shopName.substring(0, 17);
                shopName = shopName + "..";
            }
            $("#shop").html("").html(shopName);
            $("#currentShopID").val(r.id);
            /*根据公益点显示本店书单*/
            showBooks(r.id);
        }
    });
}

function showErr() {
    //alert("定位失败!");
}


/*展示书本列表*/
function showBooks(shopID) {
    $.ajax({
        url: "${pageContext.request.contextPath}/wechat/showBooksByShopID",
        dataType: "json",
        data: {
            shopID: shopID,
            "bookName":localStorage.getItem("bookName")
        },
        success: function (r) {
            var books = r.books;
            var shopName = r.shopName;
//            $("#shop").html("").html(shopName);
            $("#bookPanel").html("");
            for (var i = 0; i < books.length; i++) {
                var book = books[i];
                var str = "<a href='javascript:void(0);' class='weui-media-box weui-media-box_appmsg open-popup' id='" + book.isbn + "'>";
                str += "<div class='weui-media-box__hd'>";
                str += "<img class='weui-media-box__thumb' src=" + book.simgUrl + " alt=''></div>";
                str += "<div class='weui-media-box__bd'>";
                str += "<h4 class='weui-media-box__title'>" + book.bookName + "</h4>";
                str += "<div><span>" + book.author + "</span><span class='myRight'>借阅次数：<span>" + book.borrowTimes + "</span></span></div>";
                str += "<div><span class='myLeft'>可借数量："+book.allowBookletBorrowNumber+"<span></div>";
                var summary = book.summary;
                if (summary.length > 38) {
                    summary = summary.substr(0, 38);
                }
                /*str += "<p class='weui-media-box__desc'>" + summary + "</p></div></a>";*/
                $("#bookPanel").append(str);
            }
            /*保存至全局变量*/
            saveToGlobalVar(books);
        }
    });
}
/*给每一个书单a标签绑定事件*/
/*因为ajax生成的元素，所以必须用以下这种方式或者bind来绑定click事件*/
$('body').on('click', '.open-popup', function () {
    var isbn = $(this).attr("id");
    var book = getBookDataFromArray(isbn);
    $("#bookName").html("").html(book.bookName);
    $("#author").html("").html(book.author);
    $("#press").html("").html(book.press);
    $("#isbn").html("").html(isbn);
    $("#simg").attr("src", book.simgUrl);
    $("#price").html("").html(book.price);
    $("#grade").html("").html(book.grade);
    $("#summary").html("").html(book.summary);
    /*为pop层手动添加id，再弹出*/
    var id = isbn + "pop";
    $("div.weui-popup__container").attr("id", id);
    $("#" + id).popup();





});


/*借书按钮调用微信扫一扫接口*/
$(".borrowButton").on("click", function (r) {
    wx.scanQRCode({
        // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
        needResult : 1,
        desc : 'scanQRCode desc',
        success : function(res) {
            //扫码后获取结果参数赋值给Input
            var bianhao = res.resultStr;
            window.location.href = "${pageContext.request.contextPath}/wechat/borrow?bianhao=" + bianhao;
        }
    });
});

/*借书按钮调用微信扫一扫接口*/
$(".returnButton").on("click", function (r) {
    wx.scanQRCode({
        // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
        needResult : 1,
        desc : 'scanQRCode desc',
        success : function(res) {
            //扫码后获取结果参数赋值给Input
            var bianhao = res.resultStr;
            window.location.href = "${pageContext.request.contextPath}/wechat/returnBack?bianhao=" + bianhao;
        }
    });
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



function saveToGlobalVar(books) {
    for (var i = 0; i < books.length; i++) {
        var book = books[i];
        bookList.push({"isbn": book.isbn, "data": book});
    }
}

<!-- 从数组中取出 -->
function getBookDataFromArray(isbn) {
    for(var i = 0; i < bookList.length; i++) {
        var book = bookList[i];
        if(book.isbn === isbn) {
            return bookList[i].data;
        }
    }
    return null;
}


$("#position").on("click", function() {
	window.location.href="${pageContext.request.contextPath}/wechat/position?shopID=" + $("#currentShopID").val();
});

$("#bookListTab").on("click", function() {
	$(this).addClass("weui-bar__item_on");
	$(this).siblings().removeClass("weui-bar__item_on");
	$("#bookList").show();
	$("#personalCenter").hide();
	replaceState($(this).attr("id"));
});
$("#personalCenterTab").on("click", function() {
	$(this).addClass("weui-bar__item_on");
	$(this).siblings().removeClass("weui-bar__item_on");
	$("#personalCenter").show();
	$("#bookList").hide();
	replaceState($(this).attr("id"));
})
$("#personInfo").on("click", function() {
	window.location.href = "${pageContext.request.contextPath}/wechat/personInfo";
})
$("#record").on("click", function() {
	window.location.href = "${pageContext.request.contextPath}/wechat/record";
})

function replaceState(tabId) {
	window.history.replaceState(null,"","#tab="+tabId);
}
function exitLogin() {
    location.href = "${pageContext.request.contextPath}/wechat/exitLogin";
}
</script>
</body>
</html>