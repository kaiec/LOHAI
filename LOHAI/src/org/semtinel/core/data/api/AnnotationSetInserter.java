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

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author kai
 */
public interface AnnotationSetInserter {

    void release();

    void insertAnnotations(String externalId, List<String> annotations);
    void insertAnnotations(Long id, List<Long> annotationIds);

    void insertAnnotationSet(Connection theirConn, Long theirId, Long recordsetId, Long conceptschemeId);

    Long insertAnnotationSet(String name, ConceptScheme cs);

    void insertAnnotationSetAnnotation(Long annotationSetId, Long annotationId);

    Long insertAnnotationSet(String name, ConceptScheme cs, boolean virtual);

    Long insertAnnotationSet(String name, Long conceptSchemeId, boolean virtual);

    void insertAnnotationNumber(Long setId, String singleAnnotation, long number);

    void setConceptSchemeId(Long conceptSchemeId);

    void setSetName(String setName);

    /**
     * Case Sensitivity for concept identifier match
     * @param value
     */
    void setCaseSensitive(boolean value);
    /**
     * Case Sensitivity for concept identifier match
     * @return
     */
    boolean isCaseSensitive();
}
