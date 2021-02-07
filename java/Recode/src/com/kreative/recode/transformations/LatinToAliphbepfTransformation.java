package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class LatinToAliphbepfTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "Latin to Aliphbepf";
	}
	
	@Override
	public String getDescription() {
		return "Changes Latin letters to Aliphbepf letters.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int cp) {
		if ((cp >= 'A' && cp <= 'Z') || (cp >= 'a' && cp <= 'z')) cp += 0xFF380;
		append(cp);
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
