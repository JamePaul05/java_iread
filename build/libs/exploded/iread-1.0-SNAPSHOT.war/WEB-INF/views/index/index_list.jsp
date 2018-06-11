<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .echart {
        min-height: 400px;
        width: 100%;
    }
</style>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="echart" id="echart1">

                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="echart" id="echart2">

                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var option1;
    var option2;
    var chart;
    option1 = {
        color: ['#3398DB'],
        title : {
            text : "公益点城市数量图"
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : [],
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis : [
            {
                name: '(家)',
                type : 'value'
            }
        ],
        series : [
            {
                name:'公益点数',
                type:'bar',
                barWidth: '60%',
                data:[]
            }
        ]
    };
    option2 = {
        title: {
            text: '公益点图书详细',
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['图书总数', '外借数量']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            axisLabel: {
                interval:0,//横轴信息全部显示
                rotate:-20//-30度角倾斜显示
            },
            data: []
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        series: [
            {
                name: '图书总数',
                type: 'bar',
                data: []
            },
            {
                name: '外借数量',
                type: 'bar',
                data: []
            }
        ]
    };
    $(document).ready(function(){
        setShops();
        setBooklets();
    });
    
    function setShops() {
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/index/getShopsByCity",
            dataType: "json",
            success: function (r) {
                option1.xAxis[0].data = r.cityNames;
                option1.series[0].data = r.shops;
                setShopOption(chart, option1, "echart1");
            }
        });
    }

    function setBooklets() {
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/index/getBookletsChart",
            dataType: "json",
            success: function (r) {
                option2.series[0].data = r.bookletTotal;
                option2.series[1].data = r.borrowedTotal;
                option2.xAxis.data = r.shopNames;
                setShopOption(chart, option2, "echart2");
            }
        });
    }

    /*function handleTooLong(shopNames) {
        var result = [];
        for (var i = 0; i < shopNames.length; i++) {
            var shopName = shopNames[i];
            var length = 6;
            if (shopName.length > length) {
                var temp = shopName.substring(0, 6);
                shopName = shopName.replace(temp, temp + " \r\n ");
            }
            result.push(shopName);
        }
        return result;
    }*/

    function setShopOption(chart, option, id) {
        try {
            chart.clear();
        } catch(e) {
            console.log(e.name + ": " + e.message);
        }

        chart=echarts.init(document.getElementById(id));
        chart.setOption(option);
    }


</script>