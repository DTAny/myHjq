create database myHjq; --建库
use myHjq; --切换数据库

--建表
create table Books --图书
(
    bid    smallint PRIMARY KEY identity (1, 1), --图书编号
    bname  nvarchar(50) NOT NULL , --书名
    author nvarchar(50) NOT NULL , --作者
    state  bit DEFAULT 1 --可借阅状态
);
create table Reader --借阅人
(
    rid   varchar(20) PRIMARY KEY, --读者身份证号
    rname nvarchar(50) NOT NULL , --读者姓名
    rsex  nchar(1) CHECK (rsex = '男' or rsex = '女'), --读者性别
    rage  tinyint, --读者年龄
    rtel  varchar(20) NOT NULL --读者联系电话
);
create table Manager --管理员
(
    mid   smallint PRIMARY KEY identity (1, 1), --管理员id
    macc  varchar(20) NOT NULL , --管理员账号
    mpwd  varchar(20) NOT NULL , --管理员密码
    mname nvarchar(50) DEFAULT '管理员' --管理员姓名
);
create table Super --超管
(
    sacc varchar(20) PRIMARY KEY , --超管账号
    spwd varchar(20) NOT NULL --超管密码
);
create table LendLog --借出记录
(
    bid   smallint     NOT NULL, --图书编号
    ldate datetime NOT NULL, --借出时间点
    ddl   datetime NOT NULL, --归还期限
    rid   varchar(20)      NOT NULL, --读者身份证号
    FOREIGN KEY (bid) references Books (bid),
    FOREIGN KEY (rid) references Reader (rid),
    PRIMARY KEY (bid, ldate)
);
create table ReturnLog --归还记录
(
    bid   smallint NOT NULL , --图书编号
    rdate datetime NOT NULL , --归还时间
    rid   varchar(20) NOT NULL , --读者身份证号
    FOREIGN KEY (bid) references Books(bid),
    FOREIGN KEY (rid) references Reader(rid),
    PRIMARY KEY (bid, rdate)
);
insert into Books values ('活着', '余华', 1);
insert into Books values ('血色莫扎特', '房伟', 1);
insert into Books values ('三体', '刘慈欣', 1);
insert into Books values ('Counter Strike: Globle Offensive', 'G胖', 1);

insert into Reader values ('513436200011218134', '张三', '男', 10, '13812345678');
insert into Reader values ('51343620001121781X', '李四', '女', 30, '17712345678');
insert into Reader values ('513436200011219735', '韩琦琦', '女', 18, '15212345678');

insert into Manager values ('123456', '123456', '老邓');
insert into Manager values ('wang123', '88888888', '王五')

insert into Super values ('root', '123456');

drop database myHjq;




















