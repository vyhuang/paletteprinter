A program that creates a color grid.

Functionality, in order of implementation:  
- *Image Creation*
  1. Create completely blue image of 50x50 pixels
- *Bit Palette Creation*
  1. Create a rgb111 image strip, with each segment being 50x50 pixels
  2. Create a rgb111 image grid, with each cell being 50x50 pixels
  3. Create a rgb222 image grid, with each cell being 50x50 pixels
  4. Create a image grid with user-allocated rgb bits, with each cell being 50x50 pixels
- *Custom Palette Creation*
  1. Read in a simple input file and create an *mXn* image grid of its values, where *m* is the
  number of rows and *n* is the number of columns.
  2. Order this image grid according to the following specifications, allowing for blank spaces:
    * Y-axis position is determined by H from the HSL color scheme
    * X-axis position is determined by L from the HSL color scheme
  3. Order this image grid according to the following specifications, allowing for blank spaces:
    * Y-axis position is determined by H from the CIELCH color scheme
    * X-axis position is determined by V from the CIELCH color scheme


