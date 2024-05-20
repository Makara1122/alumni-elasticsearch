FROM ruby:2.7

RUN apt-get update && apt-get install -y bash netcat postgresql-client

WORKDIR /pgsync

COPY Gemfile Gemfile.lock ./
RUN bundle install

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
