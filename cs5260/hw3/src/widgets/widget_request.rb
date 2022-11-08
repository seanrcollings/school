# frozen_string_literal: true

require 'json-schema'

# https://stackoverflow.com/questions/8706930
def to_snake_case(string)
  string.gsub(/::/, '/')
        .gsub(/([A-Z]+)([A-Z][a-z])/, '\1_\2')
        .gsub(/([a-z\d])([A-Z])/, '\1_\2')
        .tr('-', '_')
        .downcase
end

module Widgets
  WIDGET_SCHEMA = {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "type": "object",
    "properties": {
      "type": {
        "type": "string",
        "pattern": "create|update|delete"
      },
      "requestId": {
        "type": "string"
      },
      "widgetId": {
        "type": "string"
      },
      "owner": {
        "type": "string",
        "pattern": "[A-Za-z ]+"
      },
      "label": {
        "type": "string"
      },
      "description": {
        "type": "string"
      },
      "otherAttributes": {
        "type": "array",
        "items": [
          {
            "type": "object",
            "properties": {
              "name": {
                "type": "string"
              },
              "value": {
                "type": "string"
              }
            },
            "required": [
              "name",
              "value"
            ]
          }
        ]
      }
    },
    "required": [
      "type",
      "requestId",
      "widgetId",
      "owner"
    ]
  }.freeze

  class WidgetRequest
    attr_accessor :request_id, :widget_id, :owner, :metadata

    def initialize(request_id:, widget_id:, owner:)
      @request_id = request_id
      @widget_id = widget_id
      @owner = owner
    end

    def self.create(type:, **data)
      case type
      when 'create'
        CreateWidgetRequest.new(**data)
      when 'update'
        UpdateWidgetRequest.new(**data)
      when 'delete'
        DeleteWidgetRequest.new(**data)
      else
        nil
      end
    end

    def self.from_json(json)
      raise 'Cannot pass empty string' if json.empty?
      data = JSON.parse(json)
      JSON::Validator.validate!(WIDGET_SCHEMA, data)
      data = data.transform_keys { |k| to_snake_case(k).to_sym }
      WidgetRequest.create(**data)
    end

    def type
      return self.class::TYPE
    end

    def to_h
      {
        "type" => type,
        "requestId" => request_id,
        "widgetId" => widget_id,
        "owner" => owner
      }
    end

    def to_json(*args)
      to_h.delete_if { |k, v| v.nil? }.to_json(*args)
    end
  end

  class CreateWidgetRequest < WidgetRequest
    attr_accessor :label, :description, :attributes

    TYPE = 'create'.freeze

    def initialize(label: nil, description: nil, other_attributes: nil, **kwargs)
      @label = label
      @description = description
      @attributes = other_attributes
      super(**kwargs)
    end

    def to_h
      super.merge(
        {
          "label" => label,
          "description" => description,
          "otherAttributes" => attributes
        }
      )
    end
  end

  class UpdateWidgetRequest < WidgetRequest
    attr_accessor :label, :description, :attributes

    TYPE = 'update'.freeze

    def initialize(label: nil, description: nil, other_attributes: nil, **kwargs)
      @label = label
      @description = description
      @attributes = other_attributes
      super(**kwargs)
    end

    def to_h
      super.merge(
        {
          "label" => label,
          "description" => description,
          "otherAttributes" => attributes
        }
      )
    end
  end

  class DeleteWidgetRequest < WidgetRequest
    TYPE = 'delete'.freeze
  end
end
