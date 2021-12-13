package com.example.myhjq.service;

import com.example.myhjq.pojo.Manager;
import com.example.myhjq.pojo.SuperManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    public int login(String acc, String pwd, boolean superLogin, JdbcTemplate template){
        int result = -1;
        if(superLogin){
            result = checkSuperManager(acc, pwd, template);
        }
        else {
            result = checkManager(acc, pwd, template);
        }
        return result;
    }

    public List<Manager> getAllManager(JdbcTemplate template){
        return template.query("select * from myHjq.dbo.Manager", new BeanPropertyRowMapper<>(Manager.class));
    }
    public int checkManager(String macc, String mpwd, JdbcTemplate template){
        for(Manager i : getAllManager(template)){
            if(i.getMacc().equals(macc)){
                if(i.getMpwd().equals(mpwd))
                    return 1;
                return 0;
            }
        }
        return -1;
    }
    public String getManagerName(String macc, JdbcTemplate template){
        return template.queryForObject("select mname from myHjq.dbo.Manager where macc = ?", String.class, macc);
    }

    public int checkSuperManager(String sacc, String spwd, JdbcTemplate template){
        SuperManager superManager = template.queryForObject("select * from myHjq.dbo.Super", new BeanPropertyRowMapper<>(SuperManager.class));
        if (superManager.getSacc().equals(sacc)){
            if (superManager.getSpwd().equals(spwd))
                return 1;
            return 0;
        }
        return -1;
    }
}
