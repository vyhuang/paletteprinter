package io.github.vyhuang.paletteprinter;

import java.awt.Color;

public abstract class Palette {
  protected int[][] internalPalette;
  private String initError = "Invalid operation, please call initializePallete() first.";

  public void initializePalette() {}

  public int paletteColumns() {
    if (internalPalette == null) {
      System.err.println(initError);
      return -1;
    }
    return internalPalette[0].length;
  }

  public int paletteRows() {
    if (internalPalette == null) {
      System.err.println(initError);
      return -1;
    }
    return internalPalette.length;
  }

  public Color getColor(int row, int column) {
    if (internalPalette == null) {
      System.err.println(initError);
      return null;
    }
    return new Color(internalPalette[row][column]); 
  }
}
