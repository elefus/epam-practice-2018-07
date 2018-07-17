x=y


[+]          x(cell #0) = 0
>+++++       y(cell #0) = 5
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