package com.puma.core.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puma.common.BusinessHandler;
import com.puma.common.LogLevelEnum;
import com.puma.common.LogTypeEnum;
import com.puma.common.ResponseMessage;
import com.puma.core.annotation.LogIt;
import com.puma.core.base.BaseController;
import com.puma.core.service.LogService;

@Controller
@RequestMapping("/log")
public class LogController extends BaseController{
	
	@Autowired
	private LogService logService;
	
	@LogIt(message="获取日志表格数据选项",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@RequestMapping("/logtable.json")
	public @ResponseBody Map<String, Object> memberTableJson(@RequestParam(value="order",required=false)String order
													,@RequestParam(value="page",required=true)Integer page
													,@RequestParam(value="rows",required=true)Integer rows
													,@RequestParam(value="sort",required=false)String sort){
		Map<String,Object> map=new HashMap<String,Object>();
		page -= 1;
		String rNum = logService.countAll();
		map.put("total", rNum);
		
		Sort s = null;
		PageRequest pageable = null;
		if(sort == null){
			s = new Sort(Direction.DESC, "createDate");
		}else if(sort != null){
			s = new Sort(order.equalsIgnoreCase("asc")?Direction.ASC:Direction.DESC, sort);
		}
		pageable=new PageRequest(page, rows, s);
		map.put("rows", logService.findAllByPage(pageable));
			
		return map;
	}
	
	@LogIt(message="删除用户日志信息",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.DELETE)
	@RequestMapping("/batchdel.do")
	public @ResponseBody ResponseMessage batchDelete(@RequestParam(value = "ids[]") final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				List<String> idList = new ArrayList<String>();
				Collections.addAll(idList, ids);
				logService.batchRemove(idList);
				return "success";
			}
		}, logger);
		return response;
	}
	
}
