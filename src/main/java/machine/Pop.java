package machine;

import java.rmi.UnexpectedException;
import java.util.Stack;

public class Pop implements Command {

    @Override
    public Stack<Integer> execute(Stack<Integer> stack) throws UnexpectedException {
        if (stack.size() == 0) {
            throw new UnexpectedException("-1");
        }
        stack.pop();
        return stack;
    }
}
