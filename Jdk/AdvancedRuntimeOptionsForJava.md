# Advanced Runtime Options for Java

jdk官网链接:https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html

These java options control the runtime behavior of the Java HotSpot VM.

## -XX:ActiveProcessorCount=x

Overrides the number of CPUs that the VM will use to calculate the size of thread pools it will use for various operations such as Garbage Collection and ForkJoinPool.

The VM normally determines the number of available processors from the operating system. This flag can be useful for partitioning CPU resources when running multiple Java processes in docker containers. This flag is honored even if UseContainerSupport is not enabled. See -XX:-UseContainerSupport for a description of enabling and disabling container support.

## -XX:AllocateHeapAt=path

Takes a path to the file system and uses memory mapping to allocate the object heap on the memory device. Using this option enables the HotSpot VM to allocate the Java object heap on an alternative memory device, such as an NV-DIMM, specified by the user.

Alternative memory devices that have the same semantics as DRAM, including the semantics of atomic operations, can be used instead of DRAM for the object heap without changing the existing application code. All other memory structures (such as the code heap, metaspace, and thread stacks) continue to reside in DRAM.

Some operating systems expose non-DRAM memory through the file system. Memory-mapped files in these file systems bypass the page cache and provide a direct mapping of virtual memory to the physical memory on the device. The existing heap related flags (such as -Xmx and -Xms) and garbage-collection related flags continue to work as before.

## -XX:-CompactStrings

Disables the Compact Strings feature. By default, this option is enabled. When this option is enabled, Java Strings containing only single-byte characters are internally represented and stored as single-byte-per-character Strings using ISO-8859-1 / Latin-1 encoding. This reduces, by 50%, the amount of space required for Strings containing only single-byte characters. For Java Strings containing at least one multibyte character: these are represented and stored as 2 bytes per character using UTF-16 encoding. Disabling the Compact Strings feature forces the use of UTF-16 encoding as the internal representation for all Java Strings.

Cases where it may be beneficial to disable Compact Strings include the following:

When it's known that an application overwhelmingly will be allocating multibyte character Strings

In the unexpected event where a performance regression is observed in migrating from Java SE 8 to Java SE 9 and an analysis shows that Compact Strings introduces the regression

In both of these scenarios, disabling Compact Strings makes sense.

## -XX:ErrorFile=filename

Specifies the path and file name to which error data is written when an irrecoverable error occurs. By default, this file is created in the current working directory and named hs_err_pidpid.log where pid is the identifier of the process that encountered the error.

The following example shows how to set the default log file (note that the identifier of the process is specified as %p):

## -XX:ErrorFile=./hs_err_pid%p.log

Linux and macOS: The following example shows how to set the error log to /var/log/java/java_error.log:

## -XX:ErrorFile=/var/log/java/java_error.log

Windows: The following example shows how to set the error log file to C:/log/java/java_error.log:

## -XX:ErrorFile=C:/log/java/java_error.log

If the file exists, and is writeable, then it will be overwritten. Otherwise, if the file can't be created in the specified directory (due to insufficient space, permission problem, or another issue), then the file is created in the temporary directory for the operating system:

Linux and macOS: The temporary directory is /tmp.

Windows: The temporary directory is specified by the value of the TMP environment variable; if that environment variable isn't defined, then the value of the TEMP environment variable is used.

## -XX:+ExtensiveErrorReports

Enables the reporting of more extensive error information in the ErrorFile. This option can be turned on in environments where maximal information is desired - even if the resulting logs may be quite large and/or contain information that might be considered sensitive. The information can vary from release to release, and across different platforms. By default this option is disabled.
## -XX:FlightRecorderOptions=parameter=value (or)
## -XX:FlightRecorderOptions:parameter=value
Sets the parameters that control the behavior of JFR.

The following list contains the available JFR parameter=value entries:

globalbuffersize=size
Specifies the total amount of primary memory used for data retention. The default value is based on the value specified for memorysize. Change the memorysize parameter to alter the size of global buffers.
maxchunksize=size
Specifies the maximum size (in bytes) of the data chunks in a recording. Append m or M to specify the size in megabytes (MB), or g or G to specify the size in gigabytes (GB). By default, the maximum size of data chunks is set to 12 MB. The minimum allowed is 1 MB.
memorysize=size
Determines how much buffer memory should be used, and sets the globalbuffersize and numglobalbuffers parameters based on the size specified. Append m or M to specify the size in megabytes (MB), or g or G to specify the size in gigabytes (GB). By default, the memory size is set to 10 MB.
numglobalbuffers
Specifies the number of global buffers used. The default value is based on the memory size specified. Change the memorysize parameter to alter the number of global buffers.
old-object-queue-size=number-of-objects
Maximum number of old objects to track. By default, the number of objects is set to 256.
repository=path
Specifies the repository (a directory) for temporary disk storage. By default, the system's temporary directory is used.
retransform={true|false}
Specifies whether event classes should be retransformed using JVMTI. If false, instrumentation is added when event classes are loaded. By default, this parameter is enabled.
samplethreads={true|false}
Specifies whether thread sampling is enabled. Thread sampling occurs only if the sampling event is enabled along with this parameter. By default, this parameter is enabled.
stackdepth=depth
Stack depth for stack traces. By default, the depth is set to 64 method calls. The maximum is 2048. Values greater than 64 could create significant overhead and reduce performance.
threadbuffersize=size
Specifies the per-thread local buffer size (in bytes). By default, the local buffer size is set to 8 kilobytes, with a minimum value of 4 kilobytes. Overriding this parameter could reduce performance and is not recommended.
You can specify values for multiple parameters by separating them with a comma.

## -XX:LargePageSizeInBytes=size

Sets the maximum large page size (in bytes) used by the JVM. The size argument must be a valid page size supported by the environment to have any effect. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. By default, the size is set to 0, meaning that the JVM will use the default large page size for the environment as the maximum size for large pages. See Large Pages.

The following example describes how to set the large page size to 1 gigabyte (GB):

## -XX:LargePageSizeInBytes=1g


## -XX:MaxDirectMemorySize=size

Sets the maximum total size (in bytes) of the java.nio package, direct-buffer allocations. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. By default, the size is set to 0, meaning that the JVM chooses the size for NIO direct-buffer allocations automatically.

The following examples illustrate how to set the NIO size to 1024 KB in different units:

-XX:MaxDirectMemorySize=1m
-XX:MaxDirectMemorySize=1024k
-XX:MaxDirectMemorySize=1048576
-XX:-MaxFDLimit
Disables the attempt to set the soft limit for the number of open file descriptors to the hard limit. By default, this option is enabled on all platforms, but is ignored on Windows. The only time that you may need to disable this is on Mac OS, where its use imposes a maximum of 10240, which is lower than the actual system maximum.
## -XX:NativeMemoryTracking=mode
Specifies the mode for tracking JVM native memory usage. Possible mode arguments for this option include the following:

off
Instructs not to track JVM native memory usage. This is the default behavior if you don't specify the -XX:NativeMemoryTracking option.
summary
Tracks memory usage only by JVM subsystems, such as Java heap, class, code, and thread.
detail
In addition to tracking memory usage by JVM subsystems, track memory usage by individual CallSite, individual virtual memory region and its committed regions.
## -XX:ObjectAlignmentInBytes=alignment
Sets the memory alignment of Java objects (in bytes). By default, the value is set to 8 bytes. The specified value should be a power of 2, and must be within the range of 8 and 256 (inclusive). This option makes it possible to use compressed pointers with large Java heap sizes.

The heap size limit in bytes is calculated as:

4GB * ObjectAlignmentInBytes

Note: As the alignment value increases, the unused space between objects also increases. As a result, you may not realize any benefits from using compressed pointers with large Java heap sizes.

## -XX:OnError=string

Sets a custom command or a series of semicolon-separated commands to run when an irrecoverable error occurs. If the string contains spaces, then it must be enclosed in quotation marks.

Linux and macOS: The following example shows how the -XX:OnError option can be used to run the gcore command to create a core image, and start the gdb debugger to attach to the process in case of an irrecoverable error (the %p designates the current process identifier):

## -XX:OnError="gcore %p;gdb -p %p"

Windows: The following example shows how the -XX:OnError option can be used to run the userdump.exe utility to obtain a crash dump in case of an irrecoverable error (the %p designates the current process identifier). This example assumes that the path to the userdump.exe utility is specified in the PATH environment variable:

## -XX:OnError="userdump.exe %p"

## -XX:OnOutOfMemoryError=string

Sets a custom command or a series of semicolon-separated commands to run when an OutOfMemoryError exception is first thrown. If the string contains spaces, then it must be enclosed in quotation marks. For an example of a command string, see the description of the -XX:OnError option.
## -XX:+PrintCommandLineFlags

Enables printing of ergonomically selected JVM flags that appeared on the command line. It can be useful to know the ergonomic values set by the JVM, such as the heap space size and the selected garbage collector. By default, this option is disabled and flags aren't printed.
-XX:+PreserveFramePointer
Selects between using the RBP register as a general purpose register (-XX:-PreserveFramePointer) and using the RBP register to hold the frame pointer of the currently executing method (-XX:+PreserveFramePointer . If the frame pointer is available, then external profiling tools (for example, Linux perf) can construct more accurate stack traces.
-XX:+PrintNMTStatistics
Enables printing of collected native memory tracking data at JVM exit when native memory tracking is enabled (see -XX:NativeMemoryTracking). By default, this option is disabled and native memory tracking data isn't printed.
## -XX:SharedArchiveFile=path
Specifies the path and name of the class data sharing (CDS) archive file

See Application Class Data Sharing.

## -XX:SharedArchiveConfigFile=shared_config_file
Specifies additional shared data added to the archive file.
## -XX:SharedClassListFile=file_name
Specifies the text file that contains the names of the classes to store in the class data sharing (CDS) archive. This file contains the full name of one class per line, except slashes (/) replace dots (.). For example, to specify the classes java.lang.Object and hello.Main, create a text file that contains the following two lines:

java/lang/Object
hello/Main
The classes that you specify in this text file should include the classes that are commonly used by the application. They may include any classes from the application, extension, or bootstrap class paths.

See Application Class Data Sharing.

## -XX:+ShowCodeDetailsInExceptionMessages
Enables printing of improved NullPointerException messages. When an application throws a NullPointerException, the option enables the JVM to analyze the program's bytecode instructions to determine precisely which reference is null, and describes the source with a null-detail message. The null-detail message is calculated and returned by NullPointerException.getMessage(), and will be printed as the exception message along with the method, filename, and line number. By default, this option is enabled.
## -XX:+ShowMessageBoxOnError
Enables the display of a dialog box when the JVM experiences an irrecoverable error. This prevents the JVM from exiting and keeps the process active so that you can attach a debugger to it to investigate the cause of the error. By default, this option is disabled.
## -XX:StartFlightRecording=parameter=value
Starts a JFR recording for the Java application. This option is equivalent to the JFR.start diagnostic command that starts a recording during runtime. You can set the following parameter=value entries when starting a JFR recording:

delay=time
Specifies the delay between the Java application launch time and the start of the recording. Append s to specify the time in seconds, m for minutes, h for hours, or d for days (for example, 10m means 10 minutes). By default, there's no delay, and this parameter is set to 0.
disk={true|false}
Specifies whether to write temporary data to the disk repository. By default, this parameter is set to false. To enable it, set the parameter to true.
dumponexit={true|false}
Specifies if the running recording is dumped when the JVM shuts down. If enabled and a filename is not entered, the recording is written to a file in the directory where the process was started. The file name is a system-generated name that contains the process ID, recording ID, and current timestamp, similar to hotspot-pid-47496-id-1-2018_01_25_19_10_41.jfr. By default, this parameter is disabled.
duration=time
Specifies the duration of the recording. Append s to specify the time in seconds, m for minutes, h for hours, or d for days (for example, specifying 5h means 5 hours). By default, the duration isn't limited, and this parameter is set to 0.
filename=path
Specifies the path and name of the file to which the recording is written when the recording is stopped, for example:

recording.jfr
/home/user/recordings/recording.jfr
c:\recordings\recording.jfr
name=identifier
Takes both the name and the identifier of a recording.
maxage=time
Specifies the maximum age of disk data to keep for the recording. This parameter is valid only when the disk parameter is set to true. Append s to specify the time in seconds, m for minutes, h for hours, or d for days (for example, specifying 30s means 30 seconds). By default, the maximum age isn't limited, and this parameter is set to 0s.
maxsize=size
Specifies the maximum size (in bytes) of disk data to keep for the recording. This parameter is valid only when the disk parameter is set to true. The value must not be less than the value for the maxchunksize parameter set with -XX:FlightRecorderOptions. Append m or M to specify the size in megabytes, or g or G to specify the size in gigabytes. By default, the maximum size of disk data isn't limited, and this parameter is set to 0.
path-to-gc-roots={true|false}
Specifies whether to collect the path to garbage collection (GC) roots at the end of a recording. By default, this parameter is disabled.

The path to GC roots is useful for finding memory leaks, but collecting it is time-consuming. Enable this option only when you start a recording for an application that you suspect has a memory leak. If the settings parameter is set to profile, the stack trace from where the potential leaking object was allocated is included in the information collected.

settings=path
Specifies the path and name of the event settings file (of type JFC). By default, the default.jfc file is used, which is located in JAVA_HOME/lib/jfr. This default settings file collects a predefined set of information with low overhead, so it has minimal impact on performance and can be used with recordings that run continuously.

A second settings file is also provided, profile.jfc, which provides more data than the default configuration, but can have more overhead and impact performance. Use this configuration for short periods of time when more information is needed.

You can specify values for multiple parameters by separating them with a comma. Event settings and .jfc options can be specified using the following syntax:

option=value
Specifies the option value to modify. To list available options, use the JAVA_HOME/bin/jfr tool.
event-setting=value
Specifies the event setting value to modify. Use the form: #= To add a new event setting, prefix the event name with '+'.
You can specify values for multiple event settings and .jfc options by separating them with a comma. In case of a conflict between a parameter and a .jfc option, the parameter will take precedence. The whitespace delimiter can be omitted for timespan values, i.e. 20ms. For more information about the settings syntax, see Javadoc of the jdk.jfr package.

## -XX:ThreadStackSize=size

Sets the Java thread stack size (in kilobytes). Use of a scaling suffix, such as k, results in the scaling of the kilobytes value so that -XX:ThreadStackSize=1k sets the Java thread stack size to 1024*1024 bytes or 1 megabyte. The default value depends on the platform:

Linux/x64 (64-bit): 1024 KB

macOS (64-bit): 1024 KB

Windows: The default value depends on virtual memory

The following examples show how to set the thread stack size to 1 megabyte in different units:

-XX:ThreadStackSize=1k
-XX:ThreadStackSize=1024
This option is similar to -Xss.

## -XX:-UseCompressedOops
Disables the use of compressed pointers. By default, this option is enabled, and compressed pointers are used. This will automatically limit the maximum ergonomically determined Java heap size to the maximum amount of memory that can be covered by compressed pointers. By default this range is 32 GB.

With compressed oops enabled, object references are represented as 32-bit offsets instead of 64-bit pointers, which typically increases performance when running the application with Java heap sizes smaller than the compressed oops pointer range. This option works only for 64-bit JVMs.

It's possible to use compressed pointers with Java heap sizes greater than 32 GB. See the -XX:ObjectAlignmentInBytes option.

## -XX:-UseContainerSupport
The VM now provides automatic container detection support, which allows the VM to determine the amount of memory and number of processors that are available to a Java process running in docker containers. It uses this information to allocate system resources. This support is only available on Linux x64 platforms. If supported, the default for this flag is true, and container support is enabled by default. It can be disabled with -XX:-UseContainerSupport.

Unified Logging is available to help to diagnose issues related to this support.

Use -Xlog:os+container=trace for maximum logging of container information. See Enable Logging with the JVM Unified Logging Framework for a description of using Unified Logging.

## -XX:+UseHugeTLBFS
Linux only: This option is the equivalent of specifying -XX:+UseLargePages. This option is disabled by default. This option pre-allocates all large pages up-front, when memory is reserved; consequently the JVM can't dynamically grow or shrink large pages memory areas; see -XX:UseTransparentHugePages if you want this behavior.

See Large Pages.

## -XX:+UseLargePages
Enables the use of large page memory. By default, this option is disabled and large page memory isn't used.

See Large Pages.

## -XX:+UseTransparentHugePages
Linux only: Enables the use of large pages that can dynamically grow or shrink. This option is disabled by default. You may encounter performance problems with transparent huge pages as the OS moves other pages around to create huge pages; this option is made available for experimentation.
## -XX:+AllowUserSignalHandlers
Enables installation of signal handlers by the application. By default, this option is disabled and the application isn't allowed to install signal handlers.
## -XX:VMOptionsFile=filename
Allows user to specify VM options in a file, for example, java -XX:VMOptionsFile=/var/my_vm_options HelloWorld.