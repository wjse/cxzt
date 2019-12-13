package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.model.User;
import com.k66.cxzt.utils.WebContextUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuApi {

    @GetMapping
    public JSONObject getMenu(){
        User user = WebContextUtil.getUser();
        User.UserType userType = user.getType();
        if(User.UserType.ADMIN.equals(userType)){
            return getAdminMenus();
        }else if(User.UserType.NORMAL.equals(userType)){
            return getNormalMenus();
        }
        return null;
    }

    private JSONObject getNormalMenus() {
        JSONObject result = new JSONObject();
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("/scan.html" , "发票扫描" , "interface-windows"));
        list.add(new Menu("/invoice.html" , "我提交的发票" , "list"));
        result.put("menus" , list);
        return result;
    }

    private JSONObject getAdminMenus() {
        JSONObject result = new JSONObject();
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("/scan.html" , "发票扫描" , "interface-windows"));
        list.add(new Menu("/invoice.html" , "发票列表" , "list"));
        list.add(new Menu("/user.html" , "用户列表" , "user"));
        result.put("menus" , list);
        return result;
    }

    @Data
    class Menu{
        String href;
        String name;
        String icon;

        private Menu(String href , String name , String icon){
            this.href = href;
            this.name = name;
            this.icon = icon;
        }
    }
}
