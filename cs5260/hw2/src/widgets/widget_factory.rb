# frozen_string_literal: true

module Widgets
  module WidgetFactory
    def self.create_from_request(request)
      raise 'Can only create widget from create requests' unless request.type == 'create'

      Widget.new(
        id: request.widget_id,
        owner: request.owner,
        label: request.label,
        description: request.description,
        attributes: request.attributes
      )
    end

    def self.merge(widget, request)
      # stub
    end
  end

  Widget = Struct.new('Widget', :id, :owner, :label, :description, :attributes, keyword_init: true)
end
