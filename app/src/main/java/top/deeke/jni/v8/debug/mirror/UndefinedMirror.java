/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package top.deeke.jni.v8.debug.mirror;

import top.deeke.jni.v8.V8Object;

/**
 * Represents 'Undefined' Mirrors
 */
public class UndefinedMirror extends ValueMirror {


    UndefinedMirror(final V8Object v8Object) {
        super(v8Object);
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    @Override
    public String toString() {
        return "undefined";
    }

}
