package io.github.vyhuang.paletteprinter;

import java.awt.Color;

public abstract class Palette {
  protected int[][] internalPalette;

  public abstract void initializePalette();

  public int paletteColumns() {
    if (internalPalette == null) {
      System.err.println("Invalid operation; please call initalizePalette() first.");
    }
    return internalPalette[0].length;
  }

  public int paletteRows() {
    if (internalPalette == null) {
      System.err.println("Invalid operation; please call initalizePalette() first.");
    }
    return internalPalette.length;
  }

  public Color getColor(int row, int column) {
    if (internalPalette == null) {
      System.err.println("Invalid operation; please call initalizePalette() first.");
    }
    return new Color(internalPalette[row][column]); 
  }
}
