x=x^y


++           x(cell #0) = 2
>+++++       y(cell #1) = 5
>[+]         counter0(cell #2) = 0
>[+]         counter1(cell #3) = 0
>[+]         counter2(cell #4) = 0
<<<<[        while(x)
  >>+
  <<-
]
+
>
[            while(y)
  <
  [          while(x)
    >>>>+
    <<<<-
  ]
  >>>>
  [          while(c2)
    <<
    [        while(c0)
      <<+
      >>>+
      <-
    ]
    >
    [        while(c1)
      <+
      >-
    ]
    >-
  ]
  <<<-
]<.