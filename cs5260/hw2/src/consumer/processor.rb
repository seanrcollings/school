require 'aws-sdk'
require 'pry'
require_relative '../widgets'


module Consumer
  class Processor

    def initialize(location, logger)
      @location = location
      @logger = logger
    end

    def process(request)
      @logger.info "Process #{request.type} request #{request.request_id}"

      case request.type
      when 'create'
        process_create(request)
      when 'update'
        process_update(request)
      when 'delete'
        process_delete(request)
      end
    end

    def process_create(request)
      true
    end

    def process_update(request)
      true
    end

    def process_delete(request)
      true
    end
  end

  class S3Processor < Processor
    S3 = Aws::S3::Resource.new(region: 'us-east-1')

    def process_create(request)
      bucket.put_object(
        key: "widgets/#{request.owner}/#{request.widget_id}",
        body: request.to_json,
      )
      true
    end

    def process_update(request)
      true
    end

    def process_delete(request)
      true
    end

    def bucket
      @bucket ||= S3.bucket(@location)
    end
  end


  class DynamoDBProcessor < Processor

    def process_create(request)
      item = request_to_item(request)

      client.put_item(
        table_name: @location,
        item: item,
      )
      true
    end

    def process_update(request)
      true
    end

    def process_delete(request)
      true
    end

    def request_to_item(request)
      item = request.to_h
      id = item.delete("widgetId")
      item['id'] = id
      attributes = item.delete("otherAttributes") || []
      item.delete('type')
      item.merge(attributes.map { |a| [a["name"], a["value"]] }.to_h )
    end

    def client
      @client ||= Aws::DynamoDB::Client.new(region: 'us-east-1')
    end
  end
end