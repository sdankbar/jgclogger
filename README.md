# jGCLogger

Java library that logs, using SLF4J, every time a garbage collection event occurs.  Logging is enabled by
calling GarbageCollectionLogger.startLogging(). For details on SLF4J configuration, see [SLF4J](https://www.slf4j.org/).

# Example Log 

	19:30:16 [Service Thread] INFO  com.github.sdankbar.gc_log.GCNotificationLogger - 
		GarbageCollection: G1 Young Generation
		Cause=G1 Evacuation Pause
		Count=6
		Start=2019-02-18T01:30:16.359Z End=2019-02-18T01:30:16.367Z
		GC Duration 8 milliseconds
			CodeHeap 'non-nmethods'
				Before/After init =       2555904 (   2.44 MB) used =       1181312 (   1.13 MB) committed =       2555904 (   2.44 MB) max =       5832704 (   5.56 MB)
			CodeHeap 'non-profiled nmethods'
				Before/After init =       2555904 (   2.44 MB) used =        541696 ( 529.00 KB) committed =       2555904 (   2.44 MB) max =     122912768 ( 117.22 MB)
			CodeHeap 'profiled nmethods'
				Before/After init =       2555904 (   2.44 MB) used =       2767360 (   2.64 MB) committed =       2818048 (   2.69 MB) max =     122912768 ( 117.22 MB)
			Compressed Class Space
				Before/After init =             0              used =       1296328 (   1.24 MB) committed =       1572864 (   1.50 MB) max =    1073741824 (   1.00 GB)
			G1 Eden Space
				Before init =      15728640 (  15.00 MB) used =      44040192 (  42.00 MB) committed =      47185920 (  45.00 MB) max =            -1             
				After  init =      15728640 (  15.00 MB) used =             0              committed =      57671680 (  55.00 MB) max =            -1             
				Delta  init =             0              used =     -44040192 ( -42.00 MB) committed =      10485760 (  10.00 MB) max =             0             
			G1 Old Gen
				Before init =      45088768 (  43.00 MB) used =      17548064 (  16.74 MB) committed =      52428800 (  50.00 MB) max =     973078528 ( 928.00 MB)
				After  init =      45088768 (  43.00 MB) used =      16803368 (  16.02 MB) committed =      37748736 (  36.00 MB) max =     973078528 ( 928.00 MB)
				Delta  init =             0              used =       -744696 (-727.24 KB) committed =     -14680064 ( -14.00 MB) max =             0             
			G1 Survivor Space
				Before init =             0              used =       1048576 (   1.00 MB) committed =       1048576 (   1.00 MB) max =            -1             
				After  init =             0              used =       5242880 (   5.00 MB) committed =       5242880 (   5.00 MB) max =            -1             
				Delta  init =             0              used =       4194304 (   4.00 MB) committed =       4194304 (   4.00 MB) max =             0             
			Metaspace
				Before/After init =             0              used =      10450416 (   9.97 MB) committed =      11010048 (  10.50 MB) max =            -1 