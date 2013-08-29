<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<meta charset="UTF-8">  
<div id="role-management-layout-id" class="easyui-layout" data-options="fit:true,noheader:true">  
    <div data-options="region:'west'" style="width:200px;border-right:dashed 1px #ddd;">
    	<div class="easyui-layout" data-options="fit:true,noheader:true">  
    		<div data-options="region:'north'" style="">
    			<div style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
			        <a id="add-role-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'<@security.authorize ifNotGranted='/role/createnew.do'>,disabled:true</@security.authorize>">新增</a>  
			        <a id="edit-role-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'<@security.authorize ifNotGranted='/role/update.do'>,disabled:true</@security.authorize>">编辑</a>
			        <a id="delete-role-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'<@security.authorize ifNotGranted='/role/batchdel.do'>,disabled:true</@security.authorize>">删除</a>  
			    </div> 
    		</div>
    		<div data-options="region:'center'">
    			<div id="role-tree-id" style="padding:5px 0px;"></div>
    		</div>
    	</div>
    </div>  
    <div data-options="region:'center'">
    	<div class="easyui-layout" data-options="fit:true,noheader:true">  
    		<div data-options="region:'north'" style="">
    			<div style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
			        <a id="select-all-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-check'">全选</a>  
			        <a id="unselect-all-resrouces-btn-id" "href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-uncheck'">全不选</a>
			        <a id="expand-all-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-category'">全部展开</a>  
			        <a id="collapse-all-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-folder'">全部收起</a>
			        <span class="toolbar-seperator"></span>
			        <a id="refresh-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新</a>
			        <span class="toolbar-seperator"></span>
			        <a id="save-role-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'<@security.authorize ifNotGranted='/role/combineresource.do'>,disabled:true</@security.authorize>">保存</a>
			    </div> 
    		</div>
    		<div data-options="region:'center'">
    			<div id="resource-tree-id" style="padding:5px 0px;"></div>
    		</div>
    	</div>
    </div>  
</div>  