/*
 * Copyright (C) 2006-2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.gwt.beansbinding.core.client.ext;


/**
 * 
 * @author sky
 * @author Shannon Hickey
 */
public interface BeanAdapterProvider {

  boolean providesAdapter(Class<?> type, String property);

  BeanAdapter createAdapter(Object source, String property);

  Class<?> getAdapterClass(Class<?> type);
}