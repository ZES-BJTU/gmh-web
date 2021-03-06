<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-用户管理</title>
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
  <button class="ui blue button load-staff-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search staff-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-staff">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>邮箱</th>
                  <th>姓名</th>
                  <th>电话</th>
                  <th>店铺</th>
                  <th>角色</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="staff-list">
              </tbody>
            </table>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="ui pagination menu paging">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="ui mini modal new-staff-modal">
    <div class="header">新建用户</div>
    <div class="content">
      <form id="new-staff" class="ui form">
        <div class="field">
          <label>邮箱</label>
          <input type="text" name="email" placeholder="请输入邮箱">
        </div>
        <div class="field">
          <label>姓名</label>
          <input type="text" name="name" placeholder="请输入姓名">
        </div>
        <div class="field">
          <label>电话</label>
          <input type="text" name="mobile" placeholder="请输入电话">
        </div>
        <div class="field">
          <label>店铺</label>
          <select name="storeId" class="ui fluid dropdown new-staff-shop-select">
            <option value="">请选择店铺</option>
          </select>
        </div>
        <div class="field">
          <label>角色</label>
          <select name="staffLevel" class="ui fluid dropdown new-staff-level-select">
            <option value="">请选择角色</option>
            <option value="1">前台</option>
            <option value="2">美容师</option>
          </select>
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
  <div class="ui mini modal mod-staff-modal">
    <div class="header">修改用户</div>
    <div class="content">
      <span id="mod-staff-id" style="display:none"></span>
      <form id="mod-staff" class="ui form">
        <div class="field">
          <label>邮箱</label>
          <input type="text" name="email" placeholder="请输入邮箱" disabled="">
        </div>
        <div class="field">
          <label>姓名</label>
          <input type="text" name="name" placeholder="请输入姓名">
        </div>
        <div class="field">
          <label>电话</label>
          <input type="text" name="mobile" placeholder="请输入电话">
        </div>
        <div class="field">
          <label>店铺</label>
          <select name="storeId" class="ui fluid dropdown mod-staff-shop-select" disabled="">
            <option value="">请选择店铺</option>
          </select>
        </div>
        <div class="field">
          <label>角色</label>
          <select name="staffLevel" class="ui fluid dropdown mod-staff-level-select" disabled="">
            <option value="">请选择角色</option>
            <option value="1">前台</option>
            <option value="2">美容师</option>
          </select>
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
  <div class="ui basic tiny modal del-staff-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-staff-id" style="display:none"></span>
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
  
    activeMenu('staff');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var shopData = [];
    $(document).ready(function () {
      //加载店铺
      $('.fake-button').api({
        action: 'shop getAll',
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
            shopData = response.data;
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      })
      //店铺管理搜索
      $('.staff-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          loadSearchStaffList(1, 10, 'search');
        }
      })

      $('.staff-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .last-page', function () {
        var pageNow = Number($('.page-now').val());
        if(pageNow > 1){
          loadSearchStaffList(Number(pageNow - 1), 10, 'paging');
        }else{
        alert('已是第一页！')
        }
      })
      //分页按钮点击事件
      $(document).on('click', '.paging .next-page', function () {
          var pageNow = Number($('.page-now').val());
          var pageTotal = Number($('.page-total').text());
          if(pageNow < pageTotal){
            loadSearchStaffList(Number(pageNow + 1), 10, 'paging');
          }else{
          alert('已是最后一页！')
          }
      })
      //监听回车事件
      $(document).on('keyup','.page-now', function(event) {
          if (event.keyCode == "13") {
          //回车执行查询
          var pageNow = Number($('.page-now').val());
          var pageTotal = Number($('.page-total').text());
          if(/^[0-9]*[1-9][0-9]*$/ .test(Number($('.page-now').val()))){
              if(pageNow <= pageTotal){
                loadSearchStaffList(Number($('.page-now').val()), 10, 'paging');
              }else{
              alert('最多' + pageTotal + '页！')
              }
          }else{
              alert("请输入正确的页码!");
          }
          }
      });

      function loadSearchStaffList(pagenum, pagesize, type) {
        $('.load-staff-list').api({
          action: 'staff search',
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
              $('#staff-list').empty();
              $('.paging').empty();
              $('.paging').append($('<a class="item last-page"><i class="icon left arrow"></i></a>'));
              $('.paging').append($('<a class="item">第<input class="page-now" value=' + pagenum + '>页 / 共<span class="page-total">' + response.data.totalPages + '</span>页</a>'));
              $('.paging').append($('<a class="item next-page"><i class="icon right arrow"></i></a>'));
              
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="staffId" style="display:none">' + data.id + '</td>');
                var $email = $('<td class="staffEmail">' + data.email + '</td>');
                var $staffName = $('<td class="staffName">' + data.name + '</td>');
                var $staffMobile = $('<td class="staffMobile">' + data.mobile + '</td>');
                var $storeId = $('<td class="storeId" style="display:none">' + data.storeId + '</td>');
                var $storeName = $('<td class="storeName">' + data.storeName + '</td>');
                var $staffLevel = $('<td class="staffLevel" style="display:none">' + data.staffLevel + '</td>');
                var staffLevelName;
                if(data.staffLevel == 1){
                  staffLevelName = '前台';
                }else if(data.staffLevel == 2){
                  staffLevelName = '美容师';
                }else{
                  staffLevelName = '管理员';
                }
                var $staffLevelName = $('<td class="staffLevelName">' + staffLevelName + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-staff">修改</button><button class="ui tiny red button del-staff">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($email);
                $tr.append($staffName);
                $tr.append($staffMobile);
                $tr.append($storeId);
                $tr.append($storeName);
                $tr.append($staffLevel);
                $tr.append($staffLevelName);
                $tr.append($operate);
                $('#staff-list').append($tr);
              })
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建店铺模态框
      $('.new-staff').on('click', function () {
        loadShopData();
        $('.new-staff-modal').modal({
            closable: false,
            onDeny: function () {
              clearStaffSelect();
              $('#new-staff').form('clear');
            },
            onApprove: function () {
              $('#new-staff').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建店铺信息提交
      $('#new-staff').form({
        on: 'submit',
        inline: true,
        fields: {
          newEmail: {
            identifier: 'email',
            rules: [{
                type: 'empty',
                prompt: '邮箱不能为空'
              },
              {
                type: 'email',
                prompt: '请输入有效的邮箱'
              }
            ]
          },
          newName: {
            identifier: 'name',
            rules: [{
                type: 'empty',
                prompt: '姓名不能为空'
              }
            ]
          },
          newMobile: {
            identifier: 'mobile',
            rules: [{
                type: 'empty',
                prompt: '电话不能为空'
              },
              {
                type: 'number',
                prompt: '请输入有效的电话'
              }
            ]
          },
          newStoreId: {
            identifier: 'storeId',
            rules: [{
              type: 'empty',
              prompt: '店铺不能为空'
            }]
          },
          newStaffLevel: {
            identifier: 'staffLevel',
            rules: [{
              type: 'empty',
              prompt: '角色不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'staff insert',
        method: 'POST',
        serializeForm: true,
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
            clearStaffSelect();
            $('#new-staff').form('clear');
            $('.new-staff-modal').modal('hide');
            loadSearchStaffList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改店铺模态框
      $(document).on('click', '.mod-staff', function () {
        loadShopData();
        $('#mod-staff-id').text($(this).parent().parent().find('.staffId').text())
        $('#mod-staff').find('input[name="email"]').val($(this).parent().parent().find('.staffEmail').text());
        $('#mod-staff').find('input[name="name"]').val($(this).parent().parent().find('.staffName').text());
        $('#mod-staff').find('input[name="mobile"]').val($(this).parent().parent().find('.staffMobile').text());
        $('.mod-staff-shop-select select').val($(this).parent().parent().find('.storeId').text());
        $('.mod-staff-shop-select .text').removeClass('default');
        $('.mod-staff-shop-select .text').text($(this).parent().parent().find('.storeName').text());
        $('.mod-staff-level-select select').val($(this).parent().parent().find('.staffLevel').text());
        $('.mod-staff-level-select .text').removeClass('default');
        $('.mod-staff-level-select .text').text($(this).parent().parent().find('.staffLevelName').text());
        $('.mod-staff-modal').modal({
            closable: false,
            onDeny: function () {
              clearStaffSelect();
              $('#mod-staff').form('clear');
              $('#mod-staff-id').text('');
            },
            onApprove: function () {
              $('#mod-staff').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改店铺信息提交
      $('#mod-staff').form({
        on: 'submit',
        inline: true,
        fields: {
          modEmail: {
            identifier: 'email',
            rules: [{
                type: 'empty',
                prompt: '邮箱不能为空'
              },
              {
                type: 'email',
                prompt: '请输入有效的邮箱'
              }
            ]
          },
          modName: {
            identifier: 'name',
            rules: [{
                type: 'empty',
                prompt: '姓名不能为空'
              }
            ]
          },
          modMobile: {
            identifier: 'mobile',
            rules: [{
                type: 'empty',
                prompt: '电话不能为空'
              },
              {
                type: 'number',
                prompt: '请输入有效的电话'
              }
            ]
          },
          modStoreId: {
            identifier: 'storeId',
            rules: [{
              type: 'empty',
              prompt: '店铺不能为空'
            }]
          },
          modStaffLevel: {
            identifier: 'staffLevel',
            rules: [{
              type: 'empty',
              prompt: '角色不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'staff update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-staff-id').text() != '') {
            settings.data.id = $('#mod-staff-id').text();
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
            clearStaffSelect();
            $('#mod-staff-id').text('');
            $('#mod-staff').form('clear');
            $('.mod-staff-modal').modal('hide');
            loadSearchStaffList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除店铺模态框
      $(document).on('click', '.del-staff', function () {
        $('#del-staff-id').text($(this).parent().parent().find('.staffId').text())
        $('.del-staff-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-staff-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'staff delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-staff-id').text() != '') {
                    settings.data.id = $('#del-staff-id').text();
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
                    $('#del-staff-id').text('');
                    $('.del-staff-modal').modal('hide');
                    loadSearchStaffList(1, 10, 'search');
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

      function loadShopData() {
        $.each(shopData, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.shopName + '</option>');
          $('.new-staff-shop-select select').append($option);
          $('.mod-staff-shop-select select').append($option.clone());
        })
      }

      function clearStaffSelect() {
        $('.new-staff-shop-select select').find('option:not(:first)').remove();
        $('.new-staff-shop-select .text').text('');
        $('.mod-staff-shop-select select').find('option:not(:first)').remove();
        $('.mod-staff-shop-select .text').text('');
      }
    })
  </script>
</body>

</html>