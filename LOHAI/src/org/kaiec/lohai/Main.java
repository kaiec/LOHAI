/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.lohai;

import java.util.List;
import org.semtinel.core.data.api.Annotation;
import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.data.api.Language;
import org.semtinel.core.data.api.Record;
import org.semtinel.core.data.api.RecordSet;
import org.semtinel.core.data.api.RecordSetGroup;

/**
 *
 * @author kai
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("IMPORTANT: This implementation is not actually working. It should only demonstrate how to get started. Please use your own implementations of the interfaces to give LOHAI access to your data.");
        Language lang = new MyLanguage();
        ConceptScheme cs = new MyConceptScheme();
        RecordSet rs = new MyRecordSet();
        AnnotationSource target = new MyAnnotationSource();
        LOHAI indexer = new LOHAI(lang, target, cs, rs);

        indexer.prepareConceptScheme();
        indexer.index();

        for (Annotation a:target.getAnnotations()) {
            System.out.println("Annotation: " + a.getRecord() + " -> " + a.getConcept().getPrefLabel(lang));
        }
    }

    static class MyLanguage implements Language {

        public Long getId() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    static class MyAnnotationSource implements AnnotationSource {

        public ConceptScheme getConceptScheme() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Long getId() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setConceptScheme(ConceptScheme conceptScheme) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<Annotation> getAnnotations() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setAnnotations(List<Annotation> annotations) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int getSize() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setDescription(String description) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    static class MyRecordSet implements RecordSet {

        public Long getId() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<Record> getRecords() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int getSize() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<RecordSetGroup> getRecordSetGroups() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setRecordSetGroups(List<RecordSetGroup> recordSetGroups) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeFromGroup(RecordSetGroup rsg) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    static class MyConceptScheme implements ConceptScheme {

        public Long getId() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Concept getTopConcept() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getUri() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setTopConcept(Concept topConcept) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setUri(String uri) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<Concept> getConcepts() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }


}
