	.text
	
	.globl main

main:
    li $t0 3
    li $t1 4
    add $t3 $t0 $t1

    move $a0 $t3
    li $v0 1
    syscall
    