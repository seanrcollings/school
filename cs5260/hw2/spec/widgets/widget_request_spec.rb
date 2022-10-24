require_relative "../../src/widgets"

def directory_contents(dirname)
  Dir.foreach(dirname) do |filename|
    next if filename == '.' or filename == '..'
    json = File.read("#{dirname}/#{filename}")
    next if json.empty?
    yield json
  end
end


describe Widgets::WidgetRequest do
  let :type_map do
    {
      'create' => Widgets::CreateWidgetRequest,
      'update' => Widgets::UpdateWidgetRequest,
      'delete' => Widgets::DeleteWidgetRequest,
    }
  end

  it 'creates the appropriate request type for each sample' do
    directory_contents('sample-requests/good') do |json|
      request = Widgets::WidgetRequest.from_json(json)
      data = JSON.parse(json)
      type = data["type"]
      expect(request).to be_instance_of(type_map[type])
      # I doubt that this will always be stable, but it's passing for now
      expect(request.to_json).to eq(json)
    end
  end

  it 'raises an error if they do not follow the specification' do
    directory_contents('sample-requests/bad') do |json|
      expect {
        Widgets::WidgetRequest.from_json(json)
      }.to raise_error(JSON::Schema::ValidationError)
    end
  end
end



