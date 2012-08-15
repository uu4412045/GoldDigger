package com.golddigger.utils;

import java.util.ArrayList;
import java.util.List;

public class Container<B> extends ArrayList<B> {
	private static final long serialVersionUID = 594580900166583046L;

	@SuppressWarnings("unchecked")
	public <T extends B> List<T> filter(Class<T> classOfT) {
		List<T> list = new ArrayList<T>();
		for (B element : this) {
			if (classOfT.isInstance(element)) list.add((T) element);
		}
		return list;
	}
}