##Jsm-agent

####使用jvm插装技术，在jvm加载class文件时将监控代码插入其中，然后将监控日志写入文件。

######java annotation模式（此方式对业务系统有入侵）

    1，应用中添加 jsm-annotation-[version].jar包
    
    2，在需要监控的类上添加注解@MonitorClass
    
    3，在需要监控的方法上添加注解@MonitorMethod
    
######agentArgs模式（此方式对业务系统无入侵，推荐使用）

    1，在JVM启动参数中添加以下内容
    -javaagent:/绝对路径/jsm-agent-[version].jar=
    {
      "appName": "testapp",
      "monitor": {
        "class1": [
          "method1",
          "method2"
        ],
        "class2": [
          "method1",
          "method2"
        ]
      }
    }
    
    2，在agentArgs参数的monitor中配置自定义需要监控的全限定类名和方法名。
