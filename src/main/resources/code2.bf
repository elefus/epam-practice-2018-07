x=x plus y


+++++ +++++  x(cell #0) = 10
>+++++       y(cell #1) = 5
>[+]         counter(cell #2) = 0
<
[            while(y)
  >+         counter add 1
  <<+        x add 1
  >-         y sub 1
]
>
[            while(counter)
  <+         y add 1
  >-         counter sub 1
]