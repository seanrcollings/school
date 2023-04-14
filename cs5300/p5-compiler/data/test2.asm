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

# println(""This program prints 7 7 7 7 7 (separated by newlines)"");

la $a0 label0       
li $v0 4             # PRINT_STRING
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(7);

li $t0 7            
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(3 + 4);

li $t0 3            
li $t1 4            
add $t0, $t0, $t1    # 3 + 4
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(14 / 2);

li $t0 14           
li $t1 2            
div $t0, $t0, $t1    # 14 / 2
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println(7 * 1);

li $t0 7            
li $t1 1            
mul $t0, $t0, $t1    # 7 * 1
move $a0 $t0        
li $v0 1             # PRINT_INTEGER
syscall             
la $a0 newline      
li $v0 4             # PRINT_STRING
syscall             

# println((7 * 2) / 2);

li $t0 7            
li $t1 2            
mul $t0, $t0, $t1    # 7 * 2
li $t1 2            
div $t0, $t0, $t1    # (7 * 2) / 2
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
# Remaining registers: [$t0, $t1]

# All memory structures are placed after the
# .data assembler directive
.data

   newline:    .asciiz "\n"
    label0:    .asciiz "This program prints 7 7 7 7 7 (separated by newlines)"
