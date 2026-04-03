# baseProject

文件基础包--导入导出，项目搭建--controller-service- manager- dao- mapper



启动mysql
mysql.server start  # 启动mysql
mysql.server stop # 停止mysql
mysql.server restart # 重启mysql


启动redis
brew services start redis


启动 xxl-job 
去官网下载后 ，本地启动即可
    pom.xml引入依赖
    yml添加配置项

nacos
下载地址
https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
解压后进入bin目录
cd  /Users/caotengfei/Downloads/nacos/bin   （sh startup.sh -m standalone）
访问控制台：http://localhost:8848/nacos

下载 Sentinel 控制台 jar
https://github.com/alibaba/Sentinel/releases/download/1.8.6/sentinel-dashboard-1.8.6.jar
pom.xml引入依赖
yml添加配置项
示例：
Java -jar /Users/caotengfei/Downloads/sentinel-dashboard-1.8.6.jar



全链路日志追踪   (sleuth,zipkin)
引入sleuth，zipkin依赖，修改配置文件
下载zipkin 控制台
https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.19/zipkin-server-2.23.19-exec.jar





