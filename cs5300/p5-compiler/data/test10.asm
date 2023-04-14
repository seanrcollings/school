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
# if (i == 0)
li $t0 4             # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 0            
seq $t0, $t0, $t1    # i == 0
subi $t0 $t0 1      
bne $t0 $zero label0
li $t1 1            
sw $t1 0($sp)        # Store return value
addi $sp $sp 8       # Add back offset relative to all enclosing scopes
jr $ra              
j label1            
label0:             
label1:             
# if (i == 1)
li $t0 4             # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 1            
seq $t0, $t0, $t1    # i == 1
subi $t0 $t0 1      
bne $t0 $zero label2
li $t1 1            
sw $t1 0($sp)        # Store return value
addi $sp $sp 8       # Add back offset relative to all enclosing scopes
jr $ra              
j label3            
label2:             
label3:             
move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 4             # Load the offset of i
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of i from memory
li $t2 1            
sub $t1, $t1, $t2    # i - 1
sw $t1 -8($sp)      
addi $sp $sp -4     
jal fib              # Call the function
addi $sp $sp 4      
# Load temporaries
lw $t0 -4($sp)
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
sub $t2, $t2, $t3    # i - 2
sw $t2 -12($sp)     
addi $sp $sp -8     
jal fib              # Call the function
addi $sp $sp 8      
# Load temporaries
lw $t0 -4($sp)
lw $t1 -8($sp)
move $ra $t1         # Load return address back into $ra
lw $t1 -16($sp)      # Load return value
add $t0, $t0, $t1    # fib(i - 1) + fib(i - 2)
sw $t0 0($sp)        # Store return value
addi $sp $sp 8       # Add back offset relative to all enclosing scopes
jr $ra              
# Exiting scope
addi $sp $sp 8       # Update the stack pointer
# ---------------------------------------
jr $ra              

main:
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -0      # Update the stack pointer

# println(""This program prints the first 11 numbers of the Fibonacci sequence"");

la $a0 label4       
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

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 1            
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

# println(fib(2));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 2            
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

# println(fib(3));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 3            
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

# println(fib(4));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 4            
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

# println(fib(5));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 5            
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

# println(fib(6));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 6            
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

# println(fib(7));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 7            
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

# println(fib(8));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 8            
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

# println(fib(9));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 9            
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

# println(fib(10));

move $t0 $ra         # Put Return Address in a temp
# Store off temporaries
sw $t0 -4($sp)
# Store arguments for function on the stack
li $t1 10           
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
    label4:    .asciiz "This program prints the first 11 numbers of the Fibonacci sequence"
