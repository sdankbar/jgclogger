/**
 * The MIT License
 * Copyright © 2019 Stephen Dankbar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.sdankbar.gc_log;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

import javax.management.NotificationEmitter;

/**
 * Class for logging when garbage collection occurs. Use by getting the
 * singleton instance of this class via getInstance(). The GC logs will be made
 * using slf4j.
 */
public class GarbageCollectionLogger {

	@SuppressWarnings("unused")
	private static final GarbageCollectionLogger INSTANCE = new GarbageCollectionLogger();

	/**
	 * Begins logging garbage collection events.
	 */
	public static void startLogging() {
		// Empty Implementation
	}

	private GarbageCollectionLogger() {
		final long jvmStartTimeMilli = ManagementFactory.getRuntimeMXBean().getStartTime();
		for (final GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
			final NotificationEmitter emitter = (NotificationEmitter) gcbean;
			emitter.addNotificationListener(new GCNotificationLogger(jvmStartTimeMilli), null, null);
		}
	}

}
