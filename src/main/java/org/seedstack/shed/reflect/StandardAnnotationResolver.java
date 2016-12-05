/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.shed.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class StandardAnnotationResolver<E extends AnnotatedElement, A extends Annotation> implements Function<E, Optional<A>>, Predicate<E> {
    private final Class<E> annotatedElementClass;
    private final Class<A> annotationClass;

    @SuppressWarnings("unchecked")
    protected StandardAnnotationResolver() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        annotatedElementClass = (Class<E>) actualTypeArguments[0];
        annotationClass = (Class<A>) actualTypeArguments[1];
    }

    @Override
    public Optional<A> apply(E element) {
        return resolveAnnotation(element);
    }

    @Override
    public boolean test(E element) {
        return resolveAnnotation(element).isPresent();
    }

    private Optional<A> resolveAnnotation(E element) {
        Annotations.OnAnnotatedElement on;
        if (Executable.class.isAssignableFrom(annotatedElementClass)) {
            on = Annotations.on(((Executable) element)).traversingOverriddenMembers();
        } else {
            on = Annotations.on(element);
        }
        return on.fallingBackOnClasses()
                .includingMetaAnnotations()
                .find(annotationClass);
    }
}