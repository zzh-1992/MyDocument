# Extra Options for Java

jdk官网链接:https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html

The following java options are general purpose options that are specific to the Java HotSpot Virtual Machine.

## -Xbatch
Disables background compilation. By default, the JVM compiles the method as a background task, running the method in interpreter mode until the background compilation is finished. The -Xbatch flag disables background compilation so that compilation of all methods proceeds as a foreground task until completed. This option is equivalent to -XX:-BackgroundCompilation.
-Xbootclasspath/a:directories|zip|JAR-files
Specifies a list of directories, JAR files, and ZIP archives to append to the end of the default bootstrap class path.

Linux and macOS: Colons (:) separate entities in this list.

Windows: Semicolons (;) separate entities in this list.

## -Xcheck:jni
Performs additional checks for Java Native Interface (JNI) functions.

The following checks are considered indicative of significant problems with the native code, and the JVM terminates with an irrecoverable error in such cases:

The thread doing the call is not attached to the JVM.
The thread doing the call is using the JNIEnv belonging to another thread.
A parameter validation check fails:
A jfieldID, or jmethodID, is detected as being invalid. For example:
Of the wrong type
Associated with the wrong class
A parameter of the wrong type is detected.
An invalid parameter value is detected. For example:
NULL where not permitted
An out-of-bounds array index, or frame capacity
A non-UTF-8 string
An invalid JNI reference
An attempt to use a ReleaseXXX function on a parameter not produced by the corresponding GetXXX function
The following checks only result in warnings being printed:

A JNI call was made without checking for a pending exception from a previous JNI call, and the current call is not safe when an exception may be pending.
The number of JNI local references existing when a JNI function terminates exceeds the number guaranteed to be available. See the EnsureLocalcapacity function.
A class descriptor is in decorated format (Lname;) when it should not be.
A NULL parameter is allowed, but its use is questionable.
Calling other JNI functions in the scope of Get/ReleasePrimitiveArrayCritical or Get/ReleaseStringCritical
Expect a performance degradation when this option is used.

## -Xdebug
Does nothing. Provided for backward compatibility.
## -Xdiag
Shows additional diagnostic messages.
## -Xint
Runs the application in interpreted-only mode. Compilation to native code is disabled, and all bytecode is executed by the interpreter. The performance benefits offered by the just-in-time (JIT) compiler aren't present in this mode.
## -Xinternalversion
Displays more detailed JVM version information than the -version option, and then exits.
## -Xlog:option
Configure or enable logging with the Java Virtual Machine (JVM) unified logging framework. See Enable Logging with the JVM Unified Logging Framework.
## -Xmixed
Executes all bytecode by the interpreter except for hot methods, which are compiled to native code. On by default. Use -Xint to switch off.
## -Xmn size
Sets the initial and maximum size (in bytes) of the heap for the young generation (nursery) in the generational collectors. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. The young generation region of the heap is used for new objects. GC is performed in this region more often than in other regions. If the size for the young generation is too small, then a lot of minor garbage collections are performed. If the size is too large, then only full garbage collections are performed, which can take a long time to complete. It is recommended that you do not set the size for the young generation for the G1 collector, and keep the size for the young generation greater than 25% and less than 50% of the overall heap size for other collectors. The following examples show how to set the initial and maximum size of young generation to 256 MB using various units:

-Xmn256m
-Xmn262144k
-Xmn268435456
Instead of the -Xmn option to set both the initial and maximum size of the heap for the young generation, you can use -XX:NewSize to set the initial size and -XX:MaxNewSize to set the maximum size.

## -Xms size
Sets the minimum and the initial size (in bytes) of the heap. This value must be a multiple of 1024 and greater than 1 MB. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. The following examples show how to set the size of allocated memory to 6 MB using various units:

-Xms6291456
-Xms6144k
-Xms6m
If you do not set this option, then the initial size will be set as the sum of the sizes allocated for the old generation and the young generation. The initial size of the heap for the young generation can be set using the -Xmn option or the -XX:NewSize option.

Note that the -XX:InitalHeapSize option can also be used to set the initial heap size. If it appears after -Xms on the command line, then the initial heap size gets set to the value specified with -XX:InitalHeapSize.

## -Xmx size
Specifies the maximum size (in bytes) of the heap. This value must be a multiple of 1024 and greater than 2 MB. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. The default value is chosen at runtime based on system configuration. For server deployments, -Xms and -Xmx are often set to the same value. The following examples show how to set the maximum allowed size of allocated memory to 80 MB using various units:

-Xmx83886080
-Xmx81920k
-Xmx80m
The -Xmx option is equivalent to -XX:MaxHeapSize.

## -Xnoclassgc
Disables garbage collection (GC) of classes. This can save some GC time, which shortens interruptions during the application run. When you specify -Xnoclassgc at startup, the class objects in the application are left untouched during GC and are always be considered live. This can result in more memory being permanently occupied which, if not used carefully, throws an out-of-memory exception.
## -Xrs
Reduces the use of operating system signals by the JVM. Shutdown hooks enable the orderly shutdown of a Java application by running user cleanup code (such as closing database connections) at shutdown, even if the JVM terminates abruptly.

Linux and macOS:

The JVM catches signals to implement shutdown hooks for unexpected termination. The JVM uses SIGHUP, SIGINT, and SIGTERM to initiate the running of shutdown hooks.

Applications embedding the JVM frequently need to trap signals such as SIGINT or SIGTERM, which can lead to interference with the JVM signal handlers. The -Xrs option is available to address this issue. When -Xrs is used, the signal masks for SIGINT, SIGTERM, SIGHUP, and SIGQUIT aren't changed by the JVM, and signal handlers for these signals aren't installed.

Windows:

The JVM watches for console control events to implement shutdown hooks for unexpected termination. Specifically, the JVM registers a console control handler that begins shutdown-hook processing and returns TRUE for CTRL_C_EVENT, CTRL_CLOSE_EVENT, CTRL_LOGOFF_EVENT, and CTRL_SHUTDOWN_EVENT.

The JVM uses a similar mechanism to implement the feature of dumping thread stacks for debugging purposes. The JVM uses CTRL_BREAK_EVENT to perform thread dumps.

If the JVM is run as a service (for example, as a servlet engine for a web server), then it can receive CTRL_LOGOFF_EVENT but shouldn't initiate shutdown because the operating system doesn't actually terminate the process. To avoid possible interference such as this, the -Xrs option can be used. When the -Xrs option is used, the JVM doesn't install a console control handler, implying that it doesn't watch for or process CTRL_C_EVENT, CTRL_CLOSE_EVENT, CTRL_LOGOFF_EVENT, or CTRL_SHUTDOWN_EVENT.

There are two consequences of specifying -Xrs:

Linux and macOS: SIGQUIT thread dumps aren't available.

Windows: Ctrl + Break thread dumps aren't available.

User code is responsible for causing shutdown hooks to run, for example, by calling the System.exit() when the JVM is to be terminated.

## -Xshare:mode
Sets the class data sharing (CDS) mode.

Possible mode arguments for this option include the following:

auto
Use shared class data if possible (default).
on
Require using shared class data, otherwise fail.
Note: The -Xshare:on option is used for testing purposes only. It may cause the VM to unexpectedly exit during start-up when the CDS archive cannot be used (for example, when certain VM parameters are changed, or when a different JDK is used). This option should not be used in production environments.

off
Do not attempt to use shared class data.
## -XshowSettings
Shows all settings and then continues.
-XshowSettings:category
Shows settings and continues. Possible category arguments for this option include the following:

all
Shows all categories of settings. This is the default value.
locale
Shows settings related to locale.
properties
Shows settings related to system properties.
vm
Shows the settings of the JVM.
system
Linux: Shows host system or container configuration and continues.
## -Xss size
Sets the thread stack size (in bytes). Append the letter k or K to indicate KB, m or M to indicate MB, or g or G to indicate GB. The default value depends on the platform:

Linux/x64 (64-bit): 1024 KB

macOS (64-bit): 1024 KB

Windows: The default value depends on virtual memory

The following examples set the thread stack size to 1024 KB in different units:

-Xss1m
-Xss1024k
-Xss1048576
This option is similar to -XX:ThreadStackSize.

--add-reads module=target-module(,target-module)*
Updates module to read the target-module, regardless of the module declaration. target-module can be all unnamed to read all unnamed modules.
--add-exports module/package=target-module(,target-module)*
Updates module to export package to target-module, regardless of module declaration. The target-module can be all unnamed to export to all unnamed modules.
--add-opens module/package=target-module(,target-module)*
Updates module to open package to target-module, regardless of module declaration.
--limit-modules module[,module...]
Specifies the limit of the universe of observable modules.
--patch-module module=file(;file)*
Overrides or augments a module with classes and resources in JAR files or directories.
--source version
Sets the version of the source in source-file mode.