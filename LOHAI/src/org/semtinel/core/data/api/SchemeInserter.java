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
public interface SchemeInserter {

    long insertConcept(String uri, String prefLabel, List<String> altLabels, List<String> hiddenLabels, String scopeNote);

    long insertConcept(String uri, String prefLabel);

    Long insertConcept(String label, String notation, Long parent);

    void insertParentChild(Long parent, Long child);

    void insertParentChild(Long parent, Long child, boolean validation);

    void release();

    void setLanguage(String name);

    Language getLanguage();

    ConceptScheme getConceptScheme();

    Concept createConcept();

    Label createLabel();

    Note createNote();

    Notation createNotation();

    long insertConcept(Concept concept);

	 void insertScheme(Connection theirConn, Long theirId);
}
