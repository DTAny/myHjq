package com.example.myhjq.controller;

import com.example.myhjq.pojo.Book;
import com.example.myhjq.pojo.Log;
import com.example.myhjq.pojo.Manager;
import com.example.myhjq.pojo.Reader;
import com.example.myhjq.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JsonController {
    @Autowired
    JdbcTemplate template;

    @Autowired
    DataService service;

    @RequestMapping(value = "bookManage/table" ,produces = "application/json;charset=UTF-8")
    public List<Book> getBooks(){
        return service.getAllBooks(template);
    }

    @RequestMapping(value = "readerManage/table" ,produces = "application/json;charset=UTF-8")
    public List<Reader> getAllReaders(){
        return service.getAllReaders(template);
    }

    @RequestMapping(value = "logManage/table" ,produces = "application/json;charset=UTF-8")
    public List<Log> getAllLogs(){
        return service.getAllLogs(template);
    }

    @RequestMapping(value = "superManage/table" ,produces = "application/json;charset=UTF-8")
    public List<Manager> getManagers(){
        return service.getAllManager(template);
    }

    @RequestMapping("superManage/edit")
    public Manager getManagerEdit(){
        return service.getManager(1, template);
    }
}
