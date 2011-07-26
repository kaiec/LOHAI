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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;

/**
 * The CoreManager interface is the central accesspoint of the Semtinel 
 * API. To get an instance of the current CoreManager implementation,
 * use Lookup.getDefault().lookup(CoreManager.class);
 *
 * Via the CoreManager, you have access to the underlying database.
 *
 * @author Kai Eckert
 */
public interface CoreManager {

    /**
     * Flag for the AnnotationSetInserter: Use URI to identify the
     * concept of the annotation.
     */
    public static final int ANNOTATIONSET_INSERTER_TYPE_URI = 0;
    /**
     * Flag for the AnnotationSetInserter: Use preferred label to
     * identify the concept of the annotation.
     */
    public static final int ANNOTATIONSET_INSERTER_TYPE_PREFLABEL = 1;
    /**
     * Flag for the AnnotationSetInserter: Use database id to identify
     * the concept of the annotation.
     */
    public static final int ANNOTATIONSET_INSERTER_TYPE_ID = 2;
    /**
     * Flag for the AnnotationSetInserter: Use any matching label
     * to identify the concept of the annotation (may be ambiguous, first
     * hit is used).
     */
    public static final int ANNOTATIONSET_INSERTER_TYPE_ANYLABEL = 3;

    /**
     * This method returns a List of all concept schemes currently available
     * in the database.
     * @return A list of all available concept schemes.
     */
    List<ConceptScheme> getConceptSchemes();

    /**
     * This method returns a concept scheme based on its id
     * in the database.
     * @return A concept scheme or null.
     */
    ConceptScheme getConceptScheme(Long id);
   /**
     * This method returns a record set based on its id
     * in the database.
     * @return A record set or null.
     */
    RecordSet getRecordSet(Long id);
   /**
     * This method returns an annotation set based on its id
     * in the database.
     * @return An annotation set or null.
     */
    AnnotationSet getAnnotationSet(Long id);

    /**
     * This method returns a List of all record sets currently available
     * in the database.
     * @return A list of all available record sets.
     */
    List<RecordSet> getRecordSets();

    /**
     * This method returns a List of all annotation sets currently available
     * in the database.
     *
     * @return A list of all available annotation sets.
     */
    List<AnnotationSet> getAnnotationSets();

    /**
     * This method returns a List of all record sources currently available
     *
     * @return A list of all availabe record sources.
     */
    List<RecordSource> getRecordSources();

    /**
     * This method returns a List of all annotation sources currently available
     *
     * @return A list of all availabe annotation sources.
     */
    List<AnnotationSource> getAnnotationSources();

    /**
     * The {@link AnnotationSetInserter} is used to bypass Hibernate and
     * directly mass insert annotation sets in the database.
     *
     * @param setName The name of the new annotation set.
     * @param recordSetId The ID of the associated record set.
     * @param conceptSchemeId The id of the associated concept scheme.
     * @return A new instance of an {@link AnnotationSetInserter}
     */
    AnnotationSetInserter createAnnotationSetInserter(String setName, Long recordSetId, Long conceptSchemeId);

    /**
     * The {@link AnnotationSetInserter} is used to bypass Hibernate and
     * directly mass insert annotation sets in the database.
     *
     * @param setName The name of the new annotation set.
     * @param recordSetId The ID of the associated record set.
     * @param conceptSchemeId The id of the associated concept scheme.
     * @param type The type of the AnnotationSetInserter, for example {@link #ANNOTATIONSET_INSERTER_TYPE_ANYLABEL}
     * @return A new instance of an {@link AnnotationSetInserter}
     */
    AnnotationSetInserter createAnnotationSetInserter(String setName, Long recordSetId, Long conceptSchemeId, int type);

    /**
     * The {@link AnnotationSetInserter} is used to bypass Hibernate and
     * directly mass insert annotation sets in the database.
     *
     * @param type The type of the AnnotationSetInserter, for example {@link #ANNOTATIONSET_INSERTER_TYPE_ANYLABEL}
     * @return A new instance of an {@link AnnotationSetInserter}
     */
    AnnotationSetInserter createAnnotationSetInserter(int type);

    /**
     * The {@link DocumentSetInserter} is used to bypass Hibernate and
     * directly mass insert record sets in the database.
     *
     * @param setName The name of the new record set.
     * @return A new instance of an {@link DocumentSetInserter}
     */
    DocumentSetInserter createDocumentSetInserter(String setName);

    /**
     * The {@link DocumentSetInserter} is used to bypass Hibernate and
     * directly mass insert record sets in the database.
     *
     * @return A new instance of an {@link DocumentSetInserter}
     */
    DocumentSetInserter createDocumentSetInserter();

    /**
     * The {@link SchemeInserter} is used to bypass Hibernate and
     * directly mass insert a concept scheme in the database.
     *
     * @param setName The name of the new concept scheme
     * @param defaultLanguage The default language of new concepts.
     * @return A new instance of an {@link SchemeInserter}
     */
    SchemeInserter createSchemeInserter(String setName, String defaultLanguage);

    /**
     * The {@link SchemeInserter} is used to bypass Hibernate and
     * directly mass insert a concept scheme in the database.
     *
     * @return A new instance of an {@link SchemeInserter}
     */
    SchemeInserter createSchemeInserter();

    /**
     * Closes all open database connections and is invoked when Semtinel
     * exits.
     */
    void close();

    /**
     * Deletes a concept scheme.
     * @param scheme The scheme to delete
     */
    void deleteConceptScheme(ConceptScheme scheme);

    /**
     * Deletes an annotation set.
     * @param annotationSet The annotation set to delete.
     */
    void deleteAnnotationSet(AnnotationSet annotationSet);

    /**
     * Deletes a record set.
     * @param recordSet The record set to delete.
     */
    void deleteRecordSet(RecordSet recordSet);

    /**
     * Deletes an annotation source.
     * @param annotationSource The annotation source to delete.
     */
    void deleteRecordSetGroup(RecordSetGroup recordSetGroup);

    /**
     * Deletes an annotation source.
     * @param annotationSource The annotation source to delete.
     */
    void deleteAnnotationSetGroup(AnnotationSetGroup annotationSetGroup);

    /**
     * Deletes an annotation source.
     * @param annotationSource The annotation source to delete.
     */
    void deleteAnnotationSource(AnnotationSource annotationSource);

    /**
     * Deletes a record source.
     * @param recordSource The record source to delete.
     */
    void deleteRecordSource(RecordSource recordSource);

    /**
     * Returns the current API version of the implementation.
     * @return the current version of the Semtinel API.
     */
    String getAPIVersion();

    /**
     * Adds a {@link ChangeListener} that is invoked when
     * the config has changed.
     * @param listener The new listener
     * @see #getConfig()
     */
    void addConfigChangeListener(ChangeListener listener);

    /**
     * The configuration is a simple {@code Map<String,String>}
     * that contains some information like tha currently used
     * JDBC URL. The primary purpose of this Map is to allow the
     * Core Manager implementation to publish some details that
     * are usually implementation specific, but nevertheless
     * has to be shown to the user.
     *
     * @return The configuration map.
     */
    Map<String, String> getConfig();

    /**
     * Removes a {@link ChangeListener} for config changes.
     * @param listener The new listener
     * @see #getConfig()
     */
    void removeConfigChangeListener(ChangeListener listener);

    /**
     * Returns a unique database URL for the given class. This should
     * be used by plugins which need to create their own H2 database.
     * The database directory is located below the global database
     * directory that is chosen by the user.
     * @param pluginClass A class of the invoking plugin to unambiguously identify the plugin.
     * @return A JDBC databse URL.
     */
    String getPluginDatabaseUrl(Class pluginClass);

    /**
     * Returns a ready-to-use JDBC connection using {@link #getPluginDatabaseUrl(java.lang.Class)} as URL.
     * @param pluginClass A class of the invoking plugin to unambiguously identify the plugin.
     * @return A JDBC {@link Connection}.
     * @throws java.sql.SQLException
     */
    Connection getPluginDatabaseConnection(Class pluginClass) throws SQLException;

    /**
     * Returns the path to a directory within the global database directory
     * chosen by the user. Within this directory, all data (including databases created by {@link #getPluginDatabaseConnection(java.lang.Class)})
     * are stored. The directory can be used to store arbitrary data that is needed
     * by the plugin.
     * @param pluginClass A class of the invoking plugin to unambiguously identify the plugin.
     * @return The canonical path to the plugin data directory.
     */
    String getPluginDataDirectory(Class pluginClass);

    /**
     * The {@link PersistentAnalysisMap} can be used by analysis plugins
     * to store precalculated values for speed reasons.
     *
     * @param pluginClass A class of the invoking plugin to unambiguously identify the plugin.
     * @return A new instance of PersistentAnalysisMap.
     */
    PersistentAnalysisMap getPersistentAnalysisMap(Class pluginClass);

    /**
     * This method allows returns the database connection used by Semtinel and
     * thus allows to execute arbitrary SQL queries directly on the database.
     * Use it with care!
     * The Hibernate session is disconnected after the call to this method, so
     * you have to ensure that Semtinel can not access the database in parallel.
     * When finished, you must release the connection back to Semtinel by {@link #releaseDirectDatabaseConnection()}.
     * @return The JDBC {@link Connection} to the Semtinel database.
     */
    Connection getDirectDatabaseConnection();

    /**
     * Reconnects a disconnected database connection with Hibernate, see
     * {@link #getDirectDatabaseConnection()}.
     */
    void releaseDirectDatabaseConnection();

    /**
     * Creates a new RecordSetGroup that may be used to group several
     * RecordSets.
     * @param name The name of the new group.
     * @return The instance of the new group.
     */
    RecordSetGroup createRecordSetGroup(String name);

    /**
     * This method returns a List of all record set groups currently available
     * in the database.
     * @return A list of all available record set groups.
     */
    AnnotationSetGroup createAnnotationSetGroup(String name);

    /**
     * This method returns a List of all record set groups currently available
     * in the database.
     * @return A list of all available record set groups.
     */
    List<RecordSetGroup> getRecordSetGroups();

    /**
     * This method returns a List of all record set groups currently available
     * in the database.
     * @return A list of all available record set groups.
     */
    List<AnnotationSetGroup> getAnnotationSetGroups();

    /**
     * This method creates a new ConceptScheme
     *
     */
    ConceptScheme createNewConceptScheme(String Uri);

    /**
     * This method creates a new Concept
     *
     */
    Concept createNewConcept(String Uri, ConceptScheme conceptScheme);

    /**
     * This method creates a new RecordSet
     *
     */
    RecordSource createNewRecordSource(String name);

    /**
     * This method creates a new Record
     *
     */
    Record createNewRecord(String externalId, RecordSource recordSource);

    /**
     * This method creates a new AnnotationSource
     *
     */
    AnnotationSource createNewAnnotationSource(String name, ConceptScheme conceptScheme);

    /**
     * This method creates a new Annotation
     *
     */
    Annotation createAnnotation(Concept concept, Record record, AnnotationSource annotationSource);

    /**
     * Returns a list of Concepts, labels of which contain the specified term. 
     * @param term The term for which matching concepts should be returned
     * @param prefLabelsOnly Specifies if only concepts, preferred Labels of which contain the term, should be returned
     * @return A list of Concepts 
     */
    List<Concept> getConcepts(String term, boolean prefLabelsOnly);

    /**
     * Sets a new location, where the data can be stored.
     * @param location
     */
    public void setDatabaseLocation(String location);

    /**
     * Returns the language with the given name, if available.
     *
     * @param name
     * @return
     */
    Language getLanguage(String name);

    /**
     * Returns languages
     * @return
     */
    List<Language> getLanguages();

    /**
     * Returns a specific concept
     * @param id
     * @return
     */
    Concept getConcept(Long id);
}
