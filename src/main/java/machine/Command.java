package machine;

import java.rmi.UnexpectedException;
import java.util.Stack;

public interface Command {
    Stack<Integer> execute(Stack<Integer> stack) throws UnexpectedException;
}
