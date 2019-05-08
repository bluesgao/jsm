package com.github.bluesgao.jsm.agent.monitorcfg;

import com.github.bluesgao.jsm.annotation.MonitorClass;
import com.github.bluesgao.jsm.annotation.MonitorMethod;
import javassist.CtClass;
import javassist.CtMethod;

import java.util.HashSet;
import java.util.Set;

public class AnnotationMonitorCfg {
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
    public static Set<CtMethod> getMonitoredMethods(ClassLoader loader, CtClass ctClass) {
        try {
            //类的所有的声明方法
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            //需要被监控的方法
            Set<CtMethod> monitoredMethods = new HashSet<CtMethod>();

            //如果对类进行监控，意味着对类的所有方法进行监控
            if (isMonitoredClass(ctClass)) {
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    monitoredMethods.add(m);
                    //String key = ctClass.getName() + "$" + m.getName() + "$" + m.getSignature();
                    //TRACE_METHODS.put(key, m);
                }
            } else {//只对某个类的某些方法进行监控
                //获取被监控的方法
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    if (isMonitoredMethod(m)) {
                        monitoredMethods.add(m);
                        //类名$方法名$方法签名
                        //String key = ctClass.getName() + "$" + m.getName() + "$" + m.getSignature();
                    }
                }
            }

            if (!monitoredMethods.isEmpty()) {
                return monitoredMethods;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
