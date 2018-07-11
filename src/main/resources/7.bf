// x = x / y
// Create decimal value 48 (zero code) in the cell #1 : (for test)
>++++++++
[
  <++++++
  >-
]
// Enter and print input values :
,.                 enter 'x' value to cell #2 and print
>,.                enter 'x' value to cell #3 and print
// Decimal translation (code of number 48 (zero code)) :
<<[                until the cell #1 is not 0
  >-               decrease cell #2
  >-               decrease cell #3
  <<-              decrease cell #1
]                  check if the cell #1 is 0
// Move 'x' value to cell #1 :
>[                 until the cell #2 is not 0
  <+               increase cell #1 ('x' now)
  >-               decrease cell #2 ('y' now)
]                  check if the cell #2 is 0
// Move 'y' value to cell #2 :
>[                 until the cell #3 is not 0
  <+               increase cell #2
  >-               decrease cell #3
]                  check if the cell #3 is 0
// Clear temp cells (#3; 4; 5; 6) :
[-]                clear cell #3
>[-]               clear cell #4
>[-]               clear cell #5
>[-]               clear cell #6
// Copy 'x' value to cell #3 :
<<<<<[            until the cell #1 (x) is not 0
  >>+             increase cell #3
  <<-             decrease cell #1
]                 check if the cell #1 is 0
// Division :
>>[               until the cell #3 (temp x) is not 0
 
  <[              until the cell #2 (y) is not 0
    >>+           increase cell #4
    >+            increase cell #5
    <<<-          decrease cell #2
  ]               check if the cell #2 is 0
 
  >>>[            until the cell #5 (y) is not 0
     <<<+         increase cell #2
     >>>-         decrease cell #5
  ]               check if the cell #5 is 0
   
  <[              until the cell #4 is not 0
     
    >+            increase cell #5
    <<-[          until the cell #3 is not 0
      >>[-]       clear cell #5
      >+          increase cell #6
      <<<-        decrease cell #3
    ]             check if the cell #3 is 0
     
    >>>[          until the cell #6 is not 0
      <<<+        increase cell #3
      >>>-        decrease cell #6
    ]             check if the cell #6 is 0
     
    <[            until the cell #5 is not 0
      <-[         until the cell #4 is not 0
        <<<-      increase cell #1 (x)
        >>>[-]    decrease cell #4
      ]           check if the cell #4 is 0
      +           increase cell #4
      
      >-          decrease cell #5  
    ]             check if the cell #5 is 0
     
    <-            decrease cell #4
  ]
  
  <<<+            increase cell #1
  >>              go to cell #3
]
// Print 'x' value :
<<.>.