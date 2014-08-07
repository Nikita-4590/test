package com.hrs.mediarequesttool.common;

import java.util.HashMap;
import java.util.Map;

public class Dictionary<K1, K2, V> {
	private Map<String, V> map;

	public Dictionary() {
		map = new HashMap<String, V>();
	}

	public void put(K1 key1, K2 key2, V value) {
		map.put(createKey(key1, key2), value);
	}

	public V get(K1 key1, K2 key2) {
		return map.get(createKey(key1, key2));
	}

	public boolean containsKeys(K1 key1, K2 key2) {
		return map.containsKey(createKey(key1, key2));
	}

	private String createKey(K1 key1, K2 key2) {
		StringBuilder builder = new StringBuilder();

		builder.append(System.identityHashCode(key1));
		builder.append("_");
		builder.append(System.identityHashCode(key2));

		return builder.toString();
	}
}
