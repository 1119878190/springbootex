package com.shiro.demo.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @RequestMapping("/index")
    public String toIndex(Model model){
        model.addAttribute("msg","helloworld");
        System.out.println("跳转到index");
        return "index";
    }

    @RequestMapping("/user/add")
    @RequiresPermissions(value = {"user:add"})
    public String add(){
        return "user/add";
    }

    @RequestMapping("/user/update")
    @RequiresPermissions(value = {"user:update"})
    public String update(){
        return "user/update";
    }

    /**
     * 跳转到登录页
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping("/login")
    public String login(String username,String password,Model model){
        // 获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);// 执行登录的方法，如果没有异常就说明成功
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg","用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    @RequestMapping("/noauth")
    @ResponseBody
    public String unauthorized(){
        return "未经授权无法访问此页面";
    }
}
