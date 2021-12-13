package com.example.myhjq.service;

import com.example.myhjq.pojo.Book;
import com.example.myhjq.pojo.Log;
import com.example.myhjq.pojo.Manager;
import com.example.myhjq.pojo.Reader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class DataService {
    public List<Manager> getAllManager(JdbcTemplate template){
        return template.query("select * from myHjq.dbo.Manager", new BeanPropertyRowMapper<>(Manager.class));
    }

    public List<Book> getAllBooks(JdbcTemplate template){
        return template.query("select * from myHjq.dbo.Books", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBook(int bid, JdbcTemplate template){
        return template.queryForObject("select * from myHjq.dbo.Books where bid = ?", new BeanPropertyRowMapper<>(Book.class), bid);
    }

    public List<Reader> getAllReaders(JdbcTemplate template){
        return template.query("select * from myHjq.dbo.Reader", new BeanPropertyRowMapper<>(Reader.class));
    }

    public List<Log> getAllLogs(JdbcTemplate template) {
        return template.query("select myHjq.dbo.LendLog.bid, myHjq.dbo.LendLog.ldate, myHjq.dbo.LendLog.ddl, myHjq.dbo.LendLog.rid, min(rdate) as rdate\n" +
                "    from myHjq.dbo.LendLog\n" +
                "        left join myHjq.dbo.ReturnLog\n" +
                "        on LendLog.bid = ReturnLog.bid and LendLog.rid = ReturnLog.rid and LendLog.ldate < ReturnLog.rdate\n" +
                "    group by ldate, myHjq.dbo.LendLog.bid, ddl, myHjq.dbo.LendLog.rid", new BeanPropertyRowMapper<>(Log.class));
    }

    public void editBook(int bid, String bname, String author, boolean state, JdbcTemplate template){
        template.update("update myHjq.dbo.Books set bname = ?, author = ?, state = ? where bid = ?", bname, author, state, bid);
    }

    public void deleteBook(int bid, JdbcTemplate template){
        template.update("delete from myHjq.dbo.Books where bid = ?", bid);
    }

    public int addBook(String bname, String author, boolean state, JdbcTemplate template){
        String sql = "insert into myHjq.dbo.Books (bname, author, state) values (? , ? , ?) select SCOPE_IDENTITY()";
        try {
            Connection connection = template.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bname);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, String.valueOf(state));
            int key = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                key = resultSet.getInt(1);
            }
            return key;
        } catch (SQLException e) {
            return -1;
        }
    }

    public void deleteReader(String rid, JdbcTemplate template) {
        template.update("delete from myHjq.dbo.Reader where rid = ?", rid);
    }

    public int addReader(String rid, String rname, String rsex, String rage, String rtel, JdbcTemplate template) {
        String sql = "insert into myHjq.dbo.Reader (rid, rname, rsex, rage, rtel) values (?, ?, ? , ?, ?) select SCOPE_IDENTITY()";
        try {
            Connection connection = template.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, rid);
            preparedStatement.setString(2, rname);
            preparedStatement.setString(3, rsex);
            preparedStatement.setString(4, rage);
            preparedStatement.setString(5, rtel);
            int key = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                key = resultSet.getInt(1);
            }
            return key;
        } catch (SQLException e) {
            return -1;
        }
    }

    public Reader getReader(String rid, JdbcTemplate template) {
        return template.queryForObject("select * from myHjq.dbo.Reader where rid = ?", new BeanPropertyRowMapper<>(Reader.class), rid);
    }

    public void editReader(String fromRid, String toRid, String rname, String rsex, String rage, String rtel, JdbcTemplate template) {
        template.update("update myHjq.dbo.Reader set rid = ?, rname = ?, rsex = ?, rage = ?, rtel = ? where rid = ?", toRid, rname, rsex, rage, rtel, fromRid);
    }

    public int lendBook(String bid, String bname, String author, int ddl, String rid, JdbcTemplate template){
        if (!bid.equals("")){
            if (template.queryForList("select rid from myHjq.dbo.Reader where rid = ?", rid).isEmpty())
                return -1; //借阅人不存在
            if (template.queryForList("select * from myHjq.dbo.Books where bid = ?", bid).isEmpty())
                return -2; //图书不存在
            if (!template.queryForObject("select state from myHjq.dbo.Books where bid = ?", Boolean.class, bid))
                return -3; //已被借阅
            template.update("update myHjq.dbo.Books set state = ? where bid = ?", false, bid);
            template.update("insert into myHjq.dbo.LendLog (bid, ldate, ddl, rid) values (?, getdate(), dateadd(month, ?, getdate()), ?)", bid, ddl, rid);
            return 1;
        }
        else if (!bname.equals("") && !author.equals("")) {
            if (template.queryForList("select rid from myHjq.dbo.Reader where rid = ?", rid).isEmpty())
                return -1; //借阅人不存在
            List<Book> bookList = template.query("select * from myHjq.dbo.Books where bname = ? and author = ?", new BeanPropertyRowMapper<>(Book.class), bname, author);
            if (bookList.isEmpty())
                return -2; //图书不存在
            for (Book book : bookList){
                if (book.getState()){
                    template.update("update myHjq.dbo.Books set state = ? where bid = ?", false, book.getBid());
                    template.update("insert into myHjq.dbo.LendLog (bid, ldate, ddl, rid) values (?, getdate(), dateadd(month, ddl, getdate()), ?)", book.getBid(), ddl, rid);
                    return 1;
                }
            }
            return -3; //已被借阅
        }
        return 0; //输入格式错误
    }

    public int returnBook(String bid, String rid, JdbcTemplate template){
        if (template.queryForList("select * from myHjq.dbo.Books where bid = ?", bid).isEmpty())
            return -2; //图书不存在
        if (template.queryForObject("select state from myHjq.dbo.Books where bid = ?", Boolean.class, bid))
            return -4; //已还书
        if (template.queryForList("select * from myHjq.dbo.LendLog where bid = ? and (getdate() <= ddl and getdate() > ldate)", bid).isEmpty())
            return -5; //已逾期
        if (!template.queryForObject("select rid from myHjq.dbo.LendLog where bid = ? and ldate >= all(select ldate from myHjq.dbo.LendLog where bid = ?)", String.class, bid, bid).equals(rid))
            return -6; //身份证号不一致
        template.update("update myHjq.dbo.Books set state = ? where bid = ?", true, bid);
        template.update("insert into myHjq.dbo.ReturnLog (bid, rdate, rid) values (?, getdate(), ?)", bid, rid);
        return 1;
    }

    public boolean addDdl(String bid, String ddl, JdbcTemplate template){
        try {
            template.update("update myHjq.dbo.LendLog set ddl = dateadd(month, ?, ddl) where bid = ?", Integer.parseInt(ddl), bid);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public int addManager(String macc, String mpwd, String mname, JdbcTemplate template) {
        if (!template.queryForList("select macc from myHjq.dbo.Manager where macc = ?", String.class, macc).isEmpty()){
            return -1;
        }
        String sql = "insert into myHjq.dbo.Manager (macc, mpwd, mname) values (?, ?, ?) select SCOPE_IDENTITY()";
        try {
            Connection connection = template.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, macc);
            preparedStatement.setString(2, mpwd);
            preparedStatement.setString(3, mname);
            int key = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                key = resultSet.getInt(1);
            }
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteManager(String mid, JdbcTemplate template) {
        template.update("delete from myHjq.dbo.Manager where mid = ?", mid);
    }

    public void editSuper(int mid, String macc, String mpwd, String mname, JdbcTemplate template) {
        template.update("update myHjq.dbo.Manager set macc = ?, mpwd = ?, mname = ? where mid = ?", macc, mpwd, mname, mid);
    }

    public Manager getManager(int mid, JdbcTemplate template) {
        return template.queryForObject("select * from myHjq.dbo.Manager where mid = ?", new BeanPropertyRowMapper<>(Manager.class), mid);
    }
}
