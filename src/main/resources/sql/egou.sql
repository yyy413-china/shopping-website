-- =============================================
-- E购商城数据库脚本
-- 数据库：MySQL 8.0
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS egou DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE egou;

-- =============================================
-- 1. 用户表(user)
-- =============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
  `account` VARCHAR(50) NOT NULL COMMENT '账号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `sex` VARCHAR(10) DEFAULT NULL COMMENT '性别',
  `idcard` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `role` INT NOT NULL DEFAULT 0 COMMENT '角色：0-普通买家，1-商家',
  `onlineyes` INT NOT NULL DEFAULT 0 COMMENT '在线状态：0-离线，1-在线',
  `logintime` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 2. 商家表(supplier)
-- =============================================
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '商家ID，主键自增',
  `userid` INT NOT NULL COMMENT '关联用户ID',
  `sname` VARCHAR(100) NOT NULL COMMENT '商家名称',
  `sphone` VARCHAR(20) DEFAULT NULL COMMENT '商家电话',
  `saddress` VARCHAR(200) DEFAULT NULL COMMENT '商家地址',
  `sstatus` INT NOT NULL DEFAULT 1 COMMENT '商家状态：0-停用，1-正常',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userid` (`userid`),
  CONSTRAINT `fk_supplier_userid` FOREIGN KEY (`userid`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- =============================================
-- 3. 商品分类表(category)
-- =============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键自增',
  `cname` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- =============================================
-- 4. 商品表(commodity)
-- =============================================
DROP TABLE IF EXISTS `commodity`;
CREATE TABLE `commodity` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键自增',
  `supplierid` INT NOT NULL COMMENT '商家ID，外键',
  `categoryid` INT NOT NULL COMMENT '分类ID，外键',
  `cname` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `cprice` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `cnum` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `cdesc` VARCHAR(500) DEFAULT NULL COMMENT '商品描述',
  `cstatus` INT NOT NULL DEFAULT 1 COMMENT '商品状态：0-停售，1-在售',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_commodity_supplierid` FOREIGN KEY (`supplierid`) REFERENCES `supplier`(`id`),
  CONSTRAINT `fk_commodity_categoryid` FOREIGN KEY (`categoryid`) REFERENCES `category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- =============================================
-- 5. 商品图片表(image)
-- =============================================
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '图片ID，主键自增',
  `commodityid` INT NOT NULL COMMENT '商品ID，外键',
  `iname` VARCHAR(200) DEFAULT NULL COMMENT '图片名称',
  `ipath` VARCHAR(500) NOT NULL COMMENT '图片路径',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_image_commodityid` FOREIGN KEY (`commodityid`) REFERENCES `commodity`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

-- =============================================
-- 6. 购物车表(shopcart)
-- =============================================
DROP TABLE IF EXISTS `shopcart`;
CREATE TABLE `shopcart` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '购物车ID，主键自增',
  `userid` INT NOT NULL COMMENT '用户ID，外键',
  `commodityid` INT NOT NULL COMMENT '商品ID，外键',
  `cnum` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  `confirm` INT NOT NULL DEFAULT 0 COMMENT '结算状态：0-未结算，1-已结算',
  `orderid` INT DEFAULT NULL COMMENT '关联订单ID',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_shopcart_userid` FOREIGN KEY (`userid`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_shopcart_commodityid` FOREIGN KEY (`commodityid`) REFERENCES `commodity`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- =============================================
-- 7. 订单表(order)
-- =============================================
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键自增',
  `orderno` VARCHAR(50) NOT NULL COMMENT '订单编号(唯一)',
  `userid` INT NOT NULL COMMENT '用户ID，外键',
  `totalprice` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  `ostatus` INT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待发货，1-已发货，2-已完成',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderno` (`orderno`),
  CONSTRAINT `fk_order_userid` FOREIGN KEY (`userid`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- =============================================
-- 插入测试数据
-- =============================================

-- 插入用户（密码均为123456）
INSERT INTO `user` (`account`,`password`,`name`,`sex`,`idcard`,`phone`,`role`,`onlineyes`) VALUES
('buyer1','123456','张三','男','110101199001011234','13800138001',0,0),
('buyer2','123456','李四','女','110101199202022345','13800138002',0,0),
('seller1','123456','王老板','男','110101198801013456','13800138003',1,0),
('seller2','123456','赵老板','女','110101198902024567','13800138004',1,0);

-- 插入商家
INSERT INTO `supplier` (`userid`,`sname`,`sphone`,`saddress`,`sstatus`) VALUES
(3,'王记数码','010-12345678','北京市海淀区中关村大街1号',1),
(4,'赵姐服饰','010-87654321','北京市朝阳区建国门外大街2号',1);

-- 插入商品分类
INSERT INTO `category` (`cname`) VALUES
('数码电子'),
('服装鞋帽'),
('食品饮料'),
('家居日用'),
('图书文具');

-- 插入商品
INSERT INTO `commodity` (`supplierid`,`categoryid`,`cname`,`cprice`,`cnum`,`cdesc`,`cstatus`) VALUES
(1,1,'华为Mate60 Pro','6999.00',100,'华为旗舰手机，麒麟芯片回归',1),
(1,1,'小米14 Ultra','5999.00',80,'徕卡光学镜头，影像旗舰',1),
(1,1,'AirPods Pro 2','1899.00',200,'苹果降噪耳机，空间音频',1),
(2,2,'优衣库羽绒服','599.00',300,'轻薄保暖，90%白鸭绒',1),
(2,2,'Nike Air Max','899.00',150,'经典气垫跑鞋，舒适百搭',1),
(2,3,'三只松鼠坚果礼盒','168.00',500,'每日坚果，健康零食',1),
(1,4,'小米台灯Pro','199.00',400,'智能调光，护眼台灯',1),
(2,5,'《Java编程思想》','108.00',200,'程序员必读经典',1);

-- 插入商品图片
INSERT INTO `image` (`commodityid`,`iname`,`ipath`) VALUES
(1,'华为Mate60 Pro','/upload/huawei_mate60.svg'),
(2,'小米14 Ultra','/upload/xiaomi14.svg'),
(3,'AirPods Pro 2','/upload/airpods_pro2.svg'),
(4,'优衣库羽绒服','/upload/uniqlo_down.svg'),
(5,'Nike Air Max','/upload/nike_airmax.svg'),
(6,'三只松鼠坚果礼盒','/upload/squirrel_nuts.svg'),
(7,'小米台灯Pro','/upload/xiaomi_lamp.svg'),
(8,'Java编程思想','/upload/java_book.svg');

-- 插入购物车
INSERT INTO `shopcart` (`userid`,`commodityid`,`cnum`,`confirm`) VALUES
(1,1,1,0),
(1,3,2,0),
(2,5,1,0);

-- =============================================
-- 8. 收藏表(favorite)
-- =============================================
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键自增',
  `userid` INT NOT NULL COMMENT '用户ID，外键',
  `commodityid` INT NOT NULL COMMENT '商品ID，外键',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_commodity` (`userid`,`commodityid`),
  CONSTRAINT `fk_favorite_userid` FOREIGN KEY (`userid`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorite_commodityid` FOREIGN KEY (`commodityid`) REFERENCES `commodity`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- =============================================
-- 9. 商品评价表(review)
-- =============================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '评价ID，主键自增',
  `userid` INT NOT NULL COMMENT '用户ID，外键',
  `commodityid` INT NOT NULL COMMENT '商品ID，外键',
  `content` VARCHAR(500) NOT NULL COMMENT '评价内容',
  `rating` INT NOT NULL DEFAULT 5 COMMENT '评分（1-5星）',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_commodity` (`userid`,`commodityid`),
  CONSTRAINT `fk_review_userid` FOREIGN KEY (`userid`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_commodityid` FOREIGN KEY (`commodityid`) REFERENCES `commodity`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';

-- 插入测试评价数据
INSERT INTO `review` (`userid`,`commodityid`,`content`,`rating`) VALUES
(1,1,'手机很好用，拍照清晰，运行流畅',5),
(2,2,'徕卡镜头效果不错，值得购买',4),
(1,3,'音质很好，降噪效果明显',5);

-- =============================================
-- MySQL 8.0 caching_sha2_password 连接报错解决方案
-- 如果使用旧版客户端连接MySQL 8.0报错，执行以下语句修改密码认证方式：
-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
-- FLUSH PRIVILEGES;
-- =============================================
