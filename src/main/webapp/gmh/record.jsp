<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-消费记录管理</title>
  <link rel="shortcut icon" type="image/x-icon" href="css/images/favicon.ico">

  <!--JQuery-->
  <script src="js/jquery.min.js"></script>
  <script src="js/jquery.serialize-object.min.js"></script>

  <!--Semantic-UI-->
  <link rel="stylesheet" type="text/css" href="css/semantic-ui/semantic.css">
  <script src="css/semantic-ui/semantic.js"></script>

  <!--Style-->
  <link href="js/bootstrap-datetimepicker-master/datetimepicker.css" rel="stylesheet">
  <link rel="stylesheet" href="css/style.css">
  
  <!--Script-->
  <script src="js/action.js"></script>
  <script src="js/tools.js"></script>
  <script src="js/echarts.js"></script>
  <script src="js/bootstrap-datetimepicker-master/bootstrap-datetimepicker.min.js"></script>
  <script src="js/bootstrap-datetimepicker-master/locales/bootstrap-datetimepicker.zh-CN.js"></script>
  <script src="js/script.js"></script>
</head>

<body>
  <button class="ui blue button fake-button" style="display:none;"></button>
  <button class="ui blue button load-record-list" style="display:none;"></button>
  <a id="export-button" href="http://localhost:8080/consume/export" style="display:none"></a>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search record-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <input type="text" class="time-select start-time" placeholder="开始日期">
                <input type="text" class="time-select end-time" placeholder="结束日期">
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-record">新建</button>
            <button class="ui teal button export-record">导出</button>
            <button class="ui teal button print-record">打印</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>消费人</th>
                  <th>联系方式</th>
                  <th>消费者类型</th>
                  <th>年龄</th>
                  <th>性别</th>
                  <th>操作员</th>
                  <th>经理/咨询师</th>
                  <th>消费项目</th>
                  <th>消费金额</th>
                  <th>消费方式</th>
                  <th>消费时间</th>
                  <th>来源</th>
                  <th>备注</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="record-list">
              </tbody>
            </table>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="ui borderless menu paging">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="ui large modal new-record-modal">
    <div class="header">新建消费记录</div>
    <div class="content">
      <form id="new-record" class="ui form">
        <div class="two fields">
          <div class="field">
            <label>消费人手机号</label>
            <input type="text" name="mobile" id="new-record-mobile" placeholder="请输入预约人手机号">
          </div>
          <div class="field">
            <label>消费人姓名</label>
            <input type="text" name="consumerName" placeholder="请输入预约人姓名">
          </div>
        </div>
        <div class="field">
          <label>性别</label>
          <select name="sex" class="ui fluid dropdown new-record-sex-select">
          <option value="">请选择性别</option>
          <option value="0">女</option>
          <option value="1">男</option>
        </select>
        </div>
        <table class="ui compact table theme">
          <thead>
            <tr>
              <th>美容项目</th>
              <th>操作员</th>
              <th>价格</th>
              <th>折扣(%)</th>
              <th>实付价格</th>
              <th>经理/咨询师</th>
              <th class="add-project"><i class="plus icon"></i></th>
            </tr>
          </thead>
          <tbody id="project-list">
          </tbody>
        </table>
        <div class="field">
          <label>支付方式</label>
          <select name="chargeWay" class="ui fluid dropdown new-record-chargeWay-select">
            <option value="">请选择支付方式</option>
            <option value="1">会员卡</option>
            <option value="2">其他</option>
            <option value="3">赠送</option>
          </select>
        </div>
        <div class="field" id="card-field" style="display:none">
          <label>会员卡</label>
          <select name="menberId" class="ui fluid dropdown new-record-card-select">
            <option value="">请选择会员卡</option>
          </select>
        </div>
        <div class="field">
          <label>支付金额</label>
          <input type="text" name="charge" id="finalCharge" placeholder="请输入支付金额">
        </div>
        <div class="field">
          <label>来源</label>
          <input type="text" name="source" placeholder="请输入来源">
        </div>
        <div class="field">
          <label>备注</label>
          <textarea name="remark" rows="3"></textarea>
        </div>
      </form>
    </div>
    <div class="actions">
      <div class="ui black deny right labeled icon button">
        取消
        <i class="remove icon"></i>
      </div>
      <div class="ui positive right labeled icon button">
        提交
        <i class="checkmark icon"></i>
      </div>
    </div>
  </div>
  <div class="ui large modal mod-record-modal">
    <div class="header">修改消费记录</div>
    <div class="content">
      <span id="mod-record-id" style="display:none"></span>
      <form id="mod-record" class="ui form">
        <div class="two fields">
          <div class="field">
            <label>消费人手机号</label>
            <input type="text" name="mobile" id="mod-record-mobile" placeholder="请输入预约人手机号">
          </div>
          <div class="field">
            <label>消费人姓名</label>
            <input type="text" name="consumerName" placeholder="请输入预约人姓名">
          </div>
        </div>
        <div class="field">
          <label>性别</label>
          <select name="sex" class="ui fluid dropdown mod-record-sex-select">
          <option value="">请选择性别</option>
          <option value="0">女</option>
          <option value="1">男</option>
        </select>
        </div>
        <table class="ui compact table theme">
          <thead>
            <tr>
              <th>美容项目</th>
              <th>操作员</th>
              <th>价格</th>
              <th>折扣(%)</th>
              <th>实付价格</th>
              <th>经理/咨询师</th>
              <th class="add-project"><i class="plus icon"></i></th>
            </tr>
          </thead>
          <tbody id="mod-project-list">
          </tbody>
        </table>
        <div class="field">
          <label>支付方式</label>
          <select name="chargeWay" class="ui fluid dropdown mod-record-chargeWay-select">
            <option value="">请选择支付方式</option>
            <option value="1">会员卡</option>
            <option value="2">其他</option>
            <option value="3">赠送</option>
          </select>
        </div>
        <div class="field" id="mod-card-field" style="display:none">
          <label>会员卡</label>
          <select name="menberId" class="ui fluid dropdown mod-record-card-select">
            <option value="">请选择会员卡</option>
          </select>
        </div>
        <div class="field">
          <label>支付金额</label>
          <input type="text" name="charge" id="modFinalCharge" placeholder="请输入支付金额">
        </div>
        <div class="field">
          <label>来源</label>
          <input type="text" name="source" placeholder="请输入来源">
        </div>
        <div class="field">
          <label>备注</label>
          <textarea name="remark" rows="3"></textarea>
        </div>
      </form>
    </div>
    <div class="actions">
      <div class="ui black deny right labeled icon button">
        取消
        <i class="remove icon"></i>
      </div>
      <div class="ui positive right labeled icon button">
        提交
        <i class="checkmark icon"></i>
      </div>
    </div>
  </div>
  <div class="ui tiny coupled modal add-project-modal">
    <span id="add-project-type" style="display:none"></span>
    <div class="header">添加项目</div>
    <div class="content">
      <div class="ui grid">
        <div class="one column">
          <form id="new-record-add-project" class="ui form">
            <div class="field">
              <label>顶级分类</label>
              <select name="topTypeId" class="ui fluid dropdown new-record-top-type-select">
              <option value="">请选择顶级分类</option>
              <option value="1">美甲</option>
              <option value="2">美睫</option>
              <option value="3">美容</option>
              <option value="4">产品</option>
            </select>
            </div>
            <div class="field">
              <label>美容项目分类</label>
              <select name="projectTypeId" class="ui fluid dropdown new-record-type-select">
              <option value="">请选择美容项目分类</option>
            </select>
            </div>
            <div class="field">
              <label>美容项目</label>
              <select name="projectId" class="ui fluid dropdown new-record-project-select">
              <option value="">请选择美容项目</option>
            </select>
            </div>
            <div class="field">
              <label>操作员</label>
              <select name="employeeId" class="ui fluid dropdown new-record-employee-select">
                <option value="">请选择操作员</option>
              </select>
            </div>
            <div class="field">
              <label>美容项目价格</label>
              <input type="text" name="beginTime" value="" id="projectCharge" placeholder="美容项目价格" disabled="">
            </div>
          </form>
        </div>
      </div>
    </div>
    <div class="actions">
      <div class="ui black deny right labeled icon button">
        取消
        <i class="remove icon"></i>
      </div>
      <div class="ui positive right labeled icon button">
        添加
        <i class="checkmark icon"></i>
      </div>
    </div>
  </div>
  <div class="ui mini modal export-record-modal">
    <div class="header">导出消费记录</div>
    <div class="content">
      <form id="export-record" class="ui form">
        <div class="field">
          <label>开始时间</label>
          <input type="text" name="startTime" value="" id="startTime" placeholder="请输入开始时间">
        </div>
        <div class="field">
          <label>结束时间</label>
          <input type="text" name="endTime" value="" id="endTime" placeholder="请输入结束时间">
        </div>
      </form>
    </div>
    <div class="actions">
      <div class="ui black deny right labeled icon button">
        取消
        <i class="remove icon"></i>
      </div>
      <div class="ui positive right labeled icon button">
        提交
        <i class="checkmark icon"></i>
      </div>
    </div>
  </div>
  <script>
  
    activeMenu('record');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchStartTime = '';
    var searchEndTime = '';
    var counselorsData = [];
    $(document).ready(function () {
      $('.start-time').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: true,
        showMeridian: 1
      }).on('changeDate', function (ev) {
        $('.start-time').focus();
        $('.start-time').blur();
      });
      $('.end-time').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: true,
        showMeridian: 1
      }).on('changeDate', function (ev) {
        $('.end-time').focus();
        $('.end-time').blur();
      });
      $('#startTime').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        weekStart: 1,
        todayBtn: 1,
        minView: 2,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: true,
        showMeridian: 1
      }).on('changeDate', function (ev) {
        $('#startTime').focus();
        $('#startTime').blur();
      });
      $('#endTime').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        weekStart: 1,
        todayBtn: 1,
        minView: 2,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: true,
        showMeridian: 1,
        endDate:new Date()
      }).on('changeDate', function (ev) {
        $('#endTime').focus();
        $('#endTime').blur();
      });

      //加载经理或者咨询师
      $('.fake-button').api({
        action: 'employee listCounselors',
        method: 'GET',
        on: 'now',
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            counselorsData = response.data;
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      })

      //新增时，点击顶级分类，切换美容项分类
      $(document).on('change', '.new-record-top-type-select', function () {
        var topType = $(this).find('select').val();
        if (topType == '') {
          return;
        }
        loadProjectTypeByTopType(topType, $('.new-record-type-select select'), 'new', null);
        $('.new-record-project-select select').find('option:not(:first)').remove();
        $('.new-record-project-select .text').text('');
        $('.new-record-project-select select').val('');
        $('.new-record-project-select select').parent().find('.text').addClass('default');
        $('.new-record-project-select select').parent().find('.text').text('请选择美容项目');
        $('.new-record-employee-select select').find('option:not(:first)').remove();
        $('.new-record-employee-select .text').text('');
        $('.new-record-employee-select select').val('');
        $('.new-record-employee-select select').parent().find('.text').addClass('default');
        $('.new-record-employee-select select').parent().find('.text').text('请选择操作员');
      })

      //新增时，点击美容项目分类，切换美容项目
      $(document).on('change', '.new-record-type-select', function () {
        var typeId = $(this).find('select').val();
        if (typeId == '') {
          return;
        }
        loadProjectByProjectType(typeId, $('.new-record-project-select select'), 'new', null);
      })

      //新增时，点击美容项目，切换操作员
      $(document).on('change', '.new-record-project-select', function () {
        var projectId = $(this).find('select').val();
        if (projectId == '') {
          return;
        }
        loadEmployeeByProject(projectId, $('.new-record-employee-select select'), 'new', null);
        loadProjectCharge(projectId);
      })

      //消费记录搜索
      $('.record-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchStartTime = $('.start-time').val() == '' ? '' : toTimeStamp($('.start-time').val());
          searchEndTime = $('.end-time').val() == '' ? '' : toTimeStamp($('.end-time').val());
          loadSearchRecordList(1, 10, 'search');
        }
      })

      $('.record-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .item', function () {
        loadSearchRecordList($(this).text(), 10, 'paging');
      })

      function loadSearchRecordList(pagenum, pagesize, type) {
        $('.load-record-list').api({
          action: 'record search',
          method: 'POST',
          serializeForm: true,
          on: 'now',
          data: {
            pageNum: pagenum,
            pageSize: pagesize
          },
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            if (type == 'search') {
              settings.data.searchString = $('.search-input').val();
              settings.data.startTime = $('.start-time').val() == '' ? '' : toTimeStamp($('.start-time').val());
              settings.data.endTime = $('.end-time').val() == '' ? '' : toTimeStamp($('.end-time').val());
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.startTime = searchStartTime;
              settings.data.endTime = searchEndTime;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#record-list').empty();
              $('.paging').empty();
              for (var i = 0; i < response.data.totalPages; i++) {
                var $item = $('<a class="item">' + (i + 1) + '</a>');
                $('.paging').append($item);
              }
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="recordId" style="display:none">' + data.id + '</td>');
                var $memberId = $('<td class="memberId" style="display:none">' + data.memberId + '</td>');
                // var $memberName = $('<td class="memberName" style="display:none">' + data.memberName + '</td>');
                var $consumerName = $('<td class="consumerName">' + data.consumerName + '</td>');
                var $mobile = $('<td class="mobile">' + data.mobile + '</td>');
                var $consumerType = $('<td class="consumerType">' + data.consumerDesc + '</td>');
                var $age = $('<td class="age">' + (data.age == null ? '无' : data.age) + '</td>');
                var $sex = $('<td class="sex">' + data.sex + '</td>');

                var projectIds = [];
                var projectNames = [];
                var employeeIds = [];
                var employeeNames = [];
                var projectNames = [];
                var counselorIds = [];
                var counselorNames = [];
                var charges = [];
                var retailPrices = [];

                $.each(data.consumeRecordProjectVos,function(i,data){
                  projectIds.push(data.projectId);
                  projectNames.push(data.projectName);
                  employeeIds.push(data.employeeId);
                  employeeNames.push(data.employeeName);

                  var cId = data.counselorId == null ? '无' : data.counselorId
                  counselorIds.push(cId);
                  var cName = data.counselorName == null ? '无' : data.counselorName
                  counselorNames.push(cName);

                  charges.push(data.charge);
                  retailPrices.push(data.retailPrice);
                })

                var $projectId = $('<td class="projectId" style="display:none">' + projectIds + '</td>');
                var $projectName = $('<td class="projectName">' + projectNames.join('<br>') + '</td>');
                var $employeeId = $('<td class="employeeId" style="display:none">' + employeeIds + '</td>');
                var $employeeName = $('<td class="employeeName">' + employeeNames.join('<br>') + '</td>');
                var $counselorId = $('<td class="counselorId" style="display:none">' + counselorIds + '</td>');
                var $counselorName = $('<td class="counselorName">' + counselorNames.join('<br>') + '</td>');
                var $rerailPrice = $('<td class="rerailPrice">' + rerailPrices.join('<br>') + '</td>');
                var $charge = $('<td class="charge">' + charges.join('<br>') + '</td>');

                // var $chargeWayId = $('<td class="chargeWayId" style="display:none">' + data.chargeWayId + '</td>');
                var $chargeWay = $('<td class="chargeWay">' + data.chargeWay + '</td>');
                var $consumeTime = $('<td class="consumeTime">' + toDatetimeMin(data.consumeTime) +
                  '</td>');
                var $source = $('<td class="source">' + data.source + '</td>');
                var remark = (data.remark == null || data.remark == '') ? '无' : String(data.remark);
                var afterRemark = '';
                if (remark == '无') {
                  afterRemark = '无'
                } else if (remark.length > 6) {
                  afterRemark = remark.substring(0, 6) + '...';
                } else {
                  afterRemark = remark;
                }
                var $remark = $('<td class="remark" title="' + remark + '">' + afterRemark + '</td>');
                var $operate = $(
                    '<td><button class="ui tiny orange button mod-appointment">修改</button>' +
                    '<button class="ui tiny teal button del-appointment">打印</button></td>');

                $tr.append($id);
                $tr.append($memberId);
                // $tr.append($memberName);
                $tr.append($consumerName);
                $tr.append($mobile);
                $tr.append($consumerType);
                $tr.append($age);
                $tr.append($sex);
                $tr.append($projectIds);
                $tr.append($projectName);
                $tr.append($employeeIds);
                $tr.append($employeeName);
                $tr.append($counselorId);
                $tr.append($counselorName);
                $tr.append($rerailPrice);
                $tr.append($charge);
                // $tr.append($chargeWayId);
                $tr.append($chargeWay);
                $tr.append($consumeTime);
                $tr.append($source);
                $tr.append($remark);
                $tr.append($operate);
                $('#record-list').append($tr);
              })
              $('.paging').children().eq(pagenum - 1).addClass('active');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //区分是新增预约还是修改预约
      $(document).on('click','.add-project',function(){
        $('#add-project-type').text('add');
      })
      $(document).on('click','.mod-add-project',function(){
        $('#add-project-type').text('mod');
      })

      //项目删除
      $(document).on('click', '.minus-project', function () {
        $(this).parent().remove();
        changeCharge();
      })

      //切换支付方式
      $(document).on('change','.new-record-chargeWay-select',function(){
        //不同支付方式的不同处理
        var code = $(this).find('select').val();
        if(code == 1){
          //输入手机号，变更会员卡
          changeCharge();
          var mobile = Number($('#new-record-mobile').val());
          var reg = new RegExp("^1\\d{10}$");  
          if (mobile != '') {
            if(reg.test(mobile)) {  
              //加载会员卡
              $('.fake-button').api({
                action: 'record listByPhone',
                method: 'GET',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if (mobile != '') {
                    settings.data.phone = mobile;
                    return settings;
                  } else {
                    alert('手机号为空');
                    return false;
                  }
                },
                onSuccess: function (response) {
                  clearCard();
                  if (response.error != null) {
                    alert(response.error);
                    verifyStatus(response.code);
                  } else {
                    $.each(response.data, function (i, data) {
                      var $option = $('<option value="' + data.id + '">' + data.memberLevelName + '</option>');
                      $('.new-record-card-select .text').removeClass('unselected');
                      $('.new-record-card-select select').append($option);
                    })
                  }
                },
                onFailure: function (response) {
                  alert('服务器开小差了');
                }
              })
            }else{
              alert('请输入正确的手机号！');
            } 
          }else{
            alert('请输入手机号！');
          }
          $('#card-field').show();
        }else if(code == 2){
          changeCharge();
          $('#card-field').hide();
        }else if(code == 3){
          $('#finalCharge').val('0');
          $('#card-field').hide();
        }
      })

      //输入折扣，变更实付金额
      $(document).on('blur', '.discount', function () {
        var pcharge = $(this).parent().parent().find('.projectCharge').text();
        var reg = new RegExp("^(\\d|[1-9]\\d|100)$");  
        if ($(this).val() != '') {
          if(reg.test($(this).val())) {  
              $(this).parent().parent().find('.charge').text(Number(pcharge) * Number($(this).val()) * 0.01);
          } 
        }else{
          $(this).parent().parent().find('.charge').text(Number(pcharge));
        }
        changeCharge();
      })

      //允许模态框叠加
      $('.coupled.modal').modal({
        allowMultiple: true
      })

      //添加项目模态框
      $('.add-project-modal')
        .modal('attach events', '.new-record-modal .add-project')
        // .modal('attach events', '.mod-record-modal .mod-add-project')
        .modal('setting', 'closable', false)
        .modal({
          onDeny: function () {
            clearSelect();
            $('#add-project-type').text('');
            $('#new-record-add-project').form('clear');
          },
          onApprove: function () {
            $('#new-record-add-project').submit();
            return false;
          }
        })

      $('#new-record-add-project').form({
        on: 'submit',
        inline: true,
        fields: {
          newTopTypeId: {
            identifier: 'topTypeId',
            rules: [{
              type: 'empty',
              prompt: '顶层分类不能为空'
            }]
          },
          newProjectTypeId: {
            identifier: 'projectTypeId',
            rules: [{
              type: 'empty',
              prompt: '美容项目分类不能为空'
            }]
          },
          newProjectId: {
            identifier: 'projectId',
            rules: [{
              type: 'empty',
              prompt: '美容项目不能为空'
            }]
          },
          newEmployeeId: {
            identifier: 'employeeId',
            rules: [{
              type: 'empty',
              prompt: '操作员不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          var beginTime = $('#beginTime').val();
          var endTime = $('#endTime').val();
          var projectId = $('.new-record-project-select select').val();
          var projectName = $('.new-record-project-select .text').text();
          var employeeId = $('.new-record-employee-select select').val();
          var employeeNmame = $('.new-record-employee-select .text').text();
          var projectCharge = $('#projectCharge').val();

          var option = '';
          $.each(counselorsData, function (i, data) {
            option += '<option value="' + data.id + '">' + data.emName + '</option>';
          })

          var $tr = $('<tr><td style="display:none" class="projectId">' + projectId + '</td><td>' + projectName +
              '</td><td style="display:none" class="employeeId">' + employeeId + '</td><td>' + employeeNmame + 
              '</td><td class="projectCharge">' + projectCharge + 
              '</td><td><input type="text" class="discount" placeholder="请输入折扣">' + 
              '</td><td class="charge">' + projectCharge +
              '</td><td class="counselor"><select class="ui fluid dropdown new-record-counselor-select"><option value="">请选择经理/咨询师</option>' + option + '</select></td>'+
              '<td class="minus-project"><i class="minus icon"></i></td></tr>');
          if($('#add-project-type').text() == 'add'){
            $('#project-list').append($tr);
          }else{
            $('#mod-project-list').append($tr);
          }
           //计算总价
           changeCharge();
          //重新加载下拉框
          $('.ui.dropdown').dropdown();

          clearSelect();
          $('#newReserveTime').val('');
          $('#add-project-type').text('');
          $('#new-record-time').empty();
          $('#new-record-add-project').form('clear');
          $('.add-project-modal').modal('hide');
        }
      })

      //新建消费记录模态框
      $('.new-record').on('click', function () {
        $('.new-record-modal').modal({
            closable: false,
            onDeny: function () {
              clearCounselor();
              clearCard();
              $('#project-list').empty();
              $('#card-field').hide();
              $('#new-record').form('clear');
            },
            onApprove: function () {
              $('#new-record').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建消费记录信息提交
      $('#new-record').form({
        on: 'submit',
        inline: true,
        fields: {
          newConsumerName: {
            identifier: 'consumerName',
            rules: [{
              type: 'empty',
              prompt: '消费人姓名不能为空'
            }]
          },
          newMobile: {
            identifier: 'mobile',
            rules: [{
              type: 'empty',
              prompt: '消费人手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            }, {
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          newSex: {
            identifier: 'sex',
            rules: [{
              type: 'empty',
              prompt: '性别不能为空'
            }]
          },
          newChargeWay: {
            identifier: 'chargeWay',
            rules: [{
              type: 'empty',
              prompt: '支付方式不能为空'
            }]
          },
          newCharge: {
            identifier: 'charge',
            rules: [{
              type: 'empty',
              prompt: '支付金额不能为空'
            }, {
              type: 'decimal',
              prompt: '请输入数字'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'record create',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          var counselorStatus = 0;
          if(settings.data.chargeWay == 1 && settings.data.chargeCard == ''){
            alert('请选择会员卡！');
            return false;
          }
          if($('#project-list').children().length == 0){
            alert('项目为空!');
            return false;
          }
          var projects = '';
          $('#project-list').children().each(function(){
            var pid = $(this).find('.projectId').text();
            var eid = $(this).find('.employeeId').text();
            var pcharge = $(this).find('.charge').text();
            var counselor = $(this).find('.new-record-counselor-select select').val();
            if(counselor == ''){
              counselor = 0;
            }
            projects += ';' + pid + ',' + eid + ',' + pcharge  + ',' + counselor;
          })
          projects = projects.substring(1);
          settings.data.projects = projects;
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            clearCounselor();
            clearCard();
            $('#project-list').empty();
            $('#card-field').hide();
            $('#new-record').form('clear');
            $('.new-record-modal').modal('hide');
            loadSearchRecordList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });
      //修改消费记录模态框
      $('.mod-record').on('click', function () {
        $('.mod-record-modal').modal({
            closable: false,
            onDeny: function () {
              clearCounselor();
              clearCard();
              $('#mod-project-list').empty();
              $('#mod-card-field').hide();
              $('#mod-record').form('clear');
            },
            onApprove: function () {
              $('#mod-record').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改消费记录信息提交
      $('#mod-record').form({
        on: 'submit',
        inline: true,
        fields: {
          modConsumerName: {
            identifier: 'consumerName',
            rules: [{
              type: 'empty',
              prompt: '消费人姓名不能为空'
            }]
          },
          modMobile: {
            identifier: 'mobile',
            rules: [{
              type: 'empty',
              prompt: '消费人手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            }, {
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          modSex: {
            identifier: 'sex',
            rules: [{
              type: 'empty',
              prompt: '性别不能为空'
            }]
          },
          modChargeWay: {
            identifier: 'chargeWay',
            rules: [{
              type: 'empty',
              prompt: '支付方式不能为空'
            }]
          },
          modCharge: {
            identifier: 'charge',
            rules: [{
              type: 'empty',
              prompt: '支付金额不能为空'
            }, {
              type: 'decimal',
              prompt: '请输入数字'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'record update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-record-id').text() != '') {
            alert('ID为空');
            return false;
          }
          settings.data.id = $('#mod-record-id').text();
          if(settings.data.chargeWay == 1 && settings.data.chargeCard == ''){
            alert('请选择会员卡！');
            return false;
          }
          if($('#project-list').children().length == 0){
            alert('项目为空!');
            return false;
          }
          var projects = '';
          $('#mod-project-list').children().each(function(){
            var pid = $(this).find('.projectId').text();
            var eid = $(this).find('.employeeId').text();
            var pcharge = $(this).find('.charge').text();
            var counselor = $(this).find('.mod-record-counselor-select select').val();
            if(counselor == ''){
              counselor = 0;
            }
            projects += ';' + pid + ',' + eid + ',' + pcharge  + ',' + counselor;
          })
          projects = projects.substring(1);
          settings.data.projects = projects;
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            clearCounselor();
            clearCard();
            $('#mod-project-list').empty();
            $('#mod-card-field').hide();
            $('#mod-record').form('clear');
            $('.mod-record-modal').modal('hide');
            loadSearchRecordList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //导出消费记录模态框
      $('.export-record').on('click', function () {
        $('.export-record-modal').modal({
            closable: false,
            onDeny: function () {
              $('#export-record').form('clear');
            },
            onApprove: function () {
              $('#export-record').submit();
              return false;
            }
          })
          .modal('show');
      })
      //导出消费记录信息
      // $(document).on('click','#export-record',function(){
      //   $('#export-button').attr('href')
      // })
      $('#export-record').form({
        on: 'submit',
        inline: true,
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          var href = "http://localhost:8080/consume/export";
          var startTime = $('#startTime').val() == '' ? '' : toTimeStamp($('#startTime').val());
          var endTime = $('#endTime').val() == '' ? '' : toTimeStamp($('#endTime').val());
          verifyToken();
          href = href +'?startTime='+ startTime + '&endTime=' + endTime + '&token=' + getSessionStorage('token');
          window.location.href = href;
        }
      })

      $(document).on('click','.print-record',function(){
        window.open('printTemplet.html');
      })
      
      function loadCounselorsData() {
        $.each(counselorsData, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.emName + '</option>');
          $('.new-record-counselor-select select').append($option);
        })
      }

      function loadProjectTypeByTopType(topType, $select, type, info) {
        $('.fake-button').api({
          action: 'projectType listByTopType',
          method: 'GET',
          on: 'now',
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            settings.data.topType = topType;
            return settings;
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $select.find('option:not(:first)').remove();
              $.each(response.data, function (i, data) {
                var $option = $('<option value="' + data.id + '">' + data.typeName + '</option>');
                $select.append($option);
              })
            }

            $select.val('');
            $select.parent().find('.text').addClass('default');
            $select.parent().find('.text').text('请选择美容项目分类');
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      function loadProjectByProjectType(typeId, $select, type, info) {
        $('.fake-button').api({
          action: 'project listByProjectType',
          method: 'GET',
          on: 'now',
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            settings.data.typeId = typeId;
            return settings;
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $select.find('option:not(:first)').remove();
              $.each(response.data, function (i, data) {
                var $option = $('<option value=' + data.id + '>' + data.projectName + '</option>');
                $select.append($option);
              })
            }
            $select.val('');
            $select.parent().find('.text').addClass('default');
            $select.parent().find('.text').text('请选择美容项目');
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      function loadEmployeeByProject(projectId, $select, type, info) {
        $('.fake-button').api({
          action: 'appointment listEmployeesByProject',
          method: 'GET',
          on: 'now',
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            settings.data.projectId = projectId;
            return settings;
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $select.find('option:not(:first)').remove();
              $.each(response.data, function (i, data) {
                var $option = $('<option value="' + data.employeeId + '">' + data.employeeName +
                  '</option>');
                $select.append($option);
              })
            }

            $select.val('');
            $select.parent().find('.text').addClass('default');
            $select.parent().find('.text').text('请选择操作员');
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      function loadProjectCharge(projectId) {
        $('.fake-button').api({
          action: 'project query',
          method: 'GET',
          on: 'now',
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            settings.data.id = projectId;
            return settings;
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#projectCharge').val(response.data.retailPrice);
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      function clearSelect() {
        $('.new-record-type-select select').find('option:not(:first)').remove();
        $('.new-record-type-select .text').text('');
        $('.new-record-project-select select').find('option:not(:first)').remove();
        $('.new-record-project-select .text').text('');
        $('.new-record-employee-select select').find('option:not(:first)').remove();
        $('.new-record-employee-select .text').text('');
      }

      function clearCounselor(){
        $('.new-record-counselor-select select').find('option:not(:first)').remove();
        $('.new-record-counselor-select .text').text('请选择经理/咨询师');
        $('.new-record-card-select .text').addClass('unselected');
      }
      
      function clearCard(){
        $('.new-record-card-select select').find('option:not(:first)').remove();
        $('.new-record-card-select .text').text('请选择会员卡');
        $('.new-record-card-select .text').addClass('unselected');
      }

      function changeCharge(){
        var charge = 0;
        $('#project-list').find('.charge').each(function(){
          charge += Number($(this).text());
        })
        $('#finalCharge').val(charge);
      }
    })
  </script>
</body>

</html>