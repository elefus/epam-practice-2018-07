// x = y

// Enter and print input values :
,.           enter 'x' value to cell #1 and print
>,.          enter 'x' value to cell #2 and print

// Clear cells (cell #1 and temp cell):
<[-]         clear cell #1 (x)
>>[-]        clear temp cell

// Copy 'y' value to temp cell and 'x' cell :
<[           until the cell #2 is not 0
  <+         increase cell #1
  >>+        increase temp cell
  <-         decrease cell #2
]            check if the cell #2 is 0

// Copy 'y' value from temp cell to cell #2 :
>[           until the temp cell is not 0
  <+         increase cell #2 (y)
  >-         decrease temp cell
]            check if the temp cell is 0

// Print 'x' and 'y' values :
<<.>.

