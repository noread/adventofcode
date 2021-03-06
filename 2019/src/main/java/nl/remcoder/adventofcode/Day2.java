package nl.remcoder.adventofcode;

import nl.remcoder.adventofcode.intcodecomputer.IntCodeComputer;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day2 {
    public long handlePart1(Stream<String> input) {
        String line = input.findFirst().orElseThrow(AssertionError::new);

        long[] opcodes = Arrays.stream(line.split(","))
                               .mapToLong(Long::parseLong)
                               .toArray();

        opcodes[1] = 12;
        opcodes[2] = 2;

        IntCodeComputer intCodeComputer = new IntCodeComputer(opcodes, null, null);

        intCodeComputer.runProgram();

        return intCodeComputer.retrieveValueFromPosition(0);
    }

    public int handlePart2(Stream<String> input, int desiredResult) {
        String line = input.findFirst()
                           .orElseThrow(AssertionError::new);

        long[] opcodesInput = Arrays.stream(line.split(","))
                                    .mapToLong(Long::parseLong)
                                    .toArray();

        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                long[] opcodes = Arrays.copyOf(opcodesInput, opcodesInput.length);

                opcodes[1] = noun;
                opcodes[2] = verb;

                IntCodeComputer intCodeComputer = new IntCodeComputer(opcodes, null, null);

                intCodeComputer.runProgram();

                if (intCodeComputer.retrieveValueFromPosition(0) == desiredResult) {
                    return (100 * noun) + verb;
                }
            }
        }

        throw new AssertionError();
    }
}