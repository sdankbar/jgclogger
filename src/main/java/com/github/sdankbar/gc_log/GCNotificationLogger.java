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

import java.lang.management.MemoryUsage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.GarbageCollectionNotificationInfo;

/**
 * Listens for GarbageCollectionNotifications and logs the data about the
 * Garbage Collection.
 */
class GCNotificationLogger implements NotificationListener {

	private static final Logger logger = LoggerFactory.getLogger(GCNotificationLogger.class);
	private static final MemoryUsage EMPTY_USAGE = new MemoryUsage(0, 0, 0, 0);

	private static final double BYTES_PER_GB = 1024 * 1024 * 1024;
	private static final double BYTES_PER_MB = 1024 * 1024;
	private static final double BYTES_PER_KB = 1024;

	private static void append(final StringBuilder b, final long init, final long used, final long committed,
			final long max) {
		b.append("init = " + convertyBytesToString(init));
		b.append(" used = " + convertyBytesToString(used));
		b.append(" committed = " + convertyBytesToString(committed));
		b.append(" max = " + convertyBytesToString(max));
	}

	private static void append(final StringBuilder b, final Map<String, MemoryUsage> beforeMap,
			final Map<String, MemoryUsage> afterMap) {
		final Set<String> unionSet = new HashSet<>();
		unionSet.addAll(beforeMap.keySet());
		unionSet.addAll(afterMap.keySet());
		final List<String> sortedKeys = new ArrayList<>(unionSet);
		Collections.sort(sortedKeys);
		for (final String key : sortedKeys) {
			final MemoryUsage before = beforeMap.getOrDefault(key, EMPTY_USAGE);
			final MemoryUsage after = afterMap.getOrDefault(key, EMPTY_USAGE);

			// Do a toString() since equals is identity based.
			final boolean changed = !before.toString().equals(after.toString());

			b.append("\n\t\t");
			b.append(key);

			if (changed) {
				b.append("\n\t\t\tBefore ");
				append(b, before.getInit(), before.getUsed(), before.getCommitted(), before.getMax());

				b.append("\n\t\t\tAfter  ");
				append(b, after.getInit(), after.getUsed(), after.getCommitted(), after.getMax());

				b.append("\n\t\t\tDelta  ");
				append(b, after.getInit() - before.getInit(), after.getUsed() - before.getUsed(),
						after.getCommitted() - before.getCommitted(), after.getMax() - before.getMax());
			} else {
				b.append("\n\t\t\tBefore/After ");
				append(b, before.getInit(), before.getUsed(), before.getCommitted(), before.getMax());
			}
		}
	}

	private static String convertyBytesToString(final long bytes) {
		final Long byteObj = Long.valueOf(bytes);
		// Assuming only need to format sizes up to 1 TB.
		if (Math.abs(bytes) >= BYTES_PER_GB) {
			return String.format("%13d (%7.2f GB)", byteObj, Double.valueOf(bytes / BYTES_PER_GB));
		} else if (Math.abs(bytes) >= BYTES_PER_MB) {
			return String.format("%13d (%7.2f MB)", byteObj, Double.valueOf(bytes / BYTES_PER_MB));
		} else if (Math.abs(bytes) >= BYTES_PER_KB) {
			return String.format("%13d (%7.2f KB)", byteObj, Double.valueOf(bytes / BYTES_PER_KB));
		} else {
			return String.format("%13d             ", byteObj);
		}
	}

	private final long jvmStartTimeMilli;

	GCNotificationLogger(final long jvmStartTimeMilli) {
		this.jvmStartTimeMilli = jvmStartTimeMilli;
	}

	@Override
	public void handleNotification(final Notification notification, final Object handback) {
		if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
			final GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
					.from((CompositeData) notification.getUserData());

			final StringBuilder b = new StringBuilder(512);
			b.append("\n\tGarbageCollection: ");
			b.append(info.getGcName());
			b.append("\n\t");

			b.append("Cause=");
			b.append(info.getGcCause());
			b.append("\n\t");

			b.append("Count=");
			b.append(info.getGcInfo().getId());
			b.append("\n\t");

			b.append("Start=");
			b.append(Instant.ofEpochMilli(jvmStartTimeMilli + info.getGcInfo().getStartTime()));
			b.append(" End=");
			b.append(Instant.ofEpochMilli(jvmStartTimeMilli + info.getGcInfo().getEndTime()));

			b.append("\n\t");
			b.append("GC Duration ");
			b.append(info.getGcInfo().getDuration());
			b.append(" milliseconds");

			append(b, info.getGcInfo().getMemoryUsageBeforeGc(), info.getGcInfo().getMemoryUsageAfterGc());
			b.append("\n");

			logger.info(b.toString());
		}
	}
}