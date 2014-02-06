Hi,

I have implemented the LeftWalker class with the specified algorithm in the assignment brief, but I found it had a number of flaws in it and would often get into infinite loops or there would be mazes which it wouldn't be able to solve due to the target being in an "island" of a sort.

Instead of trying to fix it up, because that would require a number of special cases / conditions and would make the code messy, I have implemented a TremauxWalker which uses a different maze solving algorithm. It is documented / explains what it does inside the class. This algorithm will solve any and all mazes that are actually solvable (ie. the target isn't blocked off completely by walls) and will only go across a path at a max of 2 times - meaning if there is no possible solution, it will return to the start of the maze and stay there. This also helps to make it clear when it is actually trying to solve the maze and when it knows that there is no solution and has stopped.

To use the TremauxWalker, pass the argument "-tremauxw" to the main(String[]) method. I have also changed the default walker to the TremauxWalker as it is much better than the LeftWalker.

Thanks,
Henry