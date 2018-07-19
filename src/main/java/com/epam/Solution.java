package com.epam;

import java.util.Stack;

public class Solution {

  private static Stack<Integer> stack = new Stack<>();

  private static int solution(String S) {
    if (S.length() > 2000) {
      return -1;
    }
    String[] operations = S.split(" ");
    int number;
    for (String str : operations) {
      switch (str) {
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
          if (stack.size() < 2) {
            return -1;
          }
          number = stack.pop() + stack.pop();
          if (number > 1048575) {
            return -1;
          }
          stack.push(number);
          break;
        case "-":
          if (stack.size() < 2) {
            return -1;
          }
          number = stack.pop() - stack.pop();
          if (number < 0) {
            return -1;
          }
          stack.push(number);
          break;
        default:
          number = Integer.parseInt(str); //valid sequence
          stack.push(number);
          break;
      }
    }
    if (stack.size() == 0) {
      return -1;
    }
    return stack.pop();
  }

  public static void main(String[] args) {
    String s = "13 DUP 4 POP 5 DUP + DUP + -";
    System.out.println(solution(s));

  }
}
