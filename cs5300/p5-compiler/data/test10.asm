# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

fib:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -8      # Update the stack pointer
move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 4             # Load the offset of i
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of i from memory
li $t2 1            
sub $t0, $t1, $t2   
sw $t0 -8($sp)      
addi $sp $sp -4     
jal fib              # Call the function
addi $sp $sp 4      
# Load temporaries
move $ra $t0         # Load return address back into $ra
lw $t0 -12($sp)      # Load return value
move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
# Store arguments for function on the stack
li $t2 4             # Load the offset of i
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of i from memory
li $t3 2            
sub $t0, $t2, $t3   
sw $t0 -12($sp)     
addi $sp $sp -8     
jal fib              # Call the function
addi $sp $sp 8      
# Load temporaries
move $ra $t1         # Load return address back into $ra
lw $t0 -16($sp)      # Load return value
add $t0, $t0, $t0   
sw $t0 0($sp)        # Store return value
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This program prints the first 11 numbers of the Fibonacci sequence"");

la $a0 datalabel0   
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(0));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 0            
sw $t1 -8($sp)      
addi $sp $sp -4     
jal fib              # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
move $ra $t0         # Load return address back into $ra
lw $t0 -12($sp)      # Load return value
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(1));

move $t1 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
# Store arguments for function on the stack
li $t2 1            
sw $t2 -12($sp)     
addi $sp $sp -8     
jal fib              # Call the function
addi $sp $sp 8      
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
move $ra $t1         # Load return address back into $ra
lw $t1 -16($sp)      # Load return value
move $a0 $t1        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(2));

move $t2 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
# Store arguments for function on the stack
li $t3 2            
sw $t3 -16($sp)     
addi $sp $sp -12    
jal fib              # Call the function
addi $sp $sp 12     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
move $ra $t2         # Load return address back into $ra
lw $t2 -20($sp)      # Load return value
move $a0 $t2        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(3));

move $t3 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
# Store arguments for function on the stack
li $t4 3            
sw $t4 -20($sp)     
addi $sp $sp -16    
jal fib              # Call the function
addi $sp $sp 16     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
move $ra $t3         # Load return address back into $ra
lw $t3 -24($sp)      # Load return value
move $a0 $t3        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(4));

move $t4 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
# Store arguments for function on the stack
li $t5 4            
sw $t5 -24($sp)     
addi $sp $sp -20    
jal fib              # Call the function
addi $sp $sp 20     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
move $ra $t4         # Load return address back into $ra
lw $t4 -28($sp)      # Load return value
move $a0 $t4        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(5));

move $t5 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
# Store arguments for function on the stack
li $t6 5            
sw $t6 -28($sp)     
addi $sp $sp -24    
jal fib              # Call the function
addi $sp $sp 24     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
move $ra $t5         # Load return address back into $ra
lw $t5 -32($sp)      # Load return value
move $a0 $t5        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(6));

move $t6 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
# Store arguments for function on the stack
li $t7 6            
sw $t7 -32($sp)     
addi $sp $sp -28    
jal fib              # Call the function
addi $sp $sp 28     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
move $ra $t6         # Load return address back into $ra
lw $t6 -36($sp)      # Load return value
move $a0 $t6        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(7));

move $t7 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
# Store arguments for function on the stack
li $t8 7            
sw $t8 -36($sp)     
addi $sp $sp -32    
jal fib              # Call the function
addi $sp $sp 32     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
lw $t7 -32($sp)
move $ra $t7         # Load return address back into $ra
lw $t7 -40($sp)      # Load return value
move $a0 $t7        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(8));

move $t8 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
sw $t8 -36($sp)
# Store arguments for function on the stack
li $t9 8            
sw $t9 -40($sp)     
addi $sp $sp -36    
jal fib              # Call the function
addi $sp $sp 36     
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
lw $t7 -32($sp)
lw $t8 -36($sp)
move $ra $t8         # Load return address back into $ra
lw $t8 -44($sp)      # Load return value
move $a0 $t8        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(fib(9));

move $t9 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
sw $t8 -36($sp)
sw $t9 -40($sp)
# Store arguments for function on the stack

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
datalabel0:    .asciiz "This program prints the first 11 numbers of the Fibonacci sequence"
