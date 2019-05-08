package com.github.bluesgao.jsm.agent;


import com.github.bluesgao.jsm.agent.transformer.JsmTransformer;
import com.github.bluesgao.jsm.common.util.LogUtils;

import java.lang.instrument.Instrumentation;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsmAgent {
    private static final Logger LOGGER = Logger.getLogger(JsmAgent.class.getCanonicalName());

    public static void premain(String agentArgs, Instrumentation inst) {
        init(agentArgs, inst);
    }

    private static void init(String agentArgs, Instrumentation inst) {
        LOGGER.log(Level.INFO, "jsm agent instrumentation premain start."+agentArgs);
        //添加字节码转换器
        inst.addTransformer(new JsmTransformer(), false);

        //jvm关闭的回调
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                //todo 应用关闭,清理工作
                LOGGER.log(Level.INFO, "应用关闭钩子函数执行...");
            }
        }));
    }
}
