require 'thor'

require_relative "../src/consumer"

module Consumer
  class CLI < Thor
    desc "execute", 'execute the program'
    option :sbucket
    option :dservice
    option :destination

    def execute
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
