x=xdivy


+++++ +++++  x(cell #0) = 10
>+++++       y(cell #1) = 5
>[+]         counter0(cell #2) = 0
>[+]         counter1(cell #3) = 0
>[+]         counter2(cell #4) = 0
>[+]         counter3(cell #5) = 0
<<<<<
[            while(x)
  >>+
  <<-
]
>>
[            while(c0)
  <
  [          while(y)
    >>+
    >+
    <<<-
  ]
  >>>
  [          while(c2)
    <<<+
    >>>-
  ]
  <
  [          while(c1)
    >+
    <<-
    [        while(c0)
      >>[-]    while(c2)
      >+
      <<<-
    ]
    >>>
    [        while(c3)
      <<<+
      >>>-
    ]
    <
    [        while(c2)
      <-
      [      while(c1)
        <<<-
        >>>[-]
      ]
      +
    >-
    ]
    <-
  ]
  <<<+
>>
]