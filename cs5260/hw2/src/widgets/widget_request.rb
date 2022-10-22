# frozen_string_literal: true

# https://stackoverflow.com/questions/8706930
def to_snake_case(string)
  string.gsub(/::/, '/')
        .gsub(/([A-Z]+)([A-Z][a-z])/, '\1_\2')
        .gsub(/([a-z\d])([A-Z])/, '\1_\2')
        .tr('-', '_')
        .downcase
end

module Widgets
  class WidgetRequest
    attr_accessor :request_id, :widget_id, :owner, :type

    def initialize(type:, request_id:, widget_id:, owner:)
      @type = type
      @request_id = request_id
      @widget_id = widget_id
      @owner = owner
    end

    def self.create(type:, **kwargs)
      case type
      when 'create'
        CreateWidgetRequest.new(type: type, **kwargs)
      when 'update'
        UpdateWidgetRequest.new(type: type, **kwargs)
      when 'delete'
        DeleteWidgetRequet.new(type: type, **kwargs)
      else
        nil
      end
    end

    def self.from_json(json)
      json = json.transform_keys { |k| to_snake_case(k).to_sym }
      WidgetRequest.create(**json)
    end

    def to_h
      {
        type: type,
        requestId: request_id,
        widgetId: widget_id,
        owner: owner
      }
    end

    def to_json(*args)
      to_h.delete_if { |k, v| v.nil? }.to_json(*args)
    end
  end

  class CreateWidgetRequest < WidgetRequest
    attr_accessor :label, :description, :attributes


    def initialize(label: nil, description: nil, other_attributes: nil, **kwargs)
      @label = label
      @description = description
      @attributes = other_attributes
      super(**kwargs)
    end

    def to_h
      super.merge(
        {
          label: label,
          description: description,
          otherAttributes: attributes
        }
      )
    end
  end

  class UpdateWidgetRequest < WidgetRequest
    attr_accessor :label, :description, :attributes


    def initialize(label: nil, description: nil, other_attributes: nil, **kwargs)
      @label = label
      @description = description
      @attributes = other_attributes
      super(**kwargs)
    end

    def to_h
      super.merge(
        {
          label: label,
          description: description,
          otherAttributes: attributes
        }
      )
    end
  end

  class DeleteWidgetRequet < WidgetRequest
  end
end
