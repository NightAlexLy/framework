package org.luisyang.util.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b> LRU 即 Least Rencetly Used(最近最少使用)缓存替换策略</b>
 * <p>http://blogread.cn/it/article/4266?f=wb</p>
 * <p>http://blog.csdn.net/njchenyi/article/details/8046914</p>
 * <p>http://janeky.iteye.com/blog/1534352</p>
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> {
	
	private static final long serialVersionUID = -248574785443822095L;
	protected final int maxCapacity;

	public LRUMap(int maxCapacity) {
		super(1000, 0.75F, true);
		this.maxCapacity = maxCapacity;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > this.maxCapacity;
	}
}
