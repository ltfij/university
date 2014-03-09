package com.hjwylde.uni.swen222.assignment02.cluedo.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.hjwylde.uni.swen222.assignment02.cluedo.test.game.BoardTests;
import com.hjwylde.uni.swen222.assignment02.cluedo.test.game.NotebookTests;
import com.hjwylde.uni.swen222.assignment02.cluedo.test.game.PlayerTests;

/**
 * Test suite. This test suite is by no means complete! Most testing was done through black box
 * testing and playing the game (ensuring corner cases etc.).
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 10/08/2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BoardTests.class, NotebookTests.class, PlayerTests.class})
public class Tests {}
