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

/**
 * The tree position is used to build a tree out of a
 * (polyhierarchic) thesaurus. As a concept is allowed
 * to have several different positions within the thesaurus
 * (i.e. to have several different parent concepts), we actually
 * build a second tree structure with tree positions that is
 * monohierarchic.
 * 
 * @author Kai Eckert
 */
public interface TreePosition {
    /**
     * Returns the parent of this tree position.
     * @return The parent.
     */
    TreePosition getParent();
    /**
     * Returns the {@link Concept} associated with this position.
     *
     * @return The concept.
     */
    Concept getConcept();
}
