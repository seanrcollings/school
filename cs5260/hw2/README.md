# Homework 2
I implemented this homework using [ruby](https://www.ruby-lang.org/en/). To run it, you will need to install it.


# Installation
The project uses bundler for dependancy management
```
$ gem install bundler:2.3.18
```
> Note: `gem` is ruby's package manager and should have been installed along with ruby

To install the dependancies:
```
$ bundle install
```

# Execution
The program is ran using the `bin/main.rb` entrypoint. You can view the help for the consumer like so:
```
$ ruby bin/main.rb help consumer
```

An example of running it would look like:
```
$ ruby bin/main.rb consumer \
    --sbucket usu-cs5260-sean-requests \
    --dservice dynamodb \
    --destination widgets
```

# Tests
You can run the test suite with:
```
$ bundle exec rspec spec
```