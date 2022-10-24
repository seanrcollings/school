require 'stringio'
require 'logger'
require_relative '../../src/consumer'

describe Consumer do
  describe Consumer::Processor do
    before do
      @processor = Consumer::Processor.new('location', Logger.new(StringIO.new))
    end

    describe '#process' do
      it 'calls #process_create' do
        expect(@processor).to receive(:process_create).and_return(true)
        request = Widgets::CreateWidgetRequest.new(
          request_id: '1234',
          widget_id: '1234',
          owner: 'me',
          label: 'Label',
          description: 'Description',
          other_attributes: []
        )
        expect(@processor.process(request)).to be true
      end

      it 'calls #process_update' do
        expect(@processor).to receive(:process_update).and_return(true)
        request = Widgets::UpdateWidgetRequest.new(
          request_id: '1234',
          widget_id: '1234',
          owner: 'me',
          label: 'Label',
          description: 'Description',
          other_attributes: []
        )
        expect(@processor.process(request)).to be true
      end


      it 'calls #process_delete' do
        expect(@processor).to receive(:process_delete).and_return(true)
        request = Widgets::DeleteWidgetRequest.new(
          request_id: '1234',
          widget_id: '1234',
          owner: 'me',
        )
        expect(@processor.process(request)).to be true
      end
    end
  end

  describe Consumer::DynamoDBProcessor do
    before do
      @processor = Consumer::DynamoDBProcessor.new('location', Logger.new(StringIO.new))
    end

    describe '#request_to_item' do
      it 'formats the resposne into a table item' do
        request = Widgets::CreateWidgetRequest.new(
          request_id: '1234',
          widget_id: '1234',
          owner: 'me',
          label: 'Label',
          description: 'Description',
          other_attributes: [
            {
              'name' => 'name1',
              'value' => 'value1'
            },
            {
              'name' => 'name2',
              'value' => 'value2'
            },
          ]
        )

        item = {
          "id" => '1234',
          "requestId" => '1234',
          "owner" => 'me',
          "label" => 'Label',
          "description" => 'Description',
          "name1" => 'value1',
          "name2" => 'value2',
        }

        expect(@processor.request_to_item(request)).to eq item
      end
    end
  end
end