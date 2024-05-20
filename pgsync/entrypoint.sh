#!/bin/bash

# Function to check if a service is available
wait_for_service() {
  local host=$1
  local port=$2
  while ! nc -z $host $port; do
    echo "Waiting for $host:$port..."
    sleep 1
  done
}

# Function to check Elasticsearch readiness
wait_for_elasticsearch() {
  local url=$1
  until curl -s $url | grep -q '"status":"green"'; do
    echo "Waiting for Elasticsearch at $url..."
    sleep 5
  done
}

# Wait for PostgreSQL to be ready
wait_for_service postgres 5432

# Wait for Elasticsearch to be ready
wait_for_service elasticsearch 9200
wait_for_elasticsearch http://elasticsearch:9200/_cluster/health?wait_for_status=green&timeout=50s

# Debugging information
echo "Database URL: $DATABASE_URL"
echo "Running pgsync..."
pgsync --config /root/.pgsync.yml --debug || true

# Keep the container running for debugging
tail -f /dev/null
