#!/bin/sh

# GENERATE DEFINITE INVALID FILES

FILES=$(ls *.while)

echo "package wyjc.testing.tests;"
echo ""
echo "import org.junit.*;"
echo "import wyjc.testing.TestHarness;"
echo ""
echo "public class BaseInvalidTests extends TestHarness {"
echo " public BaseInvalidTests() {"
echo "  super(\"tests/base/invalid\",\"tests/base/invalid\",\"sysout\");"
echo " }"
echo ""

for f in $FILES 
do
    nf=$(echo $f | sed -e "s/.whiley//")
    echo -n " @Test public void $nf"
    echo "() { runTest(\"$nf\"); }"
done

echo "}"
 
