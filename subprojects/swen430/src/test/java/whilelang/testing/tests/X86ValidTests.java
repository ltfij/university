package whilelang.testing.tests;

import org.junit.Ignore;
import org.junit.Test;

import whilelang.testing.TestHarness;

public class X86ValidTests extends TestHarness {

    public X86ValidTests() {
        super("tests/valid", "tests/valid", "sysout");
    }

    @Test
    public void BoolAssign_Valid_1() {
        runX86Test("BoolAssign_Valid_1");
    }

    @Test
    public void BoolAssign_Valid_2() {
        runX86Test("BoolAssign_Valid_2");
    }

    @Test
    public void BoolAssign_Valid_3() {
        runX86Test("BoolAssign_Valid_3");
    }

    @Test
    public void BoolAssign_Valid_4() {
        runX86Test("BoolAssign_Valid_4");
    }

    @Test
    public void BoolIfElse_Valid_1() {
        runX86Test("BoolIfElse_Valid_1");
    }

    @Test
    public void BoolIfElse_Valid_2() {
        runX86Test("BoolIfElse_Valid_2");
    }

    @Test
    public void BoolList_Valid_1() {
        runX86Test("BoolList_Valid_1");
    }

    @Test
    public void BoolList_Valid_2() {
        runX86Test("BoolList_Valid_2");
    }

    @Test
    public void BoolRecord_Valid_1() {
        runX86Test("BoolRecord_Valid_1");
    }

    @Test
    public void BoolRecord_Valid_2() {
        runX86Test("BoolRecord_Valid_2");
    }

    @Test
    public void BoolReturn_Valid_1() {
        runX86Test("BoolReturn_Valid_1");
    }

    @Test
    public void Cast_Valid_1() {
        runX86Test("Cast_Valid_1");
    }

    @Test
    public void Cast_Valid_2() {
        runX86Test("Cast_Valid_2");
    }

    @Test
    public void Cast_Valid_3() {
        runX86Test("Cast_Valid_3");
    }

    @Test
    public void Cast_Valid_4() {
        runX86Test("Cast_Valid_4");
    }

    @Ignore("List Append")
    @Test
    public void Char_Valid_1() {
        runX86Test("Char_Valid_1");
    }

    @Test
    public void Char_Valid_2() {
        runX86Test("Char_Valid_2");
    }

    @Test
    public void Char_Valid_3() {
        runX86Test("Char_Valid_3");
    }

    @Test
    public void Const_Valid_1() {
        runX86Test("Const_Valid_1");
    }

    @Test
    public void Const_Valid_2() {
        runX86Test("Const_Valid_2");
    }

    @Test
    public void Const_Valid_3() {
        runX86Test("Const_Valid_3");
    }

    @Test
    public void Const_Valid_4() {
        runX86Test("Const_Valid_4");
    }

    @Test
    public void Define_Valid_1() {
        runX86Test("Define_Valid_1");
    }

    @Test
    public void Define_Valid_2() {
        runX86Test("Define_Valid_2");
    }

    @Test
    public void Function_Valid_1() {
        runX86Test("Function_Valid_1");
    }

    @Test
    public void Function_Valid_2() {
        runX86Test("Function_Valid_2");
    }

    @Test
    public void Function_Valid_4() {
        runX86Test("Function_Valid_4");
    }

    @Test
    public void IfElse_Valid_1() {
        runX86Test("IfElse_Valid_1");
    }

    @Test
    public void IfElse_Valid_2() {
        runX86Test("IfElse_Valid_2");
    }

    @Test
    public void IfElse_Valid_3() {
        runX86Test("IfElse_Valid_3");
    }

    @Test
    public void IfElse_Valid_5() {
        runX86Test("IfElse_Valid_5");
    }

    @Test
    public void IfElse_Valid_6() {
        runX86Test("IfElse_Valid_6");
    }

    @Test
    public void IfElse_Valid_7() {
        runX86Test("IfElse_Valid_7");
    }

    @Test
    public void IfElse_Valid_8() {
        runX86Test("IfElse_Valid_8");
    }

    @Test
    public void IntDefine_Valid_1() {
        runX86Test("IntDefine_Valid_1");
    }

    @Test
    public void IntDiv_Valid_1() {
        runX86Test("IntDiv_Valid_1");
    }

    @Test
    public void IntDiv_Valid_2() {
        runX86Test("IntDiv_Valid_2");
    }

    @Test
    public void IntEquals_Valid_1() {
        runX86Test("IntEquals_Valid_1");
    }

    @Test
    public void IntMul_Valid_1() {
        runX86Test("IntMul_Valid_1");
    }

    @Test
    public void LengthOf_Valid_1() {
        runX86Test("LengthOf_Valid_1");
    }

    @Test
    public void LengthOf_Valid_5() {
        runX86Test("LengthOf_Valid_5");
    }

    @Test
    public void ListAccess_Valid_1() {
        runX86Test("ListAccess_Valid_1");
    }

    @Test
    public void ListAccess_Valid_3() {
        runX86Test("ListAccess_Valid_3");
    }

    @Test
    public void ListAccess_Valid_4() {
        runX86Test("ListAccess_Valid_4");
    }

    @Test
    public void ListAppend_Valid_1() {
        runX86Test("ListAppend_Valid_1");
    }

    @Test
    public void ListAppend_Valid_2() {
        runX86Test("ListAppend_Valid_2");
    }

    @Test
    public void ListAppend_Valid_3() {
        runX86Test("ListAppend_Valid_3");
    }

    @Ignore("unions")
    @Test
    public void ListAppend_Valid_4() {
        runX86Test("ListAppend_Valid_4");
    }

    @Test
    public void ListAppend_Valid_5() {
        runX86Test("ListAppend_Valid_5");
    }

    @Test
    public void ListAppend_Valid_6() {
        runX86Test("ListAppend_Valid_6");
    }

    @Test
    public void ListAppend_Valid_7() {
        runX86Test("ListAppend_Valid_7");
    }

    @Test
    public void ListAssign_Valid_1() {
        runX86Test("ListAssign_Valid_1");
    }

    @Ignore("unions")
    @Test
    public void ListAssign_Valid_10() {
        runX86Test("ListAssign_Valid_10");
    }

    @Test
    public void ListAssign_Valid_2() {
        runX86Test("ListAssign_Valid_2");
    }

    @Test
    public void ListAssign_Valid_3() {
        runX86Test("ListAssign_Valid_3");
    }

    @Test
    public void ListAssign_Valid_4() {
        runX86Test("ListAssign_Valid_4");
    }

    @Test
    public void ListAssign_Valid_5() {
        runX86Test("ListAssign_Valid_5");
    }

    @Test
    public void ListAssign_Valid_6() {
        runX86Test("ListAssign_Valid_6");
    }

    @Test
    public void ListConversion_Valid_1() {
        runX86Test("ListConversion_Valid_1");
    }

    @Test
    public void ListEmpty_Valid_1() {
        runX86Test("ListEmpty_Valid_1");
    }

    @Test
    public void ListEquals_Valid_1() {
        runX86Test("ListEquals_Valid_1");
    }

    @Test
    public void ListGenerator_Valid_1() {
        runX86Test("ListGenerator_Valid_1");
    }

    @Test
    public void ListGenerator_Valid_2() {
        runX86Test("ListGenerator_Valid_2");
    }

    @Test
    public void ListGenerator_Valid_3() {
        runX86Test("ListGenerator_Valid_3");
    }

    @Test
    public void ListLength_Valid_1() {
        runX86Test("ListLength_Valid_1");
    }

    @Test
    public void ListLength_Valid_2() {
        runX86Test("ListLength_Valid_2");
    }

    @Ignore("comments")
    @Test
    public void MultiLineComment_Valid_1() {
        runX86Test("MultiLineComment_Valid_1");
    }

    @Ignore("comments")
    @Test
    public void MultiLineComment_Valid_2() {
        runX86Test("MultiLineComment_Valid_2");
    }

    @Test
    public void Print_Valid_1() { runX86Test("Print_Valid_1"); }

    @Test
    public void Print_Valid_10() { runX86Test("Print_Valid_10"); }

    @Test
    public void Print_Valid_11() { runX86Test("Print_Valid_11"); }

    @Test
    public void Print_Valid_12() { runX86Test("Print_Valid_12"); }

    @Test
    public void Print_Valid_13() { runX86Test("Print_Valid_13"); }

    @Test
    public void Print_Valid_2() { runX86Test("Print_Valid_2"); }

    @Test
    public void Print_Valid_3() { runX86Test("Print_Valid_3"); }

    @Test
    public void Print_Valid_4() { runX86Test("Print_Valid_4"); }

    @Test
    public void Print_Valid_5() { runX86Test("Print_Valid_5"); }

    @Test
    public void Print_Valid_6() { runX86Test("Print_Valid_6"); }

    @Test
    public void Print_Valid_7() { runX86Test("Print_Valid_7"); }

    @Test
    public void Print_Valid_8() { runX86Test("Print_Valid_8"); }

    @Test
    public void Print_Valid_9() { runX86Test("Print_Valid_9"); }

    @Test
    public void RealDiv_Valid_1() {
        runX86Test("RealDiv_Valid_1");
    }

    @Test
    public void RealDiv_Valid_3() {
        runX86Test("RealDiv_Valid_3");
    }

    @Test
    public void RealDiv_Valid_4() {
        runX86Test("RealDiv_Valid_4");
    }

    @Test
    public void RealDiv_Valid_5() {
        runX86Test("RealDiv_Valid_5");
    }

    @Test
    public void RealNeg_Valid_1() {
        runX86Test("RealNeg_Valid_1");
    }

    @Test
    public void RealSub_Valid_1() {
        runX86Test("RealSub_Valid_1");
    }

    @Test
    public void RealSub_Valid_2() {
        runX86Test("RealSub_Valid_2");
    }

    @Test
    public void Real_Valid_1() {
        runX86Test("Real_Valid_1");
    }

    @Test
    public void RecordAccess_Valid_2() {
        runX86Test("RecordAccess_Valid_2");
    }

    @Test
    public void RecordAssign_Valid_1() {
        runX86Test("RecordAssign_Valid_1");
    }

    @Test
    public void RecordAssign_Valid_2() {
        runX86Test("RecordAssign_Valid_2");
    }

    @Test
    public void RecordAssign_Valid_3() {
        runX86Test("RecordAssign_Valid_3");
    }

    @Test
    public void RecordAssign_Valid_4() {
        runX86Test("RecordAssign_Valid_4");
    }

    @Test
    public void RecordAssign_Valid_5() {
        runX86Test("RecordAssign_Valid_5");
    }

    @Test
    public void RecordAssign_Valid_6() {
        runX86Test("RecordAssign_Valid_6");
    }

    @Test
    public void RecordDefine_Valid_1() {
        runX86Test("RecordDefine_Valid_1");
    }

    @Ignore("unknown")
    @Test
    public void Remainder_Valid_1() {
        runX86Test("Remainder_Valid_1");
    }

    @Ignore("comments")
    @Test
    public void SingleLineComment_Valid_1() {
        runX86Test("SingleLineComment_Valid_1");
    }

    @Test
    public void StringAssign_Valid_1() { runX86Test("StringAssign_Valid_1"); }

    @Test
    public void StringAssign_Valid_10() { runX86Test("StringAssign_Valid_10"); }

    @Ignore("TODO: TEMP - LOOPING")
    @Test
    public void StringAssign_Valid_11() { runX86Test("StringAssign_Valid_11"); }

    @Test
    public void StringAssign_Valid_2() { runX86Test("StringAssign_Valid_2"); }

    @Test
    public void StringAssign_Valid_4() { runX86Test("StringAssign_Valid_4"); }

    @Test
    public void StringAssign_Valid_5() { runX86Test("StringAssign_Valid_5"); }

    @Test
    public void StringAssign_Valid_6() { runX86Test("StringAssign_Valid_6"); }

    @Test
    public void StringAssign_Valid_7() { runX86Test("StringAssign_Valid_7"); }

    @Test
    public void StringAssign_Valid_8() { runX86Test("StringAssign_Valid_8"); }

    @Test
    public void StringAssign_Valid_9() { runX86Test("StringAssign_Valid_9"); }

    @Test
    public void String_Valid_1() {
        runX86Test("String_Valid_1");
    }

    @Ignore("TODO: TEMP - LOOPING")
    @Test
    public void String_Valid_2() {
        runX86Test("String_Valid_2");
    }

    @Test
    public void String_Valid_3() {
        runX86Test("String_Valid_3");
    }

    @Ignore("unions")
    @Test
    public void String_Valid_4() {
        runX86Test("String_Valid_4");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_1() {
        runX86Test("Switch_Valid_1");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_2() {
        runX86Test("Switch_Valid_2");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_3() {
        runX86Test("Switch_Valid_3");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_4() {
        runX86Test("Switch_Valid_4");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_6() {
        runX86Test("Switch_Valid_6");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_7() {
        runX86Test("Switch_Valid_7");
    }

    @Ignore("switch")
    @Test
    public void Switch_Valid_8() {
        runX86Test("Switch_Valid_8");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_11_RuntimeTest() {
        runX86Test("TypeEquals_Valid_11");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_14_RuntimeTest() {
        runX86Test("TypeEquals_Valid_14");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_16_RuntimeTest() {
        runX86Test("TypeEquals_Valid_16");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_1_RuntimeTest() {
        runX86Test("TypeEquals_Valid_1");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_20_RuntimeTest() {
        runX86Test("TypeEquals_Valid_20");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_2_RuntimeTest() {
        runX86Test("TypeEquals_Valid_2");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_5_RuntimeTest() {
        runX86Test("TypeEquals_Valid_5");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_8_RuntimeTest() {
        runX86Test("TypeEquals_Valid_8");
    }

    @Ignore("unions")
    @Test
    public void TypeEquals_Valid_9_RuntimeTest() {
        runX86Test("TypeEquals_Valid_9");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_1() {
        runX86Test("UnionType_Valid_1");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_2() {
        runX86Test("UnionType_Valid_2");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_4() {
        runX86Test("UnionType_Valid_4");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_5() {
        runX86Test("UnionType_Valid_5");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_6() {
        runX86Test("UnionType_Valid_6");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_7() {
        runX86Test("UnionType_Valid_7");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_8() {
        runX86Test("UnionType_Valid_8");
    }

    @Ignore("unions")
    @Test
    public void UnionType_Valid_9() {
        runX86Test("UnionType_Valid_9");
    }

    @Test
    public void While_Valid_1() {
        runX86Test("While_Valid_1");
    }

    @Test
    public void While_Valid_2() {
        runX86Test("While_Valid_2");
    }

    @Test
    public void While_Valid_4() {
        runX86Test("While_Valid_4");
    }

    @Test
    public void While_Valid_6() {
        runX86Test("While_Valid_6");
    }
}
