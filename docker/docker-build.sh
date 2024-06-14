#!/bin/sh

#Setting Versions
VERSION='1.0.0'

cd ..
./gradlew clean build -x test

ROOT_PATH=$(pwd)
echo "$ROOT_PATH"

echo 'api1 docker image build... Start'
cd "$ROOT_PATH" && docker build -t my-server1:$VERSION .
echo 'api1 docker image build... Finish'

echo 'api2 docker image build... Start'
cd "$ROOT_PATH" && docker build -f Dockerfile2 -t my-server2:$VERSION .
echo 'api2 docker image build... Finish'

echo 'api3 docker image build... Start'
cd "$ROOT_PATH" && docker build -f Dockerfile3 -t my-server3:$VERSION .
echo 'api3 docker image build... Finish'

echo 'nginx docker image build... Start'
cd "$ROOT_PATH"/nginx && docker build -t nginx:$VERSION .
echo 'nginx docker image build... Finish'

echo 'logstash docker image build... Start'
cd "$ROOT_PATH"/logstash && docker build -t logstash:$VERSION .
echo 'logstash docker image build... Finish'