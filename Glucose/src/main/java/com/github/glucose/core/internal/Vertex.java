package com.github.glucose.core.internal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Vertex<T> {
	
	private T value;
	
	private VertexColor color;
	
	private LinkedList<Edge<T>> edges;
	
	public Vertex(T value){
		this.value = value;
		color = VertexColor.White;
		edges = new LinkedList<Edge<T>>();
	}
	
	public Vertex(Vertex<T> vertex) {
        this(vertex.value);
        this.edges.addAll(vertex.edges);
    }
	
	public T getValue() {
        return value;
    }

	public VertexColor getColor() {
		return color;
	}

	public void setColor(VertexColor color) {
		this.color = color;
	}
	
	public void addEdge(Edge<T> e) {
		edges.add(e);
	}

	public List<Edge<T>> getEdges() {
		return edges;
	}

	public Edge<T> getEdge(Vertex<T> v) {
		for (Edge<T> e : edges) {
			if (e.getToVertex().equals(v))
				return e;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int code = this.value.hashCode() + this.edges.size();
		return 31 * code;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object v1) {
		if (!(v1 instanceof Vertex))
			return false;

		final Vertex<T> v = (Vertex<T>) v1;


		final boolean edgesSizeEquals = this.edges.size() == v.edges.size();
		if (!edgesSizeEquals)
			return false;

		final boolean valuesEquals = this.value.equals(v.value);
		if (!valuesEquals)
			return false;

		final Iterator<Edge<T>> iter1 = this.edges.iterator();
		final Iterator<Edge<T>> iter2 = v.edges.iterator();
		while (iter1.hasNext() && iter2.hasNext()) {
			// Only checking the cost
			final Edge<T> e1 = iter1.next();
			final Edge<T> e2 = iter2.next();
			if(!e1.equals(e2)){
				return false;
			}
		}

		return true;
	}
	
	
	
}
