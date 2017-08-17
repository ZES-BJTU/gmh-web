<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>光美焕-查看预约</title>
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
    <script src="js/script.js"></script>
</head>

<body>
    <button class="ui blue button fake-button" style="display:none;"></button>
    <button class="ui blue button load-appointment-list" style="display:none;"></button>
    <div class="ui top fixed menu">
        <div class="item">
            <img src="css/images/logo.png">光美焕科技皮肤护理
        </div>
        <div class="ui dropdown right item">
            <span id="user-name"></span>
            <i class="dropdown icon"></i>
            <div class="menu">
                <a class="item" href="modifyPassword.jsp">修改密码</a>
                <a class="item logout">退出登录</a>
            </div>
        </div>
    </div>
    <div class="mobile-wrapper">
        <div class="ui fluid container">
            <div class="ui grid">
                <div class="one column row">
                    <div class="column">
                        <form id="" class="ui form search check-appointment-search">
                            <select class="ui selection dropdown check-appointment-select">
                                <option value="0">全部美容师</option>
                            </select>
                        </form>
                    </div>
                </div>
                <div class="one column row">
                    <div class="column">
                        <table class="ui compact striped table theme">
                            <thead>
                                <tr>
                                    <th>预约人</th>
                                    <th>联系方式</th>
                                    <th>美容项目</th>
                                    <th>操作员</th>
                                    <th>开始时间</th>
                                    <th>结束时间</th>
                                    <th>是否点排</th>
                                    <th>状态</th>
                                    <th>备注</th>
                                </tr>
                            </thead>
                            <tbody id="appointment-list">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(document).ready(function () {
            $('.fake-button').api({
                action: 'employee listBeauties',
                method: 'GET',
                on: 'now',
                beforeXHR: function (xhr) {
                    verifyToken();
                    xhr.setRequestHeader('X-token', getSessionStorage('token'));
                    xhr.setRequestHeader('Content-Type',
                        'application/x-www-form-urlencoded');
                },
                onSuccess: function (response) {
                    if (response.error != null) {
                        alert(response.error);
                        verifyStatus(response.code);
                    } else {
                        $('.check-appointment-select select').find('option:not(:first)').remove();
                        $.each(response.data, function (i, data) {
                            var $option = $('<option value="' + data.id + '">' + data.emName + '</option>');
                            $('.check-appointment-select select').append($option);
                        })
                        $('.check-appointment-search').form('submit');
                    }
                },
                onFailure: function (response) {
                    alert('服务器开小差了');
                }
            })
            $(document).on('change', '.check-appointment-select', function () {
            	loadSearchAppointmentList();
            })
            //预约搜索
            $('.check-appointment-search').form({
                onSuccess: function (e) {
                    //阻止表单的提交
                    e.preventDefault();
                    loadSearchAppointmentList();
                }
            })


            function loadSearchAppointmentList() {
            	var employeeId = $('.check-appointment-select select').val();
                $('.fake-button').api({
                    action: 'appointment listAppointmentsByEmployee',
                    method: 'POST',
                    on: 'now',
                    beforeXHR: function (xhr) {
                        verifyToken();
                        xhr.setRequestHeader('X-token', getSessionStorage('token'));
                        xhr.setRequestHeader('Content-Type',
                            'application/x-www-form-urlencoded');
                    },
                    beforeSend: function (settings) {
                        settings.data.employeeId = employeeId;
                        return settings;
                    },
                    onSuccess: function (response) {
                        if (response.error != null) {
                            alert(response.error);
                            verifyStatus(response.code);
                        } else {
                            $('#appointment-list').empty();
                            $.each(response.data, function (i, data) {
                                var $tr = $('<tr></tr>');
                                var $memberName = $('<td class="memberName">' + data.memberName + '</td>');
                                var $memberPhone = $('<td class="memberPhone">' + data.phone + '</td>');
                                var $topTypeName = $('<td class="topTypeName" style="display:none">' + data.topTypeName +'</td>');
                                var $typeName = $('<td class="typeName" style="display:none">' + data.typeName + '</td>');
                                var $projectName = $('<td class="projectName">' + data.projectName + '</td>');
                                var $employeeName = $('<td class="employeeName">' + data.employeeName + '</td>');
                                var $beginTime = $('<td class="beginTime">' + toDatetimeMin(data.beginTime) + '</td>');
                                var $endTime = $('<td class="endTime">' + toDatetimeMin(data.endTime) + '</td>');
                                var $line = $('<td class="line">' + data.line + '</td>');
                                var $status = $('<td class="status">' + data.status + '</td>');
                                var remark = (data.remark == null || data.remark == '') ? '无' : String(data.remark);
                                var afterRemark = '';
                                if (remark == '无') {
                                afterRemark = '无'
                                } else if (remark.length > 6) {
                                afterRemark = remark.substring(0, 6) + '...';
                                } else {
                                afterRemark = remark;
                                }
                                var $remark = $('<td class="remark" title="' + remark + '">' + afterRemark + '</td>');

                                $tr.append($memberName);
                                $tr.append($memberPhone);
                                $tr.append($topTypeName);
                                $tr.append($typeName);
                                $tr.append($projectName);
                                $tr.append($employeeName);
                                $tr.append($beginTime);
                                $tr.append($endTime);
                                $tr.append($line);
                                $tr.append($status);
                                $tr.append($remark);
                                $('#appointment-list').append($tr);
                            })
                        }
                    },
                    onFailure: function (response) {
                        alert('服务器开小差了');
                    }
                })
            }
        })
    </script>
</body>

</html>