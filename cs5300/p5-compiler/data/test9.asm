# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# a = 3;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 3            
sw $t1 0($t0)        # Store to memory

# println(""This program prints [1..5] correct."");

la $a0 datalabel0   
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
li $v0 10            # EXIT
syscall             

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
datalabel0:    .asciiz "This program prints [1..5] correct."
