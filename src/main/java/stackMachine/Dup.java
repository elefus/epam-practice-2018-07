package stackMachine;

import java.rmi.UnexpectedException;
import java.util.Stack;

public class Dup implements Command {

    @Override
    public Stack<Integer> execute(Stack<Integer> stack) throws UnexpectedException {
        if (stack.size() == 0) {
            throw new UnexpectedException("-1");
        }
        int temp1;
        temp1 = stack.peek();
        stack.push(temp1);
        return stack;
    }
}
