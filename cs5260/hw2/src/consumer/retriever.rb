require 'json'
require 'aws-sdk'
require 'json-schema'
require 'pry'

require_relative '../widgets'

module Consumer
  class Retriever
    def initialize(location)
      @location = location
    end

    def get(limit: 1, &blk)
      raise 'Not Implemented!'
    end

    def get_one(&blk)
      get(limit: 1) do |requests|
        request = requests.first

        if block_given?
          blk.call(request)
        else
          false
        end
      end.first
    end
  end

  class S3Retriever < Retriever
    S3 = Aws::S3::Resource.new(region: 'us-east-1')

    def get(limit: 1, &blk)
      objects = bucket.objects.limit(limit)

      requests = objects.filter_map do |obj|
        body = obj.get[:body].string
        next if body.nil? || body.empty?

        begin
          Widgets::WidgetRequest.from_json(body)
        rescue JSON::Schema::ValidationError => e
          STDERR.puts e
          # So the retriever doesn't get hung up on
          # invalid objects, delete them when we run into them
          delete_objects([obj.key])
          nil
        end
      end

      # Yield a block, so we can decide whether or not to delete
      # the object after we've already used it. Then, if something goes
      # wrong, the request won't get deleted
      if block_given?
        delete = blk.call(requests)
        delete_objects(objects.map(&:key)) if delete
      end

      requests
    end

    private

    def delete_objects(keys)
      return if keys.empty?
      bucket.delete_objects({
        delete: {
          objects: keys.map { |key| { key: key } }
        }
      })
    end

    def bucket
      @bucket ||= S3.bucket(@location)
    end
  end
end