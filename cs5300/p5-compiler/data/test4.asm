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

# println(""This program prints 7 7 7"");

la $a0 datalabel0   
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# a = 3;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 3            
sw $t1 0($t0)        # Store to memory

# b = 2;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 2            
sw $t1 0($t0)        # Store to memory
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -8      # Update the stack pointer

# a = 5;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 5            
sw $t1 0($t0)        # Store to memory

# println(a + b);

li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 0             # Load the offset of b
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of b from memory
add $t0, $t0, $t1   
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# b = 9;

li $t1 -4            # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 9            
sw $t2 0($t1)        # Store to memory

# a = -2;

li $t1 0             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
# Negate expression
li $t2 2            
li $t3 -1           
mul $t2, $t2, $t3   
sw $t2 0($t1)        # Store to memory

# println(a + b);

li $t1 0             # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
li $t2 -4            # Load the offset of b
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of b from memory
add $t0, $t1, $t2   
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------

# b = 4;

li $t1 0             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 4            
sw $t2 0($t1)        # Store to memory
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------

# println(a + b);

li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
li $t2 -8            # Load the offset of b
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of b from memory
add $t0, $t1, $t2   
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
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
datalabel0:    .asciiz "This program prints 7 7 7"
