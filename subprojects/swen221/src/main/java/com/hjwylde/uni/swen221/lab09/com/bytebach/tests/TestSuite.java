package com.hjwylde.uni.swen221.lab09.com.bytebach.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.hjwylde.uni.swen221.lab09.com.bytebach.tests.database.MyDatabaseTests;
import com.hjwylde.uni.swen221.lab09.com.bytebach.tests.model.ModelTestSuite;
import com.hjwylde.uni.swen221.lab09.com.bytebach.tests.table.TableTestSuite;

/*
 * Code for Laboratory 9, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TableTestSuite.class, MyDatabaseTests.class, ModelTestSuite.class})
public class TestSuite {

}
