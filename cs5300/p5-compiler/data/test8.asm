# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

add:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -12     # Update the stack pointer
li $t0 8             # Load the offset of x
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of x from memory
li $t1 4             # Load the offset of y
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of y from memory
add $t0, $t0, $t1    # x + y
sw $t0 0($sp)        # Store return value
addi $sp $sp 12      # Add back offset relative to all enclosing scopes
jr $ra              
# Exiting scope
addi $sp $sp 12      # Update the stack pointer
# ---------------------------------------
jr $ra              

add2:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -12     # Update the stack pointer
move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
# Store arguments for function on the stack
li $t2 8             # Load the offset of x
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of x from memory
sw $t2 -12($sp)     
li $t2 4             # Load the offset of y
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of y from memory
sw $t2 -16($sp)     
addi $sp $sp -8     
jal add              # Call the function
addi $sp $sp 8      
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
move $ra $t1         # Load return address back into $ra
lw $t1 -20($sp)      # Load return value
sw $t1 -8($sp)      
li $t1 1            
sw $t1 -12($sp)     
addi $sp $sp -4     
jal add              # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
move $ra $t0         # Load return address back into $ra
lw $t0 -16($sp)      # Load return value
sw $t0 0($sp)        # Store return value
addi $sp $sp 12      # Add back offset relative to all enclosing scopes
jr $ra              
# Exiting scope
addi $sp $sp 12      # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This program prints 7"");

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(add2(2, 4));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 2            
sw $t1 -8($sp)      
li $t1 4            
sw $t1 -12($sp)     
addi $sp $sp -4     
jal add2             # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
move $ra $t0         # Load return address back into $ra
lw $t0 -16($sp)      # Load return value
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
# Remaining registers: [$t0, $t1, $t2]

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
    label0:    .asciiz "This program prints 7"
