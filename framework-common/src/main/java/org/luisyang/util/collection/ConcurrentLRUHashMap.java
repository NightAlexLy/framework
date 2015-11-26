package org.luisyang.util.collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <b> LRU 即 Least Rencetly Used(最近最少使用)缓存替换策略</b>
 * <p>http://blogread.cn/it/article/4266?f=wb</p>
 * <p>http://blog.csdn.net/njchenyi/article/details/8046914</p>
 * <p>http://janeky.iteye.com/blog/1534352</p>
 */
public class ConcurrentLRUHashMap<K, V> {
	private final int capacity;
	private ConcurrentLinkedQueue<K> queue;
	private ConcurrentHashMap<K, V> map;

	public ConcurrentLRUHashMap(int capacity) {
		this.capacity = capacity;
		this.queue = new ConcurrentLinkedQueue();
		this.map = new ConcurrentHashMap(capacity);
	}

	public V get(K key) {
		return this.map.get(key);
	}

	public synchronized void put(K key, V value) {
		if ((null == key) || (null == value)) {
			throw new NullPointerException("ConcurrentLRUHashMap.put()");
		}
		if (this.map.containsKey(key)) {
			this.queue.remove(key);
		}
		while (this.queue.size() >= this.capacity) {
			K expiredKey = this.queue.poll();
			if (null != expiredKey) {
				this.map.remove(expiredKey);
			}
		}
		this.queue.add(key);
		this.map.put(key, value);
	}
}
