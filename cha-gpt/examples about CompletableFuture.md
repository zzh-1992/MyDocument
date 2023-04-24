## 1、Combining Multiple Futures:

```java
CompletableFuture<Integer> future1=CompletableFuture.supplyAsync(()->5);
    CompletableFuture<Integer> future2=CompletableFuture.supplyAsync(()->10);

    CompletableFuture<Integer> combinedFuture=future1.thenCombine(future2,(result1,result2)->result1+result2);

    System.out.println("Combined result: "+combinedFuture.get()); // Output: 15
```

In this example, we create two `CompletableFuture` objects that return integers asynchronously. We then use the
`thenCombine` method to combine the two futures and add their results together.

## 2、Handling Exceptions:

```java
CompletableFuture<Integer> future=CompletableFuture.supplyAsync(()->{
    // Perform a task that might throw an exception
    throw new RuntimeException("Something went wrong");
    });

    CompletableFuture<Integer> exceptionHandledFuture=future.exceptionally(ex->{
    System.out.println("Exception occurred: "+ex.getMessage());
    return-1;
    });

    System.out.println("Result: "+exceptionHandledFuture.get()); // Output: -1
```

In this example, we create a `CompletableFuture` that throws an exception when its task is executed. We then use the
`exceptionally` method to handle the exception and return a default value of `-1`.

## 3、Composing Multiple Operations:

```java
CompletableFuture<Integer> future1=CompletableFuture.supplyAsync(()->5);
    CompletableFuture<Integer> future2=CompletableFuture.supplyAsync(()->10);

    CompletableFuture<Integer> composedFuture=future1
    .thenCompose(result1->CompletableFuture.supplyAsync(()->result1*2))
    .thenCombine(future2,(result1,result2)->result1+result2);

    System.out.println("Composed result: "+composedFuture.get()); // Output: 20
```

In this example, we create two `CompletableFuture` objects that return integers asynchronously. We then use the
`thenCompose` method to chain a new asynchronous operation that multiplies the result of the first future by 2. Finally,
we use the thenCombine method to combine the results of both futures and add them together.

These are just a few examples of how to use `CompletableFuture` in Java. The class provides many other methods and
features, including `allOf` and `anyOf` for combining multiple futures, and the ability to create custom executors to run
tasks on specific threads.
