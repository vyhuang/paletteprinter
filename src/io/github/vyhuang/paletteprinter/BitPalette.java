package io.github.vyhuang.paletteprinter;

import java.awt.Color;

public class BitPalette {
  int cellWidth, cellHeight;
  char redBits, blueBits, greenBits;
  // top right corner has coordinates <0,0>
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
        //System.out.println(rgbBits[i]);
        throw new IllegalArgumentException("Each color channel must have one to eight bits.");
      }
    }
    redBits     = rgbBits[0];
    blueBits    = rgbBits[1];
    greenBits   = rgbBits[2];

    paletteInit(redBits, greenBits, blueBits);
  }

  private void paletteInit(char rB, char gB, char bB) {
    // (2^redBits)(2^blueBits) columns; (2^greenBits) rows
    int paletteRows, paletteCols;
    int redLevels, blueLevels;
    redLevels = (int) Math.pow(2,rB);
    blueLevels = (int) Math.pow(2,bB);
    paletteRows = (int) Math.pow(2,gB);
    paletteCols = redLevels * blueLevels;
    internalPalette = new Color[paletteRows][paletteCols];

    double redIncrement, greenIncrement, blueIncrement;
    redIncrement = incrementCalc(rB);
    greenIncrement = incrementCalc(rB);
    blueIncrement = incrementCalc(rB);

    double red, green, blue;
    red = green = blue = 0;
    
    // each row depends on green values, so start there.
    for (int i = 0; i < paletteRows; i += 1) {
      blue = 0;
      for (int j = 0; j < blueLevels; j += 1) {
        red = 0;
        for (int k = 0; k < redLevels; k += 1) {
          //System.out.println((int)red + "\t" + (int)green + "\t" + (int)blue);
          internalPalette[i][(j*redLevels) + k] = new Color((int)red,(int)green,(int)blue);
          red += redIncrement;
        }
        blue += blueIncrement;
      }
      green += greenIncrement;
    }
  }
  private double incrementCalc(char bits) {
    double numLevels = Math.pow(2,bits);
    return 255/(numLevels-1);
  }

  public int getCellWidth() {
    return cellWidth;
  }
  public int getCellHeight() {
    return cellHeight;
  }
  public int redBitCount() {
    return redBits;
  }
  public int greenBitCount() {
    return greenBits;
  }
  public int blueBitCount() {
    return blueBits;
  }
  public Color getColor(int x, int y) {
    return internalPalette[x][y];
  }



}
