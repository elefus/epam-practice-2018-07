x=xx


+++++ +++++  x(cell #0) = 10
>[+]         y(cell #1) = 0
>[+]         counter0(cell #2) = 0
>[+]         counter1(cell #3) = 0
<<<
[            while(x)
  >+         y add 1
  >+         counter0 add 1
  <<-        x sub 1
]            after this y = x
>>
[            while(counter0)
  <
  [          while(y)
  <+         x add 1
  >>>+       counter1 add 1
  <<-        y sub 1
  ]
  >>
  [          while(counter1)
    <<+      y add 1
    >>-      counter1 sub 1
  ]
  <-         counter0 sub 1
]<<.