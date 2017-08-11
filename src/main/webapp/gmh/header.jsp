<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  <div id="left-menu" class="ui left fixed vertical pointing menu">
    <a class="item home shop-admin" href="home.jsp">首页</a>
    <a class="item reserve shop-admin" href="reserve.jsp">预约管理</a>
    <a class="item record shop-admin" href="record.jsp">到店美容管理</a>
    <a class="item project shop-admin" href="project.jsp">美容项管理</a>
    <a class="item projectType shop-admin" href="projectType.jsp">美容项分类管理</a>
    <a class="item vip shop-admin" href="vip.jsp">会员管理</a>
    <a class="item vipLevel shop-admin" href="vipLevel.jsp">会员等级管理</a>
    <a class="item stock shop-admin" href="stock.jsp">库存管理</a>
    <a class="item stockType shop-admin" href="stockType.jsp">库存分类管理</a>
    <a class="item employee shop-admin" href="employee.jsp">员工管理</a>
    <a class="item staff admin" href="staff.jsp">用户管理</a>
    <a class="item shop admin" href="shop.jsp">店铺管理</a>
  </div>
  <div class="ui top fixed menu">
       <div class="item">
           <img src="css/images/logo.png">光美焕科技皮肤护理
       </div>
       <div class="ui dropdown right item">
           <span id="user-name"></span>
           <button id="user-type" style="display:none"></button>	
           <i class="dropdown icon"></i>
           <div class="menu">
               <a class="item mod-pwd" href="modifyPassword.jsp">修改密码</a>
               <a class="item logout">退出登录</a>
           </div>
       </div>
   </div>
   
   <div class="ui large modal remind-appointment-modal">
    <div class="header">预约提醒</div>
    <div class="content">
	  <div class="ui grid">
        <div class="one column row">
          <div class="column">
            <table class="ui compact table theme">
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
              <tbody id="remind-appointment-list">
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="actions">
      <div class="ui positive right labeled icon button">
       	 确定
        <i class="checkmark icon"></i>
      </div>
    </div>
  </div>