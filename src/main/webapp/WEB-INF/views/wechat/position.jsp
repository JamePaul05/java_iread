<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
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

    <link href="https://cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/css/mystyle.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">

    <style type="text/css">
        html, body {
            position: relative;
            height: 100%;
            overflow: scroll;
            background: #fbf9fe;
        }

        .pointer {
            cursor: pointer;
        }

        .map-container {
            width: 100%;
            height: 400px;
        }
    </style>

</head>
<body ontouchstart>
<div class="">
    <div class="weui-cells no-margin">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <i class="fa fa-angle-left" id="back">当前位置：${position}</i>
            </div>
        </div>
    </div>
    <div class="weui-search-bar" id="searchBar">
        <form class="weui-search-bar__form">
            <div class="weui-search-bar__box">
                <i class="weui-icon-search"></i>
                <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索公益点"
                       onchange="searchChange()">
                <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
            </div>
            <label class="weui-search-bar__label" id="searchText">
                <i class="weui-icon-search"></i>
                <span>搜索</span>
            </label>
        </form>
        <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
    </div>

    <!-- 定位 -->
    <div class="weui-cells__title">已定位城市</div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd pointer"><p id="cityGPS">城市</p></div>
        </div>
    </div>
    <div class="weui-cells__title">距您最近的公益借阅点</div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd pointer"><p id="shopGPS">公益点</p><input type="hidden" id="shopGPSID"></div>
        </div>
    </div>

    <!-- 自主选择城市 -->
    <div class="weui-cells__title">选择</div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__hd"><label for="city" class="weui-label">城市</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" id="city" type="text" value="" readonly="">
            </div>
        </div>
        <div class="weui-cell" id="last-cell">
            <div class="weui-cell__hd"><label for="shop" class="weui-label">公益点</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" id="shop" type="text" value="" readonly="">
            </div>
        </div>
    </div>

    <div id="container" class="map-container">

    </div>

</div>

<!-- <div class="weui-cells weui-cells_form"> -->
<!-- <div class="weui-cell"> -->
<!-- <div class="weui-cell__bd"> -->
<!-- <p>标题文字</p> -->
<!-- </div> -->
<!-- <div class="weui-cell__ft">说明文字</div> -->
<!-- </div> -->
<!-- </div> -->

<!-- Mainly scripts -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script>

    var map;

    //公益点选择框的参数
    var shopOption = {
        title: "选择公益点",
        items: [],
        onChange: function () {//用户选择后直接跳转
            var selectedShopID = $("#shop").attr("data-values");
            backToMain(selectedShopID);
        }
    };

    function searchChange() {
        localStorage.setItem("shopName", $("#searchInput").val());
    }

    $(function () {
        //页面加载时从后台获取所有的城市和所有的公益点
        $("#searchInput").val((localStorage.getItem("shopName")));
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/getCitiesAndShops",
            dataType: "json",
            data: {"shopName": localStorage.getItem("shopName")},
            success: function (r) {
                var cities = r.cities;
                var shops = r.shops;
                $("#city").select({
                    title: "选择城市",
                    items: cities,
                    onChange: funCityChange //当用户选择城市之后的回调函数，改变对应的公益点
                });
                localStorage.clear();
                shopOption.items = shops;
                $("#shop").select(shopOption);
            }
        });
    });

    <!-- 在页面包括图片、dom等都加载完毕执行 -->
    window.onload = loadJScript;  //异步加载地图

    function loadJScript() {
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=uEEhkAc1vQlG3tGGuhoOCXuf9RydExqn&callback=init";
        document.body.appendChild(script);
    }

    //初始化
    function init() {
        //定位
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(position) {
            console.log(position);
            if(this.getStatus() === 0){
                showPosition(position);
            } else {
                alert('定位失败');
            }
        },{enableHighAccuracy: true})
    }

    //当城市切换时的回调函数
    function funCityChange() {
        //清除地图上的所有覆盖物
        clearMarkers();
        var cityID = $("#city").attr("data-values");
        var cityName = $("#city").attr("value");
        map.centerAndZoom(cityName, 11);
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/findShopsByCity",
            dataType: "json",
            data: {
                cityID: cityID
            },
            success: function (r) {
                var shops = r.shops;
                var shopItems = [];
                for (var i = 0; i < shops.length; i++) {
                    var shop = shops[i];
                    var item = {
                        "value": shop.id,
                        "title": shop.name
                    };
                    shopItems.push(item);
                    addMarker(shop);
                }
                shopOption.items = shopItems;
                $("#shop").select("update", shopOption);

            }
        });
    }

    //定位功能的回调函数
    function showPosition(position) {
        var cityName = position.address.city;
        console.log(cityName);
        $("#cityGPS").html("").html(cityName);
        var lat = position.latitude;
        var lng = position.longitude;
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/getNearestShopName",
            dataType: "json",
            data: {
                lat: lat,
                lng: lng
            },
            success: function (r) {
                $("#shopGPS").html("").html(r.nearestShopName);
                $("#shopGPSID").val(r.id);
            }
        });
        initMap(position);
    }

    //初始化地图
    function initMap(position) {
        var lat = position.latitude;
        var lng = position.longitude;
        map = new BMap.Map("container");
        map.centerAndZoom(new BMap.Point(lng, lat), 11);
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        var cityName = position.address.city;
        $.ajax({
            url: "${pageContext.request.contextPath}/wechat/findShopsByCityName?cityName=" + cityName.replace("市", ""),
            dataType: "json",
            success: function (r) {
                var shops = r.shops;
                for (var i = 0; i < shops.length; i++) {
                    addMarker(shops[i]);
                }
            }
        });
    }

    //根据公益点添加覆盖物
    function addMarker(shop) {
        var point = new BMap.Point(shop.lng, shop.lat);
        var marker = new BMap.Marker(point);  // 创建标注
        map.addOverlay(marker);              // 将标注添加到地图中
        var label = new BMap.Label(shop.name, {offset:new BMap.Size(20,-10)});
        marker.setLabel(label);
    }

    //清除地图所有覆盖物
    function clearMarkers() {
        map.clearOverlays();
    }

    /*回到主页面*/
    function backToMain(shopId) {
        window.location.href = "${pageContext.request.contextPath}/wechat/main?selectedShopID=" + shopId;
    }

    $("#shopGPS").on("click", function () {
        var shopGPSID = $("#shopGPSID").val();
        backToMain(shopGPSID);
    });


    $("#back").on("click", function () {
        window.history.back(-1);
    });
    var $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    function hideSearchResult() {
        $searchResult.hide();
        $searchInput.val('');
    }

    function cancelSearch() {
        hideSearchResult();
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }

    $searchText.on('click', function () {
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });
    $searchInput
        .on('blur', function () {
            if (!this.value.length) cancelSearch();
        })
        .on('input', function () {
            if (this.value.length) {
                $searchResult.show();
            } else {
                $searchResult.hide();
            }
        })
    ;
    $searchClear.on('click', function () {
        hideSearchResult();
        $searchInput.focus();
    });
    $searchCancel.on('click', function () {
        cancelSearch();
        $searchInput.blur();
    });

</script>
</body>
</html>