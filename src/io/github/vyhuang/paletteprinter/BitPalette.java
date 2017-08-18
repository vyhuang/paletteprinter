package io.github.vyhuang.paletteprinter;

import java.awt.Color;

public class BitPalette {
  private char redBits, blueBits, greenBits;
  private int redLevels, greenLevels, blueLevels;
  // top left corner has coordinates <0,0>
  private Color[][] internalPalette;
  private int paletteRows, paletteCols;

  BitPalette(int[] rgbBits) {

    bitCheck(rgbBits);

    redBits     = (char) rgbBits[0];
    blueBits    = (char) rgbBits[1];
    greenBits   = (char) rgbBits[2];

    redLevels   = (int) Math.pow(2,redBits);
    blueLevels  = (int) Math.pow(2,blueBits);
    greenLevels = (int) Math.pow(2,greenBits);

    // (2^redBits)(2^blueBits) columns; (2^greenBits) rows
    paletteRows = greenLevels;
    paletteCols = redLevels * blueLevels;

    paletteInit();
  }
  private void bitCheck(int[] rgbBits) {
    String channelColor;
    for (int i = 0; i < rgbBits.length; i += 1) {
      switch (i) {
        case 0:   channelColor = "red";
                  break;
        case 1:   channelColor = "green";
                  break;
        case 2:   channelColor = "blue";
                  break;
        default:  channelColor = "<invalid value>";
                  break;
      }
      if (rgbBits[i] <= 0) {
        System.err.printf("The %s channel cannot have negative or 0 bits. Changing %d to 1.\n",
            channelColor, rgbBits[i]);
        rgbBits[i] = 1;
      } else if (rgbBits[i] > 8) {
        System.err.printf("The %s channel is limited to 8 bits. Changing %d to 8.\n",
            channelColor, rgbBits[i]);
        rgbBits[i] = 8;
      }
    }
  }
  private void paletteInit() {
    internalPalette = new Color[paletteRows][paletteCols];

    double redIncrement, greenIncrement, blueIncrement;
    redIncrement = incrementCalc(redBits);
    greenIncrement = incrementCalc(greenBits);
    blueIncrement = incrementCalc(blueBits);

    double red, green, blue;
    red = green = blue = 0;
    int intRed, intGreen, intBlue;
    intRed = intGreen = intBlue = 0;
    
    // each row depends on green values, so start there.
    for (int i = 0; i < greenLevels; i += 1) {
      blue = 0;
      for (int j = 0; j < blueLevels; j += 1) {
        red = 0;
        for (int k = 0; k < redLevels; k += 1) {
          //System.out.println((int)red + "\t" + (int)green + "\t" + (int)blue);
          intRed = (int) Math.round(red);
          intGreen = (int) Math.round(green);
          intBlue = (int) Math.round(blue);
          internalPalette[i][(j*redLevels) + k] = new Color(intRed, intGreen, intBlue);
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

  // Access methods
  public int redBits() {
    return redBits;
  }
  public int greenBits() {
    return greenBits;
  }
  public int blueBits() {
    return blueBits;
  }
  public int redLevels() {
    return redLevels;
  }
  public int greenLevels() {
    return greenLevels;
  }
  public int blueLevels() {
    return blueLevels;
  }
  public int paletteCols() {
    return paletteCols;
  }
  public int paletteRows() {
    return paletteRows();
  }
  public Color getColor(int x, int y) {
    return internalPalette[x][y];
  }
}
