import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

class Stack {
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

class Handlers extends Stack{
  static Scanner scanner = new Scanner(System.in);
  public Handlers(int size) {
    super(size);
  }

  public void handlepush(ArrayList<Object> Program, int pc, Stack stack) {
    Integer number = (Integer) Program.get(pc);
    pc++;
    stack.push(number);
  }

  public void handlepop(Stack stack) {
    stack.pop();
  } 

  public void handleAdd(Integer a, Integer b, Stack stack) {
    a = stack.pop();
    b = stack.pop();
    stack.push(a + b);
  }

  public void handleSub(Integer a, Integer b, Stack stack) {
    a = stack.pop();
    b = stack.pop();
    stack.push(a - b);
  }

  public void handleMult(Integer a, Integer b, Stack stack) {
    a = stack.pop();
    b = stack.pop();
    stack.push(a * b);
  }

  public void handleDivis(Integer a, Integer b, Stack stack) {
    if (b == 0) {
      System.out.println("Can't divised by 0.");
    } else {
      a = stack.pop();
      b = stack.pop();
      stack.push(a / b);
    }
  }

  public void handleModul(Integer a, Integer b, Stack stack) {
    if (b == 0) {
      System.out.println("Can't divised by 0.");
    } else {
      a = stack.pop();
      b = stack.pop();
      stack.push(a % b);
    }
  }

  public void handlePrint(ArrayList<Object> Program, int pc) {
    Object stringLiteral = Program.get(pc);
    pc++;
    System.out.println(stringLiteral);
  }

  public void handleScan(ArrayList<Object> Program, Scanner scanner, Stack stack, int pc) {
    Object stringLiteral = Program.get(pc);
    System.out.print(stringLiteral);
    Integer number = scanner.nextInt();
    stack.push(number);
  }

  public void handleJumpEq0(Stack stack, ArrayList<Object> Program, int pc, Map<String, Integer> labelTraker) {
    Integer number = stack.top();
    if (number == 0) {
      pc = labelTraker.get(Program.get(pc));
    }
    else {
      pc++;
    }
  }

  public void handleJumpGt0(Stack stack, ArrayList<Object> Program, int pc, Map<String, Integer> labelTraker) {
    Integer number = stack.top();
    if (number > 0) {
      pc = labelTraker.get(Program.get(pc));
    }
    else {
      pc++;
    }
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

  public static void lexer(List<String> Program_Lines, ArrayList<Object> Program, Map<String, Integer> labelTraker, int token_counter) {
    for (String line : Program_Lines) {
      String[] parts = line.split(" ");
      String opcode = parts[0];

      if (opcode.equals("") || opcode.equals("#")) {
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
      else if (opcode.equals("dih")) {
        String stringLiteral = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        int commentIndex = stringLiteral.indexOf("#");
        if (commentIndex != -1) {
          stringLiteral = stringLiteral.substring(0, commentIndex).trim();
        }

        if (stringLiteral.length() > 1 && stringLiteral.startsWith("\"") && stringLiteral.endsWith("\"")) {
            stringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);
        }
        Program.add(stringLiteral);
        token_counter++;
      }
      else if (opcode.equals("pmo")) {
        String stringLiteral = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        int commentIndex = stringLiteral.indexOf("#");
        if (commentIndex != -1) {
          stringLiteral = stringLiteral.substring(0, commentIndex).trim();
        }
        //System.out.println(stringLiteral);

        if (stringLiteral.length() > 1 && stringLiteral.startsWith("\"") && stringLiteral.endsWith("\"")) {
            stringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);
        }
        Program.add(stringLiteral);
        token_counter++;
      }
      else if (opcode.equals("jump.eq.0")) {
        String stringLiteral = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        int commentIndex = stringLiteral.indexOf("#");
        if (commentIndex != -1) {
          stringLiteral = stringLiteral.substring(0, commentIndex).trim();
        }
        String lab = parts[1];
        Program.add(lab);
        token_counter++;
      }
      else if (opcode.equals("jump.gt.0")) {
        String stringLiteral = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        int commentIndex = stringLiteral.indexOf("#");
        if (commentIndex != -1) {
          stringLiteral = stringLiteral.substring(0, commentIndex).trim();
        }
        String lab = parts[1];
        Program.add(lab);
        token_counter++;
      }
      /*else if (opcode.equals("jump")) {
        String lab = parts[1];
        Program.add(lab);
        token_counter++;
      }*/
    }
  }

  public static void regex(Handlers handlers, Stack stack, ArrayList<Object> Program, int pc, Integer a, Integer b, Scanner scanner, Map<String, Integer> labelTraker) {
    while (pc < Program.size() && !Program.get(pc).equals("veiny") ) {
      Object opcode = Program.get(pc);
      pc++;
      if (opcode.equals("push")) {
        handlers.handlepush(Program, pc, stack);
      }
      else if (opcode.equals("pop")) {
        handlers.handlepop(stack);
      }

      else if (opcode.equals("add")) {
        handlers.handleAdd(a, b, stack);
      }
      else if (opcode.equals("sub")) {
        handlers.handleSub(a, b, stack);
      }
      else if (opcode.equals("mult")) {
        handlers.handleMult(a, b, stack);
      }
      else if (opcode.equals("div")) {
        handlers.handleDivis(a, b, stack);
      }
      else if (opcode.equals("mod")) {
        handlers.handleModul(a, b, stack);
      }

      else if (opcode.equals("pmo")) {
        handlers.handlePrint(Program, pc);
      }
      else if (opcode.equals("dih")) {
        handlers.handleScan(Program, scanner, stack, pc);
      }

      else if (opcode.equals("jump.eq.0")) {
        handlers.handleJumpEq0(stack, Program, pc, labelTraker);
      }
      else if (opcode.equals("jump.gt.0")) {
        handlers.handleJumpGt0(stack, Program, pc, labelTraker);
      }
    }
  }

  public static void main(String[] args) {
    
    String FilePath = args[0];
    List<String> Program_Lines = readFromFile(FilePath);
    ArrayList<Object> Program = new ArrayList<>();
    int token_counter = 0;
    Map<String, Integer> labelTraker = new HashMap<>();  

    lexer(Program_Lines, Program, labelTraker, token_counter);   
    
    
    int size = 256;
    Scanner scanner = new Scanner(System.in);
    int pc = 0;
    Stack stack = new Stack(size);
    Handlers handlers = new Handlers(size);  
    Integer a = 0;
    Integer b = 0;

    regex(handlers, stack, Program, pc, a, b, scanner, labelTraker);

    scanner.close();
  }
}