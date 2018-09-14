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
INSERT INTO `user` VALUES ('1', 'FreshMan', 'e10adc3949ba59abbe56e057f20f883e', '1', '1995-02-07 17:55:19');
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

* 5、AuthInterceptor用户验证拦截器，可以自定义，框架中验证的是在请求头中添加参数：ACCESS_TOKEN
```java
//TODO：验证用户登录，或者你需要的信息
String token = request.getHeader("ACCESS_TOKEN");
logger.debug("token: " + token);
if (StringUtils.isEmpty(token)) {
    filterError(request, response, new BaseResponse(ResponseCode.HTTPMESSAGENOTWRITABLEEXCEPTION, "request header key ACCESS_TOKEN is empty"));
    return false;
}
//TODO:可以结合数据库处理数据
User user = userService.getUserId(1);
//TODO: 这儿的currentUse和CurrentUserMethodArgumentResolver.resolveArgument 方法中的currentUser需要一致
request.setAttribute("currentUser", user.getName());
return true;
```
IgnoreSecurity为忽略用户验证注解，用在方法上，例如：
```java
@IgnoreSecurity
@RequestMapping(value = "checkUserPwd", method = {RequestMethod.POST, RequestMethod.GET})
public BaseResponse checkUserPwd(HttpServletRequest request) {
    ……
}
```
CurrentUserMethodArgumentResolver为参数解析器，能够自动注入解析完成的参数，也可以自己修改对应需要的解析器。使用方法为在方法的参数中添加注解。
```java
/**
 * 登出
 * @param userInfo
 * @return
 */
@ApiOperation(value = "用户登出", notes = "用户登出")
@RequestMapping(value = "logout", method = RequestMethod.GET)
public BaseResponse logout(@CurrentUser String userInfo) {
    return new BaseResponse(ResponseCode.SUCCESS, userInfo);
}
```
* 6、添加方法的AOP拦截ServiceLogAspect，可以用于日志的记录，分析，参数的处理，异常的记录。
* 7、添加文件的上传功能，通过http://localhost:8100/upload 方法上传大小小于10M的文件，在配置文件中修改文件大小的限制
```xml
spring.http.multipart.max-file-size=10Mb
spring.http.multipart.max-request-size=100Mb
xcloud.uploadpath=D:\\uploadfile\\
```
* 8、加入了Security权限模块，如果不需要，你只需要在SecurityConfig中的configure方法修改为：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().permitAll();
    http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
}   
```
即可。如果你需要，那么就去掉源码中的注释部分，并删掉http.authorizeRequests().anyRequest().permitAll();该行，在权限列表中，你可以进行数据库的查询，在demo中是使用的默认数据项：
```java
/**
 * 加载权限表中所有权限
 */
public void loadResourceDefine() {
    map = new HashMap<>();
    Collection<ConfigAttribute> array;
    ConfigAttribute cfg;
    List<String> urls = new ArrayList<>();
    urls.add("/upload");
    urls.add("/downLoadFile");
    for (String url : urls) {
        array = new ArrayList<>();
        cfg = new SecurityConfig("ROLE_FreshMan");
        //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
        array.add(cfg);
        //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
        map.put(url, array);
    }

}
```
你需要修改为你数据库中的对应url的权限集合。

* 8、集成了Shiro权限模块，需要在ShiroConfiguration文件中配置你自己的权限设计内容。Demo中只针对测试的指定路径使用了权限验证，可以修改
```java
private String patternUrl = "/shiro";
```
为你自己指定的内容。
loadShiroFilterChain方法中设置你需要的url的权限配置。在用户的验证和权限的认证的时候需要实现你自己的验证逻辑和权限模块：
```java
/**
 * 权限认证，为当前登录的Subject授予角色和权限
 * 经测试：本例中该方法的调用时机为需授权资源被访问时
 * 并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
 * 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
 *
 * @param principalCollection
 * @return
 */
@Override
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    System.out.println("##################执行Shiro权限认证##################");
    //获取当前登录输入的用户名
    String loginName = (String) super.getAvailablePrincipal(principalCollection);

    //region TODO:获取登录用户信息
    User user = userService.getUserByName(loginName);
    //如果没有查询到返回null就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地方
    if (null == user) {
        return null;
    }
    //endregion

    //权限信息对象info，用来存放查出来的用户的所有角色（role)及权限（permission）
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

    //region TODO:用户的角色集合，需要实现自己的角色集合,获取当前用户的role信息
    Set<String> roles = new HashSet<>();
    roles.add("admin");
    roles.add("manager");
    roles.add("normal");
    //endregion

    info.setRoles(roles);

    //region TODO:获取用户的所有权限
    List<String> permissionList = new ArrayList<>();
    permissionList.add("add");
    permissionList.add("del");
    permissionList.add("update");
    permissionList.add("query");
    permissionList.add("user:query");
    permissionList.add("user:edit");
    //endregion

    //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的行可以不要
    info.addStringPermissions(permissionList);
    return info;
}

/**
 * 登录认证
 *
 * @param authenticationToken
 * @return
 * @throws AuthenticationException
 */
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    //UsernamePasswordToken对象用来存放提交的登录信息
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    System.out.println("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

    //region TODO:查出是否有此用户
    User user = userService.getUserByName(token.getUsername());
    if (user == null) {
        return null;
    }
    //endregion

    return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
}
```
访问：http://localhost:8100/shiro/sLoginAjax?username=FreshMan&password=123456 进行登录；
访问：http://localhost:8100/shiro/user?id=1 获取用户的信息；
访问：http://localhost:8100/shiro/sLogout 进行退出登录；
访问：http://localhost:8100/shiro/user/delete 由于没有配置删除权限，将会弹出到403页面

[swagger]:https://github.com/Yinghuochongxiaoq/FreshManWebApi/blob/master/src/main/resources/static/swagger.jpg?raw=true