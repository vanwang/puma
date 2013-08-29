<meta charset="UTF-8">
<div class="easyui-layout" data-options="fit:true,noheader:true,border:false">  
	<div data-options="region:'north',border:false" style="padding:10px;">
		<div class="puma-form-item" style="height:30px;">
		 	<div class="puma-form-label">
		 		菜单名称
		 	</div>
		 	<div class="puma-form-content">
		 		<input id="menumanagement-addcustommenu-name-input" name="name" class="easyui-validatebox" data-options="required:true,validType:'minLength[5]'" /> 
		 	</div>
		 </div>
	</div>
	<div data-options="region:'center',border:false">
		<div id="menumanagement-addcustommenu-tab-id" class="easyui-tabs" data-options="fit:true,plain:true">
		    <div title="自定义URL" style="padding:10px 20px;" data-options="cache:true">  
		    	<div class="puma-form-item" style="height:30px;">
				 	<div class="puma-form-label">
				 		URL地址：
				 	</div>
				 	<div class="puma-form-content">
				 		<input id="menumanagement-addcustommenuurl-address-input" style="width:200px;" type='text' name='url'  class="easyui-validatebox" data-options="required:true,validType:'url'">
				 	</div>
				 </div>
		    	<div class="puma-form-item" style="height:30px;">
				 	<div class="puma-form-label">
				 		打开方式
				 	</div>
				 	<div class="puma-form-content">
				 		<div>
				 			<input id="menumanagement-addcustommenuurl-using-url-radio" type='radio' name='opentype' checked='true' value="window"> 
				 			<label for="menumanagement-addcustommenuurl-using-url-radio">新窗口打开</label>
				 		</div>
				 		<div>
				 			<input id="menumanagement-addcustommenuurl-using-iframe-radio" type='radio' name='opentype' value="iframe"> 
				 			<label for="menumanagement-addcustommenuurl-using-iframe-radio">iframe形式打开</label>
				 		</div>
				 	</div>
				 </div>
		    </div>  
		    <div title="快捷关联到系统模块" style="overflow:auto;padding:20px;">
		    	<input id="menumanagement-addcustommenuurl-module-combobox" class="easyui-combobox easyui-validatebox" name="language" data-options="required:true,
		    																missingMessage:'请选择菜单对应的模块!',
		    																url: 'plugin/plugincombo.json',  
		    																editable:false,
															                valueField: 'hash',  
															                textField: 'text',  
															                panelWidth: 350,  
															                panelHeight: 'auto',  
															                formatter: function(row){
															                var s2 = $('<span>').attr('style','font-weight:bold;font-size:14px;').text(row.text).after($('<br>')).after($('<span>').attr('style','color:#888').text(row.attributes.hash));
															                s = $('<div>').attr('style','cursor:pointer').append(s2);  
																            return s;  
																        }   ">  
				<!-- <div title="快捷关联到系统模块" style="overflow:auto;padding:20px;">
			    	<ul class="easyui-tree customer-plugin-tree-container" data-options="url:'plugin/plugintree.json'"></ul>
			    </div> -->
		    </div>
		</div> 
	</div>
</div> 