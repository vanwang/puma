<meta charset="UTF-8">
<form method="post" id="qrcode_module_form">
<div style="margin:auto;width:1000px;">
<div class="leftBar">
	<div class="title">URL或其他文本:</div>
	<textarea onblur="(this.value=='')?this.value='http://www.pumaframework.com':this.value;" onfocus="if(this.value=='http://www.pumaframework.com'){this.value='';};this.select();" name="data" class="input-xlarge">http://www.pumaframework.com</textarea>
  	</div>
  	<div class="operateLR">
          <div class="OptDetail span1">
          	<div style="padding:8px;">输出格式:</div>
              <select class="easyui-combobox" name="output" data-options="editable:false" style="width:80px;">
                  <option selected="" value="gif">GIF</option>
                  <option value="jpg">JPEG</option>
                  <option value="png">PNG</option>
              </select>
              <div style="padding:8px;">纠错级别:</div>
              <select class="easyui-combobox" name="error" data-options="editable:false" style="width:80px;">
                  <option selected="" value="L">L 7%</option>
                  <option value="M">M 15%</option>
                  <option value="Q">Q 25%</option>
                  <option value="H">H 30%</option>
              </select>
           <div style="padding:8px;">类型:</div>
      		<select class="easyui-combobox" name="type" data-options="editable:false" style="width:80px;">
                  <option value="0">自动</option>
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                  <option value="6">6</option>
                  <option value="7">7</option>
                  <option value="8">8</option>
                  <option value="9">9</option>
                  <option value="10">10</option>
              </select>
            <div style="padding:8px;">边缘留白:</div>
  			<select class="easyui-combobox" name="margin" data-options="editable:false" style="width:80px;">
                  <option value="0">0</option>
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                  <option value="6">6</option>
                  <option value="7">7</option>
                  <option value="8">8</option>
                  <option value="9">9</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
                  <option value="13">13</option>
                  <option value="14">14</option>
                  <option value="15">15</option>
                  <option value="16">16</option>
                  <option value="17">17</option>
                  <option value="18">18</option>
                  <option value="19">19</option>
                  <option value="20">20</option>
                  <option value="21">21</option>
                  <option value="22">22</option>
                  <option value="23">23</option>
                  <option value="24">24</option>
                  <option value="25">25</option>
                  <option value="26">26</option>
                  <option value="27">27</option>
                  <option value="28">28</option>
                  <option value="29">29</option>
                  <option value="30">30</option>
                  <option value="31">31</option>
                  <option value="32">32</option>
              </select>
            <div style="padding:8px;">原胞大小:</div>
  			<select class="easyui-combobox" name="cellSize" data-options="editable:false" style="width:80px;">
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option selected="" value="4">4</option>
  			</select>
  			<div style="padding-top:10px;">
  				<a id="qrcode-module-generate-code-btn-id" class="easyui-linkbutton">生成QR码</a>         
  			</div>
		</div>
  	</div>
  	<div class="rightBar">
				<div class="title">QR码：</div>
          <div class="QRCodeDiv">
		<div class="QRWrapper">
			<a target="_blank"id="qrcode-module-gen_url"></a>
		</div>
	</div>
  	</div>
</form>
</div>