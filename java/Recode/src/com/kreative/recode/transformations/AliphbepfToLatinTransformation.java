package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class AliphbepfToLatinTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "Aliphbepf to Latin";
	}
	
	@Override
	public String getDescription() {
		return "Changes Aliphbepf letters to Latin letters.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int cp) {
		if (cp >= 0xFF3C0 && cp < 0xFF3FF) cp -= 0xFF380;
		append(cp);
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
