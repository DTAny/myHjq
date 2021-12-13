package com.example.myhjq.controller;

import com.example.myhjq.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manage")
public class ManageController {
    @Autowired
    JdbcTemplate template;

    @Autowired
    DataService service;

    @RequestMapping("/bookManage")
    public String bookManage(@RequestParam(required = false) boolean delete, @RequestParam(required = false) String bid, @RequestParam(required = false) String bname, @RequestParam(required = false) String author, @RequestParam(required = false) String state, @RequestParam(required = false) String rid, @RequestParam(required = false) String ddl, Model model){
        if(delete){
            if(bid!=null)
                service.deleteBook(Integer.parseInt(bid), template);
            model.addAttribute("msg", "删除成功");
            model.addAttribute("msgLevel", "success");
            model.addAttribute("msgIcon", "icon-check-2");
        }
        if(rid == null && bname != null){
            int add = service.addBook(bname, author, state.equals("可借"), template);
            if (add != -1){
                model.addAttribute("msg", "添加新图书成功!编号为: " + add);
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }
            else {
                model.addAttribute("msg", "添加新图书失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        if (rid != null){
            int code = 0;
            if (ddl != null){
                code = service.lendBook(bid, bname, author, Integer.parseInt(ddl), rid, template);
            }
            else {
                code = service.returnBook(bid, rid, template);
            }
            switch (code){
                case 1:
                    model.addAttribute("msg", "借书操作成功");
                    model.addAttribute("msgLevel", "success");
                    model.addAttribute("msgIcon", "icon-check-2");
                    break;
                case -1:
                    model.addAttribute("msg", "借书操作错误：借阅人不存在");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                case -2:
                    model.addAttribute("msg", "借书操作错误：图书不存在");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                case -3:
                    model.addAttribute("msg", "借书操作错误：图书已被借阅");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                case -4:
                    model.addAttribute("msg", "借书操作错误：图书已被还书");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                case -5:
                    model.addAttribute("msg", "借书操作错误：还书以逾期");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                case -6:
                    model.addAttribute("msg", "借书操作错误：还书人与借阅人身份证号不一致");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
                    break;
                default:
                    model.addAttribute("msg", "借书操作错误：输入格式出错");
                    model.addAttribute("msgLevel", "danger");
                    model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        return "/manage/bookManage";
    }

    @RequestMapping("/readerManage")
    public String readerManage(@RequestParam(required = false) boolean delete, @RequestParam(required = false) String rid, @RequestParam(required = false) String rname, @RequestParam(required = false) String rsex, @RequestParam(required = false) String rage, @RequestParam(required = false) String rtel, Model model){
        if(delete){
            if(rid!=null)
                service.deleteReader(rid, template);
            model.addAttribute("msg", "删除成功");
            model.addAttribute("msgLevel", "success");
            model.addAttribute("msgIcon", "icon-check-2");
        }
        if(rname != null){
            int add = service.addReader(rid, rname, rsex, rage, rtel, template);
            if (add != -1){
                model.addAttribute("msg", "添加新借阅人信息成功");
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }
            else {
                model.addAttribute("msg", "添加新借阅人信息失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        return "/manage/readerManage";
    }

    @RequestMapping("/logManage")
    public String logManage(@RequestParam(required = false) String bid, @RequestParam(required = false) String ddl, Model model){
        if (ddl != null){
            if (service.addDdl(bid, ddl, template)) {
                model.addAttribute("msg", "延期成功");
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }
            else {
                model.addAttribute("msg", "延期失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        return "/manage/logManage";
    }

    @RequestMapping("/superManage")
    public String superManage(@RequestParam(required = false) boolean delete, @RequestParam(required = false) String mid, @RequestParam(required = false) String macc, @RequestParam(required = false) String mpwd, @RequestParam(required = false) String mname, Model model){
        if(delete){
            if(mid!=null)
                service.deleteManager(mid, template);
            model.addAttribute("msg", "删除成功");
            model.addAttribute("msgLevel", "success");
            model.addAttribute("msgIcon", "icon-check-2");
        }
        if(macc != null){
            int add = service.addManager(macc, mpwd, mname.equals("") ? "管理员" : mname , template);
            if (add != -1){
                model.addAttribute("msg", "添加新管理员成功，编号为: " + add);
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }
            else {
                model.addAttribute("msg", "添加新管理员失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        return "/manage/superManage";
    }

    @RequestMapping("/bookManage/bookEdit")
    public String bookEdit(int bid, @RequestParam(required = false) String bname, @RequestParam(required = false) String author, @RequestParam(required = false) String state,  Model model){
        if(bname!=null && !bname.isEmpty()){
            try {
                service.editBook(bid, bname, author, state.equals("可借"), template);
                model.addAttribute("msg", "保存成功");
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }catch(Exception e){
                model.addAttribute("msg", "保存失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        model.addAttribute("book", service.getBook(bid, template));
        return "/manage/bookEdit";
    }

    @RequestMapping("/readerManage/readerEdit")
    public String readerEdit(String rid, @RequestParam(required = false) String rname, @RequestParam(required = false) String rsex, @RequestParam(required = false) String rage, @RequestParam(required = false) String rtel, Model model){
        if(rname != null && !rname.isEmpty()){
            String[] rids = rid.split(",");
            try {
                service.editReader(rids[0], rids[1], rname, rsex, rage, rtel, template);
                model.addAttribute("msg", "保存成功");
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
                model.addAttribute("reader", service.getReader(rids[1], template));
                return "/manage/readerEdit";
            }catch(Exception e){
                model.addAttribute("msg", "保存失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        model.addAttribute("reader", service.getReader(rid, template));
        return "/manage/readerEdit";
    }
    @RequestMapping("/superManage/superEdit")
    public String superEdit(int mid, @RequestParam(required = false) String macc, @RequestParam(required = false) String mpwd, @RequestParam(required = false) String mname, Model model){
        if(macc != null && !macc.isEmpty()){
            try {
                service.editSuper(mid, macc, mpwd, mname.equals("") ? "管理员" : mname, template);
                model.addAttribute("msg", "保存成功");
                model.addAttribute("msgLevel", "success");
                model.addAttribute("msgIcon", "icon-check-2");
            }catch(Exception e){
                model.addAttribute("msg", "保存失败");
                model.addAttribute("msgLevel", "danger");
                model.addAttribute("msgIcon", "icon-alert-circle-exc");
            }
        }
        model.addAttribute("manager", service.getManager(mid, template));
        return "manage/superEdit";
    }
}
