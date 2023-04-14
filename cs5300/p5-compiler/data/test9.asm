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

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# if (a < 4)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 4            
slt $t0, $t0, $t1    # a < 4
subi $t0 $t0 1      
bne $t0 $zero label1
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""1 correct"");

la $a0 label3       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label2            
label1:             
label2:             
# if (a > 4)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 4            
sgt $t0, $t0, $t1    # a > 4
subi $t0 $t0 1      
bne $t0 $zero label4
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""2 not correct"");

la $a0 label6       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label5            
label4:             
label5:             
# if (a > 4)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 4            
sgt $t0, $t0, $t1    # a > 4
subi $t0 $t0 1      
bne $t0 $zero label7
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""2 not correct"");

la $a0 label9       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label8            
label7:             
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""2 correct"");

la $a0 label10      
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
label8:             
# if (a <= 3)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 3            
sle $t0, $t0, $t1    # a <= 3
subi $t0 $t0 1      
bne $t0 $zero label11
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""3 correct"");

la $a0 label13      
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label12           
label11:            
label12:            
# if (a == 3)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 3            
seq $t0, $t0, $t1    # a == 3
subi $t0 $t0 1      
bne $t0 $zero label14
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""4 correct"");

la $a0 label16      
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label15           
label14:            
label15:            
# if (a >= 4)
li $t0 -4            # Load the offset of a
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of a from memory
li $t1 4            
sge $t0, $t0, $t1    # a >= 4
subi $t0 $t0 1      
bne $t0 $zero label17
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""5 not correct"");

la $a0 label19      
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label18           
label17:            
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(""5 correct"");

la $a0 label20      
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
label18:            
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
    label0:    .asciiz "This program prints [1..5] correct."
    label3:    .asciiz "1 correct"
    label6:    .asciiz "2 not correct"
    label9:    .asciiz "2 not correct"
   label10:    .asciiz "2 correct"
   label13:    .asciiz "3 correct"
   label16:    .asciiz "4 correct"
   label19:    .asciiz "5 not correct"
   label20:    .asciiz "5 correct"
