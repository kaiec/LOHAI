/*
 * This file is part of Semtinel (http://www.semtinel.org).
 * Copyright (c) 2007-2010 Kai Eckert (http://www.kaiec.org).
 *
 * Semtinel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semtinel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Semtinel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtinel.core.data.api;

import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 *
 * @author ridaa78
 */
public interface AnnotationSetGroup {

    public static final DataFlavor DATA_FLAVOR = new DataFlavor(AnnotationSetGroup.class, "AnnotationSetGroup");

    Long getId();

    String getName();

    List<AnnotationSet> getAnnotationSets();

    void setId(Long id);

    void setName(String name);

    void setAnnotationSets(List<AnnotationSet> annotationSets);

}