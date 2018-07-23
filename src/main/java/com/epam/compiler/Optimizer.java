package com.epam.compiler;

import com.epam.compiler.Token.Type;
import java.util.ArrayList;

public class Optimizer {

  private static ArrayList<Token> newList;

  public static ArrayList<Token> optimize(ArrayList<Token> tokens) {
    newList = new ArrayList<>();

    for (Token token : tokens) {
      switch (token.getType()) {
        case WHILE_START:
          newList.add(new Token(token));
          break;

        case WHILE_END:
          if (newList.get(newList.size() - 1).getType() == Type.CHANGE_VAL
              && newList.get(newList.size() - 2).getType() == Type.WHILE_START) {
            newList.remove(newList.size() - 1);
            newList.remove(newList.size() - 1);
            newList.add(new Token(Type.SET_TO_ZERO));
          } else {
            newList.add(new Token(token));
          }
          break;

        default:
          handleToken(token);
          break;
      }
    }

    return new ArrayList<>(newList);
  }

  private static void handleToken(Token token) {
    if (!newList.isEmpty() && newList.get(newList.size() - 1).getType() == token.getType()) {
      newList.get(newList.size() - 1).val += token.val;
    } else {
      newList.add(new Token(token));
    }
  }
}
