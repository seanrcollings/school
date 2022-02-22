use v6;
unit module MyCode;


# Parse the input into a list of tokens
# Iterative approach, rather than the recursive one taken in the example code
sub getTokens ($data) is export {
  my @results = ();
  my $text = $data;

  while $text.chars > 0 {
    my $newString = $text.trim;

    if $text.substr(0..0) ~~ m/ "#" / {
      $newString = $text ~~ m/ "\n" / ?? $/.postmatch !! "";
    }
    elsif $text.substr(0..0) ~~ m/(<:L>) / and  $text ~~ m/ (<:!L>) / {
      @results.push("IDENTIFIER:" ~ $/.prematch);
      $newString = $/ ~ $/.postmatch;
    }
    elsif $text.substr(0..0) ~~ /\+/ {
      @results.push("ADDITION:" ~ $text.substr(0..0));
      $newString = $text.substr(1..$text.chars);
    }
    elsif $text.substr(0..0) ~~ /\*/ {
      @results.push("MULTIPLICATION:" ~ $text.substr(0..0));
      $newString = $text.substr(1..$text.chars);
    }
    elsif $text.substr(0..0) ~~ m/(<:N>) / {
      if $text ~~ m/ (<:!N>) / {
        @results.push("INTEGER:" ~ $/.prematch);
        $newString = $/ ~ $/.postmatch;
      }
    }
    elsif $text.substr(0..0) ~~ m/ "\"" / {
      if $text.substr(1..$text.chars) ~~ m/ "\"" / {
        @results.push("STRING:" ~ $/ ~ $/.prematch ~ $/);
        $newString = $/.postmatch;
      }
    }
    elsif $text.substr(0..0) ~~ m/ "(" / {
      @results.push("LPAREN:" ~ "(");
      $newString = $text.substr(1..$text.chars);
    }
    elsif $text.substr(0..0) ~~ m/ ")" / {
      @results.push("RPAREN:" ~ ")");
      $newString = $text.substr(1..$text.chars);
  }

    $text = $newString;
  }

  return @results;
}



sub balance (@tokens) is export {
  my $parenCount = 0;

  for @tokens -> $token {
    my $type = split(/\:/, $token, 2, :skip-empty, :v)[0];
    if $type eq "LPAREN" {
      $parenCount += 1;
    } elsif $type eq "RPAREN" {
      $parenCount -= 1;
    }

    if $parenCount < 0 {
      return False;
    }
  }

  return $parenCount == 0;
}

sub format (@tokens) is export {
  my $result = "";
  my $indent = 0;

  for @tokens -> $token {
    my @split = split(/\:/, $token, 2, :skip-empty, :v);
    my $type = @split[0];
    my $char = @split[2];

    if $type eq "LPAREN" {
      $result ~= '  ' x $indent ~ $char ~ "\n";
      $indent += 1;
    }
    elsif $type eq "RPAREN" {
      $indent -= 1;
      $result ~= '  ' x $indent ~ $char ~ "\n";
    } else {
      $result ~= '  ' x $indent ~ $char ~ "\n";
    }
  }

  return $result;
}
