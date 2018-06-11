<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    #map {
        width: 600px;
        height: 360px;
        border: 1px solid grey;
        margin: 0 auto;
    }
</style>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>公益点管理</h2>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>创建公益点</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form:form class="form-horizontal" commandName="shop" method="post"
                               action="${pageContext.request.contextPath}/admin/shop/save?type=create" id="barForm">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">公益点名称</label>
                            <div class="col-sm-4">
                                <form:input path="shopName" class="form-control" />
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="shopName" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">城市</label>
                            <div class="col-sm-4">
                                <form:select class="select2_city" path="cityID" items="${cities}" itemValue="id" itemLabel="cityName">

                                </form:select>
                                <%--<div>
                                    <select class="select2_city">
                                        <c:forEach items="${cities}" var="city">
                                            <option value="${city.id}" >${city.cityName}</option>
                                        </c:forEach>
                                    </select>
                                </div>--%>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">地址</label>
                            <div class="col-sm-4">
                                <form:input path="address" class="form-control" id="address"/>
                            </div>
                            <div class="col-sm-4" id="searchResultPanel" style="border:1px solid #C0C0C0;height:auto; display:none;">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">经度</label>
                            <div class="col-sm-4">
                                <form:input path="longitude"  type="number" step="0.000001" class="form-control" id="lng"/>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="longitude" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">纬度</label>
                            <div class="col-sm-4">
                                <form:input path="latitude"  type="number" step="0.000001" class="form-control" id="lat"/>
                                <span class="middle">
                                    <font color="red">
                                        <form:errors path="latitude" />
                                    </font>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <div id="map"></div>
                        </div>



                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-4">
                                <button class="btn btn-primary" type="submit">保存</button>
                                <a class="btn btn-white" href="${pageContext.request.contextPath}/admin/shop/list">返回</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var map;
    var ac;
    var myValue;
    var gc;
    var select2_city;

    $(document).ready(function(){
        select2_city = $(".select2_city").select2({
            width: "100%"
        });

    });


    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
            changeLngLat(pp.lng, pp.lat);
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }


    <!-- 当选择框值发生改变时 -->
    $(".select2_city").change(function () {
        //获取城市名
        var cityName = $(this).children("option:selected").html();
        //加载城市
        loadCity(cityName);
    });

    function loadJScript() {
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=uEEhkAc1vQlG3tGGuhoOCXuf9RydExqn&callback=init";
        document.body.appendChild(script);
    }

    function init() {
        map = new BMap.Map("map");   // 创建Map实例
        map.setZoom(11);
//        map.centerAndZoom("无锡",11);//设置初始地点和地图级别，默认不可点击
        map.enableScrollWheelZoom();   //启用滚轮放大缩小
        map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));
        map.enableInertialDragging();	//开启惯性拖拽
        ac = new BMap.Autocomplete({    //建立一个自动完成的对象
                    "input" : "address",
                    "location" : map
                });
        gc = new BMap.Geocoder();//地址解析类;
        setAcEventListener();
        map.addEventListener("click", selectPoint);
        getCurrentCity();
    }

    <!-- 点击地图获取地点 -->
    function selectPoint(e) {
        map.clearOverlays();
        var mk = new BMap.Marker(e.point);
        map.addOverlay(mk);
        map.panTo(e.point);
        //获取地址信息
        gc.getLocation(e.point, function(rs){
            var address = rs.address;
            $("#address").val(address);
        });
        changeLngLat(e.point.lng, e.point.lat);
    }

    function setAcEventListener() {
        ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
            var str = "";
            var _value = e.fromitem.value;
            var value = "";
            if (e.fromitem.index > -1) {
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

            value = "";
            if (e.toitem.index > -1) {
                _value = e.toitem.value;
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
            $("#searchResultPanel").innerHTML = str;
        });

        ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
            var _value = e.item.value;
            myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            $("#searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
            setPlace();
        });
    }


    function loadCity(city) {
        map.setCenter(city);
        changeLngLat(map.getCenter().lng, map.getCenter().lat);
    }

    function changeLngLat(lng, lat) {
        $("#lng").val(lng);
        $("#lat").val(lat);
    }

    function loadAddress(address) {
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search(address);
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

    <!-- 在页面包括图片、dom等都加载完毕执行 -->
    window.onload = loadJScript;  //异步加载地图
</script>