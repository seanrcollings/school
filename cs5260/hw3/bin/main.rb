require "bundler/setup"
require 'thor'
require 'logger'

require_relative "../src/consumer"

class CLI < Thor
  desc "consumer", 'execute the consumer program'
  option :sservice, desc: "The service that the requests are coming from either s3 or sqs"
  option :source, desc: "The location of the source service (the s3 bucket or sqs url)"
  option :dservice, desc: "The service of the destination, either s3 or dynamoddb"
  option :destination, desc: "The name of the destination bucket or table"

  def consumer
    logger = Logger.new(STDOUT)
    case options[:dservice]
    when 's3'
      processor = Consumer::S3Processor.new(options[:destination], logger)
    when 'dynamodb'
      processor = Consumer::DynamoDBProcessor.new(options[:destination], logger)
    else
      raise 'Bad option for dservice'
    end

    case options[:sservice]
    when 's3'
      retriever = Consumer::S3Retriever.new(options[:source], logger)
    when 'sqs'
      retriever = Consumer::SQSRetreiver.new(options[:source], logger)
    else
      raise 'Bad option for sservice'
    end

    poller = Consumer::Poller.new(retriever, processor, logger)
    poller.execute
  end
end

CLI.start(ARGV)
