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
 * @author kai
 */
public interface AnnotationSet {
    public static final DataFlavor DATA_FLAVOR = new DataFlavor(AnnotationSet.class, "AnnotationSet");


    List<AnnotationSetGroup> getAnnotationSetGroups();

    ConceptScheme getConceptScheme();

    Long getId();

    String getName();

    void setConceptScheme(ConceptScheme conceptScheme);

    void setId(Long id);

    void setName(String name);

    List<Annotation> getAnnotations();

    void setAnnotations(List<Annotation> annotations);

    long getSize();

    /**
     * Virtual Annotation Sets are Sets that do not actually
     * contain Annotations, instead they only contain the information,
     * how many annotations are available for a given concept.
     *
     * @return true, if the AnnotationSet is virtual, false otherwise.
     */
    boolean isVirtual();

    /**
     * If the AnnotationSet is virtual, this method return the number
     * of virtual annotations. If not, it return the number of actual annotations.
     * @param concept The concept for which the number of annotations are returned.
     * @return The number of annotations
     */
    long getNumberOfAnnotations(Concept concept);

    /**
     * Removes the record set from the specified group.
     *
     * WARNING: You can not use this method inside an iterated loop over
     * the recordsets of a group or the groups of a recordset, as both
     * collections are modified by this method. Will result in a ConcurrentModificationException.
     *
     * @param rsg
     */
    void removeFromGroup(AnnotationSetGroup rsg);

    long getNumberOfAnnotations(Long conceptId);

    String getDescription();

    void setDescription(String description);

}
