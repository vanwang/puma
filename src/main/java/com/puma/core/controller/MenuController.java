package com.puma.core.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puma.common.BusinessHandler;
import com.puma.common.ResponseMessage;
import com.puma.core.base.BaseController;
import com.puma.core.domain.Member;
import com.puma.core.domain.Menu;
import com.puma.core.security.SecurityUtils;
import com.puma.core.service.MenuService;
import com.puma.util.JsonUtils;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping("/savemenutree.json")
	public @ResponseBody ResponseMessage saveMenuTree(final Menu menu, @RequestParam(value = "menu[]",required=false) final String[] menuJson){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Menu m = menuService.findById(menu.getId());
				if(menuJson == null){
					//means deleted all menu, so add new default folder
					JSONObject att = new JSONObject();
					att.put("type", "folder");
					JSONObject jo = new JSONObject();
					jo.put("text", "默认文件夹");
					jo.put("iconCls", "icon-none");
					jo.put("attributes", att);
					jo.put("checked", false);
					jo.put("state", "open");
					m.setMenuJson("["+jo.toString()+"]");
				}else{
					String[] t = menuJson;
					String s = "[";
					int i = 0;
					for(String str:t){
						s+=str;
						if(i < t.length-1){
							s += ",";
						}
					}
					s+="]";
					m.setMenuJson(s);
				}
				
				menuService.save(m);
				return JsonUtils.parseJSON2List(m.getMenuJson());
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/savetaborder.do")
	public @ResponseBody ResponseMessage saveTabOrder(final Menu menu, @RequestParam(value = "ids[]") final String[] ids){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				for(int i = 0; i < ids.length; i++){
					String id = ids[i].replaceAll("tabid-", "");
					Menu m = menuService.findById(id);
					m.setMenuOrder(i);
					menuService.save(m);
				}
				return "success";
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/getallmenutabs.json")
	public @ResponseBody ResponseMessage getAllMenuTabs(){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Sort s = new Sort(Direction.ASC,"menuOrder");
				List<Menu> menuList = menuService.findAllBySort(s);
				
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
				
				for(Menu menu:menuList){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("id", menu.getId());
					map.put("name", menu.getName());
					map.put("isDefault", menu.isDefault());
					map.put("data", JsonUtils.parseJSON2List(menu.getMenuJson()));
					resultList.add(map);
				}
				
				return resultList;
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/getsystemmenus.json")
	public @ResponseBody ResponseMessage getSystemMenus(){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Sort s = new Sort(Direction.ASC,"menuOrder");
				List<Menu> menuList = menuService.findAllBySort(s);
				
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
				
				for(Menu menu:menuList){
					List<Map<String, Object>> mapList = JsonUtils.parseJSON2List(menu.getMenuJson());
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("id", "system-"+menu.getId());
					map.put("name", menu.getName());
					map.put("isDefault", menu.isDefault());
					map.put("data", removeUnauthedMenuItem(mapList));
					resultList.add(map);
				}
				
				return resultList;
			}
		}, logger);
		return response;
	}
	
	private  List<Map<String, Object>> removeUnauthedMenuItem(List<Map<String, Object>> jsonList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map: jsonList){
			Object o = map.get("attributes");
			if(o instanceof JSONObject){
				JSONObject jsonObject = (JSONObject)o;
                String type = (String)jsonObject.get("type");
                if(type.equalsIgnoreCase("folder")){
                	Object child = map.get("children");
                	if(child instanceof ArrayList){
                		ArrayList al = (ArrayList)child;
                		if(al.size() == 0){
                			//jsonList.remove(map);
                		}else{
                			List<Map<String, Object>> newAl = (ArrayList) removeUnauthedMenuItem(al);
                			if(newAl.size() > 0){
                				map.put("children", newAl);
                				resultList.add(map);
                			}
                		}
                	}
                }else if(type.equalsIgnoreCase("plugin")){
                	String authUrl = (String)jsonObject.get("authurl");
                	if(authUrl != null){
                		Member m = SecurityUtils.getAuthedMember();
                		if(null != m){
                			if(m.isSys()){
                				resultList.add(map);
                			}else{
                				Collection<GrantedAuthority> configAttributes = m.getAuthorities();
                				Iterator<GrantedAuthority> ite = configAttributes.iterator();
                				
                				boolean canAccess = false;
                				while( ite.hasNext()){
                					GrantedAuthority ca = ite.next();
                					String userResource = ca.getAuthority();
                					if(userResource.indexOf(authUrl) != -1){
                						canAccess = true;
                						break;
                					}
                				}
                				if(canAccess){
                					resultList.add(map);
                				}
                			}
                		}
                	}else{
                		resultList.add(map);
                	}
                }
            }
		}
		return resultList;
	}
	
	@RequestMapping("/addnewtab.do")
	public @ResponseBody ResponseMessage addNewTab(@RequestParam(value = "name") final String name){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				JSONObject att = new JSONObject();
				att.put("type", "folder");
				JSONObject jo = new JSONObject();
				jo.put("text", "默认文件夹");
				jo.put("iconCls", "icon-none");
				jo.put("attributes", att);
				jo.put("checked", false);
				jo.put("state", "open");
				Menu menu = new Menu();
				menu.setName(name);
				menu.setDefault(false);
				menu.setMenuOrder(Integer.parseInt(menuService.countAll()));
				menu.setMenuJson("["+jo.toString()+"]");
				
				menuService.save(menu);
				
				return menu;
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/updatemenudefaultstatus.do")
	public @ResponseBody ResponseMessage updateMenuDefaultStatus(@RequestParam(value = "menuid") final String menuId,@RequestParam(value = "status") final boolean status){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Menu menu = menuService.findById(menuId);
				menu.setDefault(status);
				menuService.save(menu);
				return menu.getId();
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/getmenutreedata.do")
	public @ResponseBody List<Map<String, Object>> getMenuTreeData(@RequestParam(value = "id") final String id){
		Menu menu = menuService.findById(id);
		return JsonUtils.parseJSON2List(menu.getMenuJson());
		//return menu.getMenuJson();
	}
	
	@RequestMapping("/removetab.do")
	public @ResponseBody ResponseMessage removeTab(@RequestParam(value = "id") final String id){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				return menuService.remove(id);
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/updatetabtitle.do")
	public @ResponseBody ResponseMessage updateTabTitle(@RequestParam(value = "id") final String id,@RequestParam(value = "title") final String title){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Menu menu = menuService.findById(id);
				menu.setName(title);
				menuService.save(menu);
				return "success";
			}
		}, logger);
		return response;
	}
}
