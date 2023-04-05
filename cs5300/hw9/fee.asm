	.text
	
	.globl main

 
main:
    li $t0 3
    li $t1 4
    
    # Update Stack pointer
    add $sp $sp -0

    # Store values on the stack 
    sw $t0 -4($sp)
    sw $t1 -8($sp)
    jal fee
    
    # Load return value
    lw $t3 -12($sp)
    add $sp $sp 0
    
    move $a0 $t3
    li $v0 1
    syscall
    
    # Exit the Program
    li $v0 10
    syscall
    

    
# int fee(int a, int b)
fee:
    lw $t0 -4($sp)
    lw $t1 -8($sp)
    
    add $t3 $t0 $t1
    sw $t3 -12($sp)
    
    jr $ra
