<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<meta charset="UTF-8">
<div class="easyui-layout" data-options="fit:true,noheader:true">
	<div data-options="region:'north'">
		<div id="user-management-toolbar-id" style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
			<a id="resource-management-toolbar-add-new-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'<@security.authorize ifNotGranted='/resource/create.do'>,disabled:true</@security.authorize>">新增</a>  
			<a id="resource-management-toolbar-edit-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'<@security.authorize ifNotGranted='/resource/update.do'>,disabled:true</@security.authorize>">编辑</a>  
		    <a id="resource-management-toolbar-delete-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'<@security.authorize ifNotGranted='/resource/deleteresource.do'>,disabled:true</@security.authorize>">删除</a>
		    <span class="toolbar-seperator"></span>
		    <a id="resource-management-toolbar-expand-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-category'">全部展开</a>
		    <a id="resource-management-toolbar-collapse-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-folder'">全部收起</a>
		    <span class="toolbar-seperator"></span>
		    <a id="resource-management-toolbar-refresh-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新</a>
		</div>
	</div>
	<div data-options="region:'center'">
		<table id="resource-tree-grid"></table>
	</div>
</div>
