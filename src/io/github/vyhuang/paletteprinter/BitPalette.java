package io.github.vyhuang.paletteprinter;

import java.awt.Color;

public class BitPalette {
  int cellWidth, cellHeight;
  char redBits, blueBits, greenBits;
  Color[][] internalPalette;

  BitPalette(int[] cellDim, char[] rgbBits) {
    for (int i = 0; i < cellDim.length; i+= 1) {
      if (cellDim[i] <= 0) {
        throw new IllegalArgumentException("Cell dimensions must be greater than zero.");
      }
    }
    cellWidth   = cellDim[0];
    cellHeight  = cellDim[1];

    for (int i = 0; i < rgbBits.length; i += 1) {
      if (rgbBits[i] <= 0 || rgbBits[i] > 8) {
        throw new IllegalArgumentException("Each color channel must have one to eight bits.");
      }
    }
    redBits     = rgbBits[0];
    blueBits    = rgbBits[1];
    greenBits   = rgbBits[2];

    // (2^redBits)(2^blueBits) columns; (2^greenBits) rows
    int paletteRows, paletteCols;
    paletteRows = (int) Math.pow(2,greenBits);
    paletteCols = ((int) Math.pow(2,redBits))*((int) Math.pow(2,blueBits));
    internalPalette = new Color[paletteRows][paletteCols];
  }
}
