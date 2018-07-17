// x = x plus y

// Enter and print input values :
,.           enter 'x' value to cell #1 and print
>,.          enter 'x' value to cell #2 and print

// Clear temp cell :
>[-]         clear cell #3

// Copy 'y' value to cell #3 and increase cell #1 (summ) :
<[           until the cell #2 is not 0
  >+         increase cell #3 (new y)  
  <<+        increase cell #1 (difference)
  >-         decrease cell #2 (y)
]            check if the cell #2 is 0

// Copy 'y' value to cell #2 :
>[           until the cell #3 is not 0
  <+         increase cell #2
  >-         decrease cell #3
]            check if the cell #3 is 0

// Print 'x' and 'y' values :
<<.>.
