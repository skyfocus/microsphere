/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microsphere.annotation.processor.util;

import io.github.microsphere.annotation.processor.AbstractAnnotationProcessingTest;
import io.github.microsphere.annotation.processor.TestServiceImpl;
import io.github.microsphere.annotation.processor.model.Model;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Set;

import static io.github.microsphere.annotation.processor.util.MemberUtils.getAllDeclaredMembers;
import static io.github.microsphere.annotation.processor.util.MemberUtils.getDeclaredMembers;
import static io.github.microsphere.annotation.processor.util.MemberUtils.hasModifiers;
import static io.github.microsphere.annotation.processor.util.MemberUtils.isPublicNonStatic;
import static io.github.microsphere.annotation.processor.util.MemberUtils.matchParameterTypes;
import static io.github.microsphere.annotation.processor.util.MethodUtils.findMethod;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.util.ElementFilter.fieldsIn;
import static javax.lang.model.util.ElementFilter.methodsIn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link MemberUtils} Test
 *
 * @since 1.0.0
 */
public class MemberUtilsTest extends AbstractAnnotationProcessingTest {

    private TypeElement testType;

    @Override
    protected void addCompiledClasses(Set<Class<?>> classesToBeCompiled) {
    }

    @Override
    protected void beforeEach() {
        testType = getType(TestServiceImpl.class);
    }

    @Test
    public void testIsPublicNonStatic() {
        assertFalse(isPublicNonStatic(null));
        methodsIn(getDeclaredMembers(testType.asType())).forEach(method -> assertTrue(isPublicNonStatic(method)));
    }

    @Test
    public void testHasModifiers() {
        assertFalse(hasModifiers(null));
        List<? extends Element> members = getAllDeclaredMembers(testType.asType());
        List<VariableElement> fields = fieldsIn(members);
        assertTrue(hasModifiers(fields.get(0), PRIVATE));
    }

    @Test
    public void testDeclaredMembers() {
        TypeElement type = getType(Model.class);
        List<? extends Element> members = getDeclaredMembers(type.asType());
        List<VariableElement> fields = fieldsIn(members);
        assertEquals(19, members.size());
        assertEquals(6, fields.size());
        assertEquals("f", fields.get(0).getSimpleName().toString());
        assertEquals("d", fields.get(1).getSimpleName().toString());
        assertEquals("tu", fields.get(2).getSimpleName().toString());
        assertEquals("str", fields.get(3).getSimpleName().toString());
        assertEquals("bi", fields.get(4).getSimpleName().toString());
        assertEquals("bd", fields.get(5).getSimpleName().toString());

        members = getAllDeclaredMembers(type.asType());
        fields = fieldsIn(members);
        assertEquals(11, fields.size());
        assertEquals("f", fields.get(0).getSimpleName().toString());
        assertEquals("d", fields.get(1).getSimpleName().toString());
        assertEquals("tu", fields.get(2).getSimpleName().toString());
        assertEquals("str", fields.get(3).getSimpleName().toString());
        assertEquals("bi", fields.get(4).getSimpleName().toString());
        assertEquals("bd", fields.get(5).getSimpleName().toString());
        assertEquals("b", fields.get(6).getSimpleName().toString());
        assertEquals("s", fields.get(7).getSimpleName().toString());
        assertEquals("i", fields.get(8).getSimpleName().toString());
        assertEquals("l", fields.get(9).getSimpleName().toString());
        assertEquals("z", fields.get(10).getSimpleName().toString());
    }

    @Test
    public void testMatchParameterTypes() {
        ExecutableElement method = findMethod(testType, "echo", "java.lang.String");
        assertTrue(matchParameterTypes(method.getParameters(), "java.lang.String"));
        assertFalse(matchParameterTypes(method.getParameters(), "java.lang.Object"));
    }
}
