<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-店铺管理</title>
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
  <button class="ui blue button load-shop-list" style="display:none;"></button>
  <jsp:include page="header.jsp"/>
  <div class="main-wrapper">
    <div class="ui fluid container">
      <div class="ui grid">
        <div class="row">
          <div class="twelve wide left aligned column">
            <form id="" class="ui form search shop-search">
              <div class="ui action input">
                <input type="text" class="search-input" placeholder="请输入搜索内容">
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
          <div class="four wide right aligned column">
            <button class="ui blue button new-shop">新建</button>
          </div>
        </div>
        <!-- <div class="row">
          <div class="four wide left floated column">
            <form id="" class="ui form search shop-search">
              <div class="ui action input">
                <input type="text" name="searchString" placeholder="请输入搜索内容">
                <select class="ui selection dropdown">
                  <option value="all">All</option>
                  <option selected="" value="articles">Articles</option>
                  <option value="products">Products</option>
                </select>
                <select class="ui selection dropdown">
                  <option value="all">All</option>
                  <option selected="" value="articles">Articles</option>
                  <option value="products">Products</option>
                </select>
                <select class="ui selection dropdown">
                  <option value="all">All</option>
                  <option selected="" value="articles">Articles</option>
                  <option value="products">Products</option>
                </select>
                <button class="ui button submit">搜索</button>
              </div>
            </form>
          </div>
        </div> -->
        <!-- <div class="row">
          <div class="sixteen column">
            <form class="ui form">
              <div class="ui error message">
              </div>
            </form>
          </div>
        </div> -->
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
              <thead>
                <tr>
                  <th>店铺名</th>
                  <th>负责人</th>
                  <th>负责人电话</th>
                  <th>地址</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="shop-list">
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
  <div class="ui mini modal new-shop-modal">
    <div class="header">新建店铺</div>
    <div class="content">
      <form id="new-shop" class="ui form">
        <div class="field">
          <label>店铺名</label>
          <input type="text" name="shopName" placeholder="请输入店铺名">
        </div>
        <div class="field">
          <label>负责人</label>
          <input type="text" name="manager" placeholder="请输入负责人姓名">
        </div>
        <div class="field">
          <label>负责人电话</label>
          <input type="text" name="phone" placeholder="请输入负责人电话">
        </div>
        <div class="field">
          <label>地址</label>
          <input type="text" name="address" placeholder="请输入地址">
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
  <div class="ui mini modal mod-shop-modal">
    <div class="header">修改店铺</div>
    <div class="content">
      <span id="mod-shop-id" style="display:none"></span>
      <form id="mod-shop" class="ui form">
        <div class="field">
          <label>店铺名</label>
          <input type="text" name="shopName" placeholder="请输入店铺名">
        </div>
        <div class="field">
          <label>负责人</label>
          <input type="text" name="manager" placeholder="请输入负责人姓名">
        </div>
        <div class="field">
          <label>负责人电话</label>
          <input type="text" name="phone" placeholder="请输入负责人电话">
        </div>
        <div class="field">
          <label>地址</label>
          <input type="text" name="address" placeholder="请输入地址">
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
  <div class="ui basic tiny modal del-shop-modal">
    <div class="ui icon header">
      <i class="trash icon"></i>将删除该店铺所有相关联的信息！<br />确认删除？
    </div>
    <div class="content">
      <span id="del-shop-id" style="display:none"></span>
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
  
    activeMenu('shop');
    
    // 存储搜索的信息,用于点击页码时调用
    var searchInfo = '';
    $(document).ready(function () {
      //店铺管理搜索
      $('.shop-search').form({
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
          searchInfo = $('.search-input').val();
          loadSearchShopList(1, 10, 'search');
        }
      })

      $('.shop-search').form('submit');

      //分页按钮点击事件
      $(document).on('click', '.paging .last-page', function () {
        var pageNow = Number($('.page-now').val());
        if(pageNow > 1){
          loadSearchShopList(Number(pageNow - 1), 10, 'paging');
        }else{
        alert('已是第一页！')
        }
      })
      //分页按钮点击事件
      $(document).on('click', '.paging .next-page', function () {
          var pageNow = Number($('.page-now').val());
          var pageTotal = Number($('.page-total').text());
          if(pageNow < pageTotal){
            loadSearchShopList(Number(pageNow + 1), 10, 'paging');
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
                loadSearchShopList(Number($('.page-now').val()), 10, 'paging');
              }else{
              alert('最多' + pageTotal + '页！')
              }
          }else{
              alert("请输入正确的页码!");
          }
          }
      });

      function loadSearchShopList(pagenum, pagesize, type) {
        $('.load-shop-list').api({
          action: 'shop search',
          method: 'GET',
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
              $('#shop-list').empty();
              $('.paging').empty();
              $('.paging').append($('<a class="item last-page"><i class="icon left arrow"></i></a>'));
              $('.paging').append($('<a class="item">第<input class="page-now" value=' + pagenum + '>页 / 共<span class="page-total">' + response.data.totalPages + '</span>页</a>'));
              $('.paging').append($('<a class="item next-page"><i class="icon right arrow"></i></a>'));
              
              $.each(response.data.data, function (i, data) {
                var $tr = $('<tr></tr>');
                var $id = $('<td class="shopId" style="display:none">' + data.id + '</td>');
                var $shopName = $('<td class="shopName">' + data.shopName + '</td>');
                var $manager = $('<td class="shopManager">' + data.manager + '</td>');
                var $phone = $('<td class="shopPhone">' + data.phone + '</td>');
                var $address = $('<td class="shopAddress">' + data.address + '</td>');
                var $operate = $(
                  '<td><button class="ui tiny orange button mod-shop">修改</button><button class="ui tiny red button del-shop">删除</button></td>'
                )
                $tr.append($id);
                $tr.append($shopName);
                $tr.append($manager);
                $tr.append($phone);
                $tr.append($address);
                $tr.append($operate);
                $('#shop-list').append($tr);
              })
            }
          },
          onFailure: function (response) {
            alert('服务器开小差了');
          }
        })
      }

      //新建店铺模态框
      $('.new-shop').on('click', function () {
        $('.new-shop-modal').modal({
            closable: false,
            onDeny: function () {
              $('#new-shop').form('clear');
            },
            onApprove: function () {
              $('#new-shop').submit();
              return false;
            }
          })
          .modal('show');
      })
      //新建店铺信息提交
      $('#new-shop').form({
        on: 'submit',
        inline: true,
        fields: {
          newShopName: {
            identifier: 'shopName',
            rules: [{
              type: 'empty',
              prompt: '店铺名不能为空'
            }]
          },
          newShopManager: {
            identifier: 'manager',
            rules: [{
              type: 'empty',
              prompt: '负责人不能为空'
            }]
          },
          newShopPhone: {
            identifier: 'phone',
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
          newShopAddress: {
            identifier: 'address',
            rules: [{
              type: 'empty',
              prompt: '地址不能为空'
            }]
          },
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'shop insert',
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
            $('#new-shop').form('clear');
            $('.new-shop-modal').modal('hide');
            loadSearchShopList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        },
      });

      //修改店铺模态框
      $(document).on('click', '.mod-shop', function () {
        $('#mod-shop-id').text($(this).parent().parent().find('.shopId').text())
        $('#mod-shop').find('input[name="shopName"]').val($(this).parent().parent().find('.shopName').text());
        $('#mod-shop').find('input[name="manager"]').val($(this).parent().parent().find('.shopManager').text());
        $('#mod-shop').find('input[name="phone"]').val($(this).parent().parent().find('.shopPhone').text());
        $('#mod-shop').find('input[name="address"]').val($(this).parent().parent().find('.shopAddress').text());
        $('.mod-shop-modal').modal({
            closable: false,
            onDeny: function () {
              $('#mod-shop').form('clear');
              $('#mod-shop-id').text('');
            },
            onApprove: function () {
              $('#mod-shop').submit();
              return false;
            }
          })
          .modal('show');
      })
      //修改店铺信息提交
      $('#mod-shop').form({
        on: 'submit',
        inline: true,
        fields: {
          modShopName: {
            identifier: 'shopName',
            rules: [{
              type: 'empty',
              prompt: '店铺名不能为空'
            }]
          },
          modShopManager: {
            identifier: 'manager',
            rules: [{
              type: 'empty',
              prompt: '负责人不能为空'
            }]
          },
          modShopPhone: {
            identifier: 'phone',
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
          modShopAddress: {
            identifier: 'address',
            rules: [{
              type: 'empty',
              prompt: '地址不能为空'
            }]
          },
        },
        onSuccess: function (e) {
          //阻止表单的提交
          e.preventDefault();
        }
      }).api({
        action: 'shop update',
        method: 'POST',
        serializeForm: true,
        beforeXHR: function (xhr) {
          verifyToken();
          xhr.setRequestHeader('X-token', getSessionStorage('token'));
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        beforeSend: function (settings) {
          if ($('#mod-shop-id').text() != '') {
            settings.data.id = $('#mod-shop-id').text();
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
            $('#mod-shop-id').text('');
            $('#mod-shop').form('clear');
            $('.mod-shop-modal').modal('hide');
            loadSearchShopList(1, 10, 'search');
          }
        },
        onFailure: function (response) {
          alert('服务器开小差了');
        }
      });

      //删除店铺模态框
      $(document).on('click', '.del-shop', function () {
        $('#del-shop-id').text($(this).parent().parent().find('.shopId').text())
        $('.del-shop-modal').modal({
            closable: false,
            onDeny: function () {
              $('#del-shop-id').text('');
            },
            onApprove: function () {
              $('.fake-button').api({
                action: 'shop delByIds',
                method: 'POST',
                on: 'now',
                beforeXHR: function (xhr) {
                  verifyToken();
                  xhr.setRequestHeader('X-token', getSessionStorage('token'));
                  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                  if ($('#del-shop-id').text() != '') {
                    settings.data.id = $('#del-shop-id').text();
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
                    $('#del-shop-id').text('');
                    $('.del-shop-modal').modal('hide');
                    loadSearchShopList(1, 10, 'search');
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