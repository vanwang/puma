<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<meta charset="UTF-8">
<div class="easyui-layout" data-options="fit:true,noheader:true">
	<div data-options="region:'north'">
		<div id="user-management-toolbar-id" style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
			<a id="user-management-toolbar-add-new-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-suppliers'<@security.authorize ifNotGranted='/member/createnew.do'>,disabled:true</@security.authorize>">新增</a>  
			<a id="user-management-toolbar-edit-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'<@security.authorize ifNotGranted='/member/update.do'>,disabled:true</@security.authorize>">编辑</a>  
		    <a id="user-management-toolbar-search-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>  
		    <a id="user-management-toolbar-delete-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'<@security.authorize ifNotGranted='/member/batchdel.do'>,disabled:true</@security.authorize>">删除</a>
		    <a id="user-management-toolbar-reset-password-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-resetpass'<@security.authorize ifNotGranted='/member/batchresetpass.do'>,disabled:true</@security.authorize>">重设密码</a>
		    <span class="toolbar-seperator"></span>
		    <a id="user-management-toolbar-reset-refresh-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新</a>
		</div>
	</div>
	<div data-options="region:'center'">
		<table id="user-grid"></table>
	</div>
</div>