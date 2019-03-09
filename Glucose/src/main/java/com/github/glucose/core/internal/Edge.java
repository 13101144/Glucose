package com.github.glucose.core.internal;

public class Edge<T> {
	
	private Vertex<T> from = null;
    private Vertex<T> to = null;
    
    public Edge(Vertex<T> from, Vertex<T> to) {
        if (from == null || to == null)
            throw (new NullPointerException("Both 'to' and 'from' vertices need to be non-NULL."));
        this.from = from;
        this.to = to;
    }

    public Edge(Edge<T> e) {
        this(e.from, e.to);
    }

    public Vertex<T> getFromVertex() {
        return from;
    }

    public Vertex<T> getToVertex() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int hashcode = ((this.getFromVertex().hashCode() * this.getToVertex().hashCode())); 
        return 31 * hashcode;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(Object e1) {
        if (!(e1 instanceof Edge))
            return false;

        final Edge<T> e = (Edge<T>) e1;

        final boolean from = this.from.equals(e.from);
        if (!from)
            return false;

        final boolean to = this.to.equals(e.to);
        if (!to)
            return false;

        return true;
    }

	

}
