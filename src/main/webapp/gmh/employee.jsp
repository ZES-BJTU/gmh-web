<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-员工管理</title>
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
  <button class="ui blue button load-employee-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search employee-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <select class="ui selection dropdown employee-select">
                  <option value="0">全部工种</option>
                  <option value="1">美甲师</option>
                  <option value="2">美睫师</option>
                  <option value="3">美容师</option>
                  <option value="5">咨询师</option>
                  <option value="4">经理</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-employee">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>员工姓名</th>
                  <th>员工性别</th>
                  <th>员工手机号</th>
                  <th>入职日期</th>
                  <th>所属工种</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="employee-list">
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
  <div class="ui mini modal new-employee-modal">
    <div class="header">新建员工</div>
    <div class="content">
      <form id="new-employee" class="ui form">
        <div class="field">
          <label>员工名称</label>
          <input type="text" name="emName" placeholder="请输入员工名称">
        </div>
        <div class="field">
          <label>员工性别</label>
          <select name="sex" class="ui fluid dropdown new-employee-gender-select">
            <option value="">请选择员工性别</option>
            <option value="1">男</option>
            <option value="0">女</option>
          </select>
        </div>
        <div class="field">
          <label>员工手机号</label>
          <input type="text" name="phone" placeholder="请输入员工手机号">
        </div>
        <div class="field">
          <label>入职时间</label>
          <input type="text" name="entryDate" value="" id="entryDate" placeholder="请输入员工入职时间">
        </div>
        
        <div class="field newJobTypeList">
          <label>所属工种</label>
          <input type="text" id="newJobId" name="jobId" value='1' hidden>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='1'>美甲师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='2'>美睫师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='3'>美容师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='5'>咨询师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='4'>经理</label>
          </div>
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
  <div class="ui mini modal mod-employee-modal">
    <div class="header">修改员工</div>
    <div class="content">
      <span id="mod-employee-id" style="display:none"></span>
      <form id="mod-employee" class="ui form">
        <div class="field">
          <label>员工名称</label>
          <input type="text" name="emName" placeholder="请输入员工名称">
        </div>
        <div class="field">
          <label>员工性别</label>
          <select name="sex" class="ui fluid dropdown mod-employee-gender-select" disabled="">
            <option value="">请选择员工性别</option>
            <option value="1">男</option>
            <option value="0">女</option>
          </select>
        </div>
        <div class="field">
          <label>员工手机号</label>
          <input type="text" name="phone" placeholder="请输入员工手机号">
        </div>
        <div class="field">
          <label>入职时间</label>
          <input type="text" id="modEntryDate" name="entryDate" value="" placeholder="请输入员工入职时间" disabled="">
        </div>
        <div class="field modJobTypeList">
          <label>所属工种</label>
          <input type="text" id="modJobId" name="jobId" value='1' hidden>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='1'>美甲师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='2'>美睫师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='3'>美容师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='5'>咨询师</label>
          </div>
          <div class="ui checkbox">
            <input type="checkbox">
            <label data-id='4'>经理</label>
          </div>
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
  <div class="ui basic tiny modal del-employee-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认离职？
    </div>
    <div class="content">
      <span id="del-employee-id" style="display:none"></span>
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
  
    activeMenu('employee');
    
    $('#entryDate').datetimepicker({
      language:  'zh-CN',
      weekStart: 1,
      todayBtn:  1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 2,
      forceParse: true,
      showMeridian: 1
    }).on('changeDate', function(ev){
        $('#entryDate').focus();
        $('#entryDate').blur();
    });
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchJobId = '';
    var shopData = [];
    $(document).ready(function () {
      //加载店铺
      $('.fake-button').api({
        action: 'shop getAll',
        method: 'GET',
        on: 'now',
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
          } else {
            shopData = response.data;
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      })
      //员工管理搜索
      $('.employee-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchJobId = $('.employee-select select').val();
          loadSearchEmployeeList(1, 10, 'search');
        }
      })

      $('.employee-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .item', function () {
        loadSearchEmployeeList($(this).text(), 10, 'paging');
      })

      function loadSearchEmployeeList(pagenum, pagesize, type) {
        $('.load-employee-list').api({
          action: 'employee search',
          method: 'POST',
          serializeForm: true,
          on: 'now',
          data: {
            pageNum: pagenum,
            pageSize: pagesize
          },
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getCookie('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            if (type == 'search') {
              settings.data.searchString = $('.search-input').val();
              settings.data.jobId = $('.employee-select select').val();
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.jobId = searchJobId;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
            } else {
              $('#employee-list').empty();
              $('.paging').empty();
              for (var i = 0; i < response.data.totalPages; i++) {
                var $item = $('<a class="item">' + (i + 1) + '</a>');
                $('.paging').append($item);
              }
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="employeeId" style="display:none">' + data.id + '</td>');
                var $employeeName = $('<td class="employeeName">' + data.emName + '</td>');
                var $sexId = $('<td class="employeeSexId" style="display:none">' + (data.sex == '男' ? 1 : 0) + '</td>');
                var $sex = $('<td class="employeeSex">' + data.sex + '</td>');
                var $phone = $('<td class="employeePhone">' + data.phone + '</td>');
                var $employeeEntryDate = $('<td class="employeeEntryDate">' + toDatetimeMin(data.entryDate) + '</td>');
                var jobVos = [];
                var jobName = '';
                $.each(data.jobVos, function (i, data) {
                  jobVos.push(data.jobType);
                  if (jobName == '') {
                    jobName += data.jobName;
                  } else {
                    jobName += ',' + data.jobName
                  }
                })
                var $jobType = $('<td class="jobType" style="display:none">' + jobVos + '</td>');
                var $jobName = $('<td class="jobName">' + jobName + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-employee">修改</button><button class="ui tiny red button del-employee">离职</button></td>'
                )
                $tr.append($id);
                $tr.append($employeeName);
                $tr.append($sexId);
                $tr.append($sex);
                $tr.append($phone);
                $tr.append($employeeEntryDate);
                $tr.append($jobType);
                $tr.append($jobName);
                $tr.append($operate);
                $('#employee-list').append($tr);
              })
              $('.paging').children().eq(pagenum - 1).addClass('active');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建库存模态框
      $('.new-employee').on('click', function () {
        $('.new-employee-modal').modal({
            closable: false,
            onDeny: function () {
              $('#new-employee').form('clear');
            },
            onApprove: function () {
              $('#new-employee').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建库存信息提交
      $('#new-employee').form({
        on: 'change',
        inline: true,
        fields: {
          newEmName: {
            identifier: 'emName',
            rules: [{
              type: 'empty',
              prompt: '员工姓名不能为空'
            }]
          },
          newSex: {
            identifier: 'sex',
            rules: [{
              type: 'empty',
              prompt: '员工性别不能为空'
            }]
          },
          newPhone: {
            identifier: 'phone',
            rules: [{
              type: 'empty',
              prompt: '员工手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            },{
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          newEntryDate: {
            identifier: 'entryDate',
            rules: [{
              type: 'empty',
              prompt: '员工入职时间不能为空'
            }]
          },
          newJobId: {
            identifier: 'jobId',
            rules: [{
              type: 'empty',
              prompt: '所属工种不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'employee insert',
        method: 'POST',
        serializeForm: true,
        traditional: true,//使用传统方式序列化，保证数组传递
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          var $checkbox = $('.newJobTypeList').find('.checkbox');
          var checkData = [];
          $checkbox.each(function(){
            if($(this).checkbox('is checked')){
              checkData.push(Number($(this).find('label').attr('data-id')));
            }
          })
          settings.data.jobId = checkData;
          settings.data.entryDate = toTimeStamp($('#entryDate').val());
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
          } else {
            $('#new-employee').form('clear');
            $('.new-employee-modal').modal('hide');
            loadSearchEmployeeList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改员工模态框
      $(document).on('click', '.mod-employee', function () {
        $('#mod-employee-id').text($(this).parent().parent().find('.employeeId').text());
        $('#mod-employee').find('input[name="emName"]').val($(this).parent().parent().find('.employeeName').text());
        $('.mod-employee-gender-select select').val($(this).parent().parent().find('.employeeSexId').text());
        $('.mod-employee-gender-select .text').removeClass('default');
        $('.mod-employee-gender-select .text').text($(this).parent().parent().find('.employeeSex').text());
        $('#mod-employee').find('input[name="phone"]').val($(this).parent().parent().find('.employeePhone').text());
        $('#mod-employee').find('input[name="entryDate"]').val($(this).parent().parent().find('.employeeEntryDate').text());
        var jobType = $(this).parent().parent().find('.jobType').text();
        jobType = jobType.split(',');
        for(var i = 0; i<jobType.length;i++){
          $('.modJobTypeList').find('label[data-id='+jobType[i]+']').parent().checkbox('check');
        }
        $('.mod-employee-modal').modal({
            closable: false,
            onDeny: function () {
              $('#mod-employee').form('clear');
              $('#mod-employee-id').text('');
            },
            onApprove: function () {
              $('#mod-employee').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改库存信息提交
      $('#mod-employee').form({
        on: 'change',
        inline: true,
        fields: {
          modEmName: {
            identifier: 'emName',
            rules: [{
              type: 'empty',
              prompt: '员工姓名不能为空'
            }]
          },
          modSex: {
            identifier: 'sex',
            rules: [{
              type: 'empty',
              prompt: '员工性别不能为空'
            }]
          },
          modPhone: {
            identifier: 'phone',
            rules: [{
              type: 'empty',
              prompt: '员工手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            },{
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          modEntryDate: {
            identifier: 'entryDate',
            rules: [{
              type: 'empty',
              prompt: '员工入职时间不能为空'
            }]
          },
          modJobId: {
            identifier: 'jobId',
            rules: [{
              type: 'empty',
              prompt: '所属工种不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'employee update',
        method: 'POST',
        serializeForm: true,
        traditional: true,//使用传统方式序列化，保证数组传递
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-employee-id').text() != '') {
            settings.data.id = $('#mod-employee-id').text();
            return settings;
          } else {
            alert('ID为空');
            return false;
          }
          var $checkbox = $('.modJobTypeList').find('.checkbox');
          var checkData = [];
          $checkbox.each(function(){
            if($(this).checkbox('is checked')){
              checkData.push(Number($(this).find('label').attr('data-id')));
            }
          })
          settings.data.jobId = checkData;
          settings.data.entryDate = toTimeStamp($('#modEntryDate').val());
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
          } else {
            $('#mod-employee-id').text('');
            $('#mod-employee').form('clear');
            $('.mod-employee-modal').modal('hide');
            loadSearchStockList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除库存模态框
      $(document).on('click', '.del-employee', function () {
        $('#del-employee-id').text($(this).parent().parent().find('.employeeId').text())
        $('.del-employee-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-employee-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'employee leave',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getCookie('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-employee-id').text() != '') {
                    settings.data.id = $('#del-employee-id').text();
                    return settings;
                  } else {
                    alert('ID为空');
                    return false;
                  }
                },
                onSuccess: function (response) {
                  if (response.error != null) {
                    alert(response.error);
                  } else {
                    $('#del-employee-id').text('');
                    $('.del-employee-modal').modal('hide');
                    loadSearchEmployeeList(1, 10, 'search');
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
      $('.new-employee-modal .ui.checkbox').checkbox({
        // Fire on load to set parent value
        fireOnInit: true,
        // Change parent state on each child checkbox change
        onChange: function () {
          var allChecked = true,
            allUnchecked = true;
          var $checkbox = $('.newJobTypeList').find('.checkbox');
          // check to see if all other siblings are checked or unchecked
          $checkbox.each(function () {
            if ($(this).checkbox('is checked')) {
              allUnchecked = false;
            } else {
              allChecked = false;
            }
          });
          if (allUnchecked) {
            $('#newJobId').val('');
          } else {
            $('#newJobId').val('1');
          }
        }
      });
      $('.mod-employee-modal .ui.checkbox').checkbox({
        // Fire on load to set parent value
        fireOnInit: true,
        // Change parent state on each child checkbox change
        onChange: function () {
          var allChecked = true,
            allUnchecked = true;
          var $checkbox = $('.modJobTypeList').find('.checkbox');
          // check to see if all other siblings are checked or unchecked
          $checkbox.each(function () {
            if ($(this).checkbox('is checked')) {
              allUnchecked = false;
            } else {
              allChecked = false;
            }
          });
          if (allUnchecked) {
            $('#modJobId').val('');
          } else {
            $('#modJobId').val('1');
          }
        }
      });
    })
  </script>
</body>

</html>