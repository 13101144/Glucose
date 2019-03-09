package com.github.glucose.util;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class ExceptionCollector {
	
private LinkedHashSet<Throwable> throwables;
    
    public void addMultiException(MultiException me) {
        if (me == null) return;
        if (throwables == null) throwables = new LinkedHashSet<Throwable>();
        
        throwables.addAll(me.getErrors());
    }
    
    /**
     * Adds a throwable to the list of throwables in this collector
     * @param th The throwable to add to the list
     */
    public void addThrowable(Throwable th) {
        if (th == null) return;
        if (throwables == null) throwables = new LinkedHashSet<Throwable>();
        
        if (th instanceof MultiException) {
            throwables.addAll(((MultiException) th).getErrors());
        }
        else {
          throwables.add(th);
        }
    }
    
    /**
     * This method will throw if the list of throwables associated with this
     * collector is not empty
     * 
     * @throws MultiException An exception with all the throwables found in this collector
     */
    public void throwIfErrors() throws MultiException {
        if (throwables == null || throwables.isEmpty()) return;
        
        throw new MultiException(new LinkedList<Throwable>(throwables));
    }
    
    /**
     * Returns true if this collector has errors
     * 
     * @return true if the collector has errors
     */
    public boolean hasErrors() {
        return ((throwables != null) && (!throwables.isEmpty()));
    }

}
