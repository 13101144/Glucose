package com.github.glucose.core.internal;


import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class Graph<T> {
	
	private List<Vertex<T>> allVertices = new LinkedList<Vertex<T>>();
	private List<Edge<T>> allEdges = new LinkedList<Edge<T>>();
	
	public Graph(){
		
	}
	
	public Graph(Graph<T> g) {
        for (Vertex<T> v : g.getVertices())
            this.allVertices.add(new Vertex<T>(v));
        
        for (Vertex<T> v : this.getVertices()) {
            for (Edge<T> e : v.getEdges()) {
                this.allEdges.add(e);
            }
        }
    }
	
	public List<Vertex<T>> getVertices() {
	    return allVertices;
	}
	
	public List<Edge<T>> getEdges() {
	    return allEdges;
	}
	 
	public Graph(Collection<Vertex<T>> vertices, Collection<Edge<T>> edges) {


        this.allVertices.addAll(vertices);
        this.allEdges.addAll(edges);

        for (Edge<T> e : edges) {
            final Vertex<T> from = e.getFromVertex();
            final Vertex<T> to = e.getToVertex();

            if (!this.allVertices.contains(from) || !this.allVertices.contains(to))
                continue;

            from.addEdge(e);
            
        }
    }
	
	@Override
    public int hashCode() {
        int code = this.allVertices.size() + this.allEdges.size();
        for (Vertex<T> v : allVertices)
            code *= v.hashCode();
        for (Edge<T> e : allEdges)
            code *= e.hashCode();
        return 31 * code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object g1) {
        if (!(g1 instanceof Graph))
            return false;

        final Graph<T> g = (Graph<T>) g1;

        final boolean verticesSizeEquals = this.allVertices.size() == g.allVertices.size();
        if (!verticesSizeEquals)
            return false;

        final boolean edgesSizeEquals = this.allEdges.size() == g.allEdges.size();
        if (!edgesSizeEquals)
            return false;

        // Vertices can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] ov1 = this.allVertices.toArray();
        Arrays.sort(ov1);
        final Object[] ov2 = g.allVertices.toArray();
        Arrays.sort(ov2);
        for (int i=0; i<ov1.length; i++) {
            final Vertex<T> v1 = (Vertex<T>) ov1[i];
            final Vertex<T> v2 = (Vertex<T>) ov2[i];
            if (!v1.equals(v2))
                return false;
        }

        // Edges can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] oe1 = this.allEdges.toArray();
        Arrays.sort(oe1);
        final Object[] oe2 = g.allEdges.toArray();
        Arrays.sort(oe2);
        for (int i=0; i<oe1.length; i++) {
            final Edge<T> e1 = (Edge<T>) oe1[i];
            final Edge<T> e2 = (Edge<T>) oe2[i];
            if (!e1.equals(e2))
                return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Vertex<T> v : allVertices)
            builder.append(v.toString());
        return builder.toString();
    }

	
	
}
