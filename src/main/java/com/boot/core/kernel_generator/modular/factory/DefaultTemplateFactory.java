package com.boot.core.kernel_generator.modular.factory;

import com.boot.core.kernel_generator.executor.model.GenQo;
import com.boot.core.kernel_core.util.ToolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultTemplateFactory {
    public static List<Map<String, Object>> getDefaultTemplates() {
        ArrayList<Map<String, Object>> templates = new ArrayList();
        templates.add(create("controllerSwitch", "controller-控制器模板"));
        templates.add(create("entitySwitch", "entity-实体模板"));
        templates.add(create("serviceSwitch", "service-service模板"));
        templates.add(create("daoSwitch", "dao-dao模板"));
        templates.add(create("indexPageSwitch", "indexPage-首页模板"));
        templates.add(create("addPageSwitch", "addPage-添加页面模板"));
        templates.add(create("editPageSwitch", "editPage-编辑页面模板"));
        templates.add(create("jsSwitch", "indexJs-主页js模板"));
        templates.add(create("infoJsSwitch", "infoJs-详情页js模板"));
        templates.add(create("sqlSwitch", "sql-sql语句模板"));
        return templates;
    }


    public static GenQo getDefaultParams() {
        GenQo genQo = new GenQo();
        genQo.setProjectPath(ToolUtil.getWebRootPath(null));
        genQo.setAuthor("admin");
        genQo.setProjectPackage("com.boot");
        genQo.setCorePackage("com.boot.core");
        genQo.setIgnoreTabelPrefix("sys_");
        genQo.setModuleName("system");
        genQo.setParentMenuName("系统管理");
        return genQo;
    }

    private static Map<String, Object> create(String key, String desc) {
        HashMap<String, Object> template = new HashMap();
        template.put("key", key);
        template.put("desc", desc);
        return template;
    }
}
