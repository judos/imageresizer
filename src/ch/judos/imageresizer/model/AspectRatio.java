package ch.judos.imageresizer.model;

public enum AspectRatio {
	FIT_INTO, FIT_ONTO, FORCE;

	public static AspectRatio fromString(String str) {
		for (AspectRatio s : values()) {
			if (s.name().equalsIgnoreCase(str))
				return s;
		}
		return null;
	}
}
