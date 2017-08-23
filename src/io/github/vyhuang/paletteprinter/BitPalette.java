package io.github.vyhuang.paletteprinter;

import java.awt.Color;

/**
 * This class represents the RGB palette corresponding to the given intensities of red, blue, and
 * green allowed by the number of bits allocated to each color channel.
 *
 * <p>A BitPalette instance must be given parameters when initialized, is immutable, and contains:
 * <ul>
 * <li>redBits, blueBits, greenBits: the number of bits available in each color channel (max 8).
 * <li>redLevels, greenLevels, blueLevels: the number of unique intensity levels for each color.
 * <li>internalPalette: two-dimensional array spanning all combinations of color intensities.
 * <li>paletteRows, paletteCols: dimensions of the internalPalette of this BitPalette.
 * </ul>
 * </p>
 */
public class BitPalette {
  private char redBits, blueBits, greenBits;
  private int redLevels, greenLevels, blueLevels;
  private Color[][] internalPalette;
  private int paletteRows, paletteCols;

  /**
   * Empty constructor; should only be called within this package.
   */
  protected BitPalette() {}
  /**
   * Parameterized constructor, initializes this instance to hold the RGB palette corresponding to
   * the input array rgbBits.
   *
   * @param rgbBits   contains number of bits assigned to red, green, blue color channels (in order)
   */
  BitPalette(int[] rgbBits) {

    bitCheck(rgbBits);

    redBits     = (char) rgbBits[0];
    greenBits   = (char) rgbBits[1];
    blueBits    = (char) rgbBits[2];

    redLevels   = (int) Math.pow(2,redBits);
    greenLevels = (int) Math.pow(2,greenBits);
    blueLevels  = (int) Math.pow(2,blueBits);

    /** There are |redLevels|*|blueLevels| columns, and |greenLevels| rows */
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

  /**
   * Returns number of bits allocated for the red color channel.
   */
  public int redBits() {
    return redBits;
  }
  /**
   * Returns number of bits allocated for the green color channel.
   */
  public int greenBits() {
    return greenBits;
  }
  /**
   * Returns number of bits allocated for the blue color channel.
   */
  public int blueBits() {
    return blueBits;
  }
  /**
   * Returns number of unique levels possible for the red color channel.
   */
  public int redLevels() {
    return redLevels;
  }
  /**
   * Returns number of unique levels possible for the green color channel.
   */
  public int greenLevels() {
    return greenLevels;
  }
  /**
   * Returns number of unique levels possible for the blue color channel.
   */
  public int blueLevels() {
    return blueLevels;
  }
  /**
   * Returns number of columns in palette (length of a row).
   */
  public int paletteCols() {
    return paletteCols;
  }
  /**
   * Returns number of rows in a palette (height of a column).
   */
  public int paletteRows() {
    return paletteRows;
  }
  /**
   * Retrieves the java.awt.Color object at the specified location in the palette.
   *
   * @param row     the row the desired Color is in
   * @param column  the column the desired Color is in
   */
  public Color getColor(int row, int column) {
    if (row >= paletteRows || column >= paletteCols) {
      System.err.println("getColor() request out of bounds.");
      return null;
    }
    return internalPalette[row][column];
  }
}
