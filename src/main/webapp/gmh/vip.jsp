<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-会员管理</title>
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
  <button class="ui blue button load-vip-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search vip-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <select class="ui selection dropdown vip-select">
                  <option value="0">全部等级</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-vip">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>姓名</th>
                  <th>手机号</th>
                  <th>性别</th>
                  <th>年龄</th>
                  <th>生日</th>
                  <th>等级</th>
                  <th>有效期至</th>
                  <th>美容储值</th>
                  <th>美睫美甲储值</th>
                  <th>备注</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="vip-list">
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
  <div class="ui mini modal new-vip-modal">
    <div class="header">新建会员</div>
    <div class="content">
      <form id="new-vip" class="ui form">
        <div class="field">
          <label>手机号</label>
          <input type="text" name="phone" placeholder="请输入手机号码">
        </div>
        <div class="field">
          <label>姓名</label>
          <input type="text" name="name" placeholder="请输入会员姓名">
        </div>
        <div class="field">
          <label>性别</label>
          <select name="sex" class="ui selection dropdown">
            <option value="0">女</option>
            <option value="1">男</option>
          </select>
        </div>
        <div class="field">
          <label>生日</label>
          <input type="text" name="birthday" id="vipBirth" placeholder="请选择">
        </div>
        <div class="field">
          <label>发卡日期</label>
          <input type="text" name="joinDate" id="vipJoinDate" placeholder="请选择">
        </div>
        <div class="field">
          <label>有效期至</label>
          <input type="text" name="validDate" id="vipValidDate" placeholder="请选择">
        </div>
        <div class="field">
          <label>等级</label>
          <select name="memberLevelId" class="ui fluid dropdown new-vip-select">
            <option value="">请选择会员等级</option>
          </select>
        </div>
        <div class="field">
          <label>备注</label>
          <textarea name="remark" rows='3'></textarea>
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
  <div class="ui mini modal mod-vip-modal">
    <div class="header">修改会员</div>
    <div class="content">
      <span id="mod-vip-id" style="display:none"></span>
      <form id="mod-vip" class="ui form">
        <div class="field">
          <label>手机号</label>
          <input type="text" name="phone" placeholder="请输入手机号码">
        </div>
        <div class="field">
          <label>姓名</label>
          <input type="text" name="name" placeholder="请输入会员姓名">
        </div>
        <div class="field">
          <label>性别</label>
          <select name="sex" class="ui selection dropdown mod-vip-sex-select">
                  <option value="0">女</option>
                  <option value="1">男</option>
                </select>
        </div>
        <div class="field">
          <label>生日</label>
          <input type="text" name="birthday" placeholder="请选择">
        </div>
        <div class="field">
          <label>发卡日期</label>
          <input type="text" name="joinDate" placeholder="请选择">
        </div>
        <div class="field">
          <label>有效期至</label>
          <input type="text" name="validDate" placeholder="请选择">
        </div>
        <div class="field">
          <label>等级</label>
          <select name="memberLevelId" class="ui fluid dropdown mod-vip-select">
            <option value="">请选择会员等级</option>
          </select>
        </div>
        <div class="field">
          <label>备注</label>
          <textarea name="remark" rows='3'></textarea>
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
  <div class="ui mini modal charge-vip-modal">
    <div class="header">充值</div>
    <div class="content">
      <span id="charge-vip-id" style="display:none"></span>
      <form id="charge-vip" class="ui form">
        <div class="field">
          <label>美甲美睫</label>
          <input type="text" name="nailMoney" placeholder="请输入充值金额">
        </div>
        <div class="field">
          <label>美容</label>
          <input type="text" name="beautyMoney" placeholder="请输入充值金额">
        </div>
        <div class="field">
              <label>操作员</label>
              <select name="employeeId" class="ui fluid dropdown employee-select">
                <option value="">请选择操作员</option>
              </select>
        </div>
        <div class="field">
              <label>咨询师</label>
              <select name="consultantId" class="ui fluid dropdown consultant-select">
                <option value="">请选择咨询师</option>
              </select>
        </div>
        <div class="field">
          <label>充值来源</label>
          <input type="text" name="source">
        </div>
        <div class="field">
          <label>充值备注</label>
          <input type="text" name="remark">
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
  <div class="ui basic tiny modal del-vip-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-vip-id" style="display:none"></span>
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
  <div class="ui large modal new-record-modal">
    <div class="header">新建消费记录</div>
    <div class="content">
      <form id="new-record" class="ui form">
        <div class="two fields">
          <div class="field">
            <label>消费人手机号</label>
            <input type="text" name="mobile" id="new-record-mobile" placeholder="请输入手机号">
          </div>
          <div class="field">
            <label>消费人姓名</label>
            <input type="text" name="consumerName" id="new-record-name" placeholder="请输入姓名">
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
          <select name="memberId" class="ui fluid dropdown new-record-card-select">
            <option value="">请选择会员卡</option>
          </select>
        </div>
        <div class="field">
          <label>支付金额</label>
          <input type="text" name="charge" id="finalCharge" placeholder="请输入支付金额" disabled="">
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
  <script>
  
    activeMenu('vip');
    
    $('#vipBirth').datetimepicker({
      language: 'zh-CN',
      format: 'yyyy-mm-dd',
      minView: 2,
      weekStart: 1,
      todayBtn: 1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 2,
      forceParse: true,
      showMeridian: 1
    }).on('changeDate', function (ev) {
      $('#vipBirth').focus();
      $('#vipBirth').blur();
    });
    $('#vipJoinDate').datetimepicker({
      language: 'zh-CN',
      format: 'yyyy-mm-dd',
      minView: 2,
      weekStart: 1,
      todayBtn: 1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 2,
      forceParse: true,
      showMeridian: 1
    }).on('changeDate', function (ev) {
      $('#vipJoinDate').focus();
      $('#vipJoinDate').blur();
    });
    $('#vipValidDate').datetimepicker({
      language: 'zh-CN',
      format: 'yyyy-mm-dd',
      minView: 2,
      weekStart: 1,
      todayBtn: 1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 2,
      forceParse: true,
      showMeridian: 1
    }).on('changeDate', function (ev) {
      $('#vipValidDate').focus();
      $('#vipValidDate').blur();
    });
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchLevelId = '';
    var vipLevelData = [];
    var employeeData = [];
    var consultantData = [];
    $(document).ready(function () {
      //加载会员等级
      $('.fake-button').api({
        action: 'vipLevel getAll',
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
            $('.vip-select select').find('option:not(:first)').remove();
            vipLevelData = response.data;
            $.each(response.data, function (i, data) {
              var $option = $('<option value="' + data.id + '">' + data.levelName + '</option>');
              $('.vip-select select').append($option);
            })
            $('.vip-select select').val(0);
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      })
      //加载所有员工
       $('.fake-button').api({
        action: 'employee getAll',
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
            $('.employee-select select').find('option:not(:first)').remove();
            $('.consultant-select select').find('option:not(:first)').remove();
            employeeData = response.data;
            consultantData = response.data;
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
      //加载所有员工
      //会员管理搜索
      $('.vip-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchLevelId = $('.vip-select select').val();
          loadSearchVipList(1, 10, 'search');
        }
      })

      $('.vip-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .item', function () {
        loadSearchVipList($(this).text(), 10, 'paging');
      })

      function loadSearchVipList(pagenum, pagesize, type) {
        $('.load-vip-list').api({
          action: 'vip search',
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
              settings.data.memberLevelId = $('.vip-select select').val();
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.memberLevelId = searchLevelId;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#vip-list').empty();
              $('.paging').empty();
              for (var i = 0; i < response.data.totalPages; i++) {
                var $item = $('<a class="item">' + (i + 1) + '</a>');
                $('.paging').append($item);
              }
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="vipId" style="display:none">' + data.id + '</td>');
                var $vipName = $('<td class="vipName">' + data.name + '</td>');
                var $vipMobile = $('<td class="vipMobile">' + data.phone + '</td>');
                var $sexId = $('<td class="vipSexId" style="display:none">' + (data.sex == '男' ? 1 : 0) +
                  '</td>');
                var $vipSex = $('<td class="vipSex">' + data.sex + '</td>');
                var $levelId = $('<td class="levelId" style="display:none">' + data.memberLevelId +
                  '</td>');
                var $vipBirth = $('<td class="vipBirth">' + toDatetimeDay(data.birthday) + '</td>');
                var $age = $('<td class="age">' + data.age + '</td>');
                var $levelName = $('<td class="levelName">' + data.memberLevelName + '</td>');
                var $joinDate = $('<td class="joinDate" style="display:none">' + toDatetimeDay(data.joinDate) +
                  '</td>');
                var $validDate = $('<td class="validDate">' + toDatetimeDay(data.validDate) + '</td>');
                var $beautyMoney = $('<td class="beautyMoney">' + data.beautyMoney + '</td>');
                var $nailMoney = $('<td class="nailMoney">' + data.nailMoney + '</td>');
                var remark = (data.remark == null) ? '无' : String(data.remark);
                var afterRemark = '';
                if (remark == '无') {
                  afterRemark = '无'
                } else if (remark.length > 6) {
                  afterRemark = remark.substring(0, 6) + '...';
                } else {
                  afterRemark = remark;
                }
                var $remark = $('<td class="remark" title="' + remark + '">' + afterRemark + '</td>');
                //  var $remark = $('<td class="remark">' + data.remark + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny teal button consume-vip"</button>消费<button class="ui tiny green button charge-vip">充值</button><button class="ui tiny orange button mod-vip">修改</button><button class="ui tiny red button del-vip">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($vipName);
                $tr.append($vipMobile);
                $tr.append($sexId);
                $tr.append($vipSex);
                $tr.append($levelId);
                $tr.append($age);
                $tr.append($vipBirth);
                $tr.append($levelName);
                $tr.append($joinDate);
                $tr.append($validDate);
                $tr.append($beautyMoney);
                $tr.append($nailMoney);
                $tr.append($remark);
                $tr.append($operate);
                $('#vip-list').append($tr);
              })
              $('.paging').children().eq(pagenum - 1).addClass('active');
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建会员模态框
      $('.new-vip').on('click', function () {
        loadVipLevelData();
        $('.new-vip-modal').modal({
            closable: false,
            onDeny: function () {
              clearVipSelect();
              $('#new-vip').form('clear');
            },
            onApprove: function () {
              $('#new-vip').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建会员信息提交
      $('#new-vip').form({
        on: 'submit',
        inline: true,
        fields: {
          newVipMobile: {
            identifier: 'phone',
            rules: [{
              type: 'empty',
              prompt: '手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            }, {
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          newVipName: {
            identifier: 'name',
            rules: [{
              type: 'empty',
              prompt: '会员姓名不能为空'
            }]
          },
          newVipSex: {
            identifier: 'vipSex',
            rules: [{
              type: 'empty',
              prompt: '性别不能为空'
            }]
          },
          newVipBirth: {
            identifier: 'vipBirth',
            rules: [{
              type: 'empty',
              prompt: '出生日期不能为空'
            }]
          },
          newVipJoinDate: {
              identifier: 'vipJoinDate',
              rules: [{
                type: 'empty',
                prompt: '发卡日期不能为空'
              }]
          },
          newVipValidDate: {
            identifier: 'vipValidDate',
            rules: [{
              type: 'empty',
              prompt: '有效期不能为空'
            }]
          },
          newVipLevelDate: {
            identifier: 'memberLevelId',
            rules: [{
              type: 'empty',
              prompt: '会员等级不能为空'
            }]
          },
          newVipValidDate: {
              identifier: 'vipValidDate',
              rules: [{
                type: 'empty',
                prompt: '有效期不能为空'
              }]
          }

        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'vip insert',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if (settings.data.beautyMoney == '') {
            settings.data.beautyMoney = '0';
          }
          if (settings.data.nailMoney == '') {
            settings.data.nailMoney = '0';
          }
          settings.data.birthday = toTimeStamp(settings.data.birthday);
          settings.data.joinDate = toTimeStamp(settings.data.joinDate);
          settings.data.validDate = toTimeStamp(settings.data.validDate);
          return settings;
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            clearVipSelect();
            $('#new-vip').form('clear');
            $('.new-vip-modal').modal('hide');
            loadSearchVipList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改会员模态框
      $(document).on('click', '.mod-vip', function () {
        loadVipLevelData();
        $('#mod-vip-id').text($(this).parent().parent().find('.vipId').text());
        $('#mod-vip').find('input[name="name"]').val($(this).parent().parent().find('.vipName').text());
        $('.mod-vip-select select').val($(this).parent().parent().find('.levelId').text());
        $('.mod-vip-select .text').removeClass('default');
        $('.mod-vip-select .text').text($(this).parent().parent().find('.levelName').text());
        $('.mod-vip-sex-select select').val($(this).parent().parent().find('.vipSexId').text());
        $('.mod-vip-sex-select .text').removeClass('default');
        $('.mod-vip-sex-select .text').text($(this).parent().parent().find('.vipSex').text());
        $('#mod-vip').find('input[name="phone"]').val($(this).parent().parent().find('.vipMobile').text());
        $('#mod-vip').find('input[name="birthday"]').val($(this).parent().parent().find('.vipBirth').text());
        $('#mod-vip').find('input[name="joinDate"]').val($(this).parent().parent().find('.joinDate').text());
        $('#mod-vip').find('input[name="validDate"]').val($(this).parent().parent().find('.validDate').text());
        $('#mod-vip').find('input[name="beautyMoney"]').val($(this).parent().parent().find('.beautyMoney').text());
        $('#mod-vip').find('input[name="nailMoney"]').val($(this).parent().parent().find('.nailMoney').text());
        $('#mod-vip').find('textarea[name="remark"]').val($(this).parent().parent().find('.remark').text());
        $('.mod-vip-modal').modal({
            closable: false,
            onDeny: function () {
              clearVipSelect();
              $('#mod-vip').form('clear');
              $('#mod-vip-id').text('');
            },
            onApprove: function () {
              $('#mod-vip').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改会员信息提交
      $('#mod-vip').form({
        on: 'submit',
        inline: true,
        fields: {
          modVipMobile: {
            identifier: 'phone',
            rules: [{
              type: 'empty',
              prompt: '手机号不能为空'
            }, {
              type: 'number',
              prompt: '请输入正确的手机号'
            }, {
              type: 'exactLength[11]',
              prompt: '请输入11位手机号'
            }]
          },
          modVipName: {
            identifier: 'name',
            rules: [{
              type: 'empty',
              prompt: '会员姓名不能为空'
            }]
          },
          modVipSex: {
            identifier: 'vipSex',
            rules: [{
              type: 'empty',
              prompt: '性别不能为空'
            }]
          },
          modVipBirth: {
            identifier: 'vipBirth',
            rules: [{
              type: 'empty',
              prompt: '出生日期不能为空'
            }]
          },
          modVipValidDate: {
            identifier: 'vipValidDate',
            rules: [{
              type: 'empty',
              prompt: '有效期不能为空'
            }]
          },
          modVipLevelDate: {
            identifier: 'memberLevelId',
            rules: [{
              type: 'empty',
              prompt: '会员等级不能为空'
            }]
          },
          modVipValidDate: {
            identifier: 'vipValidDate',
            rules: [{
              type: 'empty',
              prompt: '有效期不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'vip update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-vip-id').text() != '') {
            settings.data.id = $('#mod-vip-id').text();
            if (settings.data.beautyMoney == '') {
              settings.data.beautyMoney = '0';
            }
            if (settings.data.nailMoney == '') {
              settings.data.nailMoney = '0';
            }
            settings.data.birthday = toTimeStamp(settings.data.birthday);
            settings.data.joinDate = toTimeStamp(settings.data.joinDate);
            settings.data.validDate = toTimeStamp(settings.data.validDate);
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
            $('#mod-vip-id').text('');
            clearVipSelect();
            $('#mod-vip').form('clear');
            $('.mod-vip-modal').modal('hide');
            loadSearchVipList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除会员模态框
      $(document).on('click', '.del-vip', function () {
        $('#del-vip-id').text($(this).parent().parent().find('.vipId').text())
        $('.del-vip-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-vip-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'vip delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-vip-id').text() != '') {
                    settings.data.id = $('#del-vip-id').text();
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
                    $('#del-vip-id').text('');
                    $('.del-vip-modal').modal('hide');
                    loadSearchVipList(1, 10, 'search');
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

      //充值模态框
      $(document).on('click', '.charge-vip', function () {
    	loadEmployeeData();
    	loadConsultantData();
        $('#charge-vip-id').text($(this).parent().parent().find('.vipId').text());
        $('.charge-vip-modal').modal({
            closable: false,
            onDeny: function () {
              clearVipSelect();
              $('#charge-vip').form('clear');
              $('#charge-vip-id').text('');
            },
            onApprove: function () {
              $('#charge-vip').submit();
              return false;
            }
          })
          .modal('show');
      })

      //充值信息提交
      $('#charge-vip').form({
        on: 'submit',
        inline: true,
        fields: {
          newNailMoney: {
            identifier: 'nailMoney',
            rules: [{
              type: 'decimal',
              prompt: '美甲美睫储值格式错误'
            }]
          },
          newBeautyMoney: {
            identifier: 'beautyMoney',
            rules: [{
              type: 'decimal',
              prompt: '美容储值格式错误'
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
        }
      }).api({
        action: 'vip charge',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#charge-vip-id').text() != ''){
            settings.data.id = $('#charge-vip-id').text();
            return settings;
          }else{
            alert('ID为空');
            return false;
          }
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
            verifyStatus(response.code);
          } else {
            $('#charge-vip-id').text('');
            $('#charge-vip').form('clear');
            $('.charge-vip-modal').modal('hide');
            loadSearchVipList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

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
                    $('#card-field').show();
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
        var reg = new RegExp("^[1-9]\\d*$");    
        if ($(this).val() != '') {
          if(reg.test($(this).val())) {  
              $(this).parent().parent().find('.charge').val(Number(pcharge) * Number($(this).val()) * 0.01);
          } 
        }else{
          $(this).parent().parent().find('.charge').val(Number(pcharge));
        }
        changeCharge();
      })
      //输入实际价钱，变更折扣和总价
      $(document).on('blur', '.discount-charge', function () {
        var pcharge = $(this).parent().parent().find('.projectCharge').text();
        var reg = new RegExp("^[1-9]\\d*$");    
        if ($(this).val() != '') {
          if(reg.test($(this).val())) {  
              $(this).parent().parent().find('.discount').val(Number($(this).val()) * 100 / Number(pcharge));
          } 
        }else{
          $(this).val(0);
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
              '</td><td><input type="text" class="charge discount-charge" placeholder="请输入实付价格" value="' + projectCharge + '">' +
              '</td><td class="counselor"><select class="ui fluid dropdown new-record-counselor-select"><option value="">请选择经理/咨询师</option>' + option + '</select></td>'+
              '<td class="minus-project"><i class="minus icon"></i></td></tr>');
            $('#project-list').append($tr);
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

      //消费
      $(document).on('click','.consume-vip',function(){
        $('#new-record-mobile').val($(this).parent().parent().find('.vipMobile').text());
        $('#new-record-name').val($(this).parent().parent().find('.vipName').text());
        $('.new-record-sex-select select').val($(this).parent().parent().find('.vipSexId').text());
        $('.new-record-sex-select .text').removeClass('default');
        $('.new-record-sex-select .text').text($(this).parent().parent().find('.vipSex').text());
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
            var pcharge = $(this).find('.charge').val();
            var counselor = $(this).find('.new-record-counselor-select select').val();
            if(counselor == ''){
              counselor = 0;
            }
            projects += ';' + pid + ',' + eid + ',' + pcharge  + ',' + counselor;
          })
          projects = projects.substring(1);
          settings.data.projects = projects;
          settings.data.charge = $('#finalCharge').val();
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
            loadSearchVipList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      function loadVipLevelData() {
        $.each(vipLevelData, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.levelName + '</option>');
          $('.new-vip-select select').append($option);
          $('.mod-vip-select select').append($option.clone());
        })
      }
      function loadEmployeeData() {
          $.each(employeeData, function (i, data) {
            var $option = $('<option value="' + data.employeeId + '">' + data.employeeName + '</option>');
            $('.employee-select select').append($option);
          })
        }
      function loadConsultantData() {
        $.each(consultantData, function (i, data) {
          var $option = $('<option value="' + data.employeeId + '">' + data.employeeName + '</option>');
          $('.consultant-select select').append($option);
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

      function clearVipSelect() {
        $('.new-vip-select select').find('option:not(:first)').remove();
        $('.new-vip-select .text').text('');
        $('.mod-vip-select select').find('option:not(:first)').remove();
        $('.mod-vip-select .text').text('');
        $('.employee-select select').find('option:not(:first)').remove();
        $('.employee-select .text').text('');
        $('.consultant-select select').find('option:not(:first)').remove();
        $('.consultant-select .text').text('');
        $('textarea[name="remark"]').text('');
      }
      function changeCharge(){
        var charge = 0;
        $('#project-list').find('.charge').each(function(){
          charge += Number($(this).val());
        })
        $('#finalCharge').val(charge);
      }
      function clearCard(){
        $('.new-record-card-select select').find('option:not(:first)').remove();
        $('.new-record-card-select .text').text('请选择会员卡');
        $('.new-record-card-select .text').addClass('unselected');
        $('.mod-record-card-select select').find('option:not(:first)').remove();
        $('.mod-record-card-select .text').text('请选择会员卡');
        $('.mod-record-card-select .text').addClass('unselected');
        $('.print-records-select select').find('option:not(:first)').remove();
        $('.print-records-select .text').text('请选择会员卡');
        $('.print-records-select .text').addClass('unselected');
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
        $('.mod-record-counselor-select select').find('option:not(:first)').remove();
        $('.mod-record-counselor-select .text').text('请选择经理/咨询师');
        $('.mod-record-card-select .text').addClass('unselected');
      }
    })
  </script>
</body>

</html>