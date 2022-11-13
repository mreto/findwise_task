package com.findwise;


import java.util.*;

public class SearchEngineApp {
    String WELCOME = "This is a simple Search Engine App. \n" +
            "The app allows loading multiple documents as strings and the looked-up term. \nThe documents are returned in" +
            "the order using the TF-IDF algorithm.\nIn order to use the app input the number of documents you wish to upload. \n" +
            "After clicking enter, input the documents as strings in separate lines. \nLastly, input the term to search in the app.\n\n";

    String NUMBER_OF_LINES_MESSAGE = "Please enter the number of documents you would like to load and press enter.";

    public SearchEngineApp() {
    }

    public void runConsoleApp() {
        SearchEngine searchEngine = new DefaultSearchEngine();

        System.out.println(WELCOME);
        System.out.println(NUMBER_OF_LINES_MESSAGE);
        Optional<Map<String, String>> optionalDocuments = loadTheDocuments();
        if (optionalDocuments.isEmpty()) {
            System.out.println("The first line was not a valid number.");
        } else {
            optionalDocuments.get().forEach(searchEngine::indexDocument);
        }
        System.out.println("Please input the term and press enter.");
        Scanner in = new Scanner(System.in);
        String term = in.nextLine();
        List<IndexEntry> indexEntries = searchEngine.search(term);
        indexEntries.stream().forEach(indexEntry ->
                System.out.println(indexEntry.getId()));
    }

    private Optional<Map<String, String>> loadTheDocuments() {
        Map<String, String> documents = new HashMap<>();
        Scanner in = new Scanner(System.in);
        String userInputNumberOfDocuments = in.nextLine();
        int numberOfDocuments;
        try {
            numberOfDocuments = Integer.parseInt(userInputNumberOfDocuments);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
        for (int i = 0; i < numberOfDocuments; i++) {
            String userInputDocument = in.nextLine();
            documents.put("document" + Integer.toString(i + 1), userInputDocument.trim());
        }
        return Optional.of(documents);
    }

}

