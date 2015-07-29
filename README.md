Alternative music notation software. For additional details, see DrTechDaddy.com MUSIC blog

ChromaTonnetz is a shape-note system for the 12-tone Equal Tempered (12-TET) chromatic scale based on Euler's "Tonnetz." It consist of twelve distinct noteheads with 4 distinct orientations and 3 distinct fill patterns arranged in two overlapping (NOT NESTED) cycles, such that every third note (minor third interval) has the same fill and every fourth note (major third interval) has the same orientation. ChromaTonnetz is implemented as a font StaffTonnetz2 in fontStruct: see http://fontstruct.com/fontstructions/show/stafftonnetz2 and as a stencil file in LilyPond.

As a font, StaffTonnetz2 also includes characters for creating rests and staff lines (staff lines running vertically on the page), allowing creation a rudimentary music notation as a TEXT FILE. I have applied this to an alternative notation with a 5-line 7-space staff corresponding to the piano keyboard.  The StaffTonnetz2 font also includes an "alpha" version of dozenal digits using variants of X and E glyphs for ten and eleven.

The ChromaTonnetz system for creating sheet music is as follows:
1. create a "piano-roll" representation in a spreadsheet, with pitches in successive columns right-to-left and time running dowo the page, each note event being on a separate line.
2. save the "piano-roll" as a .cvs file
3. run the program dtdPr2Htm to convert the .csv file into an XHTML webpage.
4. view the xhtml webpage using the stafftonnetz.css and stafftonnetzfont.css files
   and the stafftonnetz2 font files.
   
   The spreadsheet piano-roll format is designed for a vertical staff with a separate column for each pitch,
   but the font allows for a 2:1 overlap of alternate notes, typically "lines" and "spaces".
   The examples illustrate a "7-5" staff with 7 "spaces" and 5 "lines" imitating the layout of the piano keyboard.
   However, alternative staff designs are possible by placing the staff characters in differeent columns.
   The note glyphs are assigned to the letters A B C E E F G for naturals, d e g a b for flats, c f for sharps.
   In addition I J represent the black keys between C D E and K L H represent the black keys between F G A B.
   However, by reassigning the glyphs (letters) to different columns, one can change the association between glyphs and pitches, for example, one could in this fashion implement "relative do" shape-notes, reassigning the glyphs as appropriate for the key of the piece. (See Jesu example).
   
   There are currently no "time" or duration values associated with the notes.  Timiing is indicated by using a variety of different bar-line variants and spacing notes linearly in time.  A note is held until the next note in that voice occurs.   "Rest"( % ) and "hold" ( " ) symbols are used when this convention is not enough.
   
   Documentation is not yet available at initial release (July 2015), but the author will entertain questions. One may also consult the exmaples or author's blog.
   


