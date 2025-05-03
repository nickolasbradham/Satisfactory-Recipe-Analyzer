package nbradham.satisAnalize.sources;

import nbradham.satisAnalize.Item;

public final class SourcePointer implements Source {

	private Source src;

	@Override
	public float getWeight(Item targetOutput) {
		return getSource().getWeight(targetOutput);
	}

	public Source getSource() {
		return src;
	}

	public void setSource(Source source) {
		src = source;
	}

}