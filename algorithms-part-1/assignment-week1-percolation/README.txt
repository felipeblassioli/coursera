Notable input is data/input20.txt which has an instance that tests for backwash.
To handle backwash cases I got to use 2 union find structures, the first is used to test if a site is connected to
both virtualTop and virtualBottom, the other is connected exclusively to virtualTop.