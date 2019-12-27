/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.boot.core.common.constant.factory;

import com.boot.core.common.constant.cache.Cache;
import com.boot.core.common.constant.cache.CacheKey;
import com.boot.core.common.constant.state.ManagerStatus;
import com.boot.core.common.constant.state.MenuStatus;
import com.boot.core.kernel_core.util.SpringContextHolder;
import com.boot.core.kernel_core.util.ToolUtil;
import com.boot.core.log.LogObjectHolder;
import com.boot.modular.system.dao.*;
import com.boot.modular.system.model.*;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private NoticeMapper noticeMapper = SpringContextHolder.getBean(NoticeMapper.class);

    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    /**
     * 根据用户id获取用户名称
     */
    @Override
    @Cacheable(value = Cache.USER, key = "'" + CacheKey.TRUENAME_USER_ID + "'+#userId")
    public String getUserNameById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            return user.getTrueName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取用户账号
     */
    @Override
    @Cacheable(value = Cache.USER, key = "'" + CacheKey.LOGINID_USER_ID + "'+#userId")
    public String getUserAccountById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            return user.getLoginId();
        } else {
            return "--";
        }
    }

    /**
     * 删除所有的用户缓存
     **/
    @Override
    @CacheEvict(value = Cache.USER, allEntries = true)
    public void removeAllUserCache() {
    }

    /**
     * 通过角色ids获取角色名称
     */
    @Override
    @Cacheable(value = Cache.ROLE, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    public String getRoleName(String roleIds) {
        if (ToolUtil.isEmpty(roleIds)) {
            return "";
        }
        Integer[] roles = Convert.toIntArray(roleIds);
        StringBuilder sb = new StringBuilder();
        for (int role : roles) {
            Role roleObj = roleMapper.selectById(role);
            if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
                sb.append(roleObj.getName()).append(",");
            }
        }
        return StrUtil.removeSuffix(sb.toString(), ",");
    }

    /**
     * 通过角色id获取角色名称
     */
    @Override
    @Cacheable(value = Cache.ROLE, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色英文名称
     */
    @Override
    @Cacheable(value = Cache.ROLE, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    public String getSingleRoleTip(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getTips();
        }
        return "";
    }

    /**
     * 删除所有的权限缓存
     **/
    @Override
    @CacheEvict(value = Cache.ROLE, allEntries = true)
    public void removeAllRoleCache() {
    }

    /**
     * 获取部门名称
     */
    @Override
    @Cacheable(value = Cache.DEPT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Integer deptId) {
        Dept dept = deptMapper.selectById(deptId);
        if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getFullname())) {
            return dept.getFullname();
        }
        return "";
    }


    /**
     * 删除所有的部门缓存
     **/
    @Override
    @CacheEvict(value = Cache.DEPT, allEntries = true)
    public void removeAllDeptCache() {
    }


    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNames(String menuIds) {
        Integer[] menus = Convert.toIntArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (int menu : menus) {
            Menu menuObj = menuMapper.selectById(menu);
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrUtil.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    @Override
    public String getMenuName(Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuMapper.selectById(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取菜单名称通过编号
     */
    @Override
    public String getMenuNameByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        } else {
            Menu param = new Menu();
            param.setCode(code);
            Menu menu = menuMapper.selectOne(param);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取字典名称
     */
    @Override
    public String getDictName(Integer dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictMapper.selectById(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    /**
     * 获取通知标题
     */
    @Override
    public String getNoticeTitle(Integer dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Notice notice = noticeMapper.selectById(dictId);
            if (notice == null) {
                return "";
            } else {
                return notice.getTitle();
            }
        }
    }

    /**
     * 根据字典名称和字典中的值获取对应的名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'dictionary'+#name+#val")
    public String getDictsByName(String name, Integer val) {
        Dict temp = new Dict();
        temp.setName(name);
        Dict dict = dictMapper.selectOne(temp);
        if (dict == null) {
            return "";
        } else {
            Wrapper<Dict> wrapper = new EntityWrapper<>();
            wrapper = wrapper.eq("pid", dict.getId());
            List<Dict> dicts = dictMapper.selectList(wrapper);
            for (Dict item : dicts) {
                if (item.getNum() != null && item.getNum().equals(val)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 根据字典名称和字典中的值删除缓存中的记录
     */
    @Override
    @CacheEvict(value = Cache.CONSTANT, key = "'dictionary'+#name+#val")
    public void delDictsByName(String name, Integer val) {
    }

    /**
     * 根据父类编码获取词典列表
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'dictionary'+#code")
    public Object getDictsByParentCode(String code) {
        Object list = dictMapper.selectByParentCode(code);
        if (list == null) {
            return "";
        } else {
            return list;
        }
    }

    /**
     * 根据父类编码删除词典列表
     */
    @Override
    @CacheEvict(value = Cache.CONSTANT, key = "'dictionary'+#code")
    public void delDictsByParentCode(String code) {

    }

    /**
     * 获取性别名称
     */
    @Override
    public String getSexName(Integer sex) {
        return getDictsByName("性别", sex);
    }

    /**
     * 获取资源类型名称
     */
    @Override
    public String getResourceitemName(String resourceitem) {
        String[] split_resourceitem = resourceitem.split(",");
        String resourceitemName = "";
        for (int i = 0; i < split_resourceitem.length; i++) {
            if (i == split_resourceitem.length - 1) {
                resourceitemName += getDictsByName("资源类型", Integer.parseInt(split_resourceitem[i]));
            } else {
                resourceitemName += getDictsByName("资源类型", Integer.parseInt(split_resourceitem[i])) + ",";
            }
        }
        return resourceitemName;
    }

    /**
     * 解决简介乱码
     *
     * @param intro
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String getIntroName(String intro) throws UnsupportedEncodingException {
        return URLDecoder.decode(intro, "utf-8");
    }

    /**
     * 获取用户登录状态
     */
    @Override
    public String getStatusName(Integer status) {
        return ManagerStatus.valueOf(status);
    }

    /**
     * 获取菜单状态
     */
    @Override
    public String getMenuStatusName(Integer status) {
        return MenuStatus.valueOf(status);
    }

    /**
     * 查询字典
     */
    @Override
    public List<Dict> findInDict(Integer id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        } else {
            EntityWrapper<Dict> wrapper = new EntityWrapper<>();
            List<Dict> dicts = dictMapper.selectList(wrapper.eq("pid", id));
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    /**
     * 获取子部门id
     */
    @Override
    public List<Integer> getSubDeptId(Integer deptid) {
        Wrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("pids", "%[" + deptid + "]%");
        List<Dept> depts = this.deptMapper.selectList(wrapper);

        ArrayList<Integer> deptids = new ArrayList<>();

        if (depts != null && depts.size() > 0) {
            for (Dept dept : depts) {
                deptids.add(dept.getId());
            }
        }

        return deptids;
    }

    /**
     * 获取所有父部门id
     */
    @Override
    public List<Integer> getParentDeptIds(Integer deptid) {
        Dept dept = deptMapper.selectById(deptid);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Integer> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Integer.valueOf(StrUtil.removeSuffix(StrUtil.removePrefix(s, "["), "]")));
        }
        return parentDeptIds;
    }

    /**
     * 通过手机号生成验证码并存入缓存
     *
     * @param phonenum
     * @return
     */
    @Override
    @CachePut(value = Cache.CONSTANT, key = "'" + CacheKey.PHONE_NUM + "'+#phonenum")
    public String generateVerifyCode(String phonenum) {
        String verifyCode = String.valueOf((int) (Math.random() * 900000 + 100000));
        return verifyCode;
    }

    /**
     * 通过手机号从缓存中取出验证码
     *
     * @param phonenum
     * @return
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.PHONE_NUM + "'+#phonenum")
    public String getVerifyCode(String phonenum) {
        return "缓存中没有验证码";
    }

    /**
     * 通过usercenteruserid存入token
     *
     * @param usercenteruserid
     * @param token
     * @return
     */
    @Override
    @CachePut(value = Cache.CONSTANT, key = "'" + CacheKey.USERCENTERUSER_ID + "'+#usercenteruserid")
    public String setUsercenterToken(String usercenteruserid, String token) {
        return token;
    }

    /**
     * 通过usercenteruserid获取token
     *
     * @param usercenteruserid
     * @return
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.USERCENTERUSER_ID + "'+#usercenteruserid")
    public String getUsercenterToken(String usercenteruserid) {
        return "";
    }


}
