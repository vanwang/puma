<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<meta charset="UTF-8">
<div class="easyui-layout" data-options="fit:true,noheader:true">
	<div data-options="region:'north'">
		<div id="user-management-toolbar-id" style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
		    <a id="user-log-management-toolbar-search-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>  
		    <a id="user-log-management-toolbar-delete-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'<@security.authorize ifNotGranted='/member/batchdel.do'>,disabled:true</@security.authorize>">删除</a>
		    <span class="toolbar-seperator"></span>
		    <a id="user-log-management-toolbar-reset-refresh-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新</a>
		</div>
	</div>
	<div data-options="region:'center'">
		<table id="user-log-grid"></table>
	</div>
</div>