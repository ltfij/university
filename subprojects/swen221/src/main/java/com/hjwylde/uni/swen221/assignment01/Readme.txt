Hi,

For this assignment, I found it slightly ambiguous as to "how far to go" with making our parser match the "matchWithLib" function. In the documentation it clearly states what I have supported (a few extra things than was required in the assignment brief, the main ones being the '?' quantifier and reluctant and possessive quantifiers: '??', '?+', '*?', '*+', '+?', '++'), but it doesn't say what isn't supported:

I have not allowed for multiple carrots ('^', start line characters), so the match(String, String) method won't match the matchWithLib(String, String) method whenever the regex contains more than one carrot or a carrot not at the start of the regex. The two main reasons for this are:
  1. I did not fully understand how the Java regex library handle the start line character when it is placed in the middle of the regex - after a few tests with different quantifiers for it and multiple characters, I found it confusing and was unsure of what adding in the quantifiers did to it exactly.
  2. In order for implementing the multiple start line characters, it looked like it would require a number of specific conditionals for this feature. I felt this meant that there is a bit of a design flaw in the implementation of this regex grammar, as adding in conditionals for every special character cases in each of the matchStar(String, String), matchPlus(String, String), matchQuestionMark(String, String) methods would mean I was repeating myself and it would make it unclear what the code was specifically trying to do.

Thanks,
Henry