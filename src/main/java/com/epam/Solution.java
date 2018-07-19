package com.epam;

import java.util.Stack;


public class Solution {
    private static final int MAX_STRING_LENGTH = 2000;

    private int solution(String s) {
        if (s.length() > MAX_STRING_LENGTH) {
            return -1;
        }
        int tmp;
        String commands[] = s.split(" ");
        int commandCounter = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                commandCounter++;
            }
        }
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < commandCounter; i++) {
            switch (commands[i]) {
                case "DUP":
                    if (stack.size() == 0) {
                        return -1;
                    }
                    stack.push(stack.peek());
                    break;
                case "POP":
                    if (stack.size() == 0) {
                        return -1;
                    }
                    stack.pop();
                    break;
                case "+":
                    if (stack.size() <= 1) {
                        return -1;
                    }
                    tmp = stack.pop() + stack.pop();
                    stack.push(tmp);
                    break;
                case "-":
                    if (stack.size() <= 1) {
                        return -1;
                    }
                    tmp = stack.pop() - stack.pop();
                    if (tmp < 0) {
                        return -1;
                    }
                    stack.push(tmp);
                    break;
                default:
                    tmp = Integer.parseInt(commands[i]);
                    stack.push(tmp);
                    break;
            }
        }

        return stack.pop();
    }

    public static void main(String[] args) {
        String s = "13 DUP 4 POP 5 DUP + DUP + -";
        Solution solution = new Solution();
        int res = solution.solution(s);
        System.out.println(res);

    }
}
