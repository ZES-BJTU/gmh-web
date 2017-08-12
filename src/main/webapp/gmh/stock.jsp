<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-库存管理</title>
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
  <button class="ui blue button load-stock-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search stock-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <select class="ui selection dropdown stock-select">
                  <option value="0">全部库存分类</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-stock">新建</button>
          </div>
        </div>
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>库存名</th>
                  <th>库存分类</th>
                  <th>单位</th>
                  <th>数量</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="stock-list">
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
  <div class="ui mini modal new-stock-modal">
    <div class="header">新建库存</div>
    <div class="content">
      <form id="new-stock" class="ui form">
        <div class="field">
          <label>库存名</label>
          <input type="text" name="stockName" placeholder="请输入库存名">
        </div>
        <div class="field">
          <label>库存分类</label>
          <select name="typeId" class="ui fluid dropdown new-stock-select">
            <option value="">请选择库存分类</option>
          </select>
        </div>
        <div class="field">
          <label>单位</label>
          <input type="text" name="unit" placeholder="请输入单位">
        </div>
        <div class="field">
          <label>数量</label>
          <input type="text" name="amount" placeholder="请输入数量">
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
  <div class="ui mini modal mod-stock-modal">
    <div class="header">修改库存</div>
    <div class="content">
      <span id="mod-stock-id" style="display:none"></span>
      <form id="mod-stock" class="ui form">
        <div class="field">
          <label>库存名</label>
          <input type="text" name="stockName" placeholder="请输入库存名">
        </div>
        <div class="field">
          <label>库存分类</label>
          <select name="typeId" class="ui fluid dropdown mod-stock-select">
            <option value="">请选择库存分类</option>
          </select>
        </div>
        <div class="field">
          <label>单位</label>
          <input type="text" name="unit" placeholder="请输入单位">
        </div>
        <div class="field">
          <label>数量</label>
          <input type="text" name="amount" placeholder="请输入数量">
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
  <div class="ui basic tiny modal del-stock-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>确认删除？
    </div>
    <div class="content">
      <span id="del-stock-id" style="display:none"></span>
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
  
    activeMenu('stock');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    var searchTypeId = '';
    var stockTypeData = [];
    $(document).ready(function () {
      //加载库存分类
      $('.fake-button').api({
        action: 'stockType getAll',
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
            $('.stock-select select').find('option:not(:first)').remove();
            stockTypeData = response.data;
            $.each(response.data, function (i, data) {
              var $option = $('<option value="' + data.id + '">' + data.typeName + '</option>');
              $('.stock-select select').append($option);
            })
            $('.stock-select select').val(0);
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      })
      //库存管理搜索
      $('.stock-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          searchTypeId = $('.stock-select select').val();
          loadSearchStockList(1, 10, 'search');
        }
      })

      $('.stock-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .item', function () {
        loadSearchStockList($(this).text(), 10, 'paging');
      })

      function loadSearchStockList(pagenum, pagesize, type) {
        $('.load-stock-list').api({
          action: 'stock search',
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
              settings.data.typeId = $('.stock-select select').val();
              return settings;
            } else {
              settings.data.searchString = searchInfo;
              settings.data.typeId = searchTypeId;
              return settings;
            }
          },
          onSuccess: function (response) {
            if (response.error != null) {
              alert(response.error);
            } else {
              $('#stock-list').empty();
              $('.paging').empty();
              for (var i = 0; i < response.data.totalPages; i++) {
                var $item = $('<a class="item">' + (i + 1) + '</a>');
                $('.paging').append($item);
              }
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="stockId" style="display:none">' + data.id + '</td>');
                var $typeId = $('<td class="typeId" style="display:none">' + data.typeId + '</td>');
                var $stockName = $('<td class="stockName">' + data.stockName + '</td>');
                var $typeName = $('<td class="typeName">' + data.typeName + '</td>');
                var $unit = $('<td class="unit">' + data.unit + '</td>');
                var $amount = $('<td class="amount">' + data.amount + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-stock">修改</button><button class="ui tiny red button del-stock">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($typeId);
                $tr.append($stockName);
                $tr.append($typeName);
                $tr.append($unit);
                $tr.append($amount);
                $tr.append($operate);
                $('#stock-list').append($tr);
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
      $('.new-stock').on('click', function () {
        loadStockTypeData();
        $('.new-stock-modal').modal({
            closable: false,
            onDeny: function () {
              clearStockSelect();
              $('#new-stock').form('clear');
            },
            onApprove: function () {
              $('#new-stock').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建库存信息提交
      $('#new-stock').form({
        on: 'change',
        inline: true,
        fields: {
          newStockName: {
            identifier: 'stockName',
            rules: [{
              type: 'empty',
              prompt: '库存名不能为空'
            }]
          },
          newTypeId: {
            identifier: 'typeId',
            rules: [{
              type: 'empty',
              prompt: '库存类型不能为空'
            }]
          },
          newUnit: {
            identifier: 'unit',
            rules: [{
              type: 'empty',
              prompt: '单位不能为空'
            }]
          },
          newAmount: {
            identifier: 'amount',
            rules: [{
                type: 'empty',
                prompt: '数量不能为空'
              },
              {
                type: 'integer',
                prompt: '请输入整数'
              }
            ]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'stock insert',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
          if (response.error != null) {
            alert(response.error);
          } else {
            clearStockSelect();
            $('#new-stock').form('clear');
            $('.new-stock-modal').modal('hide');
            loadSearchStockList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改库存模态框
      $(document).on('click', '.mod-stock', function () {
        loadStockTypeData();
        $('#mod-stock-id').text($(this).parent().parent().find('.stockId').text());
        $('#mod-stock').find('input[name="stockName"]').val($(this).parent().parent().find('.stockName').text());
        $('.mod-stock-select select').val($(this).parent().parent().find('.typeId').text());
        $('.mod-stock-select .text').removeClass('default');
        $('.mod-stock-select .text').text($(this).parent().parent().find('.typeName').text());
        $('#mod-stock').find('input[name="unit"]').val($(this).parent().parent().find('.unit').text());
        $('#mod-stock').find('input[name="amount"]').val($(this).parent().parent().find('.amount').text());
        $('.mod-stock-modal').modal({
            closable: false,
            onDeny: function () {
              clearStockSelect();
              $('#mod-stock').form('clear');
              $('#mod-stock-id').text('');
            },
            onApprove: function () {
              $('#mod-stock').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改库存信息提交
      $('#mod-stock').form({
        on: 'change',
        inline: true,
        fields: {
          modStockName: {
            identifier: 'stockName',
            rules: [{
              type: 'empty',
              prompt: '库存名不能为空'
            }]
          },
          modTypeId: {
            identifier: 'typeId',
            rules: [{
              type: 'empty',
              prompt: '库存类型不能为空'
            }]
          },
          modUnit: {
            identifier: 'unit',
            rules: [{
              type: 'empty',
              prompt: '单位不能为空'
            }]
          },
          modAmount: {
            identifier: 'amount',
            rules: [{
                type: 'empty',
                prompt: '数量不能为空'
              },
              {
                type: 'integer',
                prompt: '请输入整数'
              }
            ]
          }
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'stock update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getCookie('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-stock-id').text() != '') {
            settings.data.id = $('#mod-stock-id').text();
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
            $('#mod-stock-id').text('');
            clearStockSelect();
            $('#mod-stock').form('clear');
            $('.mod-stock-modal').modal('hide');
            loadSearchStockList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除库存模态框
      $(document).on('click', '.del-stock', function () {
        $('#del-stock-id').text($(this).parent().parent().find('.stockId').text())
        $('.del-stock-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-stock-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'stock delete',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getCookie('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-stock-id').text() != '') {
                    settings.data.id = $('#del-stock-id').text();
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
                    $('#del-stock-id').text('');
                    $('.del-stock-modal').modal('hide');
                    loadSearchStockList(1, 10, 'search');
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

      function loadStockTypeData() {
        $.each(stockTypeData, function (i, data) {
          var $option = $('<option value="' + data.id + '">' + data.typeName + '</option>');
          $('.new-stock-select select').append($option);
          $('.mod-stock-select select').append($option.clone());
        })
      }

      function clearStockSelect() {
        $('.new-stock-select select').find('option:not(:first)').remove();
        $('.new-stock-select .text').text('');
        $('.mod-stock-select select').find('option:not(:first)').remove();
        $('.mod-stock-select .text').text('');
      }
    })
  </script>
</body>

</html>