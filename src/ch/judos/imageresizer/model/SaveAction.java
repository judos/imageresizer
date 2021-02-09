package ch.judos.imageresizer.model;

public enum SaveAction {
	OVERWRITE, APPEND_SIZE, CHOOSE_FOLDER, CHOOSE_ALL;

	public static SaveAction fromString(String str) {
		for (SaveAction s : values()) {
			if (s.name().equalsIgnoreCase(str))
				return s;
		}
		return null;
	}
}
