import java.io.*;
import java.util.Scanner;

public class Launcher {

	private static final int STACK_LENGTH = 30000;
	private static char[] mem = new char[STACK_LENGTH];
	private static int cmd_pointer = 0;
	private static int mem_pointer = 0;
	private static Scanner sc = new Scanner(System.in);

	private static class Command {
		private final static char SHIFT_RIGHT = '>';
		private final static char SHIFT_LEFT = '<';
		private final static char PLUS = '+';
		private final static char MINUS = '-';
		private final static char OUTPUT = '.';
		private final static char INPUT = ',';
		private final static char BRACKET_LEFT = '[';
		private final static char BRACKET_RIGHT = ']';
	}

	private void fileProccess(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String content = "";
		while (scanner.hasNext())
		{
			content += scanner.nextLine();
		}
		scanner.close();
		interpret(content);
	}

	private void interpret(String code) {
		int l = 0;
		for(int i = 0; i < code.length(); i++) {
			switch (code.charAt(i))
			{
				case Command.SHIFT_RIGHT:
					mem_pointer = (mem_pointer == STACK_LENGTH-1) ? 0 : mem_pointer + 1;
					break;
				case Command.SHIFT_LEFT:
					mem_pointer = (mem_pointer == 0) ? STACK_LENGTH-1 : mem_pointer - 1;
					break;
				case Command.PLUS:
					mem[mem_pointer]++;
					break;
				case Command.MINUS:
					mem[mem_pointer]--;
					break;
				case Command.OUTPUT:
					if (mem[cmd_pointer] == 0)
						System.out.print(0);
					else
						System.out.print(mem[mem_pointer]);
					System.out.print(' ');
					break;
				case Command.INPUT:
					mem[mem_pointer] = sc.next().charAt(0);
					break;
				case Command.BRACKET_LEFT:
					if(mem[mem_pointer] == 0) {
						i++;
						while(l > 0 || code.charAt(i) != ']') {
							if(code.charAt(i) == '[') l++;
							if(code.charAt(i) == ']') l--;
							i++;
						}
					}
					break;
				case Command.BRACKET_RIGHT:
					if(mem[mem_pointer] != 0) {
						i--;
						while(l > 0 || code.charAt(i) != '[') {
							if(code.charAt(i) == ']') l++;
							if(code.charAt(i) == '[') l--;
							i--;
						}
						i--;
					}
					break;
			}
		}
	}

	public static void main(String[] args) {
		Scanner fname = new Scanner(System.in);
		System.out.print("Enter file name: ");
		String fileName = fname.nextLine();
		File file = new File("./src/main/resources/" + fileName);
		if (file.exists()) {
			Launcher i = new Launcher();
			i.fileProccess(file);
		}
		else System.out.print("File not found");
	}
}