// x = x ^ y

// Enter and print input values :
,.                 enter 'x' value to cell #2 and print
>,.                enter 'y' value to cell #3 and print

// Clear cell #7 for future :
>>>>>[-]

// Copy 'x' value to temp cell #3 :
<<<<<<[            until the cell #1 is not 0    
  >>+              increase cell #3
  <<-              decrease cell #1
]                  check if the cell #1 is 0

// Power of 'x' can be = 0; increment x :
+

// Multiplication :
>[                  until the cell #2 (y) is not 0
   >>[-]            clear ( temp ) cell #4
   >[-]             clear ( temp ) cell #5
 
   // Copy 'x' value from cell #1 to the cell #5 :
   <<<<[            until the cell #1 (x) is not 0
     >>>>+          increase cell #5
     <<<<-          decrease cell #1
   ]                check if the cell #1 is 0
 
   // Copy 'x' value into temp cells and summ it :
   >>>>[            until the cell #5 (x) is not 0
  
     <<[            until the cell #3 (temp x) is not 0
       <<+          increase cell #1 (x)
       >>>+         increase cell #6 (x)
       <-           decrease cell #3
     ]              check if the cell #3 is 0
     
     >[             until the cell #4 (new temp x) is not 0
       <+           increase cell #3
       >-           decrease cell #4
     ]              check if the cell #4 is 0

     >-             decrease cell #5
   ]                check if the cell #5 is 0
  
  // Decrease (counter; power; 'y') :    
  <<<-              
]                   check if the cell #2 (power or 'y') is 0

// Clear cell #3 :
>[-]

// Move saved 'y' (cell #7) value to cell #2 :
>>>>[             until the cell #7 is not 0
  <<<<<+           increase cell #2 (y)
  >>>>>-           decrease cell #7
]                  check if the cell #7 is 0

// Print 'x' value :
<<<<<.>.

