<meta charset="UTF-8">  
<div class="easyui-panel" data-options="fit:true" style="width:100%;">  
   	<div style="width:60%;height:100%;float:left;">
   		<div style="width:100%;">
   			<div style="padding:20px;">
   				<span class="left_bt">感谢您体验 PUMA快速开发平台演示程序</span><br>
		              <br>
		            <span class="left_txt">说明：
		            	<br>
				         PUMA Framework是一套基于JAVA语言开发，用于简化各种管理信息系统的开发平台。
				     	六大大栏目完美整合，功能强大，操作简单，后台操作易如反掌，让Java系统开发变的更简单一点！
				     </span>
				     	<br>
				     	适用于了解Java语言的，并且习惯自己编写后台业务逻辑的程序猿们，如果您有中小型项目要开发，而且时间比较紧迫的话，
				     	不妨试试这个平台，应该能加快开发速度，并且功能和性能有一定保证。
   			</div>
   		</div>
   		<div style="width:100%;height:350px;">
   			<div style="padding:20px;height:100%;">
   				<div id="system-home-info-tab-id" class="easyui-tabs" data-options="fit:true,plain:true">
		   			<div title="系统信息" class="plain-tab-content">
	   					<span class="puma-form-label">操作系统</span>
	   					<span class="puma-form-content">${serverInfo.osName}</span>
	   					<span class="puma-form-label">系统架构</span>
	   					<span class="puma-form-content">${serverInfo.osArch}</span>
	   					<span class="puma-form-label">Java版本</span>
	   					<span class="puma-form-content">${serverInfo.javaVersion}</span>
	   					<span class="puma-form-label">应用路径</span>
	   					<span class="puma-form-content">${serverInfo.webApp}</span>
	   					<span class="puma-form-label">JVM内存总量</span>
	   					<span class="puma-form-content">${serverInfo.jvmMemoryTotal}</span>
	   					<span class="puma-form-label">JVM内存使用</span>
	   					<span class="puma-form-content">${serverInfo.jvmMemoryUsed}</span>
	   					<span class="puma-form-label">磁盘大小</span>
	   					<span class="puma-form-content">${serverInfo.diskTotal}</span>
	   					<span class="puma-form-label">磁盘空闲</span>
	   					<span class="puma-form-content">${serverInfo.diskFree}</span>
	   					<span class="puma-form-label">在线人数</span>
	   					<span class="puma-form-content">${serverInfo.userNumbers}</span>
		   			</div>
		   			<div title="版本信息" class="plain-tab-content">
		   				<span class="puma-form-label">Version:</span>
	   					<span class="puma-form-content">1.0 beta</span>
		   			</div>
		   			<div title="版权说明" class="plain-tab-content">
		   				版权说明
		   			</div>
		   			<div title="程序说明" class="plain-tab-content">
		   				程序说明
		   			</div>
		   		</div>
			</div>
   		</div>
   	</div>
   	<div style="width:35%;height:100%;float:left;">
   		<div style="width:100%;height:100%;padding:10px;">
   			<div style="padding-bottom:20px;">
   				<table width="100%" height="144" cellspacing="0" cellpadding="0" border="0" class="datagrid-header">
		          <tbody>
		          <tr class="datagrid-header" style="height:30px;">
		            <td width="93%" style="padding-left:20px;">最新动态</td>
		          </tr>
		          <tr>
		            <td valign="top" height="102">
		            	1. Puma framework V1.0 beta 发布
		            </td>
		          </tr>
		          <tr>
		            <td height="5">&nbsp;</td>
		          </tr>
		        </tbody>
		       </table>
   			</div>
   			<div style="padding-bottom:20px;">
   				<table width="100%" height="144" cellspacing="0" cellpadding="0" border="0" class="datagrid-header">
		          <tbody>
		          <tr class="datagrid-header" style="height:30px;">
		            <td width="93%" style="padding-left:20px;">网友建议</td>
		          </tr>
		          <tr>
		            <td valign="top" height="102">
		            	欢迎广大网友提出建议!
		            </td>
		          </tr>
		          <tr>
		            <td height="5">&nbsp;</td>
		          </tr>
		        </tbody>
		       </table>
   			</div>
   		</div>
   	</div>
</div>  