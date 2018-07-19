package stackMachine;

import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Machine {

    public static final int MAX_INT = 1048575;
    private static final int MAX_NUM_OF_SYMBOLS_IN_STRING = 2000;
    private static int codeProcessing(String code) {

        if (code.length() > MAX_NUM_OF_SYMBOLS_IN_STRING) {
            return -1;
        }

        Stack<Integer> stack = new Stack<>();

        int numOfCommands = 1;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == ' ') {
                numOfCommands++;
            }
        }

        String[] instruction = code.split(" ", numOfCommands);

        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("DUP", new Dup());
        commandMap.put("POP", new Pop());
        commandMap.put("+", new Plus());
        commandMap.put("-", new Minus());

        boolean numeric;

        for (int i = 0; i < numOfCommands; i++) {
            numeric = true;

            try {
                Integer.parseInt(instruction[i]);
            } catch (Exception NotNumeric) {
                numeric = false;
            }

            if (numeric) {
                Command command = new Push(instruction[i]);
                try {
                    command.execute(stack);
                } catch (UnexpectedException e) {
                    return -1;
                }
            } else {
                Command command = commandMap.get(instruction[i]);
                try {
                    command.execute(stack);
                } catch (UnexpectedException e) {
                    return -1;
                }
            }
        }

        if (stack.size() == 0) {
            return -1;
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String code = "3 4-";

        System.out.println(codeProcessing(code));
    }
}
