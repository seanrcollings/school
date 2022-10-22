# frozen_string_literal: true

module Widgets
  # Factory to handle widget creation
  module WidgetFactory
    def self.create_from_request(request)
      assert request.type == 'create', 'Can only create widget from create requests'

      Widget.new(
        id: request.widget_id,
        owner: request.owner,
        label: request.label,
        description: request.desc,
        attributes: request.attributes
      )
    end

    def self.merge(widget, request)
      # stub
    end
  end

  Widget = Struct.new(:id, :owner, :label, :description, :attributes)
end
