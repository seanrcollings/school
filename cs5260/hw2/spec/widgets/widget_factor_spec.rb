require_relative "../../src/widgets"

describe Widgets::WidgetFactory do
  describe "::create_from_request" do
    it 'creates a widget from a request' do
      request = Widgets::CreateWidgetRequest.new(
        request_id: '1234',
        widget_id: '1234',
        owner: 'me',
        label: 'Label',
        description: 'Description',
        other_attributes: []
      )

      widget = Widgets::WidgetFactory.create_from_request(request)
      expect(widget.id).to eq(request.widget_id)
    end

    it 'must be a create request' do
      request = Widgets::DeleteWidgetRequest.new(
        request_id: '1234',
        widget_id: '1234',
        owner: 'me',
      )

      expect {
        Widgets::WidgetFactory.create_from_request(request)
      }.to raise_error(RuntimeError)
    end
  end
end