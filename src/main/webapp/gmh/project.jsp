<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-美容项管理</title>
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
  <button class="ui blue button load-project-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search project-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <select class="ui selection dropdown top-type-select">
                  <option value="0" class="topType">全部顶级分类</option>
                  <option value="1" class="topType">美甲</option>
                  <option value="2" class="topType">美睫</option>
                  <option value="3" class="topType">美容</option>
                  <option value="4" class="topType">产品</option>
                </select>
                <select class="ui selection dropdown project-type-select">
                  <option value="0">全部美容项分类</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-project">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>美容项目名</th>
                  <th>美容项目分类</th>
                  <th>顶级分类</th>
                  <th>零售价</th>
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
            <div class="ui borderless menu paging">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="ui mini modal new-project-modal">
    <div class="header">新建美容项目</div>
    <div class="content">
      <form id="new-project" class="ui form">
        <div class="field">
          <label>美容项目名</label>
          <input type="text" name="projectName" placeholder="请输入美容项目分类名">
        </div>
        <div class="field">
          <label>顶层分类</label>
          <select name="topTypeId" class="ui fluid dropdown new-project-top-type-select">
            <option value="">请选择顶层分类</option>
          </select>
        </div>
        <div class="field">
          <label>美容项目分类</label>
          <select name="projectTypeId" class="ui fluid dropdown new-project-type-select">
            <option value="">请选择美容项目分类</option>
          </select>
        </div>
        <div class="field">
          <label>美容项目原价</label>
          <input type="text" name="retailPrice" placeholder="请输入美容项目原价">
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
  <div class="ui mini modal mod-project-modal">
    <div class="header">修改美容项目</div>
    <div class="content">
      <span id="mod-project-id" style="display:none"></span>
      <form id="mod-project" class="ui form">
        <div class="field">
          <label>美容项目名</label>
          <input type="text" name="projectName" placeholder="请输入美容项目分类名">
        </div>
        <div class="field">
          <label>顶层分类</label>
          <select name="topTypeId" class="ui fluid dropdown mod-project-top-type-select">
            <option value="">请选择顶层分类</option>
          </select>
        </div>
        <div class="field">
          <label>美容项目分类</label>
          <select name="projectTypeId" class="ui fluid dropdown mod-project-type-select">
            <option value="">请选择美容项目分类</option>
          </select>
        </div>
        <div class="field">
          <label>美容项目原价</label>
          <input type="text" name="retailPrice" placeholder="请输入美容项目原价">
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
  <div class="ui basic tiny modal del-project-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-project-id" style="display:none"></span>
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
  
    activeMenu('project');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchTopTypeId = '';
    var searchProjectTypeId = '';
    var projectTypeData = [];
    $(document).ready(function () {
      //加载全部美容项分类
      $('.fake-button').api({
        action: 'projectType getAll',
        method: 'GET',
        on: 'now',
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.code + ' : ' + response.error);
          } else {
            $('.project-type-select select').find('option:not(:first)').remove();
            projectTypeData = response.data;
            $.each(response.data, function (i, data) {
              var $option = $('<option value="' + data.id + '">' + data.typeName + '</option>');
              $('.project-type-select select').append($option);
            })
            $('.project-type-select select').val(0);
          }
        },
        onFailure: function (response) {
          alert('服务器暂无响应');
        }
      })
      //点击顶级分类，切换美容项分类
      $(document).on('change', '.top-type-select', function () {
        var topType = $(this).find('select').val();
        if (topType == '') {
          return;
        }
        loadProjectTypeByTopType(topType, $('.project-type-select select'), 'load', null);
      })
      //新增时，点击顶级分类，切换美容项分类
      $(document).on('change', '.new-project-top-type-select', function () {
        var topType = $(this).find('select').val();
        if (topType == '') {
          return;
        }
        loadProjectTypeByTopType(topType, $('.new-project-type-select select'), 'new', null);
      })
      //修改时，点击顶级分类，切换美容项分类
      $(document).on('change', '.mod-project-top-type-select', function () {
        var topType = $(this).find('select').val();
        if (topType == '') {
          return;
        }
        loadProjectTypeByTopType(topType, $('.mod-project-type-select select'), 'mod', null);
      })
      //美容项目管理搜索
      $('.project-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchTopTypeId = $('.top-type-select select').val();
          searchProjectTypeId = $('.project-type-select select').val();
          loadSearchProjectList(1, 10, 'search');
        }
      })

      $('.project-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .item', function () {
        loadSearchProjectList($(this).text(), 10, 'paging');
      })

      function loadSearchProjectList(pagenum, pagesize, type) {
        $('.load-project-list').api({
          action: 'project search',
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
              settings.data.topType = $('.top-type-select select').val();
              settings.data.projectType = $('.project-type-select select').val();
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.topType = searchTopTypeId;
              settings.data.projectType = searchProjectTypeId;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.code + ' : ' + response.error);
            } else {
              $('#project-type-list').empty();
              $('.paging').empty();
              for (var i = 0; i < response.data.totalPages; i++) {
                var $item = $('<a class="item">' + (i + 1) + '</a>');
                $('.paging').append($item);
              }
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="projectId" style="display:none">' + data.id + '</td>');
                var $projectName = $('<td class="projectName">' + data.projectName + '</td>');
                var $projectTypeId = $('<td class="projectTypeId" style="display:none">' + data.typeId + '</td>');
                var $projectTypeName = $('<td class="projectTypeName">' + data.typeName + '</td>');
                var $topTypeId = $('<td class="topTypeId" style="display:none">' + data.topType + '</td>');
                var $topTypeName = $('<td class="topTypeName">' + data.topTypeName + '</td>');
                var $retailPrice = $('<td class="retailPrice">' + data.retailPrice + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-project">修改</button><button class="ui tiny red button del-project">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($projectName);
                $tr.append($projectTypeId);
                $tr.append($projectTypeName);
                $tr.append($topTypeId);
                $tr.append($topTypeName);
                $tr.append($retailPrice);
                $tr.append($operate);
                $('#project-type-list').append($tr);
              })
              $('.paging').children().eq(pagenum - 1).addClass('active');
            }
          },
          onFailure: function (response) {
            alert('服务器暂无响应');
          }
        })
      }

      //新建美容项目模态框
      $('.new-project').on('click', function () {
        loadTopTypeData();
        $('.new-project-modal').modal({
            closable: false,
            onDeny: function () {
              clearSelect();
              $('#new-project').form('clear');
            },
            onApprove: function () {
              $('#new-project').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建美容项目信息提交
      $('#new-project').form({
        on: 'change',
        inline: true,
        fields: {
          newProjectName: {
            identifier: 'projectName',
            rules: [{
              type: 'empty',
              prompt: '美容项目名不能为空'
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
          newRetailPrice: {
            identifier: 'retailPrice',
            rules: [{
              type: 'empty',
              prompt: '美容项目原价不能为空'
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
        action: 'project insert',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.code + ' : ' + response.error);
          } else {
            clearSelect();
            $('#new-project').form('clear');
            $('.new-project-modal').modal('hide');
            loadSearchProjectList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器暂无响应');
        },
      });

      //修改美容项目模态框
      $(document).on('click', '.mod-project', function () {
        loadTopTypeData();
        $('#mod-project-id').text($(this).parent().parent().find('.projectId').text());
        $('#mod-project').find('input[name="projectName"]').val($(this).parent().parent().find('.projectName').text());
        var topType = $(this).parent().parent().find('.topTypeId').text();
        $('.mod-project-top-type-select select').val(topType);
        $('.mod-project-top-type-select .text').removeClass('default');
        $('.mod-project-top-type-select .text').text($(this).parent().parent().find('.topTypeName').text());
        //为防止异步的问题，将信息加载放在success里面
        var projectTypeId = $(this).parent().parent().find('.projectTypeId').text();
        var projectTypeName = $(this).parent().parent().find('.projectTypeName').text();
        var info = {
          'projectTypeId': projectTypeId,
          'projectTypeName': projectTypeName
        };
        loadProjectTypeByTopType(topType, $('.mod-project-type-select select'), 'beforeMod', info);

        $('#mod-project').find('input[name="retailPrice"]').val($(this).parent().parent().find('.retailPrice').text());
        $('.mod-project-modal').modal({
            closable: false,
            onDeny: function () {
              clearSelect();
              $('#mod-project').form('clear');
              $('#mod-project-id').text('');
            },
            onApprove: function () {
              $('#mod-project').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改美容项目信息提交
      $('#mod-project').form({
        on: 'change',
        inline: true,
        fields: {
          modProjectName: {
            identifier: 'projectName',
            rules: [{
              type: 'empty',
              prompt: '美容项目名不能为空'
            }]
          },
          modTopTypeId: {
            identifier: 'topTypeId',
            rules: [{
              type: 'empty',
              prompt: '顶层分类不能为空'
            }]
          },
          modProjectTypeId: {
            identifier: 'projectTypeId',
            rules: [{
              type: 'empty',
              prompt: '美容项目分类不能为空'
            }]
          },
          modRetailPrice: {
            identifier: 'retailPrice',
            rules: [{
              type: 'empty',
              prompt: '美容项目原价不能为空'
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
        action: 'project update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-project-id').text() != '') {
            settings.data.id = $('#mod-project-id').text();
            return settings;
          } else {
            alert('ID为空');
            return false;
          }
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.code + ' : ' + response.error);
          } else {
            clearSelect();
            $('#mod-project-id').text('');
            $('#mod-project').form('clear');
            $('.mod-project-modal').modal('hide');
            loadSearchProjectList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器暂无响应');
        }
      });

      //删除美容项目模态框
      $(document).on('click', '.del-project', function () {
        $('#del-project-id').text($(this).parent().parent().find('.projectId').text())
        $('.del-project-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-project-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'project delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getCookie('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-project-id').text() != '') {
                    settings.data.id = $('#del-project-id').text();
                    return settings;
                  } else {
                    alert('ID为空');
                    return false;
                  }
                },
                onSuccess: function (response) {
                  if (response.error != null) {
                    alert(response.code + ' : ' + response.error);
                  } else {
                    $('#del-project-id').text('');
                    $('.del-project-modal').modal('hide');
                    loadSearchProjectList(1, 10, 'search');
                  }
                },
                onFailure: function (response) {
                  alert('服务器暂无响应');
                }
              })
              return false;
            }
          })
          .modal('show');
      })

      function loadProjectTypeByTopType(topType, $select, type, info) {
        $('.fake-button').api({
          action: 'projectType listByTopType',
          method: 'GET',
          on: 'now',
          beforeXHR: function (xhr) {
            verifyToken();
            xhr.setRequestHeader('X-token', getCookie('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          beforeSend: function (settings) {
            settings.data.topType = topType;
            return settings;
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.code + ' : ' + response.error);
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
            alert('服务器暂无响应');
          }
        })
      }

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
          $('.new-project-top-type-select select').append($option);
          $('.mod-project-top-type-select select').append($option.clone());
        })
      }

      function clearSelect() {
        $('.new-project-top-type-select select').find('option:not(:first)').remove();
        $('.new-project-top-type-select .text').text('');
        $('.mod-project-top-type-select select').find('option:not(:first)').remove();
        $('.mod-project-top-type-select .text').text('');
        $('.new-project-type-select select').find('option:not(:first)').remove();
        $('.new-project-type-select .text').text('');
        $('.mod-project-type-select select').find('option:not(:first)').remove();
        $('.mod-project-type-select .text').text('');
      }
    })
  </script>
</body>

</html>