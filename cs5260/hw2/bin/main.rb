require "bundler/setup"
require 'thor'

require_relative "../src/consumer"

module Consumer
  class CLI < Thor
    desc "consumer", 'execute the consumer program'
    option :sbucket, desc: "The source S3 bucket name"
    option :dservice, desc: "The service of the destination, either s3 or dynamoddb"
    option :destination, desc: "The name of the destination bucket or table"

    def consumer
      case options[:dservice]
      when 's3'
        processor = Consumer::S3Processor.new(options[:destination])
      when 'dynamodb'
        processor = Consumer::DynamoDBProcessor.new(options[:destination])
      else
        raise 'Bad option for dservice'
      end

      retriever = Consumer::S3Retriever.new(options[:sbucket])

      poller = Consumer::Poller.new(retriever, processor)
      poller.execute
    end
  end
end

Consumer::CLI.start(ARGV)
