package machine;

import java.util.Stack;

public class Push implements Command {

    @Override
    public Stack<Integer> execute(Stack<Integer> stack) {
        stack.push(Integer.parseInt(Machine.currentCommand));
        return stack;
    }
}

