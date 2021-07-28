package com.shzx.application.common.tool;

import com.shzx.application.business.sysmenu.model.SysMenu;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ToolPrivilege {

	/**
	 * 遍历菜单树，把所有的权限遍历出来放到同一个集合中返回，并且其中所有权限的名称都修改了，以表示层次。
	 *
	 * @return
	 */
	public static List<SysMenu> getAllDepartments(List<SysMenu> sysMenuList) {
		List<SysMenu> topList = new ArrayList<SysMenu>();
		for (SysMenu s : sysMenuList) {
			if("-1".equals(s.getMenuSuperId())){
				topList.add(s);
			}
		}
		walkDepartmentTreeList(topList,sysMenuList);
		return topList;
	}

	/**
	 * 遍历部门树，把遍历出的部门信息放到指定的集合中
	 * @param superList 顶级列表
	 * @param AllList 所有列表
	 * //@param void 返回
	 */
	private static void walkDepartmentTreeList(List<SysMenu> superList, List<SysMenu> AllList) {
		for (SysMenu s : superList) {
			List<SysMenu> childrenList=new ArrayList<>();
			for(SysMenu sysMenu:AllList){
				if(s.getMenuId()==sysMenu.getMenuSuperId()){
					childrenList.add(sysMenu);
				}
			}
			if(CollectionUtils.isNotEmpty(childrenList)){
				s.setChildrenMenu(childrenList);
				walkDepartmentTreeList(childrenList,AllList);
			}
		}
	}
}
