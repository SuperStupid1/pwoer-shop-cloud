package com.powernode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysMenu;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/25 17:18
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenuById(String userId) {
        List<SysMenu> sysMenuList = sysMenuMapper.selectMenuById(userId);
        // 对查询出来的菜单进行组装为树结构
        return getMenuTree(sysMenuList,0);
    }

    /**
     * 构建树状结构
     * @param sysMenuList
     * @param parentId
     * @return
     */
    private List<SysMenu> getMenuTree(List<SysMenu> sysMenuList, long parentId) {
        List<SysMenu> roots = sysMenuList.stream()
                .filter(sysMenu -> sysMenu.getParentId().equals(parentId))
                .collect(Collectors.toList());
        roots.forEach(root->root.setList(getMenuTree(sysMenuList,root.getMenuId())));
        return roots;
    }
}
