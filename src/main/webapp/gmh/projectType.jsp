<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-美容项分类管理</title>
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
  <button class="ui blue button load-project-type-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search project-type-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <select class="ui selection dropdown top-type-select">
                  <option value="0">全部顶级分类</option>
                  <option value="1">美甲</option>
                  <option value="2">美睫</option>
                  <option value="3">美容</option>
                  <option value="4">产品</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-project-type">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>美容项目分类名</th>
                  <th>顶级分类</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="project-type-list">
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
  <div class="ui mini modal new-project-type-modal">
    <div class="header">新建美容项目分类</div>
    <div class="content">
      <form id="new-project-type" class="ui form">
        <div class="field">
          <label>美容项目分类名</label>
          <input type="text" name="typeName" placeholder="请输入美容项目分类名">
        </div>
        <div class="field">
          <label>顶层分类</label>
          <select name="topType" class="ui fluid dropdown new-project-type-select">
            <option value="">请选择顶层分类</option>
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
  <div class="ui mini modal mod-project-type-modal">
    <div class="header">修改库存</div>
    <div class="content">
      <span id="mod-project-type-id" style="display:none"></span>
      <form id="mod-project-type" class="ui form">
        <div class="field">
          <label>美容项目分类名</label>
          <input type="text" name="typeName" placeholder="请输入美容项目分类名">
        </div>
        <div class="field">
          <label>顶层分类</label>
          <select name="topType" class="ui fluid dropdown mod-project-type-select">
            <option value="">请选择顶层分类</option>
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
  <div class="ui basic tiny modal del-project-type-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-project-type-id" style="display:none"></span>
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
  	
    activeMenu('projectType');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchTopTypeId = '';
    $(document).ready(function () {
      //美容项目分类管理搜索
      $('.project-type-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchTopTypeId = $('.top-type-select select').val();
          loadSearchProjectTypeList(1, 10, 'search');
        }
      })

      $('.project-type-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .last-page', function () {
        var pageNow = Number($('.page-now').val());
        if(pageNow > 1){
          loadSearchProjectTypeList(Number(pageNow - 1), 10, 'paging');
        }else{
        alert('已是第一页！')
        }
      })
      //分页按钮点击事件
      $(document).on('click', '.paging .next-page', function () {
          var pageNow = Number($('.page-now').val());
          var pageTotal = Number($('.page-total').text());
          if(pageNow < pageTotal){
            loadSearchProjectTypeList(Number(pageNow + 1), 10, 'paging');
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
                loadSearchProjectTypeList(Number($('.page-now').val()), 10, 'paging');
              }else{
              alert('最多' + pageTotal + '页！')
              }
          }else{
              alert("请输入正确的页码!");
          }
          }
      });

      function loadSearchProjectTypeList(pagenum, pagesize, type) {
        $('.load-project-type-list').api({
          action: 'projectType search',
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
              settings.data.topType = $('.top-type-select select').val();
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.topType = searchTopTypeId;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
              verifyStatus(response.code);
            } else {
              $('#project-type-list').empty();
              $('.paging').empty();
              $('.paging').append($('<a class="item last-page"><i class="icon left arrow"></i></a>'));
              $('.paging').append($('<a class="item">第<input class="page-now" value=' + pagenum + '>页 / 共<span class="page-total">' + response.data.totalPages + '</span>页</a>'));
              $('.paging').append($('<a class="item next-page"><i class="icon right arrow"></i></a>'));
              
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="projectTypeId" style="display:none">' + data.id + '</td>');
                var $projectTypeName = $('<td class="projectTypeName">' + data.typeName + '</td>');
                var $topTypeId = $('<td class="topTypeId" style="display:none">' + data.topType + '</td>');
                var $topTypeName = $('<td class="topTypeName">' + data.topTypeName + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-project-type">修改</button><button class="ui tiny red button del-project-type">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($projectTypeName);
                $tr.append($topTypeId);
                $tr.append($topTypeName);
                $tr.append($operate);
                $('#project-type-list').append($tr);
              })
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建美容项目分类模态框
      $('.new-project-type').on('click', function () {
        loadTopTypeData();
        $('.new-project-type-modal').modal({
            closable: false,
            onDeny: function () {
              clearSelect();
              $('#new-project-type').form('clear');
            },
            onApprove: function () {
              $('#new-project-type').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建美容项目分类信息提交
      $('#new-project-type').form({
        on: 'submit',
        inline: true,
        fields: {
          newTypeName: {
            identifier: 'typeName',
            rules: [{
              type: 'empty',
              prompt: '美容项目分类名不能为空'
            }]
          },
          newTopType: {
            identifier: 'topType',
            rules: [{
              type: 'empty',
              prompt: '顶层分类不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'projectType insert',
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
            clearSelect();
            $('#new-project-type').form('clear');
            $('.new-project-type-modal').modal('hide');
            loadSearchProjectTypeList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改库存模态框
      $(document).on('click', '.mod-project-type', function () {
        loadTopTypeData();
        $('#mod-project-type-id').text($(this).parent().parent().find('.projectTypeId').text());
        $('#mod-project-type').find('input[name="typeName"]').val($(this).parent().parent().find(
          '.projectTypeName').text());
        $('.mod-project-type-select select').val($(this).parent().parent().find('.topTypeId').text());
        $('.mod-project-type-select .text').removeClass('default');
        $('.mod-project-type-select .text').text($(this).parent().parent().find('.topTypeName').text());
        $('.mod-project-type-modal').modal({
            closable: false,
            onDeny: function () {
              clearSelect();
              $('#mod-project-type').form('clear');
              $('#mod-project-type-id').text('');
            },
            onApprove: function () {
              $('#mod-project-type').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改库存信息提交
      $('#mod-project-type').form({
        on: 'submit',
        inline: true,
        fields: {
          modTypeName: {
            identifier: 'typeName',
            rules: [{
              type: 'empty',
              prompt: '美容项目分类名不能为空'
            }]
          },
          modTopType: {
            identifier: 'topType',
            rules: [{
              type: 'empty',
              prompt: '顶层分类不能为空'
            }]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'projectType update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-project-type-id').text() != '') {
            settings.data.id = $('#mod-project-type-id').text();
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
            clearSelect();
            $('#mod-project-type-id').text('');
            $('#mod-project-type').form('clear');
            $('.mod-project-type-modal').modal('hide');
            loadSearchProjectTypeList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除库存模态框
      $(document).on('click', '.del-project-type', function () {
        $('#del-project-type-id').text($(this).parent().parent().find('.projectTypeId').text())
        $('.del-project-type-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-project-type-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'projectType delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-project-type-id').text() != '') {
                    settings.data.id = $('#del-project-type-id').text();
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
                    $('#del-project-type-id').text('');
                    $('.del-project-type-modal').modal('hide');
                    loadSearchProjectTypeList(1, 10, 'search');
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
          $('.new-project-type-select select').append($option);
          $('.mod-project-type-select select').append($option.clone());
        })
      }

      function clearSelect() {
        $('.new-project-type-select select').find('option:not(:first)').remove();
        $('.new-project-type-select .text').text('');
        $('.mod-project-type-select select').find('option:not(:first)').remove();
        $('.mod-project-type-select .text').text('');
      }
    })
  </script>
</body>

</html>