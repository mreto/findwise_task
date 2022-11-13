package com.findwise;


import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DefaultSearchEngine implements SearchEngine {
    private final Map<String, Map<String, Long>> documentIndex = new HashMap<>();
    private final Logger logger = Logger.getLogger(DefaultSearchEngine.class.getName());

    public DefaultSearchEngine() {
    }

    @Override
    public void indexDocument(String id, String content) {
        if (!documentIndex.containsKey(id)) {
            documentIndex.put(id, computeOccurrenceOfWords(content));
        } else {
            logger.warning("Document with ID was not overwritten: " + id);
        }
    }

    private Map<String, Long> computeOccurrenceOfWords(String doc) {
        return Arrays.stream(doc.split(" ")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public List<IndexEntry> search(String term) {
        Map<String, Double> relativeFrequencyMap = documentIndex.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<String, Long> occurrenceMap = entry.getValue();
                            Long rawFrequency = occurrenceMap.getOrDefault(term, 0L);
                            long sumOfAllWordsInDocuments = occurrenceMap.values().stream().reduce(0L, Long::sum);
                            if (sumOfAllWordsInDocuments != 0) {
                                return (double) rawFrequency / sumOfAllWordsInDocuments;
                            } else {
                                return 0.0;
                            }
                        }));
        long numberOfOccurrenceInAllDocuments = relativeFrequencyMap.values().stream().filter(i -> i > 0).count();
        long numberOfAllDocuments = documentIndex.size();

        List<IndexEntry> indexEntries = relativeFrequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> {
                    double score = entry.getValue() * Math.log((double) numberOfAllDocuments / numberOfOccurrenceInAllDocuments);
                    return new DefaultIndexEntry(entry.getKey(), score);
                })
                .sorted(Comparator.comparingDouble(IndexEntry::getScore).reversed())
                .collect(Collectors.toList());
        return indexEntries;
    }
}
