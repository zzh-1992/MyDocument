## 1、Asynchronous REST endpoint

```java

@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @GetMapping("/async")
    public CompletableFuture<String> asyncEndpoint() {
        return myService.asyncMethod();
    }
}

@Service
public class MyService {

    @Async
    public CompletableFuture<String> asyncMethod() {
        // Perform a long-running task here
        return CompletableFuture.completedFuture("result");
    }
}
```

In this example, we create an asynchronous REST endpoint in a Spring Boot controller. The endpoint calls a method in a
Spring service, which is annotated with `@Async` to run the method asynchronously on a separate thread. The method
returns a `CompletableFuture` that resolves with a result when the long-running task is complete.

## 2、Parallel execution of multiple methods

```java

@Service
public class MyService {

    @Async
    public CompletableFuture<String> method1() {
        // Perform a long-running task here
        return CompletableFuture.completedFuture("result1");
    }

    @Async
    public CompletableFuture<String> method2() {
        // Perform a long-running task here
        return CompletableFuture.completedFuture("result2");
    }

    @Async
    public CompletableFuture<String> method3() {
        // Perform a long-running task here
        return CompletableFuture.completedFuture("result3");
    }

    public CompletableFuture<List<String>> executeAll() {
        List<CompletableFuture<String>> futures = List.of(method1(), method2(), method3());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                                .thenApply(v -> futures.stream()
                                                       .map(CompletableFuture::join)
                                                       .collect(Collectors.toList()));
    }
}

```

In this example, we create three methods in a Spring service that perform long-running tasks asynchronously and return
`CompletableFuture` objects. We then define a new method, `executeAll`, that calls all three methods in parallel using
the `allOf` method. Finally, we use the `join` method to retrieve the results of each future and return them as a list.

## 3、Asynchronous database query

```java

@Repository
public class MyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CompletableFuture<Integer> countRows() {
        return CompletableFuture.supplyAsync(() -> jdbcTemplate.queryForObject("SELECT COUNT(*) FROM my_table", Integer.class));
    }
}

```

In this example, we create a Spring repository that queries a database using `JdbcTemplate`. We wrap the query in a
`supplyAsync` method call to run it asynchronously on a separate thread and return a `CompletableFuture` that resolves
with the result when the query is complete.

These are just a few examples of how to use `CompletableFuture` in a Spring Boot application. The class provides many
other methods and features that can be used in conjunction with Spring, such as composing multiple asynchronous
operations, handling exceptions, and customizing thread pools.
