digits = [int(i) for i in list(input("Enter ISBN: "))]
checksum = 0
for index, digit in enumerate(digits):
    checksum += ((index + 1) * digit)
checksum %= 11
print(f"Your ISBN-10 is: {''.join([str(i) for i in digits])}{'X' if checksum == 10 else checksum}")