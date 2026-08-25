# E购商城 CentOS7.9 服务器部署教程

## 一、环境准备

### 1. 安装JDK 1.8
yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel
java -version

### 2. 安装MySQL 8.0
# 下载MySQL官方Yum源
wget https://dev.mysql.com/get/mysql80-community-release-el7-7.noarch.rpm
rpm -ivh mysql80-community-release-el7-7.noarch.rpm
yum install -y mysql-community-server
systemctl start mysqld
systemctl enable mysqld

# 获取初始密码
grep 'temporary password' /var/log/mysqld.log

# 登录MySQL并修改密码
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Root@123456';
FLUSH PRIVILEGES;

# 如果遇到caching_sha2_password连接报错，执行：
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Root@123456';
FLUSH PRIVILEGES;

# 创建数据库并导入数据
CREATE DATABASE egou DEFAULT CHARACTER SET utf8mb4;
USE egou;
SOURCE /opt/egou/sql/egou.sql;

### 3. 安装RabbitMQ
# 安装Erlang
yum install -y erlang

# 安装RabbitMQ
wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.11.15/rabbitmq-server-3.11.15-1.el7.noarch.rpm
rpm -ivh rabbitmq-server-3.11.15-1.el7.noarch.rpm

# 启动RabbitMQ
systemctl start rabbitmq-server
systemctl enable rabbitmq-server

# 开启Web管理界面（可选）
rabbitmq-plugins enable rabbitmq_management

# 添加用户和权限
rabbitmqctl add_user guest guest
rabbitmqctl set_user_tags guest administrator
rabbitmqctl set_permissions -p / guest ".*" ".*" ".*"

## 二、项目打包

### 1. 修改配置文件
编辑 application.properties，将数据库密码和RabbitMQ地址改为服务器实际配置：
spring.datasource.password=Root@123456
spring.rabbitmq.host=localhost

### 2. Maven打包
在项目根目录执行：
mvn clean package -DskipTests

打包完成后，jar包位于 target/egou-1.0.0.jar

## 三、上传部署

### 1. 上传文件到服务器
mkdir -p /opt/egou/upload
将 egou-1.0.0.jar 上传到 /opt/egou/
将 egou.sql 上传到 /opt/egou/sql/

### 2. 创建上传目录
mkdir -p /upload
chmod 777 /upload

### 3. 修改application.properties中的上传路径
file.upload-path=/upload/

### 4. 启动项目
# 前台启动（测试用）
java -jar /opt/egou/egou-1.0.0.jar

# 后台启动
nohup java -jar /opt/egou/egou-1.0.0.jar > /opt/egou/egou.log 2>&1 &

## 四、防火墙配置

# 开放8080端口
firewall-cmd --zone=public --add-port=8080/tcp --permanent
firewall-cmd --reload

# 查看已开放端口
firewall-cmd --list-ports

## 五、访问测试

浏览器访问：http://服务器IP:8080

测试账号：
- 买家账号：buyer1 / 123456
- 商家账号：seller1 / 123456

## 六、常用命令

# 查看Java进程
ps -ef | grep java

# 停止项目
kill -9 $(ps -ef | grep egou | grep -v grep | awk '{print $2}')

# 查看日志
tail -f /opt/egou/egou.log

# 重启项目
kill -9 $(ps -ef | grep egou | grep -v grep | awk '{print $2}')
nohup java -jar /opt/egou/egou-1.0.0.jar > /opt/egou/egou.log 2>&1 &
