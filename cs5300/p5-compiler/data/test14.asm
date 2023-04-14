# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

sum:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -12     # Update the stack pointer

# i = 0;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory

# sum = 0;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory
label0:             
li $t0 -4            # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 4             # Load the offset of n
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of n from memory
slt $t0, $t0, $t1    # i < n
subi $t0 $t0 1      
bne $t0 $zero label1
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -8      # Update the stack pointer

# sum = sum + x[i];

li $t1 0             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 0             # Load the offset of sum
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of sum from memory
li $t3 16            # Load the offset of x
add $t3 $t3 $sp      # Add stack pointer to the offset for absolute address
lw $t3 0($t3)        # Load the value of x from memory
add $t2, $t2, $t3    # sum + x[i]
sw $t2 0($t1)        # Store to memory

# i = i + 1;

li $t1 4             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 4             # Load the offset of i
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of i from memory
li $t3 1            
add $t2, $t2, $t3    # i + 1
sw $t2 0($t1)        # Store to memory
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------
j label0            
label1:             
li $t1 -8            # Load the offset of sum
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of sum from memory
sw $t1 0($sp)        # Store return value
addi $sp $sp 20      # Add back offset relative to all enclosing scopes
jr $ra              
# Exiting scope
addi $sp $sp 12      # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This should print 6 and 28"");

la $a0 label2       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# i = 0;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory
label3:             
li $t0 -8            # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 10           
slt $t0, $t0, $t1    # i < 10
subi $t0 $t0 1      
bne $t0 $zero label4
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -8      # Update the stack pointer

# a[i] = i;

li $t1 4             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 0             # Load the offset of i
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of i from memory
sw $t2 0($t1)        # Store to memory

# i = i + 1;

li $t1 0             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 0             # Load the offset of i
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of i from memory
li $t3 1            
add $t2, $t2, $t3    # i + 1
sw $t2 0($t1)        # Store to memory
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------
j label3            
label4:             

# println(sum(a, 4));

move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -12($sp)
sw $t1 -16($sp)
# Store arguments for function on the stack
li $t2 -4            # Load the offset of a
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of a from memory
sw $t2 -20($sp)     
li $t2 4            
sw $t2 -24($sp)     
addi $sp $sp -16    
jal sum              # Call the function
addi $sp $sp 16     
# Load temporaries
lw $t0 -12($sp)
lw $t1 -16($sp)
move $ra $t1         # Load return address back into $ra
lw $t1 -28($sp)      # Load return value
move $a0 $t1        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(sum(a, 8));

move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -12($sp)
sw $t1 -16($sp)
# Store arguments for function on the stack
li $t2 -4            # Load the offset of a
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of a from memory
sw $t2 -20($sp)     
li $t2 8            
sw $t2 -24($sp)     
addi $sp $sp -16    
jal sum              # Call the function
addi $sp $sp 16     
# Load temporaries
lw $t0 -12($sp)
lw $t1 -16($sp)
move $ra $t1         # Load return address back into $ra
lw $t1 -28($sp)      # Load return value
move $a0 $t1        
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
# Remaining registers: [$t0, $t1, $t2, $t3]

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
    label2:    .asciiz "This should print 6 and 28"
