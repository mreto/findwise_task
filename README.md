# findwise_task
This is a simple Search Engine App.
The input is the list of documents and the search term.
The output is the ordered document's list according to the TF-IDF algorithm.
The app is built using Maven. In order to run the app use following commands:
```
git clone git@github.com:mreto/findwise_task.git
cd findwise_task/
git checkout issue-1/create-simple-search-engine
mvn clean install
java -jar target/findwise_task-1.0-SNAPSHOT.jar
```

The example of the input:
```
3
the brown fox jumped over the brown dog
the lazy brown dog sat in the corner
the red fox bit the lazy dog
fox
```
