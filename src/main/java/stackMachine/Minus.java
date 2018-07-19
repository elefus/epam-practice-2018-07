package stackMachine;

import java.rmi.UnexpectedException;
import java.util.Stack;

public class Minus implements Command {

    @Override
    public Stack<Integer> execute(Stack<Integer> stack) throws UnexpectedException {
        if (stack.size() < 2) {
            throw new UnexpectedException("-1");
        }
        int temp1 = stack.pop();
        int temp2 = stack.pop();
        int temp3 = temp1 - temp2;
        if (temp3 < 0) {
            throw new UnexpectedException("-1");
        }
        stack.push(temp3);
        return stack;
    }
}
