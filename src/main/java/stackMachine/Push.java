package stackMachine;

import java.util.Stack;

public class Push implements Command {

    private String intToPush;

    public Push(String num) {
        intToPush = num;
    }
    @Override
    public Stack<Integer> execute(Stack<Integer> stack) {
        stack.push(Integer.parseInt(intToPush));
        return stack;
    }
}
