package stackMachine;

import java.util.Stack;

public class Machine {

    private static final int MAX_INT = 1048575;
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

        String[] command = code.split(" ", numOfCommands);

        int temp1;
        int temp2;
        int temp3;

        for (int i = 0; i < numOfCommands; i++) {
            switch (command[i]) {
                case "DUP":
                    if (stack.size() == 0) {
                        return -1;
                    }
                    temp1 = stack.pop();
                    stack.push(temp1);
                    stack.push(temp1);
                    break;

                case "POP":
                    if (stack.size() == 0) {
                        return -1;
                    }
                    stack.pop();
                    break;

                case "+":
                    if (stack.size() < 2) {
                        return -1;
                    }
                    temp1 = stack.pop();
                    temp2 = stack.pop();
                    temp3 = temp1 + temp2;
                    if(temp3 > MAX_INT) {
                        return -1;
                    }
                    stack.push(temp3);
                    break;

                case "-":
                    if (stack.size() < 2) {
                        return -1;
                    }
                    temp1 = stack.pop();
                    temp2 = stack.pop();
                    temp3 = temp1 - temp2;
                    if (temp3 < 0) {
                        return -1;
                    }
                    stack.push(temp3);
                    break;

                default:
                    temp1 = Integer.parseInt(command[i]);
                    stack.push(temp1);
                    break;
            }
        }
        if (stack.size() == 0) {
            return -1;
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String code = "13 DUP 4 POP 5 DUP + DUP + -";
        System.out.println(codeProcessing(code));
    }
}
