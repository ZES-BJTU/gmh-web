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
  <div class="ui mini modal new-record-modal">
    <div class="header">新建消费记录</div>
    <div class="content">
      <form id="new-record" class="ui form">
        <div class="field">
          <label>消费人姓名</label>
          <input type="text" name="consumerName" placeholder="请输入消费人姓名">
        </div>
        <div class="field">
          <label>消费人手机号</label>
          <input type="text" name="mobile" placeholder="请输入消费人手机号">
        </div>
        <div class="field">
          <label>性别</label>
          <select name="sex" class="ui fluid dropdown new-record-sex-select">
            <option value="">请选择性别</option>
            <option value="0">女</option>
            <option value="1">男</option>
          </select>
        </div>
        <div class="field">
          <label>顶级分类</label>
          <select name="topTypeId" class="ui fluid dropdown new-record-top-type-select">
            <option value="">请选择顶级分类</option>
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
          <label>经理/咨询师</label>
          <select name="counselorId" class="ui fluid dropdown new-record-counselor-select">
            <option value="">请选择经理/咨询师</option>
          </select>
        </div>
        <div class="field">
          <label>美容项目价格</label>
          <input type="text" name="projectCharge" id="projectCharge" placeholder="请输入美容项目价格" disabled="">
        </div>
        <div class="field">
          <label>支付方式</label>
          <select name="chargeWay" class="ui fluid dropdown finish-appointment-chargeWay-select">
            <option value="">请选择支付方式</option>
            <option value="1">会员卡</option>
            <option value="2">其他</option>
          </select>
        </div>
        <div class="field">
          <label>折扣(%)</label>
          <input type="text" name="discount" id="discount" placeholder="请输入折扣">
        </div>
        <div class="field">
          <label>支付金额</label>
          <input type="text" name="charge" id="charge" placeholder="">
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
        $('.start-time').focus();
        $('.start-time').blur();
      });
      $('.end-time').datetimepicker({
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
        $('.end-Time').focus();
        $('.end-Time').blur();
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

      $(document).on('blur', '#discount', function () {
        if ($(this).val() != '') {
          $('#charge').val(Number($('#projectCharge').val()) * Number($(this).val()) * 0.01);
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
                var $consumerName = $('<td class="consumerName">' + data.consumerName + '</td>');
                var $mobile = $('<td class="mobile">' + data.mobile + '</td>');
                var $consumerType = $('<td class="consumerType">' + data.consumerDesc + '</td>');
                var $age = $('<td class="age">' + (data.age == null ? '无' : data.age) + '</td>');
                var $sex = $('<td class="sex">' + data.sex + '</td>');
                var $employeeName = $('<td class="employeeName">' + data.employeeName + '</td>');
                var $counselorName = $('<td class="counselorName">' + (data.counselorName == null ? '无' : data.counselorName) + '</td>');
                var $projectName = $('<td class="projectName">' + data.projectName + '</td>');
                var $charge = $('<td class="charge">' + data.charge + '</td>');
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

                $tr.append($id);
                $tr.append($memberId);
                $tr.append($consumerName);
                $tr.append($mobile);
                $tr.append($consumerType);
                $tr.append($age);
                $tr.append($sex);
                $tr.append($employeeName);
                $tr.append($counselorName);
                $tr.append($projectName);
                $tr.append($charge);
                $tr.append($chargeWay);
                $tr.append($consumeTime);
                $tr.append($source);
                $tr.append($remark);
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

      //新建消费记录模态框
      $('.new-record').on('click', function () {
        loadTopTypeData();
        loadCounselorsData();
        $('.new-record-modal').modal({
            closable: false,
            onDeny: function () {
              clearSelect();
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
          },
          newProjectCharge: {
            identifier: 'projectCharge',
            rules: [{
              type: 'empty',
              prompt: '美容项目价格不能为空'
            }]
          },
          newChargeWay: {
            identifier: 'chargeWay',
            rules: [{
              type: 'empty',
              prompt: '支付方式不能为空'
            }]
          },
          newDiscount: {
            identifier: 'discount',
            rules: [{
              type: 'regExp',
              value: /^(|[0-9]{1,2})$/,
              prompt: '请输入正确的折扣'
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
          settings.data.charge = $('#charge').val();
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            clearSelect();
            $('#new-record').form('clear');
            $('.new-record-modal').modal('hide');
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
              clearSelect();
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
          var href = "http://123.56.26.101:8080/consume/export";
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

      function loadTopTypeData() {
        var data = [{
            'id': 1,
            'topTypeName': '美甲'
          },
          {
            'id': 2,
            'topTypeName': '美睫'
          },
          {
            'id': 3,
            'topTypeName': '美容'
          },
          {
            'id': 4,
            'topTypeName': '产品'
          }
        ];
        $.each(data, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.topTypeName + '</option>');
          $('.new-record-top-type-select select').append($option);
        })
      }
      
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
              $('#charge').val(response.data.retailPrice);
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      function clearSelect() {
        $('.new-record-top-type-select select').find('option:not(:first)').remove();
        $('.new-record-top-type-select .text').text('');
        $('.new-record-type-select select').find('option:not(:first)').remove();
        $('.new-record-type-select .text').text('');
        $('.new-record-project-select select').find('option:not(:first)').remove();
        $('.new-record-project-select .text').text('');
        $('.new-record-employee-select select').find('option:not(:first)').remove();
        $('.new-record-employee-select .text').text('');
        $('.new-record-counselor-select select').find('option:not(:first)').remove();
        $('.new-record-counselor-select .text').text('');
      }
    })
  </script>
</body>

</html>