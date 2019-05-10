package com.github.bluesgao.jsm.agent.transformer;

import com.github.bluesgao.jsm.agent.config.MonitorCfg;
import com.github.bluesgao.jsm.annotation.MonitorMethod;
import com.github.bluesgao.jsm.common.util.JsonUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsmTransformer implements ClassFileTransformer {
    private static final Logger LOGGER = Logger.getLogger(JsmTransformer.class.getCanonicalName());
    private static final Map<String, MonitorMethod> MONITOR_METHODS;
    private static final Map<ClassLoader, ClassPool> CLASS_POOL_MAP;
    private Map agentArgsMap;

    public JsmTransformer(String agentArgs) {
        LOGGER.log(Level.INFO, "JsmTransformer agentArgs:" + agentArgs);
        /*
         * {
         *   monitor:{
         *       "className1":[methodName1,methodName2],
         *       "className2":[methodName1,methodName2]
         *   }
         * }
         * */
        //将agentargs转换为map
        agentArgsMap = JsonUtils.json2map(agentArgs);
        LOGGER.log(Level.INFO, "JsmTransformer agentArgsMap:" + agentArgsMap.toString());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        //过滤不需要监控的类
        if (!AgentUtils.ignoreClass(className)) {
            //将className中的/转换为.
            className = className.replace("/", ".");

            LOGGER.log(Level.INFO, "transform className:" + className);
            CtClass ctClass = null;
            try {
                ctClass = getCtClass(loader, classfileBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (ctClass != null) {
                Set<CtMethod> monitoredMethods = MonitorCfg.allMonitoredMethods(loader, ctClass, (Map) agentArgsMap.get("monitor"));
                LOGGER.log(Level.INFO, "allMonitoredMethods:" + monitoredMethods.toString());

                if (monitoredMethods != null && !monitoredMethods.isEmpty()) {
                    //改写class
                    LOGGER.log(Level.INFO, "改写开始:" + ctClass.getName());

                    byte[] ehancedByteCode = null;
                    try {
                        //增强class
                        ehancedByteCode = ClassEnhancer.enhance(loader, ctClass, monitoredMethods);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ehancedByteCode != null) {
                        LOGGER.log(Level.INFO, "改写成功:" + ctClass.getName());
                        return ehancedByteCode;
                    }
                }
            }
        }
        return classfileBuffer;
    }

    public static ClassPool getClassPool(ClassLoader loader) {
        if (loader == null) {
            return new ClassPool(null);
        } else {
            ClassPool pool = CLASS_POOL_MAP.get(loader);
            if (pool == null) {
                pool = new ClassPool(true);
                pool.appendClassPath(new LoaderClassPath(loader));
                CLASS_POOL_MAP.put(loader, pool);
            }
            return pool;
        }
    }

    private CtClass getCtClass(ClassLoader classLoader, byte[] classFileBuffer) throws IOException {
        ClassPool classPool = getClassPool(classLoader);
        CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();
        return clazz;
    }


    static {
        CLASS_POOL_MAP = Collections.synchronizedMap(new WeakHashMap());
        MONITOR_METHODS = new ConcurrentHashMap<String, MonitorMethod>();
    }
}
