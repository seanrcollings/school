require 'aws-sdk'

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

    private

    def process_create(request)
      false
    end

    def process_update(request)
      false
    end

    def process_delete(request)
      false
    end

  end

  class S3Processor < Processor
    S3 = Aws::S3::Resource.new(region: 'us-east-1')

    private

    def process_create(request)
      bucket.put_object(
        key: "widgets/#{request.owner}/#{request.widget_id}",
        body: request.to_json,
      )
      true
    end

    def process_update(request)
      false
    end

    def process_delete(request)
      false
    end

    def bucket
      @bucket ||= S3.bucket(@location)
    end
  end


  class DynamoDBProcessor < Processor

    private

    def process_create(request)
      item = request.to_h
      id = item.delete(:widgetId)
      item['id'] = id
      attributes = item.delete(:otherAttributes) || []
      item = item.merge(attributes.map { |a| [a["name"], a["value"]] }.to_h )

      client.put_item(
        table_name: @location,
        item: item,
      )
      true
    end

    def process_update(request)
      false
    end

    def process_delete(request)
      false
    end

    def client
      @client ||= Aws::DynamoDB::Client.new(region: 'us-east-1')
    end
  end
end