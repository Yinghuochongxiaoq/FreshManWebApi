# FreshManWebApi
 WebApi精简框架
 使用的是Spring boot 1.5.9.RELEASE版本，内部默认连接了MySQL 数据库，Redis，配置信息都在文件[application.properties](https://github.com/Yinghuochongxiaoq/FreshManWebApi/blob/master/src/main/resources/application.properties)。
* 1、数据库连接配置
 ```数据库连接配置
mysql.url=jdbc:mysql://127.0.0.1:3306/costnote
mysql.username=root
mysql.password=xxx
```
 数据库使用名称为costnote,需要的表为：user
 建表SQL为
```mysql
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Sex` varchar(255) DEFAULT NULL,
  `Birthday` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'FreshMan', 'e10adc3949ba59abbe56e057f20f883e', '1', '1991-06-07 18:55:19');
```
* 2、redis连接信息配置
```redis连接信息配置
spring.redis.database=5
spring.redis.host=127.0.0.1
spring.redis.password=
spring.redis.port=6379
spring.redis.timeout=3000
spring.redis.pool.max-idle=8
spring.redis.pool.min-idle=0
spring.redis.pool.max-active=20
spring.redis.pool.max-wait=1000
```
* 3、内置了swagger-ui插件，访问地址http://localhost:8100/swagger-ui.html ，可查看效果其中的地址是可以自己修改的，访问效果如下：
![swagger]
* 4、启动类为WebapiApplication，直接运行，看到启动成功后，访问地址http://localhost:8100/checkUserPwd?username=FreshMan&password=123456 验证是否能够顺利返回值：
```json
{
    "code": "0",
    "message": "登录成功",
    "data": {
        "checkUser": true,
        "getRedis": {
            "id": 1,
            "name": "FreshMan",
            "password": "e10adc3949ba59abbe56e057f20f883e",
            "sex": "1",
            "birthday": "1995-02-07 17:55:19"
        },
        "dbUser": {
            "id": 1,
            "name": "FreshMan",
            "password": "e10adc3949ba59abbe56e057f20f883e",
            "sex": "1",
            "birthday": "1995-02-07 17:55:19"
        },
        "setRedis": "缓存存入成功"
    }
}
```
表示着从数据库，redis中读取数据，写入数据成功。

[swagger]:https://github.com/Yinghuochongxiaoq/FreshManWebApi/blob/master/src/main/resources/static/swagger.jpg?raw=true