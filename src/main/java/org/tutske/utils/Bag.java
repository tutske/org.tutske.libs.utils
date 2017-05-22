package org.tutske.utils;

import java.util.*;


public class Bag<K, V> implements Map<K, V> {

	private final Map<K, List<V>> data = new LinkedHashMap<> ();

	public Bag<K, V> add (K key, V ... items) {
		List<V> values = assureKey (key);
		values.addAll (Arrays.asList (items));
		return this;
	}

	public Bag<K, V> add (K key, List<V> values) {
		List<V> internal = assureKey (key);
		internal.addAll (values);
		return this;
	}

	@Override
	public boolean replace(K key, V old, V value) {
		List<V> values = data.get (key);
		if ( values == null || values.isEmpty () ) { return false; }
		int index = values.indexOf (old);
		if ( index < 0 ) { return false; }
		values.set (index, value);
		return true;
	}

	public List<V> getAll (K key) {
		return data.get (key);
	}

	@Override
	public int size () {
		return data.size ();
	}

	@Override
	public boolean isEmpty () {
		return data.isEmpty ();
	}

	@Override
	public boolean containsKey (Object key) {
		return data.containsKey (key);
	}

	@Override
	public boolean containsValue (Object value) {
		for ( Map.Entry<K, V> entry : entrySet () ) {
			if ( Objects.equals (entry.getValue (), value) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get (Object key) {
		List<V> values = data.get (key);
		if( values == null || values .isEmpty () ) { return null; }
		return values.get (0);
	}

	public <T> T getAs (K key, Class<T> clazz) {
		return PrimitivesParser.parse (get (key), clazz);
	}

	@Override
	public V put (K key, V value) {
		assureKey (key).add (0, value);
		return value;
	}

	public Bag<K, V> put (K key, V ... items) {
		List<V> values = assureKey (key);
		for ( int i = 0; i < items.length; i++ ) {
			values.add (i, items[i]);
		}
		return this;
	}

	@Override
	public V remove (Object key) {
		List<V> values = data.get (key);
		if ( values == null || values.isEmpty () ) { return null; }
		V value = values.remove (0);
		if ( values.isEmpty () ) { data.remove (key); }
		return value;
	}

	@Override
	public boolean remove (Object key, Object old) {
		List<V> values = data.get (key);
		if ( values == null ) { return false; }
		int index = values.indexOf (old);
		if ( index < 0 ) { return false; }
		values.remove (index);
		if ( values.isEmpty () ) { data.remove (key); }
		return true;
	}

	@Override
	public void putAll (Map<? extends K, ? extends V> map) {
		map.forEach (this::put);
	}

	public void putAll (Bag<? extends K, ? extends V> bag) {
		bag.data.forEach ((key, vs) -> {
			assureKey (key).addAll (0, vs);
		});
	}

	public void addAll (Map<? extends K, ? extends V> map) {
		map.forEach (this::add);
	}

	public void addAll (Bag<? extends K, ? extends V> bag) {
		bag.data.forEach ((key, values) -> {
			assureKey (key).addAll (values);
		});
	}

	public void replaceAll (Bag<? extends K, ? extends V> bag) {
		bag.data.forEach ((key, vs) -> {
			List<V> values = data.get (key);
			if ( values == null ) { return; }
			values.clear ();
			values.addAll (vs);
		});
	}

	public void squash (Bag<? extends K, ? extends V> bag) {
		bag.data.forEach ((key, vs) -> {
			List<V> values = assureKey (key);
			values.clear ();
			values.addAll (vs);
		});
	}

	@Override
	public void clear () {
		data.clear ();
	}

	public void clear (K key) {
		data.remove (key);
	}

	@Override
	public Set<K> keySet () {
		return data.keySet ();
	}

	@Override
	public Collection<V> values () {
		LinkedList<V> values = new LinkedList<> ();
		data.forEach ((key, vals) -> values.addAll (vals));
		return values;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet () {
		Set<Map.Entry<K, V>> entries = new HashSet<> ();
		data.forEach ((key, values) -> {
			values.forEach ((value) -> {
				entries.add (new Entry<> (this, key, value));
			});
		});
		return entries;
	}

	private List<V> assureKey (K key) {
		return data.computeIfAbsent (key, k -> new LinkedList<> ());
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {
		private final Bag<K, V> bag;
		private final K key;
		private final V value;

		public Entry (Bag<K, V> bag, K key, V value) {
			this.bag = bag;
			this.key = key;
			this.value = value;
		}

		@Override public K getKey () { return key; }
		@Override public V getValue () { return value; }
		@Override public V setValue (V value) {
			bag.replace (key, this.value, value);
			return value;
		}
	}

}
