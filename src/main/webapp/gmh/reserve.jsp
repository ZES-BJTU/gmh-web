<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  <!DOCTYPE html>
  <html>

  <head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>光美焕-预约管理</title>
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
    <button class="ui blue button load-appointment-list" style="display:none;"></button>
    <jsp:include page="header.jsp" />
    <div class="main-wrapper">
      <div class="ui fluid container">
        <div class="ui grid">
          <div class="row">
            <div class="twelve wide left aligned column">
              <form id="" class="ui form search appointment-search">
                <div class="ui action input">
                  <input type="text" class="search-input" placeholder="请输入搜索内容">
                  <button class="ui button submit">搜索</button>
                </div>
              </form>
            </div>
            <div class="four wide right aligned column">
              <button class="ui blue button new-appointment">新建</button>
            </div>
          </div>
          <div class="one column row">
            <div class="column">
              <table class="ui compact table theme">
                <thead>
                  <tr>
                    <th>预约人</th>
                    <th>联系方式</th>
                    <!-- <th>顶级分类</th> -->
                    <!-- <th>美容项目分类</th> -->
                    <th>美容项目</th>
                    <th>操作员</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>是否点排</th>
                    <th>状态</th>
                    <th>备注</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody id="appointment-list">
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
    <div class="ui small coupled modal new-appointment-modal">
      <div class="header">新建预约</div>
      <div class="content">
        <form id="new-appointment" class="ui form">
          <div class="two fields">
            <div class="field">
              <label>预约人手机号</label>
              <input type="text" name="phone" placeholder="请输入预约人手机号">
            </div>
            <div class="field">
              <label>预约人姓名</label>
              <input type="text" name="name" placeholder="请输入预约人姓名">
            </div>
          </div>
          <div class="two fields">
            <div class="field">
              <label>预约人性别</label>
              <select name="sex" class="ui fluid dropdown new-appointment-sex-select">
              <option value="">请选择性别</option>
              <option value="0">女</option>
              <option value="1">男</option>
            </select>
            </div>
            <div class="field">
              <label>是否点排</label>
              <select name="line" class="ui fluid dropdown new-appointment-line-select">
              <option value="">请选择是否点排</option>
              <option value="1">是</option>
              <option value="0">否</option>
            </select>
            </div>
          </div>
          <table class="ui compact table theme">
            <thead>
              <tr>
                <!-- <th>顶级分类</th> -->
                <!-- <th>美容项目分类</th> -->
                <th>美容项目</th>
                <th>操作员</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th class="add-project"><i class="plus icon"></i></th>
              </tr>
            </thead>
            <tbody id="project-list">
            </tbody>
          </table>
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
    <div class="ui small coupled modal mod-appointment-modal">
      <div class="header">修改预约</div>
      <div class="content">
        <span id="mod-appointment-id" style="display:none"></span>
        <form id="mod-appointment" class="ui form">
          <div class="two fields">
            <div class="field">
              <label>预约人手机号</label>
              <input type="text" name="phone" placeholder="请输入预约人手机号">
            </div>
            <div class="field">
              <label>预约人姓名</label>
              <input type="text" name="name" placeholder="请输入预约人姓名">
            </div>
          </div>
          <div class="two fields">
            <div class="field">
              <label>预约人性别</label>
              <select name="sex" class="ui fluid dropdown mod-appointment-sex-select">
              <option value="">请选择性别</option>
              <option value="0">女</option>
              <option value="1">男</option>
            </select>
            </div>
            <div class="field">
              <label>是否点排</label>
              <select name="line" class="ui fluid dropdown mod-appointment-line-select">
              <option value="">请选择是否点排</option>
              <option value="1">是</option>
              <option value="0">否</option>
            </select>
            </div>
          </div>
          <table class="ui compact table theme">
            <thead>
              <tr>
                <!-- <th>顶级分类</th> -->
                <!-- <th>美容项目分类</th> -->
                <th>美容项目</th>
                <th>操作员</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th class="mod-add-project"><i class="plus icon"></i></th>
              </tr>
            </thead>
            <tbody id="mod-project-list">
            </tbody>
          </table>
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
    <!-- <div class="ui tiny coupled modal mod-appointment-modal">
      <div class="header">修改预约</div>
      <div class="content">
        <span id="mod-appointment-id" style="display:none"></span>
        <div class="ui grid">
          <div class="ten wide column">
            <form id="mod-appointment" class="ui form">
              <div class="field">
                <label>预约人手机号</label>
                <input type="text" name="phone" placeholder="请输入预约人手机号">
              </div>
              <div class="field">
                <label>预约人姓名</label>
                <input type="text" name="name" placeholder="请输入预约人姓名">
              </div>
              <div class="field">
                <label>预约人性别</label>
                <select name="sex" class="ui fluid dropdown mod-appointment-sex-select">
                <option value="">请选择性别</option>
                <option value="0">女</option>
                <option value="1">男</option>
              </select>
              </div>
              <div class="field">
                <label>顶级分类</label>
                <select name="topTypeId" class="ui fluid dropdown mod-appointment-top-type-select">
                <option value="">请选择顶级分类</option>
              </select>
              </div>
              <div class="field">
                <label>美容项目分类</label>
                <select name="projectTypeId" class="ui fluid dropdown mod-appointment-type-select">
                <option value="">请选择美容项目分类</option>
              </select>
              </div>
              <div class="field">
                <label>美容项目</label>
                <select name="projectId" class="ui fluid dropdown mod-appointment-project-select">
                <option value="">请选择美容项目</option>
              </select>
              </div>
              <div class="field">
                <label>操作员</label>
                <select name="employeeId" class="ui fluid dropdown mod-appointment-employee-select">
                <option value="">请选择操作员</option>
              </select>
              </div>
              <div class="field">
                <label>开始时间</label>
                <input type="text" name="beginTime" value="" id="modBeginTime" placeholder="请输入开始时间">
              </div>
              <div class="field">
                <label>结束时间</label>
                <input type="text" name="endTime" value="" id="modEndTime" placeholder="请输入结束时间">
              </div>
              <div class="field">
                <label>是否点排</label>
                <select name="line" class="ui fluid dropdown mod-appointment-line-select">
                <option value="">请选择是否点排</option>
              </select>
              </div>
              <div class="field">
                <label>备注</label>
                <textarea name="remark" rows="3"></textarea>
              </div>
            </form>
          </div>
          <div class="six wide column">
            <form id="check-reserve-time" class="ui form">
              <div class="field">
                <label>查询美容师时间</label>
                <input type="text" name="reserveTime" value="" id="modReserveTime" placeholder="请输入日期">
              </div>
              <div class="ui fluid submit button mod-check-reserve-btn">查询</div>
            </form>
            <div id="mod-reserve-time" class="reserve-time"></div>
          </div>
        </div>
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
    </div> -->
    <div class="ui tiny coupled modal add-project-modal">
      <span id="add-project-type" style="display:none"></span>
      <div class="header">添加项目</div>
      <div class="content">
        <div class="ui grid">
          <div class="ten wide column">
            <form id="new-appointment-add-project" class="ui form">
              <div class="field">
                <label>开始时间</label>
                <input type="text" name="beginTime" value="" id="beginTime" placeholder="请输入开始时间">
              </div>
              <div class="field">
                <label>结束时间</label>
                <input type="text" name="endTime" value="" id="endTime" placeholder="请输入结束时间">
              </div>
              <div class="field">
                <label>顶级分类</label>
                <select name="topTypeId" class="ui fluid dropdown new-appointment-top-type-select">
                <option value="">请选择顶级分类</option>
                <option value="1">美甲</option>
                <option value="2">美睫</option>
                <option value="3">美容</option>
                <option value="4">产品</option>
              </select>
              </div>
              <div class="field">
                <label>美容项目分类</label>
                <select name="projectTypeId" class="ui fluid dropdown new-appointment-type-select">
                <option value="">请选择美容项目分类</option>
              </select>
              </div>
              <div class="field">
                <label>美容项目</label>
                <select name="projectId" class="ui fluid dropdown new-appointment-project-select">
                <option value="">请选择美容项目</option>
              </select>
              </div>
              <div class="field">
                <label>操作员</label>
                <select name="employeeId" class="ui fluid dropdown new-appointment-employee-select">
                <option value="">请选择操作员</option>
              </select>
              </div>
            </form>
          </div>
          <div class="six wide column">
            <form id="check-reserve-time" class="ui form">
              <div class="field">
                <label>查询美容师时间</label>
                <input type="text" name="reserveTime" value="" id="newReserveTime" placeholder="请输入日期">
              </div>
              <div class="ui fluid submit button new-check-reserve-btn">查询</div>
            </form>
            <div id="new-reserve-time" class="reserve-time"></div>
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

    <div class="ui mini modal finish-appointment-modal">
      <div class="header">完成预约</div>
      <div class="content">
        <span id="finish-appointment-id" style="display:none"></span>
        <form id="finish-appointment" class="ui form">
          <div class="field">
            <label>操作员</label>
            <input type="text" name="operator" placeholder="请输入操作员" disabled="">
          </div>
          <div class="field">
            <label>经理/咨询师</label>
            <select name="counselorId" class="ui fluid dropdown new-appointment-counselor-select">
            <option value="">请选择经理/咨询师</option>
          </select>
          </div>
          <div class="field">
            <label>美容项目</label>
            <input type="text" name="projectName" placeholder="请输入美容项目" disabled="">
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
            <input type="text" name="charge" id="charge" placeholder="" disabled="">
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
    <div class="ui basic tiny modal del-appointment-modal">
      <div class="ui icon header">
        <i class="trash icon"></i>确认删除？
      </div>
      <div class="content">
        <span id="del-appointment-id" style="display:none"></span>
      </div>
      <div class="actions delete-action">
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
      activeMenu('reserve');

      // 存储搜索的信息,用于点击页码时调用
      var searchInfo = '';
      var searchTopTypeId = '';
      var searchProjectTypeId = '';
      var projectTypeData = [];
      var projectData = [];
      var employeeData = [];
      var counselorsData = [];
      $(document).ready(function () {
        $('#beginTime').datetimepicker({
          language: 'zh-CN',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#beginTime').focus();
          $('#beginTime').blur();
        });
        $('#endTime').datetimepicker({
          language: 'zh-CN',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#endTime').focus();
          $('#endTime').blur();
        });
        $('#modBeginTime').datetimepicker({
          language: 'zh-CN',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#beginTime').focus();
          $('#beginTime').blur();
        });
        $('#modEndTime').datetimepicker({
          language: 'zh-CN',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#endTime').focus();
          $('#endTime').blur();
        });
        $('#newReserveTime').datetimepicker({
          language: 'zh-CN',
          format: 'yyyy-mm-dd',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          minView: 2,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#newReserveTime').focus();
          $('#newReserveTime').blur();
        });
        $('#modReserveTime').datetimepicker({
          language: 'zh-CN',
          format: 'yyyy-mm-dd',
          weekStart: 1,
          todayBtn: 1,
          autoclose: 1,
          minView: 2,
          todayHighlight: 1,
          startView: 2,
          forceParse: true,
          showMeridian: 1
        }).on('changeDate', function (ev) {
          $('#modReserveTime').focus();
          $('#modReserveTime').blur();
        });
        //加载全部美容项目分类
        $('.fake-button').api({
          action: 'projectType getAll',
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
              projectTypeData = response.data;
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })

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
        $(document).on('change', '.new-appointment-top-type-select', function () {
          var topType = $(this).find('select').val();
          if (topType == '') {
            return;
          }
          loadProjectTypeByTopType(topType, $('.new-appointment-type-select select'), 'new', null);
          $('.new-appointment-project-select select').find('option:not(:first)').remove();
          $('.new-appointment-project-select .text').text('');
          $('.new-appointment-project-select select').val('');
          $('.new-appointment-project-select select').parent().find('.text').addClass('default');
          $('.new-appointment-project-select select').parent().find('.text').text('请选择美容项目');
          $('.new-appointment-employee-select select').find('option:not(:first)').remove();
          $('.new-appointment-employee-select .text').text('');
          $('.new-appointment-employee-select select').val('');
          $('.new-appointment-employee-select select').parent().find('.text').addClass('default');
          $('.new-appointment-employee-select select').parent().find('.text').text('请选择操作员');
        })
        //新增时，点击美容项目分类，切换美容项目
        $(document).on('change', '.new-appointment-type-select', function () {
          var typeId = $(this).find('select').val();
          if (typeId == '') {
            return;
          }
          loadProjectByProjectType(typeId, $('.new-appointment-project-select select'), 'new', null);
        })
        //新增时，点击美容项目，切换操作员
        $(document).on('change', '.new-appointment-project-select', function () {
          var projectId = $(this).find('select').val();
          if (projectId == '') {
            return;
          }
          loadEmployeeByProject(projectId, $('.new-appointment-employee-select select'), 'new', null);
        })

        //项目删除
        $(document).on('click', '.minus-project', function () {
          $(this).parent().remove();
        })

        //预约搜索
        $('.appointment-search').form({
          onSuccess: function (e) {
            //阻止表单的提交
            e.preventDefault();
            searchInfo = $('.search-input').val();
            loadSearchAppointmentList(1, 10, 'search');
          }
        })

        $('.appointment-search').form('submit');

        //分页按钮点击事件
        $(document).on('click', '.paging .item', function () {
          loadSearchAppointmentList($(this).text(), 10, 'paging');
        })

        function loadSearchAppointmentList(pagenum, pagesize, type) {
          $('.load-appointment-list').api({
            action: 'appointment search',
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
                return settings;
              } else {
                settings.data.searchString = searchInfo;
                return settings;
              }
            },
            onSuccess: function (response) {
              if (response.error != null) {
                alert(response.error);
                verifyStatus(response.code);
              } else {
                $('#appointment-list').empty();
                $('.paging').empty();
                for (var i = 0; i < response.data.totalPages; i++) {
                  var $item = $('<a class="item">' + (i + 1) + '</a>');
                  $('.paging').append($item);
                }
                $.each(response.data.data, function (i, data) {
                  var $tr = $('<tr></tr>');
                  var $id = $('<td class="appointmentId" style="display:none">' + data.id + '</td>');
                  var $memberId = $('<td class="memberId" style="display:none">' + data.memberId +
                    '</td>');
                  var $memberName = $('<td class="memberName">' + data.memberName + '</td>');
                  var $memberPhone = $('<td class="memberPhone">' + data.phone + '</td>');
                  var $sexId = $('<td class="sexId" style="display:none">' + (data.sex == '男' ? 1 : 0) +
                    '</td>');
                  var $sex = $('<td class="sex" style="display:none">' + data.sex + '</td>');
                  var $topTypeId = $('<td class="topTypeId" style="display:none">' + data.topType +
                    '</td>');
                  var $topTypeName = $('<td class="topTypeName" style="display:none">' + data.topTypeName +
                    '</td>');
                  var $typeId = $('<td class="typeId" style="display:none">' + data.typeId + '</td>');
                  var $typeName = $('<td class="typeName" style="display:none">' + data.typeName +
                    '</td>');
                  var $projectId = $('<td class="projectId" style="display:none">' + data.projectId +
                    '</td>');
                  var $projectName = $('<td class="projectName">' + data.projectName + '</td>');
                  var $employeeId = $('<td class="employeeId" style="display:none">' + data.employeeId +
                    '</td>');
                  var $employeeName = $('<td class="employeeName">' + data.employeeName + '</td>');
                  var $beginTime = $('<td class="beginTime">' + toDatetimeMin(data.beginTime) + '</td>');
                  var $endTime = $('<td class="endTime">' + toDatetimeMin(data.endTime) + '</td>');
                  var $line = $('<td class="line">' + data.line + '</td>');
                  var $status = $('<td class="status">' + data.status + '</td>');
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
                  var $projectCharge = $('<td class="projectCharge" style="display:none">' + data.projectCharge +
                    '</td>');
                  var $operate = $(
                    '<td><button class="ui tiny green button finish-appointment">完成</button>' +
                    '<button class="ui tiny orange button mod-appointment">修改</button>' +
                    '<button class="ui tiny red button del-appointment">取消</button></td>');

                  $tr.append($id);
                  $tr.append($memberId);
                  $tr.append($memberName);
                  $tr.append($memberPhone);
                  $tr.append($sex);
                  $tr.append($topTypeId);
                  $tr.append($topTypeName);
                  $tr.append($typeId);
                  $tr.append($typeName);
                  $tr.append($projectId);
                  $tr.append($projectName);
                  $tr.append($employeeId);
                  $tr.append($employeeName);
                  $tr.append($beginTime);
                  $tr.append($endTime);
                  $tr.append($line);
                  $tr.append($status);
                  $tr.append($remark);
                  $tr.append($projectCharge);
                  $tr.append($operate);
                  $('#appointment-list').append($tr);
                })
                $('.paging').children().eq(pagenum - 1).addClass('active');
              }
              // addData();
            },
            onFailure: function (response) {
              alert('服务器开小差了');
            }
          })
        }

        //允许模态框叠加
        $('.coupled.modal').modal({
          allowMultiple: true
        })

        //添加项目模态框
        $('.add-project-modal')
          .modal('attach events', '.new-appointment-modal .add-project')
          .modal('attach events', '.mod-appointment-modal .mod-add-project')
          .modal('setting', 'closable', false)
          .modal({
            onDeny: function () {
              $('#newReserveTime').val('');
              $('#add-project-type').text('');
              $('#new-reserve-time').empty();
              $('#new-appointment-add-project').form('clear');
            },
            onApprove: function () {
              $('#new-appointment-add-project').submit();
              return false;
            }
          })

        $('#new-appointment-add-project').form({
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
            },
            newBeginTime: {
              identifier: 'beginTime',
              rules: [{
                type: 'empty',
                prompt: '开始时间不能为空'
              }]
            },
            newEndTime: {
              identifier: 'endTime',
              rules: [{
                type: 'empty',
                prompt: '结束时间不能为空'
              }]
            }
          },
          onSuccess: function (e) {
            //阻止表单的提交
            e.preventDefault();
            var beginTime = $('#beginTime').val();
            var endTime = $('#endTime').val();
            var projectId = $('.new-appointment-project-select select').val();
            var projectName = $('.new-appointment-project-select .text').text();
            var employeeId = $('.new-appointment-employee-select select').val();
            var employeeNmame = $('.new-appointment-employee-select .text').text();

            var $tr = $('<tr><td style="display:none" class="projectId">' + projectId + '</td><td>' + projectName +
              '</td><td style="display:none" class="employeeId">' + employeeId + '</td><td>' + employeeNmame + '</td><td class="beginTime">' +
              beginTime + '</td><td class="endTime">' + endTime + '</td><td class="minus-project"><i class="minus icon"></i></td></tr>');
            if($('#add-project-type').text() == 'add'){
              $('#project-list').append($tr);
            }else{
              $('#mod-project-list').append($tr);
            }
            $('#newReserveTime').val('');
            $('#add-project-type').text('');
            $('#new-reserve-time').empty();
            $('#new-appointment-add-project').form('clear');
            $('.add-project-modal').modal('hide');
          }
        })

        //新建预约模态框
        $('.new-appointment').on('click', function () {
          $('.new-appointment-modal').modal({
              closable: false,
              onDeny: function () {
                $('#project-list').empty();
                $('#new-appointment').form('clear');
              },
              onApprove: function () {
                $('#new-appointment').submit();
                return false;
              }
            })
            .modal('show');
        })
        //新建预约信息提交
        $('#new-appointment').form({
          on: 'submit',
          inline: true,
          fields: {
            newPhone: {
              identifier: 'phone',
              rules: [{
                type: 'empty',
                prompt: '预约人手机号不能为空'
              }, {
                type: 'number',
                prompt: '请输入正确的手机号'
              }, {
                type: 'exactLength[11]',
                prompt: '请输入11位手机号'
              }]
            },
            newName: {
              identifier: 'name',
              rules: [{
                type: 'empty',
                prompt: '预约人姓名不能为空'
              }]
            },
            newSex: {
              identifier: 'sex',
              rules: [{
                type: 'empty',
                prompt: '预约人性别不能为空'
              }]
            },
            newLine: {
              identifier: 'line',
              rules: [{
                type: 'empty',
                prompt: '是否点排不能为空'
              }]
            }
          },
          onSuccess: function (e) {
            //阻止表单的提交
            e.preventDefault();
          }
        }).api({
          action: 'appointment insert',
          method: 'POST',
          serializeForm: true,
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            if($('#project-list').children().length == 0){
              alert('请添加项目!');
              return false;
            }
            var projects = '';
            $('#project-list').children().each(function(){
              var pid = $(this).find('.projectId').text();
              var eid = $(this).find('.employeeId').text();
              var btime = toTimeStamp($(this).find('.beginTime').text());
              var etime = toTimeStamp($(this).find('.endTime').text());
              projects += ';' + pid + ',' + eid + ',' + btime + ',' + etime;
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
              $('#project-list').empty();
              $('#new-appointment').form('clear');
              $('.new-appointment-modal').modal('hide');
              loadSearchAppointmentList(1, 10, 'search');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          },
        });

        //修改预约模态框
        $(document).on('click', '.mod-appointment', function () {
          $('#mod-appointment-id').text($(this).parent().parent().find('.appointmentId').text());
          $('#mod-appointment').find('input[name="phone"]').val($(this).parent().parent().find('.memberPhone').text());
          $('#mod-appointment').find('input[name="name"]').val($(this).parent().parent().find('.memberName').text());
          $('.mod-appointment-sex-select select').val($(this).parent().parent().find('.sexId').text());
          $('.mod-appointment-sex-select .text').removeClass('default');
          $('.mod-appointment-sex-select .text').text($(this).parent().parent().find('.sex').text());
          var line = $(this).parent().parent().find('.line').text();
          if (line == '是') {
            $('.mod-appointment-line-select select').val(1);
          } else {
            $('.mod-appointment-line-select select').val(0);
          }
          $('.mod-appointment-line-select .text').removeClass('default');
          $('.mod-appointment-line-select .text').text($(this).parent().parent().find('.line').text());

          var pids = $(this).parent().parent().find('.projectId').html().split(',');
          var pnames = $(this).parent().parent().find('.projectName').html().split('<br>');
          var eids = $(this).parent().parent().find('.employeeId').html().split(',');
          var enames = $(this).parent().parent().find('.employeeName').html().split('<br>');
          var btimes = $(this).parent().parent().find('.beginTime').html().split('<br>');
          var etimes = $(this).parent().parent().find('.endTime').html().split('<br>');

          //加载多个项目
          for(var i = 0; i < pids.length; i++){
            var $tr = $('<tr><td style="display:none" class="projectId">' + pids[i] + '</td><td>' + pnames[i] +
                '</td><td style="display:none" class="employeeId">' + eids[i] + '</td><td>' + enames[i] + '</td><td class="beginTime">' +
                  btimes[i] + '</td><td class="endTime">' + etimes[i] + '</td><td class="minus-project"><i class="minus icon"></i></td></tr>');
            $('#mod-project-list').append($tr);
          }

          $('#mod-appointment').find('textarea[name="remark"]').val($(this).parent().parent().find('.remark').attr('title'));
          $('.mod-appointment-modal').modal({
              closable: false,
              onDeny: function () {
                $('#mod-appointment').form('clear');
                $('#mod-project-list').empty();
                $('#mod-appointment-id').text('');
              },
              onApprove: function () {
                $('#mod-appointment').submit();
                return false;
              }
            })
            .modal('show');
        })
        //修改预约信息提交
        $('#mod-appointment').form({
          on: 'submit',
          inline: true,
          fields: {
            modPhone: {
              identifier: 'phone',
              rules: [{
                type: 'empty',
                prompt: '预约人手机号不能为空'
              }, {
                type: 'number',
                prompt: '请输入正确的手机号'
              }, {
                type: 'exactLength[11]',
                prompt: '请输入11位手机号'
              }]
            },
            modName: {
              identifier: 'name',
              rules: [{
                type: 'empty',
                prompt: '预约人姓名不能为空'
              }]
            },
            modSex: {
              identifier: 'name',
              rules: [{
                type: 'empty',
                prompt: '预约人性别不能为空'
              }]
            },
            modLine: {
              identifier: 'line',
              rules: [{
                type: 'empty',
                prompt: '是否点排不能为空'
              }]
            }
          },
          onSuccess: function (e) {
            //阻止表单的提交
            e.preventDefault();
          }
        }).api({
          action: 'appointment update',
          method: 'POST',
          serializeForm: true,
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            if ($('#mod-appointment-id').text() != '') {
              settings.data.id = $('#mod-appointment-id').text();
              if($('#project-list').children().length == 0){
                alert('请添加项目!');
                return false;
              }
              var projects = '';
              $('#project-list').children().each(function(){
                var pid = $(this).find('.projectId').text();
                var eid = $(this).find('.employeeId').text();
                var btime = toTimeStamp($(this).find('.beginTime').text());
                var etime = toTimeStamp($(this).find('.endTime').text());
                projects += ';' + pid + ',' + eid + ',' + btime + ',' + etime;
              })
              projects = projects.substring(1);
              settings.data.projects = projects;
              return settings;
            } else {
              alert('ID为空');
              return false;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#project-list').empty();
              $('#mod-appointment-id').text('');
              $('#mod-appointment').form('clear');
              $('.mod-appointment-modal').modal('hide');
              loadSearchAppointmentList(1, 10, 'search');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        });

        //删除预约模态框
        $(document).on('click', '.del-appointment', function () {
          $('#del-appointment-id').text($(this).parent().parent().find('.appointmentId').text())
          $('.del-appointment-modal').modal({
              closable: false,
              onDeny: function () {
                $('#del-appointment-id').text('');
              },
              onApprove: function () {
                $('.fake-button').api({
                  action: 'appointment cancel',
                  method: 'POST',
                  on: 'now',
                  beforeXHR: function (xhr) {
                    verifyToken();
                    xhr.setRequestHeader('X-token', getSessionStorage('token'));
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                  },
                  beforeSend: function (settings) {
                    if ($('#del-appointment-id').text() != '') {
                      settings.data.id = $('#del-appointment-id').text();
                      return settings;
                    } else {
                      alert('ID为空');
                      return false;
                    }
                  },
                  onSuccess: function (response) {
                    if (response.error != null) {
                      alert(response.error);
                      verifyStatus(response.code);
                    } else {
                      $('#del-appointment-id').text('');
                      $('.del-appointment-modal').modal('hide');
                      loadSearchAppointmentList(1, 10, 'search');
                    }
                  },
                  onFailure: function (response) {
                    alert('服务器开小差了');
                  }
                })
                return false;
              }
            })
            .modal('show');
        })

        //完成预约模态框
        $(document).on('click', '.finish-appointment', function () {
          loadCounselorsData();
          $('#finish-appointment-id').text($(this).parent().parent().find('.appointmentId').text());
          $('#finish-appointment').find('input[name="operator"]').val($(this).parent().parent().find(
            '.employeeName').text());
          $('#finish-appointment').find('input[name="projectName"]').val($(this).parent().parent().find(
            '.projectName').text());
          $('#finish-appointment').find('input[name="projectCharge"]').val($(this).parent().parent().find(
            '.projectCharge').text());
          $('#finish-appointment').find('input[name="charge"]').val($(this).parent().parent().find(
            '.projectCharge').text());
          $('.finish-appointment-modal').modal({
              closable: false,
              onDeny: function () {
                $('#finish-appointment').form('clear');
                $('#finish-appointment-id').text('');
              },
              onApprove: function () {
                $('#finish-appointment').submit();
                return false;
              }
            })
            .modal('show');
        })

        //完成预约信息提交
        $('#finish-appointment').form({
          on: 'submit',
          inline: true,
          fields: {
            chargeWay: {
              identifier: 'chargeWay',
              rules: [{
                type: 'empty',
                prompt: '支付方式不能为空'
              }]
            },
            discount: {
              identifier: 'discount',
              rules: [{
                type: 'regExp',
                value: /^(|[0-9]{1,2})$/,
                prompt: '请输入正确的折扣'
              }]
            },
            charge: {
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
          action: 'appointment finish',
          method: 'POST',
          serializeForm: true,
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getSessionStorage('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            if ($('#finish-appointment-id').text() != '') {
              settings.data.charge = $('#charge').val();
              settings.data.id = $('#finish-appointment-id').text();
              return settings;
            } else {
              alert('ID为空');
              return false;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#finish-appointment-id').text('');
              $('#finish-appointment').form('clear');
              $('.finish-appointment-modal').modal('hide');
              loadSearchAppointmentList(1, 10, 'search');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        });

        //开始预约信息提交
        // $(document).on('click', '.start-appointment', function () {
        //   var appointmentId = $(this).parent().parent().find('.appointmentId').text();
        //   $('.fake-button').api({
        //     action: 'appointment start',
        //     method: 'POST',
        //     on: 'now',
        //     beforeXHR: function (xhr) {
        //       verifyToken();
        //       xhr.setRequestHeader('X-token', getSessionStorage('token'));
        //       xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        //     },
        //     beforeSend: function (settings) {
        //       if (appointmentId != '') {
        //         settings.data.id = appointmentId;
        //         return settings;
        //       } else {
        //         alert('ID为空');
        //         return false;
        //       }
        //     },
        //     onSuccess: function (response) {
        //       if (response.error != null) {
        //         alert(response.error);
        //         verifyStatus(response.code);
        //       } else {
        //         loadSearchAppointmentList(1, 10, 'search');
        //       }
        //     },
        //     onFailure: function (response) {
        //       alert('服务器开小差了');
        //     }
        //   })
        // })

        //区分是新增预约还是修改预约
        $(document).on('click','.add-project',function(){
          $('#add-project-type').text('add');
        })
        $(document).on('click','.mod-add-project',function(){
          $('#add-project-type').text('mod');
        })

        //新增预约时查询操作员时间
        $(document).on('click', '.new-check-reserve-btn', function () {
          var time = $('#newReserveTime').val() == '' ? '' : toTimeStamp($('#newReserveTime').val());
          var employeeId = $('.new-appointment-employee-select select').val();
          if (time == '') {
            alert('请选择时间!');
            return;
          }
          if (employeeId == '') {
            alert('请选择操作员!');
            return;
          }
          $('.fake-button').api({
            action: 'appointment queryTime',
            method: 'POST',
            on: 'now',
            beforeXHR: function (xhr) {
              verifyToken();
              xhr.setRequestHeader('X-token', getSessionStorage('token'));
              xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            beforeSend: function (settings) {
              settings.data.time = time;
              settings.data.employeeId = employeeId;
              return settings;
            },
            onSuccess: function (response) {
              if (response.error != null) {
                alert(response.error);
                verifyStatus(response.code);
              } else {
                $('#new-reserve-time').empty();
                $.each(response.data, function (i, data) {
                  var $item = $('<div class="reserve-time-item"></div>');
                  if (Number(data.percent.replace("%", "")) >= 5) {
                    $item.text(data.time);
                  }
                  $item.attr('title', data.time);
                  $item.css('height', data.percent);
                  if (data.type == '空闲') {
                    $item.addClass('free');
                  } else {
                    $item.addClass('not-free');
                  }
                  $('#new-reserve-time').append($item);
                })
              }
            },
            onFailure: function (response) {
              alert('服务器开小差了');
            }
          })
        })

        // function loadTopTypeData() {
        //   var data = [{
        //       'id': 1,
        //       'topTypeName': '美甲'
        //     },
        //     {
        //       'id': 2,
        //       'topTypeName': '美睫'
        //     },
        //     {
        //       'id': 3,
        //       'topTypeName': '美容'
        //     },
        //     {
        //       'id': 4,
        //       'topTypeName': '产品'
        //     }
        //   ];
        //   $.each(data, function (i, data) {
        //     var $option = $('<option value="' + data.id + '">' + data.topTypeName + '</option>');
        //     $('.new-appointment-top-type-select select').append($option);
        //     $('.mod-appointment-top-type-select select').append($option.clone());
        //   })
        // }

        // function loadLineData() {
        //   var data = [{
        //       'lineType': 1,
        //       'lineName': '是'
        //     },
        //     {
        //       'lineType': 0,
        //       'lineName': '否'
        //     }
        //   ];
        //   $.each(data, function (i, data) {
        //     var $option = $('<option value="' + data.lineType + '">' + data.lineName + '</option>');
        //     $('.new-appointment-line-select select').append($option);
        //     $('.mod-appointment-line-select select').append($option.clone());
        //   })
        // }

        function loadCounselorsData() {
          $.each(counselorsData, function (i, data) {
            var $option = $('<option value="' + data.id + '">' + data.emName + '</option>');
            $('.new-appointment-counselor-select select').append($option);
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
                projectTypeData = response.data;
                $.each(response.data, function (i, data) {
                  var $option = $('<option value="' + data.id + '">' + data.typeName + '</option>');
                  $select.append($option);
                })
              }
              if (type == 'load') {
                $select.val(0);
              } else if (type == 'new' || type == 'mod') {
                $select.val('');
                $select.parent().find('.text').addClass('default');
                $select.parent().find('.text').text('请选择美容项目分类');
              } else if (type == 'beforeMod') {
                $select.val(info.projectTypeId);
                $select.parent().find('.text').removeClass('default');
                $select.parent().find('.text').text(info.projectTypeName);
              }
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
                projectData = response.data;
                $.each(response.data, function (i, data) {
                  var $option = $('<option value="' + data.id + '">' + data.projectName + '</option>');
                  $select.append($option);
                })
              }
              if (type == 'new' || type == 'mod') {
                $select.val('');
                $select.parent().find('.text').addClass('default');
                $select.parent().find('.text').text('请选择美容项目');
              } else if (type == 'beforeMod') {
                $select.val(info.projectId);
                $select.parent().find('.text').removeClass('default');
                $select.parent().find('.text').text(info.projectName);
              }
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
                projectTypeData = response.data;
                $.each(response.data, function (i, data) {
                  var $option = $('<option value="' + data.employeeId + '">' + data.employeeName +
                    '</option>');
                  $select.append($option);
                })
              }
              if (type == 'new' || type == 'mod') {
                $select.val('');
                $select.parent().find('.text').addClass('default');
                $select.parent().find('.text').text('请选择操作员');
              } else if (type == 'beforeMod') {
                $select.val(info.employeeId);
                $select.parent().find('.text').removeClass('default');
                $select.parent().find('.text').text(info.employeeName);
              }
            },
            onFailure: function (response) {
              alert('服务器开小差了');
            }
          })
        }

        // function clearSelect() {
        //   $('.new-appointment-top-type-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-top-type-select .text').text('');
        //   $('.mod-appointment-top-type-select select').find('option:not(:first)').remove();
        //   $('.mod-appointment-top-type-select .text').text('');
        //   $('.new-appointment-type-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-type-select .text').text('');
        //   $('.mod-appointment-type-select select').find('option:not(:first)').remove();
        //   $('.mod-appointment-type-select .text').text('');
        //   $('.new-appointment-project-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-project-select .text').text('');
        //   $('.mod-appointment-project-select select').find('option:not(:first)').remove();
        //   $('.mod-appointment-project-select .text').text('');
        //   $('.new-appointment-employee-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-employee-select .text').text('');
        //   $('.mod-appointment-employee-select select').find('option:not(:first)').remove();
        //   $('.mod-appointment-employee-select .text').text('');
        //   $('.new-appointment-line-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-line-select .text').text('');
        //   $('.mod-appointment-line-select select').find('option:not(:first)').remove();
        //   $('.mod-appointment-line-select .text').text('');
        //   $('.new-appointment-counselor-select select').find('option:not(:first)').remove();
        //   $('.new-appointment-counselor-select .text').text('');
        // }
      })

      function addData(){
        var $tr = $('<tr></tr>');
        var $id = $('<td class="appointmentId" style="display:none">1</td>');
        var $memberId = $('<td class="memberId" style="display:none">1</td>');
        var $memberName = $('<td class="memberName">张三</td>');
        var $memberPhone = $('<td class="memberPhone">18813091990</td>');
        var $sexId = $('<td class="sexId" style="display:none">1</td>');
        var $sex = $('<td class="sex" style="display:none">男</td>');
        var pid = [1,2];
        var $projectId = $('<td class="projectId" style="display:none">' + pid + '</td>');
        var pname = ['美容1','美容2'];
        var $projectName = $('<td class="projectName">' + pname.join('<br>') + '</td>');
        var eid = [1,2];
        var $employeeId = $('<td class="employeeId" style="display:none">' + eid + '</td>');
        var ename = ['员工1','员工2'];
        var $employeeName = $('<td class="employeeName">' + ename.join('<br>') + '</td>');
        var btime = ['2010-09-01 12:00','2010-09-01 12:00'];
        var $beginTime = $('<td class="beginTime">' + btime.join('<br>') + '</td>');
        var etime = ['2010-09-01 12:00','2010-09-01 12:00'];
        var $endTime = $('<td class="endTime">' + btime.join('<br>') + '</td>');
        var $line = $('<td class="line">是</td>');
        var $status = $('<td class="status">进行中</td>');

        var $remark = $('<td class="remark" title="无">无</td>');
        var pcharge = [1000,2000];
        var $projectCharge = $('<td class="projectCharge" style="display:none">' + pcharge + '</td>');
        var $operate = $(
          '<td><button class="ui tiny green button finish-appointment">完成</button>' +
          '<button class="ui tiny orange button mod-appointment">修改</button>' +
          '<button class="ui tiny red button del-appointment">取消</button></td>');

        $tr.append($id);
        $tr.append($memberId);
        $tr.append($memberName);
        $tr.append($memberPhone);
        $tr.append($sex);
        $tr.append($projectId);
        $tr.append($projectName);
        $tr.append($employeeId);
        $tr.append($employeeName);
        $tr.append($beginTime);
        $tr.append($endTime);
        $tr.append($line);
        $tr.append($status);
        $tr.append($remark);
        $tr.append($projectCharge);
        $tr.append($operate);
        $('#appointment-list').append($tr);
      }
    </script>
  </body>

  </html>