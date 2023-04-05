	.text
	
	.globl main

main:
    # Allocate Array
    li $t0 1
    li $t1 3
    li $t2 5
    sw $t0 -4($sp)
    sw $t1 -8($sp)
    sw $t2 -12($sp)
    
    # Store of the location of the start of the array
    add $t4 $sp -4
    
    # Update Stack pointer
    add $sp $sp -12

    # Create the dope vector 
    li $t5 3 # Number of elements
    
    sw $t4 -4($sp)
    sw $t5 -8($sp)

     # Call fee
    jal fee
    
    # Load return value
    lw $t3 -12($sp)
    add $sp $sp -12
    
    move $a0 $t3
    li $v0 1
    syscall
    
    # Exit the Program
    li $v0 10
    syscall
        
fee:
    lw $t0 -4($sp) # Starting memory location
    lw $t1 -8($sp) # Number of elements
    
    move $a0 $t1
    
    li $t2 0 # Sum
    li $t3 0 # i 
    
loop:
    beq $t3 $t1 exit
    
    li $t4 -4       # Individual Offset
    mul $t4 $t3 $t4 # i * offset
    add $t5 $t0 $t4 # memory location for a[i]
    lw $t5 ($t5)    # value at a[i]
    
    add $t2 $t2 $t5
    
    addi $t3 $t3 1
    
    j loop
   
exit:
   sw $t2 -12($sp)
   jr $ra