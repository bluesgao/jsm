package com.github.bluesgao.jsm.agent.transformer;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AccessFlag;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class增强器
 */
public class ClassEnhancer {
    private static final Logger LOGGER = Logger.getLogger(ClassEnhancer.class.getCanonicalName());

    private static final String START_TIME = "\nlong startTime = System.currentTimeMillis();\n";
    private static final String END_TIME = "\nlong endTime = System.currentTimeMillis();\n";
    private static final String METHOD_RUTURN_VALUE_VAR = "__time_monitor_result";

    /**
     * 重写bytecode
     *
     * @param ctClass
     * @param ctMethods
     * @param loader
     * @return
     * @throws Exception
     */
    public static byte[] enhance(ClassLoader loader, CtClass ctClass, Set<CtMethod> ctMethods) throws Exception {
        for (CtMethod ctMethod : ctMethods) {
            LOGGER.log(Level.INFO, "ClassEnhancer enhance ctMethod：" + ctMethod.getMethodInfo() + ",modifiers:" + ctMethod.getModifiers());
            try {
                //native方法和abstract方法忽略
                if (ctMethod.getModifiers() != AccessFlag.NATIVE || ctMethod.getModifiers() != AccessFlag.ABSTRACT) {

                    String className = ctClass.getName();
                    String methodName = AgentUtils.getMethodDesc(ctMethod.getName(), ctMethod.getParameterTypes());
                    //String methodName = ctMethod.getName();

                    LOGGER.log(Level.INFO, "ClassEnhancer enhance className：" + className + ", methodName:" + methodName);

                    String appName = "testapp";
                    //应用名称-类名-方法名-输入-输出-异常
                    String monitorStr = String.format("JSM MONITOR-[%s]-[%s]-[%s]-[%s]-[%s]-[%s] ", appName, className, methodName, null, null, null);
                    String sout = "System.out.println(\"%s\");";

                    //StringBuffer beforeCode = new StringBuffer();
                    //beforeCode.append(String.format(sout, commonStr + "input[" + null + "]"));

                    //StringBuffer afterCode = new StringBuffer();
                    //afterCode.append(String.format(sout, commonStr + "output[" + null + "]"));

                    //tringBuffer catchCode = new StringBuffer();
                    //catchCode.append(String.format(sout, commonStr + "exception[" + null + "]"));

                    //CtClass throwableClass = ctClass.getClassPool().get(Throwable.class.getCanonicalName());

                    //LOGGER.log(Level.INFO, "ClassEnhancer enhance beforeCode：" + beforeCode.toString());
                    //LOGGER.log(Level.INFO, "ClassEnhancer enhance afterCode：" + afterCode.toString());
                    //LOGGER.log(Level.INFO, "ClassEnhancer enhance catchCode：" + catchCode.toString());

                    //ctMethod.insertBefore(beforeCode.toString());
                    //ctMethod.insertAfter(afterCode.toString(), true);
                    //ctMethod.addCatch(catchCode.toString(), throwableClass);

                    StringBuffer insertCode = new StringBuffer();
                    insertCode.append(String.format(sout, monitorStr));
                    ctMethod.insertAfter(insertCode.toString(), true);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = ctClass.toBytecode();
        ctClass.defrost();
        return bytes;
    }

    public static byte[] enhance2(ClassLoader loader, CtClass ctClass, Set<CtMethod> ctMethods) throws Exception {
        for (CtMethod ctMethod : ctMethods) {
            //native方法和abstract方法忽略
            if (ctMethod.getModifiers() != AccessFlag.NATIVE || ctMethod.getModifiers() != AccessFlag.ABSTRACT) {
                //获取原始方法名称
                String methodName = ctMethod.getName();
                String monitorStr = "\nSystem.out.println(\"method " + ctMethod.getLongName() + " cost:\" +(endTime - startTime) +\"ms.\");";
                //实例化新的方法名称
                String newMethodName = methodName + "$impl";
                //设置新的方法名称
                ctMethod.setName(newMethodName);
                //创建新的方法，复制原来的方法 ，名字为原来的名字
                CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass, null);

                StringBuilder bodyStr = new StringBuilder();
                //拼接新的方法内容
                bodyStr.append("{");
                //返回类型
                CtClass returnType = ctMethod.getReturnType();
                //是否需要返回
                boolean hasReturnValue = (CtClass.voidType != returnType);
                if (hasReturnValue) {
                    String returnClass = returnType.getName();
                    bodyStr.append("\n").append(returnClass + " " + METHOD_RUTURN_VALUE_VAR + ";");
                }

                bodyStr.append(START_TIME);
                if (hasReturnValue) {
                    bodyStr.append("\n").append(METHOD_RUTURN_VALUE_VAR + " = ($r)" + newMethodName + "($$);");
                } else {
                    bodyStr.append("\n").append(newMethodName + "($$);");
                }

                bodyStr.append(END_TIME);
                bodyStr.append(monitorStr);

                if (hasReturnValue) {
                    bodyStr.append("\n").append("return " + METHOD_RUTURN_VALUE_VAR + " ;");
                }

                bodyStr.append("}");
                //替换新方法
                newMethod.setBody(bodyStr.toString());
                //增加新方法
                ctClass.addMethod(newMethod);
            }
        }
        byte[] bytes = ctClass.toBytecode();
        ctClass.defrost();
        return bytes;
    }
}
