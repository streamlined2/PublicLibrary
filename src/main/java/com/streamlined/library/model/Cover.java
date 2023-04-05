package com.streamlined.library.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Embeddable
@Data
public class Cover {

	public enum Type {
		HARD, SOFT
	}

	public enum Surface {
		UNCOATED, SILK, GLOSS
	}

	@Enumerated(EnumType.STRING)
	private Type type;

	@Enumerated(EnumType.STRING)
	private Surface surface;

}
