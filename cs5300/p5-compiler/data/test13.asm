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

# println(""This should print 0, 2, 2, 3, 6 and 36"");

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# a[0] = 0;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory

# println(a[0]);

li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# a[2] = 2;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 2            
sw $t1 0($t0)        # Store to memory

# println(a[2]);

li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# i = 2;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 2            
sw $t1 0($t0)        # Store to memory

# a[i] = i;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 -8            # Load the offset of i
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of i from memory
sw $t1 0($t0)        # Store to memory

# println(a[i]);

li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# i = 0;

li $t0 -8            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory
label1:             
li $t0 -8            # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 10           
slt $t0, $t0, $t1    # i < 10
subi $t0 $t0 1      
bne $t0 $zero label2
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
j label1            
label2:             

# println(a[3]);

li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
move $a0 $t1        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(a[6]);

li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
move $a0 $t1        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(a[6] * 6);

li $t1 -4            # Load the offset of a
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of a from memory
li $t2 6            
mul $t1, $t1, $t2    # a[6] * 6
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
    label0:    .asciiz "This should print 0, 2, 2, 3, 6 and 36"
