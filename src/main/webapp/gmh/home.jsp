<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-首页</title>
  <link rel="shortcut icon" type="image/x-icon" href="css/images/favicon.ico">

  <!--JQuery-->
  <script src="js/jquery.min.js"></script>
  <script src="js/jquery.serialize-object.min.js"></script>

  <!--Semantic-UI-->
  <link rel="stylesheet" type="text/css" href="css/semantic-ui/semantic.css">
  <script src="css/semantic-ui/semantic.js"></script>

  <!--Style-->
  <link rel="stylesheet" href="css/style.css">

  <!--Script-->
  <script src="js/action.js"></script>
  <script src="js/tools.js"></script>
  <script src="js/echarts.js"></script>
  <script src="js/script.js"></script>
</head>

<body>
  <button class="ui blue button fake-button" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui center aligned grid">
        <div class="one column row">
          <div class="center aligned column">
            <div id="chart1" style="width: 1300px;height:400px;"></div>
          </div>
          <div class="column">
            <div id="chart2" style="width: 1300px;height:400px;"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script>
  	
 	var xAxisData1 = [];
    var chartData1 = [];
	var xAxisData2 = [];
 	var chartData2 = [];
  	
  	var xAxisData1 = ['一月','二月','三月','四月','5月','6月'];
  	var chartData1 = ['34','23','523','12','66','821'];
  	var xAxisData2 = ['一月','二月','三月','四月','5月','6月'];
  	var chartData2 = ['34','23','523','12','66','821'];
  
  	activeMenu('home');
  	
   	$('.fake-button').api({
        action: 'staff info',
        method: 'GET',
        on: 'now',
        beforeXHR: function (xhr) {
            //判断token是否有效
            var token = getCookie('token');
            if (token == null || typeof (token) == undefined) {
                alert('请先登录！');
                redirect('index.jsp');
            }
            xhr.setRequestHeader('X-token', getCookie('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
            if (response.error != null) {
                alert(response.code + ' : ' + response.error);
            } else {
            	$.each(response.data, function (i, data){
            		xAxisData1.push(data.month);
            		xAxisData2.push(data.month);
            		chartData1.push(data.amountCount);
            		chartData2.push(data.timesCount);
            	})
            	newChart();
            	
            }
        },
        onFailure: function (response) {
            alert('服务器开小差了');
        }
    })

    function newChart(){
  	    // 基于准备好的dom，初始化echarts实例
  	    var chart1 = echarts.init(document.getElementById('chart1'));
  	    var chart2 = echarts.init(document.getElementById('chart2'));

  	    // 指定图表的配置项和数据
  	    var option1 = {
  	      title: {
  	        text: '消费金额统计'
  	      },
  	      tooltip: {
  	        trigger: 'axis'
  	      },
  	      legend: {
  	        data: ['近六个月消费次数']
  	      },
  	      grid: {
  	        left: '3%',
  	        right: '4%',
  	        bottom: '3%',
  	        containLabel: true
  	      },
  	      toolbox: {
  	    	trigger: 'item',
  	        formatter: "{b}<br />{a} : {c}"
  	      },
  	      xAxis: {
  	        type: 'category',
  	        data: xAxisData1
  	      },
  	      yAxis: {
  	        type: 'value',
  	        axisLabel: {
  	        	formatter: '{value} 元'
  	        }
  	      },
  	      series: [{
  	        name: '消费金额',
  	        type: 'line',
  	        step: 'start',
  	        data: chartData1
  	      }]
  	    };
  	  	var option2 = {
  	      title: {
  	        text: '消费金额统计'
  	      },
  	      tooltip: {
  	        trigger: 'axis'
  	      },
  	      legend: {
  	        data: ['近六个月消费金额']
  	      },
  	      grid: {
  	        left: '3%',
  	        right: '4%',
  	        bottom: '3%',
  	        containLabel: true
  	      },
  	      toolbox: {
  	    	trigger: 'item',
  	        formatter: "{b}<br />{a} : {c}"
  	      },
  	      xAxis: {
  	        type: 'category',
  	        data: xAxisData1
  	      },
  	      yAxis: {
  	        type: 'value',
  	      	axisLabel: {
  	        	formatter: '{value} 元'
  	        }
  	      },
  	      series: [{
  	        name: '消费金额',
  	        type: 'line',
  	        step: 'start',
  	        data: chartData1
  	      }]
  	  	}


  	    // 使用刚指定的配置项和数据显示图表。
  	    chart1.setOption(option1);
  	    chart2.setOption(option2); 		
  	}
  	

  </script>
</body>

</html>