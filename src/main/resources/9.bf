// swap(x;y)
// Enter input value :
,.           enter 'x' value to cell #1 and print
>,.          enter 'y' value to cell #2 and print
// Clear temp cells (#3 and #4) :
>[-]
>[-]
// Copy 'x' value into the cell #3:
<<<[         until the cell #1 is not 0
  >>+        increase cell #3
  <<-        decrease cell #1
]            check if the cell #1 is 0
// Copy 'y' value into the cell #4:
>[           until the cell #2 is not 0
  >>+        increase cell #4
  <<-        decrease cell #2
]            check if the cell #2 is 0
// Copy 'x' value from the cell #3 to the cell #2:
>[           until the cell #3 is not 0
  <+         increase cell #2
  >-         decrease cell #3
]            check if the cell #3 is 0
// Copy 'y' value from the cell #4 to the cell #1:
>[           until the cell #4 is not 0
  <<<+        increase cell #4
  >>>-        decrease cell #2
]            check if the cell #4 is 0
// Print 'x' and 'y' values :
<<<.>.