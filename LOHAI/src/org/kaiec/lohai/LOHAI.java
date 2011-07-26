/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.lohai;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semtinel.core.data.api.Annotation;
import org.semtinel.core.data.api.AnnotationSet;
import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.data.api.Label;
import org.semtinel.core.data.api.Language;
import org.semtinel.core.data.api.Record;
import org.semtinel.core.data.api.RecordSet;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author kai
 */
public class LOHAI {

    private ConceptScheme conceptScheme;
    private RecordSet recordSet;
    private Logger log = Logger.getLogger(getClass().getName());
    MaxentTagger tagger;
    Language lang;
    AnnotationSource target;

    public LOHAI(Language lang, AnnotationSource target, ConceptScheme conceptScheme, RecordSet recordSet) {
            this.lang = lang;
            this.target = target;
            this.conceptScheme = conceptScheme;
            this.recordSet = recordSet;
        try {
            File file = new File(getClass().getResource("left3words-wsj-0-18.tagger").toURI());
            tagger = new MaxentTagger(file.getAbsolutePath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(LOHAI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LOHAI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LOHAI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    List<TaggedWord> posTagging(String in) {

        List<TaggedWord> res = new ArrayList<TaggedWord>();
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(in));
        for (List<HasWord> sentence : sentences) {
            res.addAll(tagger.tagSentence(sentence));
        }
        return res;

    }

    String snowball(String in) {
        try {
            SnowballStemmer stemmer = new englishStemmer();
            Reader reader;
            reader = new StringReader(in);
            reader = new BufferedReader(reader);
            StringBuffer input = new StringBuffer();
            StringWriter stringWriter = new StringWriter();
            Writer output;
            output = new BufferedWriter(stringWriter);
            int repeat = 1;
            Object[] emptyArgs = new Object[0];
            int character;
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                if (Character.isWhitespace((char) ch)) {
                    if (input.length() > 0) {
                        stemmer.setCurrent(input.toString());
                        for (int i = repeat; i != 0; i--) {
                            stemmer.stem();
                        }
                        output.write(stemmer.getCurrent());
                        output.write(' ');
                        input.delete(0, input.length());
                    }
                } else {
                    input.append(Character.toLowerCase(ch));
                }
            }
            if (input.length() > 0) {
                stemmer.setCurrent(input.toString());
                for (int i = repeat; i != 0; i--) {
                    stemmer.stem();
                }
                output.write(stemmer.getCurrent());
                input.delete(0, input.length());
            }
            output.flush();
            String res = stringWriter.getBuffer().toString();
            log.finer("Snowball: " + in + " -> " + res);
            return res;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    String processStrings(String in) {
        String out = in.toLowerCase();
        out = snowball(out);
        out = out.replaceAll("[^a-z -]", "");
        log.finest("String proccesing: " + in + " -> " + out);
        return out;
    }

    public boolean checkAndLoadIndex() {
        return false;
        // Not implemented in this version.

    }

    private List<Concept> findConcepts(String in, String unstemmed, Record r) {
        log.fine("Compound extraction: " + in);
        List<Concept> res = new ArrayList<Concept>();
        String[] tokens = in.split(" ");
        String[] unstemmedTokens = unstemmed.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            for (int j = tokens.length - 1; j >= i; j--) {
                StringBuffer candidate = new StringBuffer();
                StringBuffer candidateUnstemmed = new StringBuffer();
                for (int c = i; c <= j; c++) {
                    if (c > i) {
                        candidate.append(" ");
                        candidateUnstemmed.append(" ");
                    }
                    candidate.append(tokens[c]);
                    candidateUnstemmed.append(unstemmedTokens[c]);
                }
                log.finer("Checking: " + candidate.toString());
                if (lookupMap.containsKey(candidate.toString())) {
                    List<Concept> concepts = lookup(candidate.toString());
                    if (concepts.size() > 1) {
                        log.info("Disambiguation needed: " + candidate.toString());
                        if (candidate.toString().indexOf(" ") != -1) {
                            log.info("Compound disambiguation");
                        }
                        concepts = unstem(concepts, candidate.toString(), candidateUnstemmed.toString());
                        if (concepts.size()>1) {
                            concepts = wsd(concepts, r);
                        }
                    }
                    res.addAll(concepts);
                    log.finer("Concepts found!");
                    break;
                }
            }
        }
        log.fine("Returning concepts: " + res.size());
        return res;
    }

    private List<Concept> unstem(List<Concept> in, String stemmed, String unstemmed) {
        stemmed = stemmed.toLowerCase();
        unstemmed = unstemmed.toLowerCase();
        // Mistake, unstemming can also be successful, if stemmed and unstemmed are equal!
        // Example: Asset, Assets (different concepts, both labels stemmed to asset)
        //        if (stemmed.equals(unstemmed)) {
        //            return in;
        //        }
        log.fine("Stemmed: " + stemmed + ", Unstemmed: " + unstemmed);
        List<Concept> res = new ArrayList<Concept>();
        for (Concept c : in) {
            log.fine("Unstem checking: " + c.getPrefLabel().getText());
            for (Label l : c.getLabels(lang)) {
                if (unstemmed.equals(l.getText().toLowerCase())) {
                    res.add(c);
                    log.fine("Successfully unstemmed: " + c.getPrefLabel().getText());
                }
            }
        }
        if (res.size() > 0) {
            return res;
        }
        log.fine("Unstemming not successful.");
        return in;
    }

    private void addLabels(StringBuffer sb, Concept c) {
        for (Label l : c.getLabels(lang)) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(l.getText());
        }
    }

    private String createConceptEnvironment(Concept c) {
        StringBuffer conceptEnvironment = new StringBuffer();
        addLabels(conceptEnvironment, c);
        for (Concept child : c.getNarrower()) {
            addLabels(conceptEnvironment, child);
        }
        for (Concept parent : c.getBroader()) {
            addLabels(conceptEnvironment, parent);
            for (Concept sibling : parent.getNarrower()) {
                if (!sibling.equals(c)) {
                    addLabels(conceptEnvironment, sibling);
                }
            }
        }
        return conceptEnvironment.toString();
    }

    private float jaccard(String in1, String in2) {
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        StringTokenizer st1 = new StringTokenizer(in1);
        StringTokenizer st2 = new StringTokenizer(in2);
        while (st1.hasMoreTokens()) {
            set1.add(st1.nextToken());
        }
        while (st2.hasMoreTokens()) {
            set2.add(st2.nextToken());
        }
        Set<String> intersect = new HashSet<String>();
        Set<String> union = new HashSet<String>();
        union.addAll(set1);
        union.addAll(set2);
        intersect.addAll(set1);
        Iterator<String> it = intersect.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!set2.contains(next)) it.remove();
        }
        return ((float) intersect.size())/union.size();
    }

    private List<Concept> wsd(List<Concept> in, Record r) {
        log.fine("Word Sense Disambuguation entered... Candidated: " + in.size());
        List<Concept> res = new ArrayList<Concept>();
        StringBuffer sb = new StringBuffer();
        for (TaggedWord tw : posTagging(r.getTitle() + ". " + r.getAbstractText())) {
            if (!tw.tag().equals("FW") && !tw.tag().startsWith("N") && !tw.tag().startsWith("J") && !tw.tag().isEmpty()) {
                log.finer("POS filtered: " + tw.toString() + " / " + tw.tag());
                continue;
            }
            if (sb.length()>0) sb.append(" ");
            sb.append(tw.word());
        }
        String recordEnvironment = processStrings(sb.toString());
        log.fine("Record environment: " + recordEnvironment);
        float max = 0;
        for (Concept c : in) {
            log.fine("WSD Checking: " + c.getPrefLabel().getText());
            String conceptEnvironment = processStrings(createConceptEnvironment(c));
            log.fine("Concept environment: " + conceptEnvironment);
            float jaccard = jaccard(recordEnvironment, conceptEnvironment);
            log.fine("Jaccard: " + jaccard);
            if (jaccard>max) {
                res.clear();
                res.add(c);
                log.fine("Result cleared, added " + c.getPrefLabel().getText());
                max = jaccard;
            } else if (jaccard == max) {
                res.add(c);
                log.fine("Added " + c.getPrefLabel().getText());
            }
        }
        log.info("Concepts after WSD: " + res.size());
        return res;
    }

    public void index() {

        AnnotationSource as = target;
        as.setName("LOHAI " + new SimpleDateFormat().format(new Date()));
        as.setConceptScheme(conceptScheme);
        StringBuffer desc = new StringBuffer();
        desc.append("RS: " + recordSet.getName()+"\n");
        desc.append("CS: " + conceptScheme.getUri()+"\n");
        desc.append("Time: " + new SimpleDateFormat().format(new Date())+"\n");
        as.setDescription(desc.toString());
        log.info("Indexing started");
        int i = 0;
        List<Record> records = recordSet.getRecords();
        log.info("Number of records: " + records.size());
        Map<Record, Map<Concept, Integer>> allTFs = new HashMap<Record, Map<Concept, Integer>>(10000);
        Map<Concept, Integer> dfMap = new HashMap<Concept, Integer>(10000);
        int maxfreq = 0;
        for (Record r : records) {
            Map<Concept, Integer> tfmap = new HashMap<Concept, Integer>();
            allTFs.put(r, tfmap);
            log.info("Record: " + r.getTitle());
            i++;
//            StringTokenizer st = new StringTokenizer(r.getTitle() + " " + r.getAbstractText());
//            while (st.hasMoreTokens()) {
//                String word = processStrings(st.nextToken());
            StringBuffer compoundCheck = new StringBuffer();
            StringBuffer compoundCheckUnStemmed = new StringBuffer();
            for (TaggedWord tw : posTagging(r.getTitle() + ". " + r.getAbstractText())) {
                if (!tw.tag().equals("FW") && !tw.tag().startsWith("N") && !tw.tag().startsWith("J") && !tw.tag().isEmpty()) {
                    log.finer("POS filtered: " + tw.toString() + " / " + tw.tag());
                    continue;
                }
                String word = processStrings(tw.word());
                // Compound detection active
                if (partsMap.containsKey(word)) {
                    log.info("Possible part of a compound term: " + word);
                    if (compoundCheck.length() > 0) {
                        compoundCheck.append(" ");
                        compoundCheckUnStemmed.append(" ");
                    }
                    compoundCheck.append(word);
                    compoundCheckUnStemmed.append(tw.word());
                    // Check, if we have a compound candidate
                } else if (compoundCheck.length() > 0) {
                    log.info("Compound candidate: " + compoundCheck.toString());
                    for (Concept c : findConcepts(compoundCheck.toString(), compoundCheckUnStemmed.toString(), r)) {
                        if (!tfmap.containsKey(c)) {
                            Annotation a = new MyAnnotation();
                            a.setConcept(c);
                            a.setRecord(r);
                            a.setAnnotationSource(as);
                            log.info("Annotation: " + word);
                            tfmap.put(c, 1);
                            if (maxfreq < 1) {
                                maxfreq = 1;
                            }
                            if (!dfMap.containsKey(c)) {
                                dfMap.put(c, 1);
                            } else {
                                dfMap.put(c, dfMap.get(c) + 1);
                            }
                        } else {
                            int freq = tfmap.get(c) + 1;
                            if (maxfreq < freq) {
                                maxfreq = freq;
                            }
                            tfmap.put(c, freq);
                        }
                    }
                    compoundCheck.setLength(0);
                    compoundCheckUnStemmed.setLength(0);
                    // Reset compound detection
                    List<Concept> concepts = lookup(word);
                    if (concepts.size() > 1) {
                        log.info("Disambiguation needed: " + word);
                        concepts = unstem(concepts, word, tw.word());
                        if (concepts.size()>1) {
                            concepts = wsd(concepts, r);
                        }
                    }
                    for (Concept c : concepts) {
                        if (!tfmap.containsKey(c)) {
                            Annotation a = new MyAnnotation();
                            a.setConcept(c);
                            a.setRecord(r);
                            a.setAnnotationSource(as);
                            log.info("Annotation: " + word);
                            tfmap.put(c, 1);
                            if (maxfreq < 1) {
                                maxfreq = 1;
                            }
                            if (!dfMap.containsKey(c)) {
                                dfMap.put(c, 1);
                            } else {
                                dfMap.put(c, dfMap.get(c) + 1);
                            }
                        } else {
                            int freq = tfmap.get(c) + 1;
                            if (maxfreq < freq) {
                                maxfreq = freq;
                            }
                            tfmap.put(c, freq);
                        }
                    }
                } else {
                    compoundCheck.setLength(0);
                    compoundCheckUnStemmed.setLength(0);
// Check for single term labels.
                    // TODO: single term should be avoided, if a compound term is found...
                    List<Concept> concepts = lookup(word);
                    if (concepts.size() > 1) {
                        log.info("Disambiguation needed: " + word);
                        concepts = unstem(concepts, word, tw.word());
                        if (concepts.size()>1) {
                            concepts = wsd(concepts, r);
                        }
                    }
                    for (Concept c : concepts) {
                        if (!tfmap.containsKey(c)) {
                            Annotation a = new MyAnnotation();
                            a.setConcept(c);
                            a.setRecord(r);
                            a.setAnnotationSource(as);
                            log.info("Annotation: " + word);
                            tfmap.put(c, 1);
                            if (maxfreq < 1) {
                                maxfreq = 1;
                            }
                            if (!dfMap.containsKey(c)) {
                                dfMap.put(c, 1);
                            } else {
                                dfMap.put(c, dfMap.get(c) + 1);
                            }
                        } else {
                            int freq = tfmap.get(c) + 1;
                            if (maxfreq < freq) {
                                maxfreq = freq;
                            }
                            tfmap.put(c, freq);
                        }
                    }
                }



            }
        }

        log.info("Calculating ranks...");
        int D = records.size();
        log.fine("D = " + D + ", maxfreq = " + maxfreq);
        i = 0;
//        for (Record r : records) {
//            i++;
//            log.fine("Record r: " + r.getTitle());
//            for (Iterator<Annotation> it = r.getAnnotations(as).iterator(); it.hasNext();) {
//                log.info("1 " + new Date());
//                Annotation a = it.next();
//                log.info("2 "+ new Date());
//                int df = dfMap.get(a.getConcept());
//                log.info("3 "+ new Date());
//                int tf = allTFs.get(r).get(a.getConcept());
//                log.info("4 "+ new Date());
//                float tfidf = (float) ((((float) tf) / maxfreq) * Math.log(((float) D) / df));
//                log.info("5 "+ new Date());
//                String mes = i + ": Concept " + a.getConcept().getPrefLabel().getText() + ": TF " + tf + ", DF " + df + ", TFIDF " + tfidf;
//                log.info("6 "+ new Date());
//                log.finer(mes);
//                log.info("7 "+ new Date());
//                listener.progress(mes, i);
//                log.info("8 "+ new Date());
//                a.setRank(tfidf);
//            }
//        }
            for (Iterator<Annotation> it = as.getAnnotations().iterator(); it.hasNext();) {
                Annotation a = it.next();
                int df = dfMap.get(a.getConcept());
                int tf = allTFs.get(a.getRecord()).get(a.getConcept());
                float tfidf = (float) ((((float) tf) / maxfreq) * Math.log(((float) D) / df));
                String mes = i + ": Concept " + a.getConcept().getPrefLabel().getText() + ": TF " + tf + ", DF " + df + ", TFIDF " + tfidf;
                log.finer(mes);
                
                a.setRank(tfidf);
            }

       

    }
    private Map<String, List<Long>> lookupMap = new TreeMap<String, List<Long>>();
    private Map<String, List<String>> partsMap = new TreeMap<String, List<String>>();
    private Map<Long, Concept> conceptMap = new TreeMap<Long, Concept>();

    private List<Concept> lookup(String word) {
        List<Long> ids = lookupMap.get(word);
        List<Concept> res = new ArrayList<Concept>();
        if (ids == null) {
            return res;
        }
        for (Long id : ids) {
            res.add(conceptMap.get(id));
        }
        return res;
    }

    public void prepareConceptScheme() {
        log.info("Index preparation started");
        lookupMap.clear();
        partsMap.clear();
        
            int i = 0;
            


            for (Concept c : conceptScheme.getConcepts()) {
                conceptMap.put(c.getId(), c);
                i++;
                String ltext = c.getUri();
                if (c.getPrefLabel(lang) != null) {
                    ltext = c.getPrefLabel(lang).getText();
                }
                
                log.fine("Concept " + i + ": " + ltext);
                for (Label l : c.getLabels(lang)) {
                    String word = processStrings(l.getText());
                    if (!lookupMap.containsKey(word)) {
                        lookupMap.put(word, new ArrayList<Long>());
                    }
                    if (!lookupMap.get(word).contains(c.getId())) {
                        lookupMap.get(word).add(c.getId());
                    }
                    if (word.indexOf(" ") != -1) {
                        StringTokenizer st = new StringTokenizer(word);
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken();
                            if (!partsMap.containsKey(token)) {
                                partsMap.put(token, new ArrayList<String>());
                            }
                            if (!partsMap.get(token).contains(word)) {
                                partsMap.get(token).add(word);
                            }
                        }
                    }
                }
            }
            
           
           
        

    }

    public ConceptScheme getConceptScheme() {
        return conceptScheme;
    }

    public void setConceptScheme(ConceptScheme conceptScheme) {
        this.conceptScheme = conceptScheme;
        checkAndLoadIndex();
    }

    public RecordSet getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(RecordSet recordSet) {
        this.recordSet = recordSet;
    }

    class MyAnnotation implements Annotation {

        private Concept concept;
        private Long id;
        private float rank;
        private AnnotationSource annotationSource;
        private Record record;
        private List<AnnotationSet> sets;

        public AnnotationSource getAnnotationSource() {
            return annotationSource;
        }

        public void setAnnotationSource(AnnotationSource annotationSource) {
            this.annotationSource = annotationSource;
        }

        public Concept getConcept() {
            return concept;
        }

        public void setConcept(Concept concept) {
            this.concept = concept;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public float getRank() {
            return rank;
        }

        public void setRank(float rank) {
            this.rank = rank;
        }

        public Record getRecord() {
            return record;
        }

        public void setRecord(Record record) {
            this.record = record;
        }

        public List<AnnotationSet> getAnnotationSets() {
            return sets;
        }

        public void setAnnotationSets(List<AnnotationSet> annotationSets) {
            sets = annotationSets;
        }




    }
}
