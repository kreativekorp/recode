package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class LatinToAliphbepfTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Latin to Aliphbepf";
	}
	
	@Override
	public String getDescription() {
		return "Changes Latin letters to Aliphbepf letters.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-toaliphbepf", "--toaliphbepf");
	}
	
	@Override
	public int getArgumentCount() {
		return 0;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList();
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList();
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new LatinToAliphbepfTransformation();
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return null;
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return null;
	}
}
