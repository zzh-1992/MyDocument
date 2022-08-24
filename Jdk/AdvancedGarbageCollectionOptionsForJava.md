# Advanced Garbage Collection Options for Java

jdk官网链接:https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html

These java options control how garbage collection (GC) is performed by the Java HotSpot VM.

## -XX:+AggressiveHeap
Enables Java heap optimization. This sets various parameters to be optimal for long-running jobs with intensive memory
allocation, based on the configuration of the computer (RAM and CPU). By default, the option is disabled and the heap
sizes are configured less aggressively.
## -XX:+AlwaysPreTouch
Requests the VM to touch every page on the Java heap after requesting it from the operating system and before handing
memory out to the application. By default, this option is disabled and all pages are committed as the application uses
the heap space.
## -XX:ConcGCThreads=threads
Sets the number of threads used for concurrent GC. Sets threads to approximately 1/4 of the number of parallel garbage
collection threads. The default value depends on the number of CPUs available to the JVM.

For example, to set the number of threads for concurrent GC to 2, specify the following option:

-XX:ConcGCThreads=2

## -XX:+DisableExplicitGC
Enables the option that disables processing of calls to the System.gc() method. This option is disabled by default,
meaning that calls to System.gc() are processed. If processing of calls to System.gc() is disabled, then the JVM still
performs GC when necessary.
## -XX:+ExplicitGCInvokesConcurrent
Enables invoking of concurrent GC by using the System.gc() request. This option is disabled by default and can be
enabled only with the -XX:+UseG1GC option.
## -XX:G1AdaptiveIHOPNumInitialSamples=number
When -XX:UseAdaptiveIHOP is enabled, this option sets the number of completed marking cycles used to gather samples
until G1 adaptively determines the optimum value of -XX:InitiatingHeapOccupancyPercent. Before, G1 uses the value of
## -XX:InitiatingHeapOccupancyPercent directly for this purpose. The default value is 3.
## -XX:G1HeapRegionSize=size
Sets the size of the regions into which the Java heap is subdivided when using the garbage-first (G1) collector. The
value is a power of 2 and can range from 1 MB to 32 MB. The default region size is determined ergonomically based on the
heap size with a goal of approximately 2048 regions.

The following example sets the size of the subdivisions to 16 MB:

## -XX:G1HeapRegionSize=16m

## -XX:G1HeapWastePercent=percent
Sets the percentage of heap that you're willing to waste. The Java HotSpot VM doesn't initiate the mixed garbage
collection cycle when the reclaimable percentage is less than the heap waste percentage. The default is 5 percent.
## -XX:G1MaxNewSizePercent=percent
Sets the percentage of the heap size to use as the maximum for the young generation size. The default value is 60
percent of your Java heap.

This is an experimental flag. This setting replaces the -XX:DefaultMaxNewGenPercent setting.

## -XX:G1MixedGCCountTarget=number
Sets the target number of mixed garbage collections after a marking cycle to collect old regions with at most
G1MixedGCLIveThresholdPercent live data. The default is 8 mixed garbage collections. The goal for mixed collections is
to be within this target number.
## -XX:G1MixedGCLiveThresholdPercent=percent
Sets the occupancy threshold for an old region to be included in a mixed garbage collection cycle. The default occupancy
is 85 percent.

This is an experimental flag. This setting replaces the -XX:G1OldCSetRegionLiveThresholdPercent setting.

## -XX:G1NewSizePercent=percent
Sets the percentage of the heap to use as the minimum for the young generation size. The default value is 5 percent of
your Java heap.

This is an experimental flag. This setting replaces the -XX:DefaultMinNewGenPercent setting.

## -XX:G1OldCSetRegionThresholdPercent=percent
Sets an upper limit on the number of old regions to be collected during a mixed garbage collection cycle. The default is
10 percent of the Java heap.
## -XX:G1ReservePercent=percent
Sets the percentage of the heap (0 to 50) that's reserved as a false ceiling to reduce the possibility of promotion
failure for the G1 collector. When you increase or decrease the percentage, ensure that you adjust the total Java heap
by the same amount. By default, this option is set to 10%.

The following example sets the reserved heap to 20%:

## -XX:G1ReservePercent=20

## -XX:+G1UseAdaptiveIHOP
Controls adaptive calculation of the old generation occupancy to start background work preparing for an old generation
collection. If enabled, G1 uses -XX:InitiatingHeapOccupancyPercent for the first few times as specified by the value of
-XX:G1AdaptiveIHOPNumInitialSamples, and after that adaptively calculates a new optimum value for the initiating
occupancy automatically. Otherwise, the old generation collection process always starts at the old generation occupancy
determined by -XX:InitiatingHeapOccupancyPercent.

The default is enabled.

## -XX:InitialHeapSize=size
Sets the initial size (in bytes) of the memory allocation pool. This value must be either 0, or a multiple of 1024 and
greater than 1 MB. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate
gigabytes. The default value is selected at run time based on the system configuration.

The following examples show how to set the size of allocated memory to 6 MB using various units:

-XX:InitialHeapSize=6291456
-XX:InitialHeapSize=6144k
-XX:InitialHeapSize=6m
If you set this option to 0, then the initial size is set as the sum of the sizes allocated for the old generation and
the young generation. The size of the heap for the young generation can be set using the -XX:NewSize option. Note that
the -Xms option sets both the minimum and the initial heap size of the heap. If -Xms appears after -XX:InitialHeapSize
on the command line, then the initial heap size gets set to the value specified with -Xms.

## -XX:InitialRAMPercentage=percent
Sets the initial amount of memory that the JVM will use for the Java heap before applying ergonomics heuristics as a
percentage of the maximum amount determined as described in the -XX:MaxRAM option. The default value is 1.5625 percent.

The following example shows how to set the percentage of the initial amount of memory used for the Java heap:

-XX:InitialRAMPercentage=5

## -XX:InitialSurvivorRatio=ratio
Sets the initial survivor space ratio used by the throughput garbage collector (which is enabled by the -XX:
+UseParallelGC option). Adaptive sizing is enabled by default with the throughput garbage collector by using the -XX:
+UseParallelGC option, and the survivor space is resized according to the application behavior, starting with the
initial value. If adaptive sizing is disabled (using the -XX:-UseAdaptiveSizePolicy option), then the -XX:SurvivorRatio
option should be used to set the size of the survivor space for the entire execution of the application.

The following formula can be used to calculate the initial size of survivor space (S) based on the size of the young
generation (Y), and the initial survivor space ratio (R):

S=Y/(R+2)

The 2 in the equation denotes two survivor spaces. The larger the value specified as the initial survivor space ratio,
the smaller the initial survivor space size.

By default, the initial survivor space ratio is set to 8. If the default value for the young generation space size is
used (2 MB), then the initial size of the survivor space is 0.2 MB.

The following example shows how to set the initial survivor space ratio to 4:

-XX:InitialSurvivorRatio=4

## -XX:InitiatingHeapOccupancyPercent=percent
Sets the percentage of the old generation occupancy (0 to 100) at which to start the first few concurrent marking cycles
for the G1 garbage collector.

By default, the initiating value is set to 45%. A value of 0 implies nonstop concurrent GC cycles from the beginning
until G1 adaptively sets this value.

See also the -XX:G1UseAdaptiveIHOP and -XX:G1AdaptiveIHOPNumInitialSamples options.

The following example shows how to set the initiating heap occupancy to 75%:

-XX:InitiatingHeapOccupancyPercent=75

## -XX:MaxGCPauseMillis=time
Sets a target for the maximum GC pause time (in milliseconds). This is a soft goal, and the JVM will make its best
effort to achieve it. The specified value doesn't adapt to your heap size. By default, for G1 the maximum pause time
target is 200 milliseconds. The other generational collectors do not use a pause time goal by default.

The following example shows how to set the maximum target pause time to 500 ms:

-XX:MaxGCPauseMillis=500

## -XX:MaxHeapSize=size
Sets the maximum size (in byes) of the memory allocation pool. This value must be a multiple of 1024 and greater than 2
MB. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. The
default value is selected at run time based on the system configuration. For server deployments, the options -XX:
InitialHeapSize and -XX:MaxHeapSize are often set to the same value.

The following examples show how to set the maximum allowed size of allocated memory to 80 MB using various units:

-XX:MaxHeapSize=83886080
-XX:MaxHeapSize=81920k
-XX:MaxHeapSize=80m
The -XX:MaxHeapSize option is equivalent to -Xmx.

## -XX:MaxHeapFreeRatio=percent
Sets the maximum allowed percentage of free heap space (0 to 100) after a GC event. If free heap space expands above
this value, then the heap is shrunk. By default, this value is set to 70%.

Minimize the Java heap size by lowering the values of the parameters MaxHeapFreeRatio (default value is 70%) and
MinHeapFreeRatio (default value is 40%) with the command-line options -XX:MaxHeapFreeRatio and -XX:MinHeapFreeRatio.
Lowering MaxHeapFreeRatio to as low as 10% and MinHeapFreeRatio to 5% has successfully reduced the heap size without too
much performance regression; however, results may vary greatly depending on your application. Try different values for
these parameters until they're as low as possible yet still retain acceptable performance.

## -XX:MaxHeapFreeRatio=10 -XX:MinHeapFreeRatio=5

Customers trying to keep the heap small should also add the option -XX:-ShrinkHeapInSteps. See Performance Tuning
Examples for a description of using this option to keep the Java heap small by reducing the dynamic footprint for
embedded applications.

## -XX:MaxMetaspaceSize=size
Sets the maximum amount of native memory that can be allocated for class metadata. By default, the size isn't limited.
The amount of metadata for an application depends on the application itself, other running applications, and the amount
of memory available on the system.

The following example shows how to set the maximum class metadata size to 256 MB:

-XX:MaxMetaspaceSize=256m

## -XX:MaxNewSize=size
Sets the maximum size (in bytes) of the heap for the young generation (nursery). The default value is set ergonomically.
## -XX:MaxRAM=size
Sets the maximum amount of memory that the JVM may use for the Java heap before applying ergonomics heuristics. The
default value is the maximum amount of available memory to the JVM process or 128 GB, whichever is lower.

The maximum amount of available memory to the JVM process is the minimum of the machine's physical memory and any
constraints set by the environment (e.g. container).

Specifying this option disables automatic use of compressed oops if the combined result of this and other options
influencing the maximum amount of memory is larger than the range of memory addressable by compressed oops. See -XX:
UseCompressedOops for further information about compressed oops.

The following example shows how to set the maximum amount of available memory for sizing the Java heap to 2 GB:

-XX:MaxRAM=2G

## -XX:MaxRAMPercentage=percent
Sets the maximum amount of memory that the JVM may use for the Java heap before applying ergonomics heuristics as a
percentage of the maximum amount determined as described in the -XX:MaxRAM option. The default value is 25 percent.

Specifying this option disables automatic use of compressed oops if the combined result of this and other options
influencing the maximum amount of memory is larger than the range of memory addressable by compressed oops. See -XX:
UseCompressedOops for further information about compressed oops.

The following example shows how to set the percentage of the maximum amount of memory used for the Java heap:

-XX:MaxRAMPercentage=75

## -XX:MinRAMPercentage=percent
Sets the maximum amount of memory that the JVM may use for the Java heap before applying ergonomics heuristics as a
percentage of the maximum amount determined as described in the -XX:MaxRAM option for small heaps. A small heap is a
heap of approximately 125 MB. The default value is 50 percent.

The following example shows how to set the percentage of the maximum amount of memory used for the Java heap for small
heaps:

-XX:MinRAMPercentage=75

## -XX:MaxTenuringThreshold=threshold
Sets the maximum tenuring threshold for use in adaptive GC sizing. The largest value is 15. The default value is 15 for
the parallel (throughput) collector.

The following example shows how to set the maximum tenuring threshold to 10:

-XX:MaxTenuringThreshold=10

## -XX:MetaspaceSize=size
Sets the size of the allocated class metadata space that triggers a garbage collection the first time it's exceeded.
This threshold for a garbage collection is increased or decreased depending on the amount of metadata used. The default
size depends on the platform.
-XX:MinHeapFreeRatio=percent
Sets the minimum allowed percentage of free heap space (0 to 100) after a GC event. If free heap space falls below this
value, then the heap is expanded. By default, this value is set to 40%.

Minimize Java heap size by lowering the values of the parameters MaxHeapFreeRatio (default value is 70%) and
MinHeapFreeRatio (default value is 40%) with the command-line options -XX:MaxHeapFreeRatio and -XX:MinHeapFreeRatio.
Lowering MaxHeapFreeRatio to as low as 10% and MinHeapFreeRatio to 5% has successfully reduced the heap size without too
much performance regression; however, results may vary greatly depending on your application. Try different values for
these parameters until they're as low as possible, yet still retain acceptable performance.

-XX:MaxHeapFreeRatio=10 -XX:MinHeapFreeRatio=5

Customers trying to keep the heap small should also add the option -XX:-ShrinkHeapInSteps. See Performance Tuning
Examples for a description of using this option to keep the Java heap small by reducing the dynamic footprint for
embedded applications.

## -XX:MinHeapSize=size
Sets the minimum size (in bytes) of the memory allocation pool. This value must be either 0, or a multiple of 1024 and
greater than 1 MB. Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, or g or G to indicate
gigabytes. The default value is selected at run time based on the system configuration.

The following examples show how to set the mimimum size of allocated memory to 6 MB using various units:

-XX:MinHeapSize=6291456
-XX:MinHeapSize=6144k
-XX:MinHeapSize=6m
If you set this option to 0, then the minimum size is set to the same value as the initial size.

## -XX:NewRatio=ratio
Sets the ratio between young and old generation sizes. By default, this option is set to 2. The following example shows
how to set the young-to-old ratio to 1:

-XX:NewRatio=1

## -XX:NewSize=size
Sets the initial size (in bytes) of the heap for the young generation (nursery). Append the letter k or K to indicate
kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes.

The young generation region of the heap is used for new objects. GC is performed in this region more often than in other
regions. If the size for the young generation is too low, then a large number of minor GCs are performed. If the size is
too high, then only full GCs are performed, which can take a long time to complete. It is recommended that you keep the
size for the young generation greater than 25% and less than 50% of the overall heap size.

The following examples show how to set the initial size of the young generation to 256 MB using various units:

-XX:NewSize=256m
-XX:NewSize=262144k
-XX:NewSize=268435456
The -XX:NewSize option is equivalent to -Xmn.

## -XX:ParallelGCThreads=threads
Sets the number of the stop-the-world (STW) worker threads. The default value depends on the number of CPUs available to
the JVM and the garbage collector selected.

For example, to set the number of threads for G1 GC to 2, specify the following option:

-XX:ParallelGCThreads=2

## -XX:+ParallelRefProcEnabled
Enables parallel reference processing. By default, this option is disabled.
-XX:+PrintAdaptiveSizePolicy
Enables printing of information about adaptive-generation sizing. By default, this option is disabled.
## -XX:+ScavengeBeforeFullGC
Enables GC of the young generation before each full GC. This option is enabled by default. It is recommended that you
don't disable it, because scavenging the young generation before a full GC can reduce the number of objects reachable
from the old generation space into the young generation space. To disable GC of the young generation before each full
GC, specify the option -XX:-ScavengeBeforeFullGC.
## -XX:SoftRefLRUPolicyMSPerMB=time
Sets the amount of time (in milliseconds) a softly reachable object is kept active on the heap after the last time it
was referenced. The default value is one second of lifetime per free megabyte in the heap. The -XX:
SoftRefLRUPolicyMSPerMB option accepts integer values representing milliseconds per one megabyte of the current heap
size (for Java HotSpot Client VM) or the maximum possible heap size (for Java HotSpot Server VM). This difference means
that the Client VM tends to flush soft references rather than grow the heap, whereas the Server VM tends to grow the
heap rather than flush soft references. In the latter case, the value of the -Xmx option has a significant effect on how
quickly soft references are garbage collected.

The following example shows how to set the value to 2.5 seconds:

-XX:SoftRefLRUPolicyMSPerMB=2500

## -XX:-ShrinkHeapInSteps
Incrementally reduces the Java heap to the target size, specified by the option -XX:MaxHeapFreeRatio. This option is
enabled by default. If disabled, then it immediately reduces the Java heap to the target size instead of requiring
multiple garbage collection cycles. Disable this option if you want to minimize the Java heap size. You will likely
encounter performance degradation when this option is disabled.

See Performance Tuning Examples for a description of using the MaxHeapFreeRatio option to keep the Java heap small by
reducing the dynamic footprint for embedded applications.

## -XX:StringDeduplicationAgeThreshold=threshold
Identifies String objects reaching the specified age that are considered candidates for deduplication. An object's age
is a measure of how many times it has survived garbage collection. This is sometimes referred to as tenuring.

Note: String objects that are promoted to an old heap region before this age has been reached are always considered
candidates for deduplication. The default value for this option is 3. See the -XX:+UseStringDeduplication option.

## -XX:SurvivorRatio=ratio
Sets the ratio between eden space size and survivor space size. By default, this option is set to 8. The following
example shows how to set the eden/survivor space ratio to 4:

-XX:SurvivorRatio=4

## -XX:TargetSurvivorRatio=percent
Sets the desired percentage of survivor space (0 to 100) used after young garbage collection. By default, this option is
set to 50%.

The following example shows how to set the target survivor space ratio to 30%:

-XX:TargetSurvivorRatio=30

## -XX:TLABSize=size
Sets the initial size (in bytes) of a thread-local allocation buffer (TLAB). Append the letter k or K to indicate
kilobytes, m or M to indicate megabytes, or g or G to indicate gigabytes. If this option is set to 0, then the JVM
selects the initial size automatically.

The following example shows how to set the initial TLAB size to 512 KB:

-XX:TLABSize=512k

## -XX:+UseAdaptiveSizePolicy
Enables the use of adaptive generation sizing. This option is enabled by default. To disable adaptive generation sizing,
specify -XX:-UseAdaptiveSizePolicy and set the size of the memory allocation pool explicitly. See the -XX:SurvivorRatio
option.
## -XX:+UseG1GC
Enables the use of the garbage-first (G1) garbage collector. It's a server-style garbage collector, targeted for
multiprocessor machines with a large amount of RAM. This option meets GC pause time goals with high probability, while
maintaining good throughput. The G1 collector is recommended for applications requiring large heaps (sizes of around 6
GB or larger) with limited GC latency requirements (a stable and predictable pause time below 0.5 seconds). By default,
this option is enabled and G1 is used as the default garbage collector.
## -XX:+UseGCOverheadLimit
Enables the use of a policy that limits the proportion of time spent by the JVM on GC before an OutOfMemoryError
exception is thrown. This option is enabled, by default, and the parallel GC will throw an OutOfMemoryError if more than
98% of the total time is spent on garbage collection and less than 2% of the heap is recovered. When the heap is small,
this feature can be used to prevent applications from running for long periods of time with little or no progress. To
disable this option, specify the option -XX:-UseGCOverheadLimit.
## -XX:+UseNUMA
Enables performance optimization of an application on a machine with nonuniform memory architecture (NUMA) by increasing
the application's use of lower latency memory. By default, this option is disabled and no optimization for NUMA is made.
The option is available only when the parallel garbage collector is used (-XX:+UseParallelGC).
## -XX:+UseParallelGC
Enables the use of the parallel scavenge garbage collector (also known as the throughput collector) to improve the
performance of your application by leveraging multiple processors.

By default, this option is disabled and the default collector is used.

## -XX:+UseSerialGC
Enables the use of the serial garbage collector. This is generally the best choice for small and simple applications
that don't require any special functionality from garbage collection. By default, this option is disabled and the
default collector is used.
## -XX:+UseSHM
Linux only: Enables the JVM to use shared memory to set up large pages.

See Large Pages for setting up large pages.

## -XX:+UseStringDeduplication
Enables string deduplication. By default, this option is disabled. To use this option, you must enable the
garbage-first (G1) garbage collector.

String deduplication reduces the memory footprint of String objects on the Java heap by taking advantage of the fact
that many String objects are identical. Instead of each String object pointing to its own character array, identical
String objects can point to and share the same character array.

## -XX:+UseTLAB
Enables the use of thread-local allocation blocks (TLABs) in the young generation space. This option is enabled by
default. To disable the use of TLABs, specify the option -XX:-UseTLAB.
## -XX:+UseZGC
Enables the use of the Z garbage collector (ZGC). This is a low latency garbage collector, providing max pause times of
a few milliseconds, at some throughput cost. Pause times are independent of what heap size is used. Supports heap sizes
from 8MB to 16TB.
## -XX:ZAllocationSpikeTolerance=factor
Sets the allocation spike tolerance for ZGC. By default, this option is set to 2.0. This factor describes the level of
allocation spikes to expect. For example, using a factor of 3.0 means the current allocation rate can be expected to
triple at any time.
## -XX:ZCollectionInterval=seconds
Sets the maximum interval (in seconds) between two GC cycles when using ZGC. By default, this option is set to 0 (
disabled).
-XX:ZFragmentationLimit=percent
Sets the maximum acceptable heap fragmentation (in percent) for ZGC. By default, this option is set to 25. Using a lower
value will cause the heap to be compacted more aggressively, to reclaim more memory at the cost of using more CPU time.
## -XX:+ZProactive
Enables proactive GC cycles when using ZGC. By default, this option is enabled. ZGC will start a proactive GC cycle if
doing so is expected to have minimal impact on the running application. This is useful if the application is mostly idle
or allocates very few objects, but you still want to keep the heap size down and allow reference processing to happen
even when there are a lot of free space on the heap.
## -XX:+ZUncommit
Enables uncommitting of unused heap memory when using ZGC. By default, this option is enabled. Uncommitting unused heap
memory will lower the memory footprint of the JVM, and make that memory available for other processes to use.
## -XX:ZUncommitDelay=seconds
Sets the amount of time (in seconds) that heap memory must have been unused before being uncommitted. By default, this
option is set to 300 (5 minutes). Committing and uncommitting memory are relatively expensive operations. Using a lower
value will cause heap memory to be uncommitted earlier, at the risk of soon having to commit it again.