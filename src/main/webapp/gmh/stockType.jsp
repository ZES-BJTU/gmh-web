<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>光美焕-库存分类管理</title>
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
    <button class="ui blue button load-stock-type-list" style="display:none;"></button>
    <jsp:include page="header.jsp"/>
    <div class="main-wrapper">
        <div class="ui fluid container">
            <div class="ui grid">
                <div class="row">
                    <div class="twelve wide left aligned column">
                        <form id="" class="ui form search stock-type-search">
                            <div class="ui action input">
                                <input type="text" class="search-input" placeholder="请输入搜索内容">
                                <button class="ui button submit">搜索</button>
                            </div>
                        </form>
                    </div>
                    <div class="four wide right aligned column">
                        <button class="ui blue button new-stock-type">新建</button>
                    </div>
                </div>
                <div class="one column row">
                    <div class="column">
                        <table class="ui compact table theme">
                            <thead>
                                <tr>
                                    <th>库存分类名</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="stock-type-list">
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
    <div class="ui mini modal new-stock-type-modal">
        <div class="header">新建库存分类</div>
        <div class="content">
            <form id="new-stock-type" class="ui form">
                <div class="field">
                    <label>库存分类名</label>
                    <input type="text" name="typeName" placeholder="请输入库存分类名">
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
    <div class="ui mini modal mod-stock-type-modal">
        <div class="header">修改库存分类</div>
        <div class="content">
            <span id="mod-stock-type-id" style="display:none"></span>
            <form id="mod-stock-type" class="ui form">
                <div class="field">
                    <label>库存分类名</label>
                    <input type="text" name="typeName" placeholder="请输入库存分类名">
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
    <div class="ui basic tiny modal del-stock-type-modal">
        <div class="ui icon header">
            <i class="trash icon"></i>确认删除？
        </div>
        <div class="content">
            <span id="del-stock-type-id" style="display:none"></span>
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
    
        activeMenu('stockType');
        
        // 存储搜索的信息,用于点击页码时调用
        var searchInfo = '';
        $(document).ready(function () {
            //库存分类管理搜索
            $('.stock-type-search').form({
                onSuccess: function (e) {
                    //阻止表单的提交
                    e.preventDefault();
                    searchInfo = $('.search-input').val();
                    loadSearchStockTypeList(1, 10, 'search');
                }
            })

            $('.stock-type-search').form('submit');

            //分页按钮点击事件
            $(document).on('click', '.paging .item', function () {
                loadSearchStockTypeList($(this).text(), 10, 'paging');
            })

            function loadSearchStockTypeList(pagenum, pagesize, type) {
                $('.load-stock-type-list').api({
                    action: 'stockType search',
                    method: 'GET',
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
                            return settings;
                        } else {
                            settings.data.searchString = searchInfo;
                            return settings;
                        }
                    },
                    onSuccess: function (response) {
                        if (response.error != null) {
                            alert(response.error);
                        } else {
                            $('#stock-type-list').empty();
                            $('.paging').empty();
                            for (var i = 0; i < response.data.totalPages; i++) {
                                var $item = $('<a class="item">' + (i + 1) + '</a>');
                                $('.paging').append($item);
                            }
                            $.each(response.data.data, function (i, data) {
                                var $tr = $('<tr></tr>');
                                var $id = $('<td class="stockTypeId" style="display:none">' +
                                    data.id + '</td>');
                                var $typeName = $('<td class="typeName">' + data.typeName +
                                    '</td>');
                                var $operate = $(
                                    '<td><button class="ui tiny orange button mod-stock-type">修改</button><button class="ui tiny red button del-stock-type">删除</button></td>'
                                )
                                $tr.append($id);
                                $tr.append($typeName);
                                $tr.append($operate);
                                $('#stock-type-list').append($tr);
                            })
                            $('.paging').children().eq(pagenum - 1).addClass('active');
                        }
                    },
                    onFailure: function (response) {
                        alert('服务器开小差了');
                    }
                })
            }

            //新建库存分类模态框
            $('.new-stock-type').on('click', function () {
                $('.new-stock-type-modal').modal({
                        closable: false,
                        onDeny: function () {
                            $('#new-stock-type').form('clear');
                        },
                        onApprove: function () {
                            $('#new-stock-type').submit();
                            return false;
                        }
                    })
                    .modal('show');
            })
            //新建店铺信息提交
            $('#new-stock-type').form({
                on: 'change',
                inline: true,
                fields: {
                    newTypeName: {
                        identifier: 'typeName',
                        rules: [{
                            type: 'empty',
                            prompt: '库存分类名不能为空'
                        }]
                    }
                },
                onSuccess: function (e) {
                    //阻止表单的提交
                    e.preventDefault();
                }
            }).api({
                action: 'stockType insert',
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
                        $('#new-stock-type').form('clear');
                        $('.new-stock-type-modal').modal('hide');
                        loadSearchStockTypeList(1, 10, 'search');
                    }
                },
                onFailure: function (response) {
                    alert('服务器开小差了');
                },
            });

            //修改店铺模态框
            $(document).on('click', '.mod-stock-type', function () {
                $('#mod-stock-type-id').text($(this).parent().parent().find('.stockTypeId').text())
                $('#mod-stock-type').find('input[name="typeName"]').val($(this).parent().parent().find(
                    '.typeName').text());
                $('.mod-stock-type-modal').modal({
                        closable: false,
                        onDeny: function () {
                            $('#mod-stock-type').form('clear');
                            $('#mod-stock-type-id').text('');
                        },
                        onApprove: function () {
                            $('#mod-stock-type').submit();
                            return false;
                        }
                    })
                    .modal('show');
            })
            //修改库存分类信息提交
            $('#mod-stock-type').form({
                on: 'change',
                inline: true,
                fields: {
                    modTypeName: {
                        identifier: 'typeName',
                        rules: [{
                            type: 'empty',
                            prompt: '库存分类名不能为空'
                        }]
                    }
                },
                onSuccess: function (e) {
                    //阻止表单的提交
                    e.preventDefault();
                }
            }).api({
                action: 'stockType update',
                method: 'POST',
                serializeForm: true,
                beforeXHR: function (xhr) {
                    verifyToken();
                    xhr.setRequestHeader('X-token', getCookie('token'));
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                },
                beforeSend: function (settings) {
                    if ($('#mod-stock-type-id').text() != '') {
                        settings.data.id = $('#mod-stock-type-id').text();
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
                        $('#mod-stock-type-id').text('');
                        $('#mod-stock-type').form('clear');
                        $('.mod-stock-type-modal').modal('hide');
                        loadSearchStockTypeList(1, 10, 'search');
                    }
                },
                onFailure: function (response) {
                    alert('服务器开小差了');
                }
            });

            //删除店铺模态框
            $(document).on('click', '.del-stock-type', function () {
                $('#del-stock-type-id').text($(this).parent().parent().find('.stockTypeId').text())
                $('.del-stock-type-modal').modal({
                        closable: false,
                        onDeny: function () {
                            $('#del-stock-type-id').text('');
                        },
                        onApprove: function () {
                            $('.fake-button').api({
                                action: 'stockType delete',
                                method: 'POST',
                                on: 'now',
                                beforeXHR: function (xhr) {
                                    verifyToken();
                                    xhr.setRequestHeader('X-token', getCookie(
                                        'token'));
                                    xhr.setRequestHeader('Content-Type',
                                        'application/x-www-form-urlencoded');
                                },
                                beforeSend: function (settings) {
                                    if ($('#del-stock-type-id').text() != '') {
                                        settings.data.id = $('#del-stock-type-id').text();
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
                                        $('#del-stock-type-id').text('');
                                        $('.del-stock-type-modal').modal('hide');
                                        loadSearchStockTypeList(1, 10, 'search');
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