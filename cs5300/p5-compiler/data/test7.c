int identity(int x) {
  return x;
}

int add(int x, int y) {
//  println(x);
//  println(y);
  return x+y;
}

void main() {
  println("This program prints 7 7");
  println(identity(7));
  println(add(3, 4));
}

