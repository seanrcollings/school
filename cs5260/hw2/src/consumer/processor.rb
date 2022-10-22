require 'aws-sdk'

require_relative '../widgets'

module Consumer
  class Processor

    def initialize(location)
      @location = location
    end

    def process(request)
      raise "Not implemented!"
    end
  end

  class S3Processor < Processor
    S3 = Aws::S3::Resource.new(region: 'us-east-1')

    def process(request)
      puts "Process #{request.type} request #{request.request_id}"

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
    def process(request)
      puts "process request"
    end

    private

    def process_create(request)
      # stub
    end

    def process_update(request)
      # stub
    end

    def process_delete(request)
      # stub
    end

    def bucket
      @bucket ||= S3.bucket(@location)
    end
  end
end