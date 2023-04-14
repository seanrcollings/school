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

# i = 0;

li $t0 -4            # Load the offset
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
li $t1 0            
sw $t1 0($t0)        # Store to memory

# println(""This program prints 0 through 9."");

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             
label1:             
li $t0 -4            # Load the offset of i
add $t0 $t0 $sp      # Add stack pointer to the offset for absolute address
lw $t0 0($t0)        # Load the value of i from memory
li $t1 10           
slt $t0, $t0, $t1    # i < 10
subi $t0 $t0 1      
bne $t0 $zero label2
# ---------------------------------------
# Entering a new Scope
addi $sp $sp -4      # Update the stack pointer

# println(i);

li $t1 0             # Load the offset of i
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
lw $t1 0($t1)        # Load the value of i from memory
move $a0 $t1        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# i = i + 1;

li $t1 0             # Load the offset
add $t1 $t1 $sp      # Add stack pointer to the offset for absolute address
li $t2 0             # Load the offset of i
add $t2 $t2 $sp      # Add stack pointer to the offset for absolute address
lw $t2 0($t2)        # Load the value of i from memory
li $t3 1            
add $t2, $t2, $t3    # i + 1
sw $t2 0($t1)        # Store to memory
j label2             # break
# Exiting scope
addi $sp $sp 4       # Update the stack pointer
# ---------------------------------------
j label1            
label2:             
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
    label0:    .asciiz "This program prints 0 through 9."
