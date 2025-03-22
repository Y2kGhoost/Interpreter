# JRot
**JRot** is a mini interpreter with java based on the **stack** data structur.

## Installation
You need to have [JDK 23](https://www.oracle.com/java/technologies/downloads/#jdk23-windows) based on your OS (Linux | MacOs | Windows)
And a java IDE as well.

Copy the [Interpreter.java](https://github.com/Y2kGhoost/Interpreter/blob/master/Interpreter.java) as well as the [Program.jrot](https://github.com/Y2kGhoost/Interpreter/blob/master/Program.jrot)
for testing.

## Usage

In the terminal (Linux | Windows | MacOs):
```console
foo@bar:~$ javac Interpreter.java
foo@bar:~$ java Interpreter Program.jrot
```

## Syntax

`pmo` : Print. Example: `pmo "Hello World"`. <br>

`dih` : Scan without promet the user. <br>

`add` : Add two numbers. Example:  
```
dih
dih

add
```
Here you scan two numbers and add them. <br>

`sub` : Subtract two numbers. <br>

`jump.eq.0` : Jump if equal 0. Example:
```
L1:
dih
dih

sub
jump.eq.0 L1
```
`L1` is a label, you can create multipal labels. Also you can use them as Loop. <br>

`jump.gt.0` : Jump if greater than 0. <br>

`push` : Push a number into the stack. Example:
```
push 1
push 1
add
```
<br>

`pop` : Pop a number from the stack.<br>

`veiny` : Exit the program. Example:
```
dih
dih
sub

jump.eq.0 L1
pmo "Equal"
veiny

L1:
pmo "Not Equal"
veiny
```

## Credit 

**Jrot** is still on demo, so they will be some issues with the compilation time and the featureless issues as well.<br>
**The Creator**: [Y2kGhoost](https://github.com/Y2kGhoost)
