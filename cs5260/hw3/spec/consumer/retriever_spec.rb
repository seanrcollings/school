require 'json'
require 'aws-sdk'
require 'logger'
require 'stringio'
require_relative '../../src/consumer'
require_relative '../../src/widgets'

require_relative "../requests.rb"


describe Consumer do
  describe Consumer::S3Retriever do
    before do
      @client = Aws::S3::Client.new(region: 'us-east-1', stub_responses: true)
      @client.stub_responses(:list_objects_v2, contents: [{ key: 'v1' }])

      stub_const("Consumer::S3Retriever::S3", Aws::S3::Resource.new(client: @client))

      @retriever = Consumer::S3Retriever.new('bucket-name', Logger.new(StringIO.new))
    end

    describe '#get' do
      it 'responds with a creation request' do
        @client.stub_responses(:get_object, body: JSON.generate(CREATE_REQUEST))
        expect(@retriever.get.first).to be_instance_of(Widgets::CreateWidgetRequest)
      end

      it 'returns nil if the object is empty' do
        @client.stub_responses(:get_object, body: '')
        expect(@retriever.get.first).to be_nil
      end

      it 'returns nil and attempts to delete object if the object is not a valid request' do
        @client.stub_responses(:get_object, body: JSON.generate({'type': 'bad'}))
        expect(@retriever).to receive(:delete_objects).once
        expect(@retriever.get.first).to be_nil
      end

      describe 'object management' do
        it 'deletes the object if a block results in true' do
          @client.stub_responses(:get_object, body: JSON.generate(CREATE_REQUEST))
          expect(@retriever).to receive(:delete_objects).once
          @retriever.get { true }
        end

        it 'does not delete object if a block results in false' do
          @client.stub_responses(:get_object, body: JSON.generate(CREATE_REQUEST))
          expect(@retriever).not_to receive(:delete_objects)
          @retriever.get { false }
        end

        it 'does not delete object no block is passed' do
          @client.stub_responses(:get_object, body: JSON.generate(CREATE_REQUEST))
          expect(@retriever).not_to receive(:delete_objects)
          @retriever.get
        end
      end
    end
  end
end