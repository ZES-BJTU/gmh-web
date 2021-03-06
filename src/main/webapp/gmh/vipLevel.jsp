<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-会员等级管理</title>
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
  <button class="ui blue button load-vip-level-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search vip-level-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-vip-level">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>会员等级</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="vip-level-list">
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
  <div class="ui mini modal new-vip-level-modal">
    <div class="header">新建会员等级</div>
    <div class="content">
      <form onkeydown="if(event.keyCode==13)return false;" id="new-vip-level" class="ui form">
        <div class="field">
          <label>会员等级</label>
          <input type="text" name="levelName" placeholder="请输入会员等级">
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
  <div class="ui mini modal mod-vip-level-modal">
    <div class="header">修改会员等级</div>
    <div class="content">
      <span id="mod-vip-level-id" style="display:none"></span>
      <form onkeydown="if(event.keyCode==13)return false;" id="mod-vip-level" class="ui form">
        <div class="field">
          <label>会员等级</label>
          <input type="text" name="levelName" placeholder="请输入会员等级">
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
  <div class="ui basic tiny modal del-vip-level-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-vip-level-id" style="display:none"></span>
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
  
    activeMenu('vipLevel');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    $(document).ready(function () {
      //会员等级管理搜索
      $('.vip-level-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          loadSearchVipLevelList(1, 10, 'search');
        }
      })

      $('.vip-level-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .last-page', function () {
        var pageNow = Number($('.page-now').val());
        if(pageNow > 1){
          loadSearchVipLevelList(Number(pageNow - 1), 10, 'paging');
        }else{
          alert('已是第一页！')
        }
      })
      //分页按钮点击事件
      $(document).on('click', '.paging .next-page', function () {
        var pageNow = Number($('.page-now').val());
        var pageTotal = Number($('.page-total').text());
        if(pageNow < pageTotal){
          loadSearchVipLevelList(Number(pageNow + 1), 10, 'paging');
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
              loadSearchVipLevelList(Number($('.page-now').val()), 10, 'paging');
            }else{
              alert('最多' + pageTotal + '页！')
            }
          }else{
            alert("请输入正确的页码!");
          }
        }
      });

      function loadSearchVipLevelList(pagenum, pagesize, type) {
        $('.load-vip-level-list').api({
          action: 'vipLevel search',
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
              $('#vip-level-list').empty();
              $('.paging').empty();
              $('.paging').append($('<a class="item last-page"><i class="icon left arrow"></i></a>'));
              $('.paging').append($('<a class="item">第<input class="page-now" value=' + pagenum + '>页 / 共<span class="page-total">' + response.data.totalPages + '</span>页</a>'));
              $('.paging').append($('<a class="item next-page"><i class="icon right arrow"></i></a>'));
              
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="vipLevelId" style="display:none">' + data.id + '</td>');
                var $levelName = $('<td class="levelName">' + data.levelName + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-vip-level">修改</button><button class="ui tiny red button del-vip-level">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($levelName);
                $tr.append($operate);
                $('#vip-level-list').append($tr);
              })
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建会员等级模态框
      $('.new-vip-level').on('click', function () {
        $('.new-vip-level-modal').modal({
            closable: false,
            onDeny: function () {
              $('#new-vip-level').form('clear');
            },
            onApprove: function () {
              $('#new-vip-level').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建会员等级信息提交
      $('#new-vip-level').form({
        on: 'submit',
        inline: true,
        fields: {
          newLevelName: {
            identifier: 'levelName',
            rules: [{
              type: 'empty',
              prompt: '会员等级不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'vipLevel insert',
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
            $('#new-vip-level').form('clear');
            $('.new-vip-level-modal').modal('hide');
            loadSearchVipLevelList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改会员等级模态框
      $(document).on('click', '.mod-vip-level', function () {
        $('#mod-vip-level-id').text($(this).parent().parent().find('.vipLevelId').text());
        $('#mod-vip-level').find('input[name="levelName"]').val($(this).parent().parent().find('.levelName').text());
        $('.mod-vip-level-select select').val($(this).parent().parent().find('.vipLevelId').text());
        $('.mod-vip-level-select .text').removeClass('default');
        $('.mod-vip-level-modal').modal({
            closable: false,
            onDeny: function () {
              $('#mod-vip-level').form('clear');
              $('#mod-vip-level-id').text('');
            },
            onApprove: function () {
              $('#mod-vip-level').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改会员等级信息提交
      $('#mod-vip-level').form({
        on: 'submit',
        inline: true,
        fields: {
          modLevelName: {
            identifier: 'levelName',
            rules: [{
              type: 'empty',
              prompt: '会员等级不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'vipLevel update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-vip-level-id').text() != '') {
            settings.data.id = $('#mod-vip-level-id').text();
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
            $('#mod-vip-level-id').text('');
            $('#mod-vip-level').form('clear');
            $('.mod-vip-level-modal').modal('hide');
            loadSearchVipLevelList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除会员等级模态框
      $(document).on('click', '.del-vip-level', function () {
        $('#del-vip-level-id').text($(this).parent().parent().find('.vipLevelId').text())
        $('.del-vip-level-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-vip-level-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'vipLevel delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-vip-level-id').text() != '') {
                    settings.data.id = $('#del-vip-level-id').text();
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
                    $('#del-vip-level-id').text('');
                    $('.del-vip-level-modal').modal('hide');
                    loadSearchVipLevelList(1, 10, 'search');
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
    })
  </script>
</body>

</html>