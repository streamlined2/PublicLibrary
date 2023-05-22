package com.streamlined.library.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
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

	public Cover(String coverType, String coverSurface) {
		type = Type.valueOf(coverType);
		surface = Surface.valueOf(coverSurface);
	}

}
