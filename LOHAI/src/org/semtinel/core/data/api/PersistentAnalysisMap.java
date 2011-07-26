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
 * The PersistentANalysisMap can be used by analysis plugins
 * to store precalculated results for speed reasons. The Map is
 * restricted to Float values.
 *
 * Tu use this map, your analysis has to create a setting string that
 * identifies the datasets, for which the stored values are valid.
 *
 * For example, if you have an analysis that works on an annotation set
 * and calculates a value for each concept in a concept scheme, you could
 * use a string that contains both the concept scheme id and the
 * annotation set id, like "CS_8_AS_4".
 *
 * Usually, this string contains all elements of the RegisterSet used by
 * the analysis.
 *
 * If the PersistentAnalysisMap does not fulfill your needs, you can
 * use your own database with {@link CoreManager#getPluginDatabaseConnection(java.lang.Class)}
 *
 * @author Kai Eckert
 */
public interface PersistentAnalysisMap {
    /**
     * Checks, if there are stored values for the given setting.
     * @param analysisSetting The setting string.
     * @return True, if there are stored values, false otherwise.
     */
    boolean isDataAvailable(String analysisSetting);
    /**
     * Sets the setting string used by this map. The setting string
     * identifies the input data for the analysis, like involved
     * concept scheme, annotation sets and so on. See above.
     * @param analysisSetting The new setting string.
     */
    void setAnalysisSetting(String analysisSetting);
    /**
     * Stores a new value, usually for a concept. In this case,
     * the database id of the concept may be used as key.
     * @param key The key to identify the stored value.
     * @param value The value.
     */
    void put(String key, Float value);
    /**
     * Returns a stored value for a given key.
     * @param key The key, for which the value should be retrieved.
     * @return The value.
     */
    Float get(String key);

    /**
     * CHecks, if this map is in bulk mode.
     * @return True, if this map is in bulk mode, false otherwire.
     * @see #setBulkMode(boolean)
     */
    boolean isBulkMode();

    /**
     * If bulk mode is turned on, the data is not directly written
     * to the database. This should be used to store large amounts
     * of data. To actually store the data, set bulk mode back to false
     * after finishing.
     *
     * @param bulkMode The new value of bulk mode. true=on, false=off.
     */
    void setBulkMode(boolean bulkMode);

    /**
     * Clears the data associated with this map.
     */
    void clear();
}
