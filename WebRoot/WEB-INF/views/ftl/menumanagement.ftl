<meta charset="UTF-8">  
<div id="menu-management-layout-id" class="easyui-layout" data-options="fit:true,noheader:true">  
    <div data-options="region:'west'" style="width:300px;border-right:dashed 1px #ddd;">
    	<div class="easyui-layout" data-options="fit:true,noheader:true">  
		<div data-options="region:'north'" style="">
			<div style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
		        <a id="manage-menu-tab-btn-id" href="javascript:void(0)">管理标签</a>
		        <a id="add-custom-menu-btn-id" href="javascript:void(0)">管理菜单</a>
		        <a id="refresh-menu-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新</a>
		    </div> 
		</div>
		<div data-options="region:'center'">
			<div id="tab-menu-tree-id" style="padding:0px 0px;" data-options="fit:true,plain:true,tabPosition:'bottom'"></div>
			<div id="manage-menu-tab-btn-split-menu-id" style="width:100px;">
				<div id="add-menu-tab-btn-id">新增标签</div>
				<div id="edit-menu-tab-btn-id">编辑标签</div>
				<div id="delete-menu-tab-btn-id">删除标签</div>
			</div>
			<div id="add-module-btn-split-menu-id" style="width:100px;">  
				<div id="add-module-btn-split-menu-addcustom-id" data-options="iconCls:'icon-cancel'">自定义菜单</div>  
				<div id="add-module-group-btn-split-menu-addmodule-id" data-options="iconCls:'icon-ok'">菜单组</div>  
			    <div id="add-module-btn-split-menu-addmodule-id" data-options="iconCls:'icon-ok'">模块菜单</div>  
			     <div id="delete-menu-btn-id" data-options="iconCls:'icon-delete'">删除菜单项</div> 
			</div>
		</div>
		</div>
    </div>  
    <div id="menu-removable-area-id" data-options="region:'center'">
    	<div class="easyui-layout" data-options="fit:true,noheader:true">  
			<div data-options="region:'north'" style="">
				<div style="padding:1px 2px;border-bottom:solid 1px #ddd;">  
			        <a id="menu-management-refresh-resrouces-btn-id" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'">刷新模块</a>
			    </div> 
			</div>
			<div data-options="region:'center'">
				<div id="delete-drop-area" class="easyui-panel" data-options="fit:true">
					<div id="delete-drop-inner-area">
					</div>
				</div>
    	    	<ul id="module-selector-content-id" class="plugin-tree-container">a</ul>  
			</div>
		</div>
    </div>  
</div>  