# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# Code for function main
foo:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(7);

li $t0 7            
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
jr $ra              

# Code for function main
fum:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# a = 9;

li $t1 -4            # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 9            
sw $t2 0($t1)        # Store to memory

# b = 12;

li $t1 -8            # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 12           
sw $t2 0($t1)        # Store to memory

# println(b - a + 4);

li $t1 -8            # Load the offset of b
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of b from memory
li $t2 -4            # Load the offset of a
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of a from memory
sub $t0, $t1, $t2   
li $t1 4            
add $t0, $t0, $t1   
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# foo();

move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
addi $sp $sp -16    
jal foo              # Call the function
addi $sp $sp 16     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
move $ra $t1         # Load return address back into $ra
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
jr $ra              

# Code for function main
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

# foo();

move $t2 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
addi $sp $sp -12    
jal foo              # Call the function
addi $sp $sp 12     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
move $ra $t2         # Load return address back into $ra

# fum();

move $t3 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
addi $sp $sp -16    
jal fum              # Call the function
addi $sp $sp 16     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
move $ra $t3         # Load return address back into $ra
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
