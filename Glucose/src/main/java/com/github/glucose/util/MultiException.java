package com.github.glucose.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MultiException extends GlucoseRuntimeException {

	private static final long serialVersionUID = 5184959413000250737L;
	
	private final List<Throwable> throwables = new LinkedList<Throwable>();
	
	public MultiException() {
		super();
	}

	public MultiException(List<Throwable> th) {
		super(th.get(0).getMessage(), th.get(0));

		throwables.addAll(th);
	}

	public MultiException(Throwable th) {
		super(th.getMessage(), th);

		throwables.add(th);
	}

	/**
	 * 获得所有异常
	 */
	public List<Throwable> getErrors() {
		return Collections.unmodifiableList(throwables);
	}

	/**
	 * 增加异常
	 */
	public void addError(Throwable error) {
		throwables.add(error);
	}

	public String getMessage() {
		List<Throwable> listCopy = getErrors();
		StringBuffer sb = new StringBuffer("A MultiException has "
				+ listCopy.size() + " exceptions.  They are:\n");

		int lcv = 1;
		for (Throwable th : listCopy) {
			sb.append(lcv++ + ". " + th.getClass().getName()
					+ ((th.getMessage() != null) ? ": " + th.getMessage() : "")
					+ "\n");
		}

		return sb.toString();
	}

	public void printStackTrace(PrintStream s) {
		List<Throwable> listCopy = getErrors();

		if (listCopy.size() <= 0) {
			super.printStackTrace(s);
			return;
		}

		int lcv = 1;
		for (Throwable th : listCopy) {
			s.println("MultiException stack " + lcv++ + " of "
					+ listCopy.size());
			th.printStackTrace(s);
		}
	}

	public void printStackTrace(PrintWriter s) {
		List<Throwable> listCopy = getErrors();

		if (listCopy.size() <= 0) {
			super.printStackTrace(s);
			return;
		}

		int lcv = 1;
		for (Throwable th : listCopy) {
			s.println("MultiException stack " + lcv++ + " of "
					+ listCopy.size());
			th.printStackTrace(s);
		}
	}

	public String toString() {
		return getMessage();
	}
}
