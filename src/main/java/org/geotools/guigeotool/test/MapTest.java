package org.geotools.guigeotool.test;

import java.io.IOException;

import org.geotools.guigeotool.ui.MapUI;

public class MapTest {
	public static void main(String[] args) throws IOException {
		MapUI mapUI = new MapUI();
		mapUI.addControls();
		mapUI.showWindow();
	}
}
