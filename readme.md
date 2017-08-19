# Palette Printer
 Outputs an image grid containing colors of a palette.

## Use
Can take input in the two following forms:  
`java -jar paletteprinter [input.gpl] ?[HSB/CIELCH]`  
* input.gpl is a valid GIMP palette file
* The second (optional) parameter can take on two values:
  - if "HSB", a color's row is determined by its hue, and its column by its brightness
  - if "CIELCH", a color's row is determined by its hue, and it's column by its lightness. If this is absent, create an grid with the number of columns specified in the gpl file

`java -jar paletteprinter [R] [G] [B] ?[-f/-i] ?[input.gpl/output.png]`  
* R,G,B are the number of bits available for the red, green, and blue channels respectively
 The second parameter is optional. If this is absent, create the file "output.png"
 containing an image grid where R increases alongside the y-axis, G increases alongside 
 the x-axis, and B increases alongside the z-axis.
  - if "-f", then create a GIMP palette file with the specified filename where there are R distinct columns, where R is the unique values of the red channel.
  - if "-i", then create the aforementioned PNG file with the specified filename.

## Roadmap
+ *Image Creation*
  1. Create completely blue image of 50x50 pixels.   *done*
- *Bit Palette Display*
  1. Create a rgb111 image strip, with each segment being 50x50 pixels. *done*
  2. Create a rgb111 image grid, with each cell being 50x50 pixels. *done*
  3. Create a rgb222 image grid, with each cell being 50x50 pixels.
  4. Create a image grid with user-allocated rgb bits, with each cell being 50x50 pixels.
  5. Create a .gpl file with every color possible with the user-defined rgb bits, with the number of
  columns being equal to the unique values of the red channel.
- *Custom Palette Display*
  1. Read in a .gpl input file and create an *mXn* image grid of its values, where *m* is the
  number of rows and *n* is the number of columns.
  2. Order this image grid according to the following specifications, allowing for blank spaces:
      * Y-axis position is determined by H from the HSV color scheme
      * X-axis position is determined by V from the HSV color scheme
  3. Order this image grid according to the following specifications, allowing for blank spaces:
      * Y-axis position is determined by H from the CIELCH color scheme
      * X-axis position is determined by L from the CIELCH color scheme


