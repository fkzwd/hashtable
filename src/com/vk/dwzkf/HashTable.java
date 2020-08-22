package com.vk.dwzkf;

import java.util.ArrayList;
import java.util.List;

public class HashTable<V> {
    @SuppressWarnings("unchecked")
    private Data<V>[] table = (Data<V>[]) new Data[10];
    private int elements = 0;

    private class Data<T> {
        private String key = null;
        private T value = null;
        private Data<T> next = null;

        private Data(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Data<T> getNext() {
            return next;
        }

        public void setNext(Data<T> next) {
            this.next = next;
        }
    }

    public static class Element<T> {
        private String key = null;
        private T value = null;

        public Element(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    private void checkSize() {
        int tableSize = table.length;
        if (elements >= tableSize / 2) {
            elements = 0;
            Data<V>[] oldTable = table;
            table = (Data<V>[]) new Data[tableSize * 2];
            for (int i = 0; i < oldTable.length; i++) {
                Data<V> data = oldTable[i];
                while (data != null) {
                    put(data.getKey(), data.getValue());
                    data = data.getNext();
                }
            }
        }
    }

    private int hashCode(String key) {
        int hashCode = 0;
        for (char c : key.toCharArray()) {
            hashCode += (int) c;
        }
        return hashCode;
    }

    public void put(String key, V value) {
        int hash = hashCode(key);
        hash = hash % table.length;
        if (table[hash] == null) {
            table[hash] = new Data<>(key, value);
            elements++;
            checkSize();
        } else {
            Data<V> next = table[hash];
            boolean isNewElementAdded = true;
            if (next.getKey().equals(key)) {
                next.setValue(value);
            } else {
                while (next.getNext() != null) {
                    next = next.getNext();
                    if (next.getKey().equals(key)) {
                        next.setValue(value);
                        isNewElementAdded = false;
                        break;
                    }
                }
                if (isNewElementAdded) {
                    next.setNext(new Data<>(key, value));
                    elements++;
                    checkSize();
                }
            }
        }
    }

    public V get(String key) {
        int hash = hashCode(key);
        hash = hash % table.length;
        Data<V> data = table[hash];
        while (data != null) {
            if (data.getKey().equals(key)) {
                return data.getValue();
            }
            data = data.getNext();
        }
        return null;
    }

    public List<Element<V>> getAll() {
        List<Element<V>> elements = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            Data<V> data = table[i];
            while (data != null) {
                elements.add(new Element<V>(data.getKey(), data.getValue()));
                data = data.getNext();
            }
        }
        return elements;
    }

    public void remove(String key) {
        int hash = hashCode(key);
        hash = hash % table.length;
        Data<V> data = table[hash];
        if (data.getKey().equals(key)) {
            table[hash] = data.getNext();
            elements--;
        }
        else {
            while (data.getNext()!=null) {
                if (data.getNext().getKey().equals(key)) {
                    data.setNext(data.getNext().getNext());
                    elements--;
                    break;
                }
                data = data.getNext();
            }
        }
    }
}
