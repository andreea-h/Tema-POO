import java.util.*;
import java.util.ArrayList.*;
import java.awt.*;

class ArrayMap<K,V> extends AbstractMap<K,V> {
	
	ArrayList list = new ArrayList();
	Set entries;
	
	public V put(K key, V value) {
		int size = list.size();
		ArrayMapEntry<K,V> entry = null;
		int i;
		if(key == null) {
			for(i=0; i < size; i++) {
				entry = (ArrayMapEntry<K,V>)(list.get(i));
				if(entry.getKey()== null)
					break;
			}
		}
		else {
			for(i=0; i < size; i++) {
				entry = (ArrayMapEntry<K,V>)(list.get(i));
				if(key.equals(entry.getKey())) {
					break;
				}
			}
		}
		Object oldValue = null;
		if(i < size) {
			oldValue = entry.getValue();
			entry.setValue(value);
		}
		else {
			list.add(new ArrayMapEntry<K,V>(key, value));
		}
		return (V)oldValue;
	}
	
	public boolean containsKey(Object key) {
		boolean result = false;
		int i;
		int size = list.size();
		ArrayMapEntry<K,V> entry = null;
		for(i=0; i< size; i++) {
			entry = (ArrayMapEntry<K,V>)(list.get(i));
			if(key.equals(entry.getKey())) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public V get(Object key) {
		int i;
		int size = list.size();
		ArrayMapEntry<K,V> entry = null;
		for(i=0; i< size; i++) {
			entry = (ArrayMapEntry<K,V>)(list.get(i));
			if(key.equals(entry.getKey())) {
				return entry.getValue();
				}
			}
		return null;
	}
	
	public int size() {
		return list.size();
	}
	
	public Set<Entry<K,V>> entrySet() {
		if(entries == null) {
			entries = new AbstractSet() {
				public int size() {
					return list.size();
				}
				public Iterator iterator() {
					return list.iterator();
				}
			};
		}
		return entries;
	}
	
	
	class ArrayMapEntry<K, V> implements Map.Entry<K,V> {
        K key;
        V value;
        public ArrayMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public V getValue() {
            return this.value;
        }
        
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        } //returneaza vechea valoare
        
        public String toString(){
            return this.key + " = " + this.value;
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) 
                return false;
            Map.Entry e = (Map.Entry) o;
            return (key == null ? e.getKey() == null : key.equals(e.getKey())) && (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }
        
        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }
    }
	
}



