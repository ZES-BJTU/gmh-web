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
        <div class="three column row">
          <div class="column">
            <div class="ui huge statistic">
              <div class="value">
                2,204
              </div>
              <div class="label">今日预约</div>
            </div>
          </div>
          <div class="column">
            <div class="ui huge statistic">
              <div class="value">
                2,204
              </div>
              <div class="label">会员总数</div>
            </div>
          </div>
          <div class="column">
            <div class="ui huge statistic">
              <div class="value">
                2,204
              </div>
              <div class="label">美容师</div>
            </div>
          </div>
        </div>
        <div class="two column row">
          <div class="center aligned column">
            <div id="chart1" style="width: 600px;height:400px;"></div>
          </div>
          <div class="column">
            <div id="chart2" style="width: 600px;height:400px;"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script>
  
  	activeMenu('home');
  	
    // 基于准备好的dom，初始化echarts实例
    var chart1 = echarts.init(document.getElementById('chart1'));
    var chart2 = echarts.init(document.getElementById('chart2'));

    // 指定图表的配置项和数据
    var option1 = {
      title: {
        text: '预约折线图'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['Step Start', 'Step Middle', 'Step End']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: 'Step Start',
        type: 'line',
        step: 'start',
        data: [120, 132, 101, 134, 90, 230, 210]
      }]
    };
    var option2 = {
      title: {
        text: '预约折线图'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['Step Start', 'Step Middle', 'Step End']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: 'Step Start',
        type: 'line',
        step: 'start',
        data: [120, 132, 101, 134, 90, 230, 210]
      }]
    };


    // 使用刚指定的配置项和数据显示图表。
    chart1.setOption(option1);
    chart2.setOption(option2);
  </script>
</body>

</html>