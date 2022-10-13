require 'aws-sdk-s3'

s3 = Aws::S3::Resource.new(region: 'us-east-1')

s3.buckets.each do |bucket|
  puts bucket.name
end
