需要先下载并启动 sentinel-dashboard-1.8.1.jar。

``` shell
# 启动命令 全部使用的默认参数，默认登录的账号密码：sentinel
# 访问地址：http://localhost:8080/
java -jar sentinel-dashboard-1.8.1.jar
```

该 springcloud-sentinel 项目重启后，配置的规则会消失，因为没有配置持久化