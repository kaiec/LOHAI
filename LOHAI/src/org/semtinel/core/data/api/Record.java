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

import java.util.List;

/**
 *
 * @author kai
 */
public interface Record {

    List<Author> getAuthors();

    String getExternalId();

    Long getId();

    String getMeta();

    String getTitle();

    RecordSource getRecordSource();

    void setAuthors(List<Author> authors);

    void setExternalId(String externalId);

    void setId(Long id);

    void setMeta(String meta);

    void setTitle(String title);
    
    void setRecordSource(RecordSource recordSource);
    
     public String getAbstractText();
     public void setAbstractText(String abstractText);
     public String getFullText();
     public void setFullText(String fullText);

      List<Annotation> getAnnotations(AnnotationSet annotationSet);

      List<Annotation> getAnnotations(AnnotationSource annotationSource);

    Language getLanguage();

    Publication getPublication();

    Publisher getPublisher();

    void setLanguage(Language language);

    void setPublication(Publication publication);

    void setPublisher(Publisher publisher);

    Integer getYear();

    void setYear(Integer year);

    String getLocation();

    Integer getNumber();

    Integer getPageFirst();

    Integer getPageLast();

    Integer getVolume();

    void setLocation(String location);

    void setNumber(Integer number);

    void setPageFirst(Integer pageFirst);

    void setPageLast(Integer pageLast);

    void setVolume(Integer volume);

    List<RecordSet> getRecordSets();

    void setRecordSets(List<RecordSet> recordSets);
     
     
}
