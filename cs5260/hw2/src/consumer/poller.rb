require 'pry'

module Consumer
  class Poller
    def initialize(retriever, processor)
      @retriever = retriever
      @processor = processor
    end

    def execute(wait: 0.1)
      loop do
        @retriever.get_one do |request|
          if request
            @processor.process(request)
          else
            sleep(wait)
          end
        end
      end
    end
  end
end