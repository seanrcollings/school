# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# Code for function main
main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0 # Update the stack pointer
# println
la $a0 datalabel0
li $v0 4 # PRINT_STRING
syscall
la $a0 newline
li $v0 4 # PRINT_STRING
syscall

# Exiting scope
addi $sp $sp 0 # Update the stack pointer
# ---------------------------------------
li $v0 10 # EXIT
syscall

# All memory structures are placed after the
# .data assembler directive
.data

newline:	 .asciiz "\n"
datalabel0:  .asciiz "Hello world"