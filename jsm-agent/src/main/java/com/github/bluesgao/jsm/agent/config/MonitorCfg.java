package com.github.bluesgao.jsm.agent.config;

import com.github.bluesgao.jsm.annotation.MonitorClass;
import com.github.bluesgao.jsm.annotation.MonitorMethod;
import com.github.bluesgao.jsm.common.util.StringUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorCfg {
    private static final Logger LOGGER = Logger.getLogger(MonitorCfg.class.getCanonicalName());

    /**
     * 被监控的类
     *
     * @param ctClass
     * @return
     */
    public static boolean isMonitoredClass(CtClass ctClass) {
        MonitorClass tc = null;
        try {
            tc = (MonitorClass) ctClass.getAnnotation(MonitorClass.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //接口不需要监控
        if (tc != null && !ctClass.isInterface()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 被监控的方法
     *
     * @param ctMethod
     * @return
     */
    public static boolean isMonitoredMethod(CtMethod ctMethod) {
        MonitorMethod tm = null;
        try {
            tm = (MonitorMethod) ctMethod.getAnnotation(MonitorMethod.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tm != null ? true : false;
    }


    /**
     * 获取一个类中所有被监控的方法
     *
     * @param loader
     * @return
     */
    public static Set<CtMethod> allMonitoredMethods(ClassLoader loader, CtClass ctClass, Map<String, List<String>> monitorMap) {
        try {
            //类的所有的声明方法
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();

            //array转map
            Map<String, CtMethod> ctMethodMap = new HashMap<>();
            for (int i = 0; i < ctMethods.length; i++) {
                ctMethodMap.put(ctMethods[i].getName(), ctMethods[i]);
            }
            LOGGER.log(Level.INFO, ", ctClassName:" + ctClass.getName() + ", ctMethodMap:" + ctMethodMap.toString());

            //1，注解方式
            Set<CtMethod> monitoredMethodsByAnnotation = new HashSet<CtMethod>();
            //如果对类进行监控，意味着对类的所有方法进行监控
            if (isMonitoredClass(ctClass)) {
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    monitoredMethodsByAnnotation.add(m);
                }
            } else {//只对某个类的某些方法进行监控
                //获取被监控的方法
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    if (isMonitoredMethod(m)) {
                        monitoredMethodsByAnnotation.add(m);
                    }
                }
            }
            LOGGER.log(Level.INFO, "monitoredMethodsByAnnotation:" + monitoredMethodsByAnnotation.toString());

            //2，agentargs方式
            Set<CtMethod> monitoredMethodsByAgentArgs = new HashSet<CtMethod>();
            if (monitorMap != null) {
                for (Map.Entry<String, List<String>> entry : monitorMap.entrySet()) {
                    LOGGER.log(Level.INFO, "monitorMap Key = " + entry.getKey() + ", Value = " + entry.getValue() + ", ctClassName:" + ctClass.getName());
                    //先判断类名再判断方法名
                    if (StringUtils.equals(entry.getKey(), ctClass.getName())) {
                        if (entry.getValue() != null && entry.getValue() instanceof List) {
                            for (String methodName : entry.getValue()) {
                                if (ctMethodMap.get(methodName) != null) {
                                    monitoredMethodsByAgentArgs.add(ctMethodMap.get(methodName));
                                }
                            }
                        }
                    }
                }
            }
            LOGGER.log(Level.INFO, "monitoredMethodsByAgentArgs:" + monitoredMethodsByAgentArgs.toString());

            //3，配置中心方式 todo

            //合并多个set
            Set<CtMethod> monitoredMethods = new HashSet<>();
            monitoredMethods.addAll(monitoredMethodsByAnnotation);
            monitoredMethods.addAll(monitoredMethodsByAgentArgs);

            if (!monitoredMethods.isEmpty()) {
                return monitoredMethods;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
