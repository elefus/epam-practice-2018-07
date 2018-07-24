package machine;

import java.rmi.UnexpectedException;
import java.util.Stack;

public class Plus implements Command {

    @Override
    public Stack<Integer> execute(Stack<Integer> stack) throws UnexpectedException {
        if (stack.size() < 2) {
            throw new UnexpectedException("-1");
        }
        int temp1 = stack.pop();
        int temp2 = stack.pop();
        int temp3 = temp1 + temp2;
        if (temp3 > Machine.MAX_INT) {
            throw new UnexpectedException("-1");
        }
        stack.push(temp3);
        return stack;
    }
}
