FROM ruby:3.0.4
COPY ./src ./src
COPY ./bin ./bin
COPY ./Gemfile ./Gemfile
COPY ./Gemfile.lock ./Gemfile.lock
RUN gem install bundler:2.3.18
RUN bundle install
CMD ["ruby", "bin/main.rb", "consumer", "--sservice=sqs", "--source=https://sqs.us-east-1.amazonaws.com/836541945745/cs5260-requests", "--dservice=s3", "--destination=usu-cs5260-sean-web"]