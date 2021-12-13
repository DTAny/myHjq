package com.example.myhjq.controller;

import com.example.myhjq.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    JdbcTemplate template;

    @Autowired
    LoginService loginService;

    @RequestMapping(value = {"login", "/", "index"})
    public String login(@RequestParam(required = false) String acc, @RequestParam(required = false) String pwd, @RequestParam(required = false) boolean superLogin, Model model, HttpSession session, HttpServletRequest request, @RequestParam(required = false) boolean logout){
        if(logout) {
            request.getSession().invalidate();
            model.addAttribute("msg", "登出成功");
            model.addAttribute("msgLevel", "success");
        }
        else if(acc != null && pwd != null) {
            int check = loginService.login(acc, pwd, superLogin, template);
            if (check == 1) {
                if(superLogin) {
                    session.setAttribute("name", "超级管理员");
                    session.setAttribute("permission", "s");
                }
                else {
                    session.setAttribute("name", loginService.getManagerName(acc, template));
                    session.setAttribute("permission", "m");
                }
                return "login2";
            } else if (check == 0) {
                model.addAttribute("msg", "密码错误");
                model.addAttribute("msgLevel", "danger");
            } else if (check == -1) {
                model.addAttribute("msg", "账号不存在");
                model.addAttribute("msgLevel", "danger");
            }
        }
        return "login";
    }

}
