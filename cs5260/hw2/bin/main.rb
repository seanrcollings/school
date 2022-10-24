require "bundler/setup"
require 'thor'
require 'logger'

require_relative "../src/consumer"

class CLI < Thor
  desc "consumer", 'execute the consumer program'
  option :sbucket, desc: "The source S3 bucket name"
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


    retriever = Consumer::S3Retriever.new(options[:sbucket], logger)

    poller = Consumer::Poller.new(retriever, processor, logger)
    poller.execute
  end
end

CLI.start(ARGV)
