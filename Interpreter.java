import java.io.*;
import java.util.*;

class Stack{
  private Integer[] buf;
  private Integer sp;

  public Stack(int size) {
    buf = new Integer[size];
    sp = -1;
  }

  public void push(Integer number) {
    if (sp + 1 < buf.length) {
      sp++;
      buf[sp] = number;
    }
    else {
      System.out.println("Stack Overflow: Cannot push " + number);
    }
  }

  public Integer pop(){
    if (sp >= 0) {
      Integer number = buf[sp];
      sp--;
      return number;
    }

    System.out.println("Stack Overflow: The stack is empty");
    return -1;
  }

  public Integer top() {
    if (sp >= 0) {
      return buf[sp];
    }
    System.out.println("Stack empty.");
    return -1;
  }
}


public class Interpreter {

  public static List<String> readFromFile(String FilePath) {
    List<String> Program_Lines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(FilePath))){
      String Line;
      while ((Line = reader.readLine()) != null) {
        Program_Lines.add(Line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Program_Lines;
  }

  public static void main(String[] args) {
    String FilePath = args[0];
    List<String> Program_Lines = readFromFile(FilePath);
    ArrayList<Object> Program = new ArrayList<>();
    int token_counter = 0;
    Map<String, Integer> labelTraker = new HashMap<>();

    for (String line : Program_Lines) {
        String[] parts = line.split(" ");
        String opcode = parts[0];

        if (opcode.equals("")) {
          continue;
        }
        if (opcode.endsWith(":")) {
          String label = opcode.substring(0, opcode.length() - 1);
          labelTraker.put(label, token_counter);
          continue;
        }
        Program.add(opcode);
        token_counter++ ;

        if (opcode.equals("push")) {
          int number = Integer.parseInt(parts[1]);
          Program.add(number);
          token_counter++;
        }
        else if (opcode.equals("pmo")) {
          String stringLiteral = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

          if (stringLiteral.length() > 1 && stringLiteral.startsWith("\"") && stringLiteral.endsWith("\"")) {
              stringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);
          }
          Program.add(stringLiteral);
          token_counter++;
        }
        else if (opcode.equals("jump.eq.0")) {
          String lab = parts[1];
          Program.add(lab);
          token_counter++;
        }
        else if (opcode.equals("jump.gt.0")) {
          String lab = parts[1];
          Program.add(lab);
          token_counter++;
        }
    }

    Integer pc = 0;
    Stack stack = new Stack(256);
    Scanner scanner = new Scanner(System.in);

    while (pc < Program.size() && !Program.get(pc).equals("veiny") ) {
      Object opcode = Program.get(pc);
      pc++;

      if (opcode.equals("push")) {
        Integer number = (Integer)Program.get(pc);
        pc++;

        stack.push(number);
      }
      else if (opcode.equals("pop")) {
        stack.pop();
      }
      else if (opcode.equals("add")) {
        Integer a = stack.pop();
        Integer b = stack.pop();
        stack.push(a+b);
      }
      else if (opcode.equals("sub")) {
        Integer a = stack.pop();
        Integer b = stack.pop();
        stack.push(a-b);
      }
      else if (opcode.equals("pmo")) {
        Object stringLiteral = Program.get(pc);
        pc++;
        System.out.println(stringLiteral);
      }
      else if (opcode.equals("dih")) {
        Integer number = scanner.nextInt();
        stack.push(number);
      }
      else if (opcode.equals("jump.eq.0")) {
        Integer number = stack.top();
        if (number == 0) {
          pc = labelTraker.get(Program.get(pc));
        }
        else {
          pc++;
        }
      }

      else if (opcode.equals("jump.gt.0")) {
        Integer number = stack.top();
        if (number > 0) {
          pc = labelTraker.get(Program.get(pc));
        }
        else {
          pc++;
        }
      }
    }
    scanner.close();
  }
}