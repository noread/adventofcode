package nl.remcoder.adventofcode.day18;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part1 {
    public static void main(String[] args) throws Exception {
        List<String> operations =
                Files.readAllLines(Paths.get(ClassLoader.getSystemResource("day18/input").toURI()));

        Map<String, Long> registers = new HashMap<>();

        registers.put("a", 0L);
        registers.put("b", 0L);
        registers.put("f", 0L);
        registers.put("i", 0L);
        registers.put("p", 0L);

        int counter = 0;

        long sndFrequency = 0;

        boolean stop = false;

        while (!stop) {
            System.out.println(registers);

            String operation = operations.get(counter);

            String[] operationParts = operation.split(" ");

            switch (operationParts[0]) {
                case "snd":
                    sndFrequency = registers.get(operationParts[1]);
                    counter++;
                    break;
                case "set":
                    long value;
                    try {
                        value = Long.parseLong(operationParts[2]);
                    } catch (NumberFormatException e) {
                        value = registers.get(operationParts[2]);
                    }
                    registers.put(operationParts[1], value);
                    counter++;
                    break;
                case "add":
                    long add;
                    try {
                        add = Long.parseLong(operationParts[2]);
                    } catch (NumberFormatException e) {
                        add = registers.get(operationParts[2]);
                    }
                    registers.put(operationParts[1], registers.get(operationParts[1]) +
                            add);
                    counter++;
                    break;
                case "mul":
                    registers.put(operationParts[1], registers.get(operationParts[1]) * Long.parseLong(operationParts[2]));
                    counter++;
                    break;
                case "mod":
                    long modulo;
                    try {
                        modulo = Long.parseLong(operationParts[2]);
                    } catch (NumberFormatException e) {
                        modulo = registers.get(operationParts[2]);
                    }
                    registers.put(operationParts[1], registers.get(operationParts[1]) %
                            modulo);
                    counter++;
                    break;
                case "rcv":
                    if (registers.get(operationParts[1]) != 0) {
                        stop = true;
                    } else {
                        counter++;
                    }
                    break;
                case "jgz":
                    if (registers.get(operationParts[1]) != 0) {
                        counter += Long.parseLong(operationParts[2]);
                    } else {
                        counter++;
                    }
                    break;
            }
        }

        System.out.println(sndFrequency);
    }
}
