package com.github.bluesgao.jsm.agent.transformer;

import javassist.CtClass;
import javassist.NotFoundException;

public class AgentUtils {
    public static final char JVM_VOID = 'V';
    public static final char JVM_BOOLEAN = 'Z';
    public static final char JVM_BYTE = 'B';
    public static final char JVM_CHAR = 'C';
    public static final char JVM_DOUBLE = 'D';
    public static final char JVM_FLOAT = 'F';
    public static final char JVM_INT = 'I';
    public static final char JVM_LONG = 'J';
    public static final char JVM_SHORT = 'S';

    public static String getMethodDesc(final String methodName, final CtClass[] parameterTypes) {
        final StringBuilder sb = new StringBuilder(methodName);
        sb.append("(");
        if (parameterTypes != null && parameterTypes.length > 0) {
            boolean first = true;
            for (final CtClass type : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                try {
                    sb.append(desc2name(getDesc(type)));
                } catch (NotFoundException ex) {
                }
            }
        }
        sb.append(")");
        return sb.toString().replaceAll("\\s*", "");
    }

    public static String getMethodDesc(final String methodName, final Class[] parameterTypes) {
        final StringBuilder sb = new StringBuilder(methodName);
        sb.append("(");
        if (parameterTypes != null && parameterTypes.length > 0) {
            boolean first = true;
            for (final Class type : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(desc2name(getDesc(type)));
            }
        }
        sb.append(")");
        return sb.toString().replaceAll("\\s*", "");
    }

    public static String getMethodDesc(final String methodName, final String[] parameterTypes) {
        final StringBuilder sb = new StringBuilder(methodName);
        sb.append("(");
        if (parameterTypes != null && parameterTypes.length > 0) {
            boolean first = true;
            for (final String type : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(type);
            }
        }
        sb.append(")");
        return sb.toString().replaceAll("\\s*", "");
    }

    public static String getDesc(final CtClass c) throws NotFoundException {
        final StringBuilder ret = new StringBuilder();
        if (c.isArray()) {
            ret.append('[');
            ret.append(getDesc(c.getComponentType()));
        } else if (c.isPrimitive()) {
            final String t = c.getName();
            if ("void".equals(t)) {
                ret.append('V');
            } else if ("boolean".equals(t)) {
                ret.append('Z');
            } else if ("byte".equals(t)) {
                ret.append('B');
            } else if ("char".equals(t)) {
                ret.append('C');
            } else if ("double".equals(t)) {
                ret.append('D');
            } else if ("float".equals(t)) {
                ret.append('F');
            } else if ("int".equals(t)) {
                ret.append('I');
            } else if ("long".equals(t)) {
                ret.append('J');
            } else if ("short".equals(t)) {
                ret.append('S');
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

    public static String getDesc(final Class c) {
        final StringBuilder ret = new StringBuilder();
        if (c.isArray()) {
            ret.append('[');
            ret.append(getDesc(c.getComponentType()));
        } else if (c.isPrimitive()) {
            final String t = c.getCanonicalName();
            if ("void".equals(t)) {
                ret.append('V');
            } else if ("boolean".equals(t)) {
                ret.append('Z');
            } else if ("byte".equals(t)) {
                ret.append('B');
            } else if ("char".equals(t)) {
                ret.append('C');
            } else if ("double".equals(t)) {
                ret.append('D');
            } else if ("float".equals(t)) {
                ret.append('F');
            } else if ("int".equals(t)) {
                ret.append('I');
            } else if ("long".equals(t)) {
                ret.append('J');
            } else if ("short".equals(t)) {
                ret.append('S');
            }
        } else {
            ret.append('L');
            ret.append(c.getCanonicalName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

    public static String desc2name(final String desc) {
        final StringBuilder sb = new StringBuilder();
        int c = desc.lastIndexOf(91) + 1;
        if (desc.length() == c + 1) {
            switch (desc.charAt(c)) {
                case 'V': {
                    sb.append("void");
                    break;
                }
                case 'Z': {
                    sb.append("boolean");
                    break;
                }
                case 'B': {
                    sb.append("byte");
                    break;
                }
                case 'C': {
                    sb.append("char");
                    break;
                }
                case 'D': {
                    sb.append("double");
                    break;
                }
                case 'F': {
                    sb.append("float");
                    break;
                }
                case 'I': {
                    sb.append("int");
                    break;
                }
                case 'J': {
                    sb.append("long");
                    break;
                }
                case 'S': {
                    sb.append("short");
                    break;
                }
                default: {
                    throw new RuntimeException();
                }
            }
        } else {
            sb.append(desc.substring(c + 1, desc.length() - 1).replace('/', '.'));
        }
        while (c-- > 0) {
            sb.append("[]");
        }
        return sb.toString();
    }

    public static boolean ignoreClassLoader(ClassLoader loader) {
        if (loader == null) {
            return true;
        }
        final String loaderName = loader.getClass().getCanonicalName();
        return loaderName.equals("sun.reflect.DelegatingClassLoader") ||
                loaderName.endsWith("com.alibaba.fastjson.util.ASMClassLoader") ||
                loaderName.endsWith("org.apache.jasper.servlet.JasperLoader") ||
                loaderName.startsWith("com.jd.security.") ||
                loaderName.startsWith("jdk.nashorn.");
    }

    public static boolean ignoreClass(String className) {
        return className == null ||
                className.startsWith("java/") || className.startsWith("java.") ||
                className.startsWith("sun/") || className.startsWith("sun.") ||
                className.startsWith("com/intellij") || className.startsWith("com.intellij")||
                className.startsWith("com/github/bluesgao/jsm/agent");
    }
}
