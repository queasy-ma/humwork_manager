# humwork_manager
一个用户上传作业，管理员方便统一下载的springboot后端

# 配置
在application.properties中
file.upload.url 是文件上传路径
server.port  运行端口
spring.datasource.username 数据库用户名
spring.datasource.password 数据库密码
具体请百度springboot配置

# 使用
将项目打包为test.jar文件后，一同将reload.sh和reload.bat放入应用目录后
运行 

nohup java -jar ./test.jar > temp.txt &