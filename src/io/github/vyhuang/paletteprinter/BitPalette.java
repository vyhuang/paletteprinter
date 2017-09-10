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
public class BitPalette extends Palette {
  //protected int[][] internalPalette
  private byte[]    rgbBits;

 /**
   * Parameterized constructor, initializes this instance to hold the RGB palette corresponding to
   * the input array rgbBits.
   *
   * @param rgbBits   contains number of bits assigned to red, green, blue color channels (in order)
   */
  BitPalette(int[] rgbBits) {

    this.rgbBits = new byte[3];

    this.rgbBits[0]     = (byte) rgbBits[0];
    this.rgbBits[1]     = (byte) rgbBits[1];
    this.rgbBits[2]     = (byte) rgbBits[2];

    bitCheck();
  }

  private void bitCheck() {
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

  @Override
  public void initializePalette() {
    internalPalette = new int[greenLevels()][redLevels() * blueLevels()];

    double redIncrement, greenIncrement, blueIncrement;
    redIncrement = incrementCalc(redBits());
    greenIncrement = incrementCalc(greenBits());
    blueIncrement = incrementCalc(blueBits());

    double red, green, blue;
    int intRed, intGreen, intBlue;
    red = green = blue = 0;
    intRed = intGreen = intBlue = 0;
    
    // each row depends on green values, so start there.
    for (int i = 0; i < greenLevels(); i += 1) {
      blue = 0;
      for (int j = 0; j < blueLevels(); j += 1) {
        red = 0;
        for (int k = 0; k < redLevels(); k += 1) {
          //System.out.println((int)red + "\t" + (int)green + "\t" + (int)blue);
          intRed = (int) Math.round(red);
          intGreen = (int) Math.round(green);
          intBlue = (int) Math.round(blue);
          internalPalette[i][(j*redLevels()) + k] = (intRed << 16) + (intGreen << 8) + intBlue;
          red += redIncrement;
        }
        blue += blueIncrement;
      }
      green += greenIncrement;
    }
  }
  private double incrementCalc(int bits) {
    double numLevels = Math.pow(2,bits);
    return 255/(numLevels-1);
  }

  /**
   * Returns number of bits allocated for the red color channel.
   */
  public int redBits() {
    return rgbBits[0];
  }
  /**
   * Returns number of bits allocated for the green color channel.
   */
  public int greenBits() {
    return rgbBits[1];
  }
  /**
   * Returns number of bits allocated for the blue color channel.
   */
  public int blueBits() {
    return rgbBits[2];
  }
  /**
   * Returns number of unique levels possible for the red color channel.
   */
  public int redLevels() {
    return (int) Math.pow(2,redBits());
  }
  /**
   * Returns number of unique levels possible for the green color channel.
   */
  public int greenLevels() {
    return (int) Math.pow(2,greenBits());
  }
  /**
   * Returns number of unique levels possible for the blue color channel.
   */
  public int blueLevels() {
    return (int) Math.pow(2,blueBits());
  }

}
