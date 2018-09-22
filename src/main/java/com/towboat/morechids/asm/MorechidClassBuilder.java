package com.towboat.morechids.asm;

import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRegistry;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

public class MorechidClassBuilder {
    private static java.lang.reflect.Method getDefineClass()
    {
        java.lang.reflect.Method defineClass;
        try {
            defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return defineClass;
    }

    private static Class<? extends CustomOrechidSubtile> loadClass(String name, byte[] bytes) {
        final java.lang.reflect.Method defineClass = getDefineClass(); 

        Class<? extends CustomOrechidSubtile> clazz = null;
        try {
            final ClassLoader loader = CustomOrechidSubtile.class.getClassLoader();
            clazz = (Class)defineClass.invoke(loader, name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }

    public static Class<? extends CustomOrechidSubtile> generateMorechid(String name) {
        Type baseMorechidType = Type.getType(CustomOrechidSubtile.class);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, "Morechid_" + name, null, baseMorechidType.getInternalName(), null);

        // generate constructor
        Method customOrechidConstructor = Method.getMethod("void <init> ()");
        GeneratorAdapter constructorGenerator = new GeneratorAdapter(Opcodes.ACC_PUBLIC, customOrechidConstructor, null, null, classWriter);
        constructorGenerator.loadThis();
        constructorGenerator.invokeConstructor(baseMorechidType, customOrechidConstructor);
        constructorGenerator.returnValue();
        constructorGenerator.endMethod();

        classWriter.visitEnd();

        return loadClass("Morechid_"+name, classWriter.toByteArray());
    }
}
