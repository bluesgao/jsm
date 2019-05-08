Jsm-agent
* 使用jvm插装技术，在jvm加载class文件时将监控代码插入其中，然后将监控日志写入文件
* 使用sigar或者oshi获取服务器相关信息（包括操作系统、jvm、cpu、内存、硬盘、网络、io等）
* 使用java Runtime，ManagementFactory 获取jvm相关信息
* 使用javax.management NotificationListener获取gc相关信息
