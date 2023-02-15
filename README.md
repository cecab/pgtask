## pgtask - A Sample RESTful Webservice 
`pgtask` is a basic webservice built with [Micronaut](https://docs.micronaut.io/3.8.4/guide/index.html) to provide
sample data from StackExchange API. 


### Compiling and Running
To compile and start the service, use:
```shell
./gradlew run 
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
  Micronaut (v3.8.4)

16:38:15.523 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
16:38:15.640 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
16:38:15.643 [main] INFO  i.m.l.AbstractLiquibaseMigration - Running migrations for database with qualifier [default]
Feb 10, 2023 4:38:15 PM liquibase.database
INFO: Set default schema name to public
Feb 10, 2023 4:38:15 PM liquibase.changelog
INFO: Reading from public.databasechangelog
16:38:16.285 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1039ms. Server Running: http://localhost:8080
16:38:17.100 [main] INFO  ccb.pgames.services.QuestionService - Cleared questions from DB: 20
16:38:17.185 [main] INFO  ccb.pgames.services.QuestionService - Fetched and persisted questions from DB: 20
```

On every start it will fetch 20 questions from StackExchange and persist them in the database.

## Samples of REST Api usage
For the `/questions` endpoint:
- To get all the questions in the DB
```shell
% curl -q 'http://localhost:8080/questions'
[
  {
    "id": 741,
    "title": "How to perform multiple array filtering",
    "tags": [
      "javascript",
      "angular",
      "typescript"
    ],
    "is_answered": false,
    "view_count": 72,
    "answer_count": 1,
    "creation_date": "2023-02-08T14:33:04",
    "user_id": 11346489
  },
 ...
 ...
 ]
```
- To get only one question by its ID:
```shell
 % curl -q 'http://localhost:8080/questions/742'
{
  "id": 742,
  "title": "WKWebView: how to display a big HTML5 canvas?",
  "tags": [
    "canvas",
    "wkwebview"
  ],
  "is_answered": false,
  "view_count": 11,
  "answer_count": 0,
  "creation_date": "2023-02-08T14:31:51",
  "user_id": 359570
}
```
- To delete a question by id, the response is 204 for a successful deletion and 202 in case ID was not found.
```shell
 % curl -v -X DELETE 'http://localhost:8080/questions/779' 
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> DELETE /questions/779 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.79.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 204 No Content
< date: Fri, 10 Feb 2023 15:55:44 GMT
< connection: keep-alive
< 
* Connection #0 to host localhost left intact
```
- To search for questions that have specific tags, use the query string variable `tags` with all the tag values joined 
by comma:
```shell
% curl  'http://localhost:8080/questions?tags=angular,python'
[
  {
    "id": 761,
    "title": "How to perform multiple array filtering",
    "tags": [
      "vue.js",
      "angular",
      "typescript"
    ],
    "is_answered": false,
    "view_count": 72,
    "answer_count": 1,
    "creation_date": "2023-02-08T14:33:04",
    "user_id": 11346489
  },
  {
    "id": 766,
    "title": "Module Warning Angular Material themes should be created from a map containing the keys color, typography, density",
    "tags": [
      "angular",
      "sass",
      "angular-material",
      "angular15"
    ],
    "is_answered": false,
    "view_count": 115,
    "answer_count": 1,
    "creation_date": "2023-02-08T12:11:14",
    "user_id": 7620780
  },
  {
    "id": 770,
    "title": "Serve TailwindCSS with django_plotly_dash",
    "tags": [
      "python",
      "css",
      "django",
      "tailwind-css",
      "plotly-dash"
    ],
    "is_answered": false,
    "view_count": 50,
    "answer_count": 2,
    "creation_date": "2023-02-08T03:50:52",
    "user_id": 4183877
  }
]
```

## Packaging and deployment
To build a JAR file for deployment:
```shell
 % ./gradlew shadowJar
 
BUILD SUCCESSFUL in 3s
3 actionable tasks: 1 executed, 2 up-to-date
 ```

### Docker 
The application comes with a docker compose file to make easier to try it, you will need `docker` in 
your environment to run the following commands (symbol `%` is the shell prompt), and check the JAR file
was already generated (see 'packaging' section):
```shell
% docker compose up pgtask
[+] Running 0/1
 ⠿ pgtask Warning                                                                                                                                                                                                1.8s
[+] Building 1.6s (7/7) FINISHED                                                                                                                                                                                      
 => [internal] load build definition from Dockerfile                                                                                                                                                             0.0s
 => => transferring dockerfile: 177B                                                                                                                                                                             0.0s
 => [internal] load .dockerignore                                                                                                                                                                                0.0s
 => => transferring context: 2B                                                                                                                                                                                  0.0s
 => [internal] load metadata for docker.io/library/eclipse-temurin:11.0.12_7-jdk                                                                                                                                 1.2s
 => CACHED [1/2] FROM docker.io/library/eclipse-temurin:11.0.12_7-jdk@sha256:08c608c75bd1874015929a2813680793fe55c8a611e70387ca1466ecd59c656f                                                                    0.0s
 => [internal] load build context                                                                                                                                                                                0.2s
 => => transferring context: 23.01MB                                                                                                                                                                             0.2s
 => [2/2] COPY ./build/libs/pgtask-0.1-all.jar /app/                                                                                                                                                             0.1s
 => exporting to image                                                                                                                                                                                           0.0s
 => => exporting layers                                                                                                                                                                                          0.0s
 => => writing image sha256:6970cce0d5415ba229acaf0b56ada021d83c31765f34fc1a8dba7f629db01765                                                                                                                     0.0s
 => => naming to docker.io/library/pgtask:latest                                                                                                                                                                 0.0s

[+] Running 3/3
 ⠿ Network pgtask_default     Created                                                                                                                                                                            0.0s
 ⠿ Container pgtask-db-1      Created                                                                                                                                                                            0.0s
 ⠿ Container pgtask-pgtask-1  Created                                                                                                                                                                            0.0s
Attaching to pgtask-pgtask-1
pgtask-pgtask-1  |  __  __ _                                  _   
pgtask-pgtask-1  | |  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
pgtask-pgtask-1  | | |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
pgtask-pgtask-1  | | |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
pgtask-pgtask-1  | |_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
pgtask-pgtask-1  |   Micronaut (v3.8.4)
pgtask-pgtask-1  | 
pgtask-pgtask-1  | 16:27:37.812 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
pgtask-pgtask-1  | 16:27:37.959 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
pgtask-pgtask-1  | 16:27:37.963 [main] INFO  i.m.l.AbstractLiquibaseMigration - Running migrations for database with qualifier [default]
pgtask-pgtask-1  | Feb 10, 2023 4:27:38 PM liquibase.database
pgtask-pgtask-1  | INFO: Set default schema name to public
pgtask-pgtask-1  | Feb 10, 2023 4:27:38 PM liquibase.changelog
pgtask-pgtask-1  | INFO: Reading from public.databasechangelog
pgtask-pgtask-1  | 16:27:38.896 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1625ms. Server Running: http://926d82768160:8080
pgtask-pgtask-1  | 16:27:39.790 [main] INFO  ccb.pgames.services.QuestionService - Cleared questions from DB: 20
pgtask-pgtask-1  | 16:27:39.833 [main] INFO  ccb.pgames.services.QuestionService - Fetched and persisted questions from DB: 20
                
```
Verify that the container is up and running by:
```shell
% pgtask % docker ps 
CONTAINER ID   IMAGE                  COMMAND                  CREATED        STATUS          PORTS                                               NAMES
6d3f8bc5dd41   postgres:14.1-alpine   "docker-entrypoint.s…"   30 hours ago   Up 31 seconds   0.0.0.0:18543->5432/tcp                             pgtask-db-1

```

The JAR file is created in `./build/libs/pgtask-0.1-all.jar`

## About Micronaut and the libraries used 

- [API Reference](https://docs.micronaut.io/3.8.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.8.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)
- [https://www.liquibase.org/](https://www.liquibase.org/)
- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
