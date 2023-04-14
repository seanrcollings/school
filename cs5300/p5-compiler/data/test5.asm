# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

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

fum:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# a = 9;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 9            
sw $t1 0($t0)        # Store to memory

# b = 12;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 12           
sw $t1 0($t0)        # Store to memory

# println(b - a + 4);

li $t0 -8            # Load the offset of b
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of b from memory
li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
sub $t0, $t0, $t1    # b - a
li $t1 4            
add $t0, $t0, $t1    # b - a + 4
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# foo();

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -12($sp)
# Store arguments for function on the stack
addi $sp $sp -12    
jal foo              # Call the function
addi $sp $sp 12     
# Load temporaries
lw $t0 -12($sp)
move $ra $t0         # Load return address back into $ra
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This program prints 7 7 7"");

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# foo();

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
addi $sp $sp -4     
jal foo              # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
move $ra $t0         # Load return address back into $ra

# fum();

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
addi $sp $sp -4     
jal fum              # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
move $ra $t0         # Load return address back into $ra
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
li $v0 10            # EXIT
syscall             
# Remaining registers: [$t0, $t1]

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
    label0:    .asciiz "This program prints 7 7 7"
