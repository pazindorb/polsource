# Polsource project

### What is necessary to run server
- Java 11
- Maven

### How to setup database
- Database will be populated with data on start of the server
- Uses h2 in memory database, lives only during program running

### How to run server
1. call ```mvn clean install``` in main project directory (pom.xml file inside)
2. Run using command ```java -jar target/polsource-0.0.1-SNAPSHOT.jar``` or using IDE
3. server by default starts on port 8080

## Example usages for Postman
(postman collection is available to import from `PolSource.postman_collection.json` file)
#### _Functionality:_ Create Note
#### _Request:_ POST on `localhost:8080/api/notes`
#### _Body:_
```javascript
{
    "title":"some title",
    "content":"epic content"
}
```
#### _Functionality:_  Edit Note
#### _Request:_ PATCH on `localhost:8080/api/notes/{id}`
#### _Body:_
```javascript
{
    "title":"edited title",
    "content":"edited content"
}
```
#### _Functionality:_ Delete Note
#### _Request:_ GET on `localhost:8080/api/notes/{id}`

#### _Functionality:_ Get Note
#### _Request:_ GET on `localhost:8080/api/notes/{id}`

#### _Functionality:_ Get Note History
#### _Request:_ GET on `localhost:8080/api/notes/{id}/history`

#### _Functionality:_ Get All Notes
#### _Request:_ GET on `localhost:8080/api/notes/`

## Tests
* Spring Test Framework, class ```NoteControllerIT.java```