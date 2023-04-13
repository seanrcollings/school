# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

add:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -8      # Update the stack pointer

# println(x + y);

li $t0 4             # Load the offset of x
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of x from memory
li $t1 0             # Load the offset of y
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of y from memory
add $t0, $t0, $t1   
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This program prints 7 7"");

la $a0 datalabel0   
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# add(3, 4);

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -12($sp)
# Store arguments for function on the stack
li $t1 3            
sw $t1 -16($sp)     
li $t1 4            
sw $t1 -20($sp)     
addi $sp $sp -12    
jal add              # Call the function
addi $sp $sp 12     
# Load temporaries
lw $t0 -12($sp)
move $ra $t0         # Load return address back into $ra

# a = 5;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 5            
sw $t1 0($t0)        # Store to memory

# b = 2;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 2            
sw $t1 0($t0)        # Store to memory

# add(a, b);

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -12($sp)
# Store arguments for function on the stack
li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
sw $t1 -16($sp)     
li $t1 -8            # Load the offset of b
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of b from memory
sw $t1 -20($sp)     
addi $sp $sp -12    
jal add              # Call the function
addi $sp $sp 12     
# Load temporaries
lw $t0 -12($sp)
move $ra $t0         # Load return address back into $ra
# Exiting scope
addi $sp $sp 0       # Update the stack pointer
# ---------------------------------------
li $v0 10            # EXIT
syscall             

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
datalabel0:    .asciiz "This program prints 7 7"
