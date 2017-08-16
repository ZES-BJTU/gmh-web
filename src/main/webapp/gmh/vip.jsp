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
          <label>美容储值</label>
          <input type="text" name="beautyMoney" placeholder="请选择">
        </div>
        <div class="field">
          <label>美甲美睫储值</label>
          <input type="text" name="nailMoney" placeholder="请选择">
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
          <label>美容储值</label>
          <input type="text" name="beautyMoney" placeholder="请选择">
        </div>
        <div class="field">
          <label>美甲美睫储值</label>
          <input type="text" name="nailMoney" placeholder="请选择">
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
                  '<td><button class="ui tiny orange button mod-vip">修改</button><button class="ui tiny red button del-vip">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($vipName);
                $tr.append($vipMobile);
                $tr.append($sexId);
                $tr.append($vipSex);
                //  $tr.append($levelId);
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
          newBeautyMoney: {
            identifier: 'beautyMoney',
            rules: [{
              type: 'decimal',
              prompt: '请输入数字'
            }]
          },
          newNailMoney: {
            identifier: 'nailMoney',
            rules: [{
              type: 'decimal',
              prompt: '请输入数字'
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
          modBeautyMoney: {
            identifier: 'beautyMoney',
            rules: [{
              type: 'decimal',
              prompt: '请输入数字'
            }]
          },
          modNailMoney: {
            identifier: 'nailMoney',
            rules: [{
              type: 'decimal',
              prompt: '请输入数字'
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

      function loadVipLevelData() {
        $.each(vipLevelData, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.levelName + '</option>');
          $('.new-vip-select select').append($option);
          $('.mod-vip-select select').append($option.clone());
        })
      }

      function clearVipSelect() {
        $('.new-vip-select select').find('option:not(:first)').remove();
        $('.new-vip-select .text').text('');
        $('.mod-vip-select select').find('option:not(:first)').remove();
        $('.mod-vip-select .text').text('');
        $('textarea[name="remark"]').text('');
      }
    })
  </script>
</body>

</html>