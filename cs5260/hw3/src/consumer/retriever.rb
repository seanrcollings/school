require 'byebug'
require 'json'
require 'aws-sdk'
require 'json-schema'

require_relative '../widgets'

module Consumer
  class Retriever
    def initialize(location, logger)
      @location = location
      @logger = logger
    end

    def get(*)
      raise 'Not Implemented!'
    end

    def get_one(&blk)
      get(amount: 1) do |requests|
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

    def get(amount: 1, &blk)
      objects = bucket.objects.limit(amount)

      requests = objects.filter_map do |obj|
        body = obj.get[:body].string
        next if body.nil? || body.empty?

        begin
          request = Widgets::WidgetRequest.from_json(body)
          @logger.info "Recieved request: #{request.request_id}"
          request
        rescue JSON::Schema::ValidationError => e
          @logger.error e.to_s
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
      params = {
        delete: {
          objects: keys.map { |key| { key: key } }
        }
      }
      bucket.delete_objects(params)
    end

    def bucket
      @bucket ||= S3.bucket(@location)
    end
  end

  class SQSRetreiver < Retriever
    SQS = Aws::SQS::Resource.new(region: 'us-east-1')

    def get(amount: 10, &blk)
      messages = queue.receive_messages(
        max_number_of_messages: amount,
        visibility_timeout: 5,
        wait_time_seconds: 4,
        # attribute_names: ["All"]
      )

      requests = messages.filter_map do |message|
        body = message.body

        begin
          request = Widgets::WidgetRequest.from_json(body)
          request.metadata = { id: message.message_id, receipt_handle: message.receipt_handle }
          @logger.info "Recieved request: #{request.request_id}"
          request
        rescue JSON::Schema::ValidationError => e
          @logger.error e.to_s
          # So the retriever doesn't get hung up on
          # invalid objects, delete them when we run into them
          # delete_messages(messages.map {|m| { id: m.message_id, receipt_handle: m.receipt_handle } })
          nil
        end
      end

      # Yield a block, so we can decide whether or not to delete
      # the object after we've already used it. Then, if something goes
      # wrong, the request won't get deleted
      delete = blk.call(requests)
      delete_messages(requests) if delete
    end

    def get_one(amount: 10, &blk)
      @cache ||= []

      if @cache.empty?
        get(amount: amount) do |messages|
          @cache.concat(messages)
          false
        end
      end

      request = @cache.pop
      delete = blk.call(request)
      delete_messages([request]) if delete && request
    end

    private

    def delete_messages(requests)
      return if requests.empty?

      @logger.info "Removing messages(s): #{requests.map(&:request_id).join(', ')}"
      queue.delete_messages(entries: requests.map(&:metadata))
    end

    def queue
      @queue ||= SQS.queue(@location)
    end
  end
end

