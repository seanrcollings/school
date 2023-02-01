import os

i = 1


def test(cmd, expected):
    global i
    stream = os.popen(cmd)
    output = stream.read()
    output = output.strip()
    if output != expected:
        print(
            'Failed test {} ({}). Should have been "{}". Was "{}".'.format(
                i, cmd, expected, output
            )
        )
    else:
        print("Passed test {}.".format(i))
    i = i + 1


# Compile the code
os.system("javac Infix.java")

# Run the tests. The first argument is the command to run
# and the second argument is the expected output of the
# program.
test("java Infix +12", "(1+2)")
test("java Infix -+121", "((1+2)-1)")
test("java Infix -+9-8+127", "((9+(8-(1+2)))-7)")
test("java Infix -+9-81+27", "((9+(8-1))-(2+7))")
test("java Infix +7", "(7+error")
test("java Infix -9+7", "(9-(7+error")
test("java Infix -+9-8+12", "((9+(8-(1+2)))-error")
test("java Infix -+9-a+127", "((9+(error")
test("java Infix -9-8+12+7", "(9-(8-(1+2)))error")
