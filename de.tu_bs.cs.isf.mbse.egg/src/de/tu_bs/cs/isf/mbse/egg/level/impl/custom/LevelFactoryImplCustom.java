package de.tu_bs.cs.isf.mbse.egg.level.impl.custom;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.impl.LevelFactoryImpl;

public class LevelFactoryImplCustom extends LevelFactoryImpl {

	public LevelFactoryImplCustom() {
		super();
	}

	@Override
	public Level createLevel() {
		return new LevelImplCustom();
	}
	
}
