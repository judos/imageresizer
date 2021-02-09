package ch.judos.imageresizermodel;

import java.util.Iterator;
import java.util.List;

import ch.judos.imageresizermodel.ProgressIterable.Progress;

public class ProgressIterable<T> implements Iterable<Progress<T>> {

	private T[] arr;

	public ProgressIterable(T[] arr) {
		this.arr = arr;
	}

	@SuppressWarnings("unchecked")
	public ProgressIterable(List<T> list) {
		this((T[]) list.toArray());
	}

	public Iterator<Progress<T>> iterator() {
		return new Iterator<Progress<T>>() {
			int index = 0;

			public boolean hasNext() {
				return index < arr.length;
			}

			public Progress<T> next() {
				float progress = (float) index / arr.length;
				return new Progress<T>(progress, arr[index++]);
			}

			public void remove() {}

		};
	}

	public static class Progress<P> {
		private float progress;
		private P p;

		public Progress(float f, P p) {
			this.progress = f;
			this.p = p;
		}

		public float getProgress() {
			return this.progress;
		}

		public P getObject() {
			return this.p;
		}
	}
}
