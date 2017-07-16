package org.agorava.function;

import org.agorava.api.function.LongIdentifiable;
import org.agorava.api.function.Nameable;

public abstract class HasNameAndLongId implements Nameable, LongIdentifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8310575552574786544L;

	private final String name;
	private final long id;
	
	protected HasNameAndLongId(String name, long id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public long getId() {
		return id;
	}
}
