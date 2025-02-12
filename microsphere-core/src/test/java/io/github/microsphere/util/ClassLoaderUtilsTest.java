/**
 *
 */
package io.github.microsphere.util;

import io.github.microsphere.AbstractTestCase;
import io.github.microsphere.security.TestSecurityManager;
import junit.framework.Assert;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static io.github.microsphere.constants.FileConstants.CLASS_EXTENSION;
import static io.github.microsphere.util.CollectionUtils.ofSet;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ClassLoaderUtils} {@link Test}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @version 1.0.0
 * @see ClassLoaderUtils
 * @since 1.0.0
 */
public class ClassLoaderUtilsTest extends AbstractTestCase {

    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    @Test
    public void testFields() throws Exception {


        List<Field> allFields = FieldUtils.getAllFieldsList(ClassLoader.class);

//        echo(ToStringBuilder.reflectionToString(classLoader,ToStringStyle.MULTI_LINE_STYLE));
        Set<ClassLoader> classLoaders = ClassLoaderUtils.getInheritableClassLoaders(classLoader);
        for (ClassLoader classLoader : classLoaders) {
            echo(String.format("ClassLoader : %s", classLoader));
            for (Field field : allFields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    String message = String.format("Field name : %s , value : %s", field.getName(), ToStringBuilder.reflectionToString(field.get(classLoader), ToStringStyle.NO_CLASS_NAME_STYLE));
                    echo(message);
                }
            }
        }

    }


    @Test
    public void testResolve() {
        String resourceName = "META-INF/abc/def";
        String expectedResourceName = "META-INF/abc/def";
        String resolvedResourceName = ClassLoaderUtils.ResourceType.DEFAULT.resolve(resourceName);
        Assert.assertEquals(expectedResourceName, resolvedResourceName);

        resourceName = "///////META-INF//abc\\/def";
        resolvedResourceName = ClassLoaderUtils.ResourceType.DEFAULT.resolve(resourceName);
        Assert.assertEquals(expectedResourceName, resolvedResourceName);

        resourceName = "java.lang.String.class";

        expectedResourceName = "java/lang/String.class";
        resolvedResourceName = ClassLoaderUtils.ResourceType.CLASS.resolve(resourceName);
        Assert.assertEquals(expectedResourceName, resolvedResourceName);

        resourceName = "java.lang";
        expectedResourceName = "java/lang/";
        resolvedResourceName = ClassLoaderUtils.ResourceType.PACKAGE.resolve(resourceName);
        Assert.assertEquals(expectedResourceName, resolvedResourceName);

    }

    @Test
    public void testGetClassResource() {
        URL classResourceURL = ClassLoaderUtils.getClassResource(classLoader, ClassLoaderUtilsTest.class);
        Assert.assertNotNull(classResourceURL);
        echo(classResourceURL);

        classResourceURL = ClassLoaderUtils.getClassResource(classLoader, String.class.getName());
        Assert.assertNotNull(classResourceURL);
        echo(classResourceURL);
    }

    @Test
    public void testGetResource() {
        URL resourceURL = ClassLoaderUtils.getResource(classLoader, ClassLoaderUtilsTest.class.getName() + CLASS_EXTENSION);
        Assert.assertNotNull(resourceURL);
        echo(resourceURL);

        resourceURL = ClassLoaderUtils.getResource(classLoader, "///java/lang/CharSequence.class");
        Assert.assertNotNull(resourceURL);
        echo(resourceURL);

        resourceURL = ClassLoaderUtils.getResource(classLoader, "//META-INF/services/java.lang.CharSequence");
        Assert.assertNotNull(resourceURL);
        echo(resourceURL);
    }

    @Test
    public void testGetResources() throws IOException {
        Set<URL> resourceURLs = ClassLoaderUtils.getResources(classLoader, ClassLoaderUtilsTest.class.getName() + CLASS_EXTENSION);
        Assert.assertNotNull(resourceURLs);
        Assert.assertEquals(1, resourceURLs.size());
        echo(resourceURLs);

        resourceURLs = ClassLoaderUtils.getResources(classLoader, "///java/lang/CharSequence.class");
        Assert.assertNotNull(resourceURLs);
        Assert.assertEquals(1, resourceURLs.size());
        echo(resourceURLs);

        resourceURLs = ClassLoaderUtils.getResources(classLoader, "//META-INF/services/java.lang.CharSequence");
        Assert.assertNotNull(resourceURLs);
        Assert.assertEquals(1, resourceURLs.size());
        echo(resourceURLs);
    }

    @Test
    public void testClassLoadingMXBean() {
        ClassLoadingMXBean classLoadingMXBean = ClassLoaderUtils.classLoadingMXBean;
        Assert.assertEquals(classLoadingMXBean.getTotalLoadedClassCount(), ClassLoaderUtils.getTotalLoadedClassCount());
        Assert.assertEquals(classLoadingMXBean.getLoadedClassCount(), ClassLoaderUtils.getLoadedClassCount());
        Assert.assertEquals(classLoadingMXBean.getUnloadedClassCount(), ClassLoaderUtils.getUnloadedClassCount());
        Assert.assertEquals(classLoadingMXBean.isVerbose(), ClassLoaderUtils.isVerbose());

        ClassLoaderUtils.setVerbose(true);
        Assert.assertTrue(ClassLoaderUtils.isVerbose());
    }

    @Test
    public void testGetInheritableClassLoaders() {
        Set<ClassLoader> classLoaders = ClassLoaderUtils.getInheritableClassLoaders(classLoader);
        Assert.assertNotNull(classLoaders);
        Assert.assertTrue(classLoaders.size() > 1);
        echo(classLoaders);
    }

    @Test
    public void testGetLoadedClasses() {
        Set<Class<?>> classesSet = ClassLoaderUtils.getLoadedClasses(classLoader);
        Assert.assertNotNull(classesSet);
        Assert.assertFalse(classesSet.isEmpty());


        classesSet = ClassLoaderUtils.getLoadedClasses(ClassLoader.getSystemClassLoader());
        Assert.assertNotNull(classesSet);
        Assert.assertFalse(classesSet.isEmpty());
        echo(classesSet);
    }

    @Test
    public void testGetAllLoadedClasses() {
        Set<Class<?>> classesSet = ClassLoaderUtils.getAllLoadedClasses(classLoader);
        Assert.assertNotNull(classesSet);
        Assert.assertFalse(classesSet.isEmpty());


        classesSet = ClassLoaderUtils.getAllLoadedClasses(ClassLoader.getSystemClassLoader());
        Assert.assertNotNull(classesSet);
        Assert.assertFalse(classesSet.isEmpty());
        echo(classesSet);
    }

    @Test
    public void testGetAllLoadedClassesMap() {
        Map<ClassLoader, Set<Class<?>>> allLoadedClassesMap = ClassLoaderUtils.getAllLoadedClassesMap(classLoader);
        Assert.assertNotNull(allLoadedClassesMap);
        Assert.assertFalse(allLoadedClassesMap.isEmpty());
    }


    @Test
    public void testFindLoadedClass() {

        Class<?> type = null;
        for (Class<?> class_ : ClassLoaderUtils.getAllLoadedClasses(classLoader)) {
            type = ClassLoaderUtils.findLoadedClass(classLoader, class_.getName());
            Assert.assertEquals(class_, type);
        }

        type = ClassLoaderUtils.findLoadedClass(classLoader, String.class.getName());
        Assert.assertEquals(String.class, type);

        type = ClassLoaderUtils.findLoadedClass(classLoader, Double.class.getName());
        Assert.assertEquals(Double.class, type);
    }

    @Test
    public void testIsLoadedClass() {
        Assert.assertTrue(ClassLoaderUtils.isLoadedClass(classLoader, String.class));
        Assert.assertTrue(ClassLoaderUtils.isLoadedClass(classLoader, Double.class));
        Assert.assertTrue(ClassLoaderUtils.isLoadedClass(classLoader, Double.class.getName()));
    }


    @Test
    public void testFindLoadedClassesInClassPath() {
        Double d = null;
        Set<Class<?>> allLoadedClasses = ClassLoaderUtils.findLoadedClassesInClassPath(classLoader);

        Set<Class<?>> classesSet = ClassLoaderUtils.getAllLoadedClasses(classLoader);

        Set<Class<?>> remainingClasses = new LinkedHashSet<>(allLoadedClasses);

        remainingClasses.addAll(classesSet);

        Set<Class<?>> sortedClasses = new TreeSet(new ClassComparator());
        sortedClasses.addAll(remainingClasses);

        echo(sortedClasses);

        int loadedClassesSize = allLoadedClasses.size() + classesSet.size();

        int loadedClassCount = ClassLoaderUtils.getLoadedClassCount();

        echo(loadedClassesSize);
        echo(loadedClassCount);
    }

    @Test
    public void testGetCount() {
        long count = ClassLoaderUtils.getTotalLoadedClassCount();
        Assert.assertTrue(count > 0);

        count = ClassLoaderUtils.getLoadedClassCount();
        Assert.assertTrue(count > 0);

        count = ClassLoaderUtils.getUnloadedClassCount();
        Assert.assertTrue(count > -1);
    }

    @Test
    public void testFindLoadedClassesInClassPaths() {
        Set<Class<?>> allLoadedClasses = ClassLoaderUtils.findLoadedClassesInClassPaths(classLoader, ClassPathUtils.getClassPaths());
        Assert.assertFalse(allLoadedClasses.isEmpty());
    }

    @Test
    public void testOfSet() {
        Set<String> set = ofSet();
        assertEquals(emptySet(), set);

        set = ofSet(((String[]) null));
        assertEquals(emptySet(), set);

        set = ofSet("A", "B", "C");
        Set<String> expectedSet = new LinkedHashSet<>();
        expectedSet.add("A");
        expectedSet.add("B");
        expectedSet.add("C");
        assertEquals(expectedSet, set);
    }

    @Test
    public void testGetClassLoader() {
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = currentThread.getContextClassLoader();
        assertEquals(classLoader, ClassLoaderUtils.getClassLoader());

        currentThread.setContextClassLoader(null);
        assertEquals(ClassLoaderUtils.class.getClassLoader(), ClassLoaderUtils.getClassLoader());

        currentThread.setContextClassLoader(ClassLoader.getSystemClassLoader().getParent());
        TestSecurityManager.denyRuntimePermission("getClassLoader", () -> {
            new Runnable() {
                @Override
                public void run() {
                    assertEquals(ClassLoaderUtils.class.getClassLoader(), ClassLoaderUtils.getClassLoader());
                }
            }.run();
        });

    }


    private static class ClassComparator implements Comparator<Class<?>> {

        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            String cn1 = o1.getName();
            String cn2 = o2.getName();
            return cn1.compareTo(cn2);
        }
    }

}
